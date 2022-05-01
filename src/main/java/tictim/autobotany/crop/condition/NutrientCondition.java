package tictim.autobotany.crop.condition;

import com.google.common.base.Preconditions;
import net.minecraft.nbt.CompoundTag;
import tictim.autobotany.crop.factor.CropGrowthFactor;
import tictim.autobotany.crop.factor.NutrientFactor;
import tictim.autobotany.data.Substance;
import tictim.autobotany.util.SubstanceMatch;

import java.util.Objects;

public final class NutrientCondition extends ScoreCondition<NutrientCondition>{
	public static final String TYPE = "nutrient";

	public final SubstanceMatch nutrientMatch;
	/**
	 * Amount of nutrient required for each crop tick, in mcl
	 */
	public final int need;

	public NutrientCondition(Substance nutrientMatch, int need, double scoreModifier){
		this(new SubstanceMatch(nutrientMatch), need, scoreModifier);
	}
	public NutrientCondition(SubstanceMatch nutrientMatch, int need, double scoreModifier){
		super(scoreModifier);
		Preconditions.checkArgument(need>=0, "need");
		this.nutrientMatch = Objects.requireNonNull(nutrientMatch);
		this.need = need;
	}
	public static NutrientCondition read(CompoundTag tag){
		return new NutrientCondition(
				SubstanceMatch.read(tag.getCompound("Nutrient")),
				Math.max(tag.getInt("Need"), 0),
				readScoreModifier(tag));
	}

	@Override public String type(){
		return TYPE;
	}
	@Override public CropGrowthFactor<NutrientCondition> createFactor(){
		return new NutrientFactor(this);
	}

	@Override protected void writeInternal(CompoundTag tag){
		super.writeInternal(tag);
		tag.put("Nutrient", nutrientMatch.write());
		tag.putInt("Need", need);
	}

	@Override public String toString(){
		return "NutrientCondition{"+
				"scoreModifier="+scoreModifier+
				", nutrientMatch="+nutrientMatch+
				", need="+need+
				'}';
	}
}
