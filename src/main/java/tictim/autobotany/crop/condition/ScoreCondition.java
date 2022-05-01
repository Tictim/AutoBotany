package tictim.autobotany.crop.condition;

import com.google.common.base.Preconditions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public abstract sealed class ScoreCondition<C extends ScoreCondition<C>> extends CropCondition<C>
		permits IrrigationCondition, NutrientCondition, TemperatureCondition, pHCondition{
	public final double scoreModifier;

	public ScoreCondition(double scoreModifier){
		checkScoreModifier(scoreModifier, true);
		this.scoreModifier = scoreModifier;
	}

	public static boolean checkScoreModifier(double scoreModifier, boolean throwException){
		if(throwException){
			Preconditions.checkArgument(Double.isFinite(scoreModifier), "scoreModifier should be finite");
			Preconditions.checkArgument(scoreModifier>0, "scoreModifier should be positive");
			return true;
		}else return Double.isFinite(scoreModifier)&&scoreModifier>0;
	}

	public static double readScoreModifier(CompoundTag tag){
		if(tag.contains("ScoreModifier", Tag.TAG_DOUBLE)){
			double scoreModifier = tag.getDouble("ScoreModifier");
			if(checkScoreModifier(scoreModifier, false))
				return scoreModifier;
		}
		return 1;
	}

	@Override protected void writeInternal(CompoundTag tag){
		if(scoreModifier!=1)
			tag.putDouble("ScoreModifier", scoreModifier);
	}
}
