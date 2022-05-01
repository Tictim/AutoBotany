package tictim.autobotany.crop.factor;

import net.minecraft.nbt.CompoundTag;
import tictim.autobotany.crop.CropAccess;
import tictim.autobotany.crop.condition.NoCondition;

public class NoFactor extends CropGrowthFactor<NoCondition>{
	public static final NoFactor INSTANCE = new NoFactor();

	private NoFactor(){
		super(NoCondition.INSTANCE);
	}

	@Override public UpdateOrder updateOrder(){
		return UpdateOrder.ELSE;
	}
	@Override public void update(CropAccess cropAccess){}
	@Override public long calculateScore(){
		return 0;
	}
	@Override public void write(CompoundTag tag){}
	@Override public void read(CompoundTag tag){}
}
