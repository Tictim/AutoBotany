package tictim.autobotany.crop.condition;

import com.google.common.base.Preconditions;
import net.minecraft.nbt.CompoundTag;
import tictim.autobotany.crop.factor.CropGrowthFactor;
import tictim.autobotany.crop.factor.IrrigationFactor;
import tictim.autobotany.util.FluidMatch;

import java.util.Objects;

public final class IrrigationCondition extends ScoreCondition<IrrigationCondition>{
	public static final String TYPE = "irrigation";

	public final FluidMatch fluidMatch;
	/**
	 * Amount of fluid required for each crop tick, in mb
	 */
	public final int need;

	public IrrigationCondition(FluidMatch fluidMatch, int need, double scoreModifier){
		super(scoreModifier);
		Preconditions.checkArgument(need>=0, "need");
		this.fluidMatch = Objects.requireNonNull(fluidMatch);
		this.need = need;
	}
	public static IrrigationCondition read(CompoundTag tag){
		return new IrrigationCondition(
				FluidMatch.read(tag.getCompound("Fluid")),
				Math.max(tag.getInt("Need"), 0),
				readScoreModifier(tag));
	}

	@Override public String type(){
		return TYPE;
	}
	@Override public CropGrowthFactor<IrrigationCondition> createFactor(){
		return new IrrigationFactor(this);
	}

	@Override protected void writeInternal(CompoundTag tag){
		super.writeInternal(tag);
		tag.put("Fluid", fluidMatch.write());
		tag.putInt("Need", need);
	}

	@Override public String toString(){
		return "IrrigationCondition{"+
				"scoreModifier="+scoreModifier+
				", fluidMatch="+fluidMatch+
				", need="+need+
				'}';
	}
}
