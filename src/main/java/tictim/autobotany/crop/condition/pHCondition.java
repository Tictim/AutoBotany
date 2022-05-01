package tictim.autobotany.crop.condition;

import com.google.common.base.Preconditions;
import net.minecraft.nbt.CompoundTag;
import tictim.autobotany.crop.factor.CropGrowthFactor;
import tictim.autobotany.crop.factor.pHFactor;
import tictim.autobotany.util.pHs;

public final class pHCondition extends ScoreCondition<pHCondition>{
	public static final String TYPE = "ph";

	public final double idealPH, margin;

	public pHCondition(double idealPH, double margin, double scoreModifier){
		super(scoreModifier);
		Preconditions.checkArgument(pHs.isValid(idealPH), "idealTemp");
		Preconditions.checkArgument(margin>=0, "margin");
		this.idealPH = idealPH;
		this.margin = margin;
	}
	public static pHCondition read(CompoundTag tag){
		return new pHCondition(
				pHs.clamp(tag.getDouble("IdealPH")),
				Math.max(tag.getDouble("Margin"), 0),
				readScoreModifier(tag));
	}

	public boolean isInRange(double pH){
		return pH>=idealPH-margin&&pH<=idealPH+margin;
	}

	@Override public String type(){
		return TYPE;
	}
	@Override public CropGrowthFactor<pHCondition> createFactor(){
		return new pHFactor(this);
	}

	@Override protected void writeInternal(CompoundTag tag){
		super.writeInternal(tag);
		tag.putDouble("IdealPH", idealPH);
		tag.putDouble("Margin", margin);
	}

	@Override public String toString(){
		return "pHCondition{"+
				"scoreModifier="+scoreModifier+
				", idealPH="+idealPH+
				", margin="+margin+
				'}';
	}
}
