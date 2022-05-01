package tictim.autobotany.contents.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.fluids.FluidUtil;
import tictim.autobotany.contents.ModItems;
import tictim.autobotany.contents.blockentity.TrayBlockEntity;
import tictim.autobotany.crop.Crop;
import tictim.autobotany.crop.Tray;
import tictim.autobotany.data.AllSpecies;
import tictim.autobotany.data.Soil;
import tictim.autobotany.data.Species;

import javax.annotation.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public abstract class TrayBlock extends BaseEntityBlock{
	public static final BooleanProperty SOILED = BooleanProperty.create("soiled");

	public TrayBlock(Properties p){
		super(p);
	}

	@Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b){
		b.add(HORIZONTAL_FACING, SOILED);
	}

	@Nullable @Override public BlockState getStateForPlacement(BlockPlaceContext context){
		return defaultBlockState().setValue(HORIZONTAL_FACING, context.getHorizontalDirection().getOpposite());
	}

	// TODO Make it UI-based or sth
	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit){
		if(!level.isClientSide){
			if(level.getBlockEntity(pos) instanceof TrayBlockEntity trayBlockEntity)
				useInternal(state, level, pos, player, hand, hit, trayBlockEntity);
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	protected void useInternal(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit, TrayBlockEntity trayBlockEntity){
		Tray tray = trayBlockEntity.getTray();
		ItemStack stack = player.getItemInHand(hand);
		if(stack.getItem()==ModItems.SOIL.get()){
			if(!tray.hasSoil()){
				Soil soil = ModItems.SOIL.get().getEntryOrNull(stack);
				if(soil!=null){
					stack.shrink(1);
					tray.setSoil(soil);
				}
				return;
			}
		}else if(stack.getItem()==ModItems.GARDEN_TROWEL.get()){
			if(tray.hasSoil()){
				if(level instanceof ServerLevel sl){
					tray.harvestCrop(sl, pos);
					tray.setSoil(null);
					stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
				}
				return;
			}
		}else if(stack.getItem()==Items.SHEARS){ // TODO i'm not sure about this
			if(tray.hasCrop()){
				if(level instanceof ServerLevel sl){
					tray.harvestCrop(sl, pos);
					stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
				}
				return;
			}
		}else{
			if(FluidUtil.interactWithFluidHandler(player, hand, tray.getSolutionContainer())) return;

			Species species = AllSpecies.fromSeedItem(stack.getItem());
			if(species!=null){
				if(!tray.hasCrop()){
					stack.shrink(1);
					tray.plant(species);
					return;
				}
			}
		}
		// TODO gui
		Crop crop = tray.crop();
		if(crop!=null&&crop.isFullyGrown()&&level instanceof ServerLevel sl)
			tray.harvestCrop(sl, pos);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving){
		if(!state.is(newState.getBlock())){
			BlockEntity blockentity = level.getBlockEntity(pos);
			if(blockentity instanceof TrayBlockEntity tray){
				tray.onBreak();
			}

			super.onRemove(state, level, pos, newState, isMoving);
		}
	}

	@SuppressWarnings("deprecation") @Override public RenderShape getRenderShape(BlockState state){
		return RenderShape.MODEL;
	}

	@Nullable @Override public abstract BlockEntity newBlockEntity(BlockPos pos, BlockState state);
	@Nullable @Override public abstract <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type);

	@Nullable protected static <T extends BlockEntity> BlockEntityTicker<T> createTrayTicker(Level level, BlockEntityType<T> expectedType, BlockEntityType<? extends TrayBlockEntity> providedType){
		return level.isClientSide ? null : createTickerHelper(expectedType, providedType, (l, p, s, be) -> be.serverTick(l, p, s));
	}
}
