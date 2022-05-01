package tictim.autobotany.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.IFluidHandler;
import tictim.autobotany.capability.SolutionContainer;
import tictim.autobotany.crop.factor.CropGrowthFactor;
import tictim.autobotany.data.Soil;
import tictim.autobotany.data.Species;
import tictim.autobotany.util.ABNBT;

import javax.annotation.Nullable;

public class Tray{
	public static final int FLUID_CAPACITY_WITHOUT_SOIL = 4000;
	public static final int FLUID_CAPACITY_WITH_SOIL = 1000;

	@Nullable private Soil soil;
	@Nullable private Crop crop;

	private SolutionContainer solutionContainer = new TraySolutionContainer(FLUID_CAPACITY_WITHOUT_SOIL);

	@Nullable private Runnable onChanged;
	@Nullable private OnSync onSync;

	public void setOnChanged(@Nullable Runnable onChanged){
		this.onChanged = onChanged;
	}
	public void setOnSync(@Nullable OnSync onSync){
		this.onSync = onSync;
	}

	public boolean hasSoil(){
		return soil!=null;
	}
	@Nullable public Soil soil(){
		return soil;
	}
	public void setSoil(@Nullable Soil soil){
		this.soil = soil;
		changed();
		sync(true);
	}

	public SolutionContainer getSolutionContainer(){
		return solutionContainer;
	}
	private void updateSolutionContainerSize(){
		int capacity = soil==null ? FLUID_CAPACITY_WITHOUT_SOIL : FLUID_CAPACITY_WITH_SOIL;
		if(this.solutionContainer.getTankCapacity()!=capacity){
			SolutionContainer newContainer = new TraySolutionContainer(capacity);
			newContainer.fill(solutionContainer.getFluidInTank(1), solutionContainer.getSubstanceInTank(1), IFluidHandler.FluidAction.EXECUTE);
			this.solutionContainer = newContainer;
		}
	}

	public boolean hasCrop(){
		return crop!=null;
	}
	@Nullable public Crop crop(){
		return crop;
	}
	public void plant(Species species){
		this.crop = new Crop(species);
		changed();
		sync(true);
	}

	public void harvestCrop(ServerLevel level, BlockPos pos){
		dropShit(level, pos);
		this.crop = null;
		changed();
		sync(true);
	}

	public void serverTick(ServerLevel level, BlockPos pos, BlockState state){
		if(this.crop!=null){
			if(++this.crop.cropTick>20){
				this.crop.cropTick = 0;
				updateCrop(this.crop);
			}
		}
	}

	protected void updateCrop(Crop crop){
		if(crop.isFullyGrown()) return;

		TrayCropAccess access = new TrayCropAccess(this, crop);
		for(CropGrowthFactor<?> factor : crop.factors)
			factor.update(access);

		crop.growth++;

		changed();
		sync(false);
	}

	protected void dropShit(ServerLevel level, BlockPos pos){
		if(this.crop!=null){
			for(ItemStack stack : this.crop.createResult(level).yields()){
				level.addFreshEntity(new ItemEntity(level, pos.getX()+.5, pos.getY()+.75, pos.getZ()+.5, stack));
			}
		}
	}

	private void changed(){
		if(onChanged!=null) onChanged.run();
	}
	private void sync(boolean immediately){
		if(onSync!=null) onSync.sync(immediately);
	}

	public void write(CompoundTag tag, boolean save){
		if(soil!=null) tag.putString("Soil", soil.name().toString());
		tag.put("Solutions", solutionContainer.write());
		if(crop!=null) tag.put("Crop", crop.write(save));
	}
	public void read(CompoundTag tag){
		this.soil = tag.contains("Soil", Tag.TAG_STRING) ? ABNBT.readSoil(tag.getString("Soil")) : null;
		this.solutionContainer.clear();
		updateSolutionContainerSize();
		this.solutionContainer.read(tag.getCompound("Solutions"));
		this.crop = tag.contains("Crop", Tag.TAG_COMPOUND) ? new Crop(tag.getCompound("Crop")) : null;
	}

	@FunctionalInterface public interface OnSync{
		void sync(boolean immediately);
	}

	public class TraySolutionContainer extends SolutionContainer{
		public TraySolutionContainer(int capacity){
			super(capacity, Integer.MAX_VALUE);
		}

		@Override protected void onContentsChanged(){
			changed();
			sync(false);
		}
	}
}
