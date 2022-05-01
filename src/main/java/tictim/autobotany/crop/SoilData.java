package tictim.autobotany.crop;

import net.minecraft.nbt.CompoundTag;
import tictim.autobotany.capability.SolutionContainer;
import tictim.autobotany.data.AllSoils;
import tictim.autobotany.data.Soil;

public final class SoilData{
	public final Soil soil;
	public final SolutionContainer solutionContainer;

	public SoilData(Soil soil){
		this.soil = soil;
		this.solutionContainer = new SolutionContainer(soil.maxIrrigationPerTick(), Integer.MAX_VALUE);
	}
	public SoilData(CompoundTag tag){
		this(AllSoils.SOILS.getOrDefault(tag.getString("Soil")));
		this.solutionContainer.read(tag.getCompound("Solution"));
	}

	public CompoundTag write(boolean save){
		CompoundTag tag = new CompoundTag();
		tag.putString("Soil", soil.name().toString());
		tag.put("Solution", solutionContainer.write());
		return tag;
	}

	@Override public String toString(){
		return "SoilData{"+
				"soil="+soil+
				", solutionContainer="+solutionContainer+
				'}';
	}
}
