package tictim.autobotany.crop.condition;

import net.minecraft.nbt.CompoundTag;
import tictim.autobotany.crop.factor.CropGrowthFactor;

public abstract class CropCondition<C extends CropCondition<C>>{
	public abstract String type();
	public abstract CropGrowthFactor<C> createFactor();

	public final CompoundTag write(){
		CompoundTag tag = new CompoundTag();
		writeInternal(tag);
		tag.putString("Type", type());
		return tag;
	}

	protected abstract void writeInternal(CompoundTag tag);
}
