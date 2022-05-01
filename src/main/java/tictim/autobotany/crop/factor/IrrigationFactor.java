package tictim.autobotany.crop.factor;

import com.google.common.math.LongMath;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import tictim.autobotany.crop.CropAccess;
import tictim.autobotany.crop.condition.IrrigationCondition;

public class IrrigationFactor extends CropGrowthFactor<IrrigationCondition>{
	private long totalIrrigation;
	private long starvation;

	public IrrigationFactor(IrrigationCondition condition){
		super(condition);
	}

	@Override public UpdateOrder updateOrder(){
		return UpdateOrder.IRRIGATION;
	}

	@Override public void update(CropAccess cropAccess){
		long cap = Long.MAX_VALUE-totalIrrigation;
		if(cap==0) return;
		long need = Math.min(cap, condition.need+Math.min(starvation, condition.need/2));
		long consumed = 0;

		FluidStack fluid = cropAccess.irrigation();
		if(!fluid.isEmpty()&&condition.fluidMatch.matches(fluid.getFluid())){
			consumed += cropAccess.consumeIrrigation((int)Math.min(Integer.MAX_VALUE, need-consumed), false);
		}

		totalIrrigation += consumed;

		if(consumed!=Math.min(cap, condition.need)){
			//noinspection UnstableApiUsage
			starvation = LongMath.saturatedAdd(starvation, condition.need-consumed);
		}
	}

	@Override public long calculateScore(){
		long score = totalIrrigation/condition.need;
		long partial = totalIrrigation%condition.need;
		return (long)(score*condition.scoreModifier+partial*condition.scoreModifier/condition.need);
	}

	@Override public void write(CompoundTag tag){
		tag.putLong("Total", totalIrrigation);
		if(starvation>0)
			tag.putLong("Starvation", starvation);
	}
	@Override public void read(CompoundTag tag){
		this.totalIrrigation = Math.max(0, tag.getLong("Total"));
		this.starvation = Math.max(0, tag.getLong("Starvation"));
	}
}
