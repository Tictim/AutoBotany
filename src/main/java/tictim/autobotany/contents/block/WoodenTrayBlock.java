package tictim.autobotany.contents.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import tictim.autobotany.contents.ModBlockEntities;
import tictim.autobotany.contents.blockentity.WoodenTrayBlockEntity;

import javax.annotation.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class WoodenTrayBlock extends TrayBlock{
	private static final VoxelShape SHAPE_Z = Shapes.or(
			box(0, 3, 1, 2, 11, 15),
			box(14, 3, 1, 16, 11, 15),
			box(2, 3, 13, 14, 11, 15),
			box(2, 3, 1, 14, 11, 3),
			box(2, 3, 3, 14, 5, 13),
			box(12, 0, 3, 14, 3, 13),
			box(2, 0, 3, 4, 3, 13)).optimize();
	private static final VoxelShape SHAPE_X = Shapes.or(
			box(1, 3, 14, 15, 11, 16),
			box(1, 3, 0, 15, 11, 2),
			box(13, 3, 2, 15, 11, 14),
			box(1, 3, 2, 3, 11, 14),
			box(3, 3, 2, 13, 5, 14),
			box(3, 0, 2, 13, 3, 4),
			box(3, 0, 12, 13, 3, 14)).optimize();
	private static final VoxelShape SHAPE_Z_SOILED = Shapes.or(
			box(0, 3, 1, 2, 11, 15),
			box(14, 3, 1, 16, 11, 15),
			box(2, 3, 13, 14, 11, 15),
			box(2, 3, 1, 14, 11, 3),
			box(2, 3, 3, 14, 10, 13),
			box(12, 0, 3, 14, 3, 13),
			box(2, 0, 3, 4, 3, 13)).optimize();
	private static final VoxelShape SHAPE_X_SOILED = Shapes.or(
			box(1, 3, 14, 15, 11, 16),
			box(1, 3, 0, 15, 11, 2),
			box(13, 3, 2, 15, 11, 14),
			box(1, 3, 2, 3, 11, 14),
			box(3, 3, 2, 13, 10, 14),
			box(3, 0, 2, 13, 3, 4),
			box(3, 0, 12, 13, 3, 14)).optimize();

	public WoodenTrayBlock(Properties p){
		super(p);
	}

	@Nullable @Override public BlockEntity newBlockEntity(BlockPos pos, BlockState state){
		return new WoodenTrayBlockEntity(pos, state);
	}
	@Nullable @Override public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type){
		return createTrayTicker(level, type, ModBlockEntities.WOODEN_TRAY.get());
	}

	@SuppressWarnings("deprecation") @Override public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context){
		boolean zAxis = state.getValue(HORIZONTAL_FACING).getAxis()==Direction.Axis.Z;
		boolean soiled = state.getValue(SOILED);
		return soiled ? (zAxis ? SHAPE_Z_SOILED : SHAPE_X_SOILED) : (zAxis ? SHAPE_Z : SHAPE_X);
	}
}
