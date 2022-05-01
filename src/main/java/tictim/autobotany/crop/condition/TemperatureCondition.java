package tictim.autobotany.crop.condition;

import com.google.common.base.Preconditions;
import net.minecraft.nbt.CompoundTag;
import tictim.autobotany.crop.factor.CropGrowthFactor;
import tictim.autobotany.crop.factor.TemperatureFactor;
import tictim.autobotany.util.Temps;

public final class TemperatureCondition extends ScoreCondition<TemperatureCondition>{
	public static final String TYPE = "temperature";

	public final int idealTemp, margin;

	public TemperatureCondition(int idealTemp, int margin, double scoreModifier){
		super(scoreModifier);
		Preconditions.checkArgument(Temps.isValid(idealTemp), "idealTemp");
		Preconditions.checkArgument(margin>=0, "margin");
		this.idealTemp = idealTemp;
		this.margin = margin;
	}
	public static TemperatureCondition read(CompoundTag tag){
		return new TemperatureCondition(
				Temps.clamp(tag.getInt("IdealTemp")),
				Math.max(tag.getInt("Margin"), 0),
				readScoreModifier(tag));
	}

	public boolean isInRange(int temp){
		return temp>=idealTemp-margin&&temp<=idealTemp+margin;
	}

	@Override public String type(){
		return TYPE;
	}
	@Override public CropGrowthFactor<TemperatureCondition> createFactor(){
		return new TemperatureFactor(this);
	}

	@Override protected void writeInternal(CompoundTag tag){
		super.writeInternal(tag);
		tag.putInt("IdealTemp", idealTemp);
		tag.putInt("Margin", margin);
	}

	@Override public String toString(){
		return "TemperatureCondition{"+
				"scoreModifier="+scoreModifier+
				", idealTemp="+idealTemp+
				", margin="+margin+
				'}';
	}
}
