package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;

import java.util.Set;

/**
 * Soils which crops grow.
 */
public record Soil(
		ResourceLocation name,
		SoilShape shape,
		int tint,
		int maxIrrigationPerTick,
		double pH,
		// Nutrients provided by the soil.
		SubstanceSet nutrients,
		// Pathogens naturally present in the soil.
		Set<Ecosystem> microbes,
		// Seeds of various weeds present in the soil.
		Set<Species> weeds,
		// Whether it's hidden in creative tabs or database
		boolean hidden
) implements DataRegistry.Entry{
	@Override
	public boolean equals(Object o){
		return this==o||o instanceof Soil soil&&name.equals(soil.name);
	}
	@Override
	public int hashCode(){
		return name.hashCode();
	}
	@Override
	public String toString(){
		return name.toString();
	}
}
