package tictim.autobotany.contents.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import tictim.autobotany.contents.ModBlockEntities;
import tictim.autobotany.contents.blockentity.WoodenTrayBlockEntity;

import javax.annotation.Nullable;

public class WoodenTrayBlock extends TrayBlock{
	public WoodenTrayBlock(Properties p){
		super(p);
	}

	@Nullable @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state){
		return new WoodenTrayBlockEntity(pos, state);
	}
	@Nullable @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type){
		return createTrayTicker(level, type, ModBlockEntities.WOODEN_TRAY.get());
	}
}
