package tictim.autobotany.contents.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import tictim.autobotany.contents.ModBlockEntities;

public class WoodenTrayBlockEntity extends TrayBlockEntity{
	public WoodenTrayBlockEntity(BlockPos pos, BlockState state){
		super(ModBlockEntities.WOODEN_TRAY.get(), pos, state);
	}
}
