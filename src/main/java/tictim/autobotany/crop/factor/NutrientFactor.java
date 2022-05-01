package tictim.autobotany.crop.factor;

import com.google.common.math.LongMath;
import net.minecraft.nbt.CompoundTag;
import tictim.autobotany.crop.CropAccess;
import tictim.autobotany.crop.condition.NutrientCondition;
import tictim.autobotany.data.Substance;

public class NutrientFactor extends CropGrowthFactor<NutrientCondition>{
	private long totalNutrient;
	private long starvation;

	public NutrientFactor(NutrientCondition condition){
		super(condition);
	}

	@Override public UpdateOrder updateOrder(){
		return UpdateOrder.NUTRIENT;
	}

	@Override public void update(CropAccess cropAccess){
		long cap = Long.MAX_VALUE-totalNutrient;
		if(cap==0) return;
		long need = Math.min(cap, condition.need+Math.min(starvation, condition.need/2));
		long consumed = 0;

		for(Substance s : cropAccess.nutrients().substances().toArray(new Substance[0])){
			if(condition.nutrientMatch.matches(s)){
				consumed += cropAccess.nutrients().consume(s, (int)Math.min(Integer.MAX_VALUE, need-consumed), false);
				if(consumed>=need) break;
			}
		}

		totalNutrient += consumed;

		if(consumed!=Math.min(cap, condition.need)){
			//noinspection UnstableApiUsage
			starvation = LongMath.saturatedAdd(starvation, condition.need-consumed);
		}
	}

	@Override public long calculateScore(){
		long score = totalNutrient/condition.need;
		long partial = totalNutrient%condition.need;
		return (long)(score*condition.scoreModifier+partial*condition.scoreModifier/condition.need);
	}

	@Override public void write(CompoundTag tag){
		tag.putLong("Total", totalNutrient);
		if(starvation>0)
			tag.putLong("Starvation", starvation);
	}
	@Override public void read(CompoundTag tag){
		this.totalNutrient = Math.max(0, tag.getLong("Total"));
		this.starvation = Math.max(0, tag.getLong("Starvation"));
	}
}
