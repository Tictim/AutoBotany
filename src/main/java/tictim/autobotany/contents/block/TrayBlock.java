package tictim.autobotany.contents.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import tictim.autobotany.contents.ModItems;
import tictim.autobotany.contents.blockentity.TrayBlockEntity;
import tictim.autobotany.data.Soil;
import tictim.autobotany.data.Species;

import javax.annotation.Nullable;

public abstract class TrayBlock extends BaseEntityBlock{
	public TrayBlock(Properties p){
		super(p);
	}

	// TODO Make it UI-based or sth
	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit){
		if(!(level.getBlockEntity(pos) instanceof TrayBlockEntity tray)){
			return InteractionResult.PASS;
		}
		ItemStack stack = player.getItemInHand(hand);
		if(stack.getItem()==ModItems.SEED.get()){
			if(!tray.hasCrop()){
				if(!level.isClientSide){
					Species species = ModItems.SEED.get().getEntryOrNull(stack);
					if(species!=null){
						stack.shrink(1);
						tray.plant(species);
					}
				}
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
		}else if(stack.getItem()==ModItems.SOIL.get()){
			if(!tray.hasSoil()){
				if(!level.isClientSide){
					Soil soil = ModItems.SOIL.get().getEntryOrNull(stack);
					if(soil!=null){
						stack.shrink(1);
						tray.setSoil(soil);
					}
				}
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
		}else if(stack.getItem()==ModItems.GARDEN_TROWEL.get()){
			if(tray.hasSoil()){
				if(!level.isClientSide&&level instanceof ServerLevel sl){
					tray.harvestCrop(sl, pos);
					tray.setSoil(null);
					stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
				}
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
		}else if(stack.getItem()==Items.SHEARS){
			if(tray.hasCrop()){
				if(!level.isClientSide&&level instanceof ServerLevel sl){
					tray.harvestCrop(sl, pos);
					stack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
				}
				return InteractionResult.sidedSuccess(level.isClientSide);
			}
		}
		TrayBlockEntity.CropData cropData = tray.getCropData();
		if(cropData!=null){
			if(cropData.isFullyGrown()){
				if(!level.isClientSide&&level instanceof ServerLevel sl){
					tray.harvestCrop(sl, pos);
				}
			}
		}
		return InteractionResult.sidedSuccess(level.isClientSide);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!state.is(newState.getBlock())) {
			BlockEntity blockentity = level.getBlockEntity(pos);
			if (blockentity instanceof TrayBlockEntity tray) {
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
