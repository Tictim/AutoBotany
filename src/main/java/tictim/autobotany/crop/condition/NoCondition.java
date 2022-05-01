package tictim.autobotany.crop.condition;


import net.minecraft.nbt.CompoundTag;
import tictim.autobotany.crop.factor.CropGrowthFactor;
import tictim.autobotany.crop.factor.NoFactor;

public final class NoCondition extends CropCondition<NoCondition>{
	public static final String TYPE = "";
	public static final NoCondition INSTANCE = new NoCondition();



	@Override public String type(){
		return TYPE;
	}

	@Override public CropGrowthFactor<NoCondition> createFactor(){
		return NoFactor.INSTANCE;
	}
	@Override protected void writeInternal(CompoundTag tag){}
}
