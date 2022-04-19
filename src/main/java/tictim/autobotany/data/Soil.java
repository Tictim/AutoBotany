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
		// Fluid capacity of the soil - the larger capacity is, the better soils are. Generally.
		int fluidCapacity,
		// Nutrients provided by the soil.
		SubstanceSet nutrients,
		// Pathogens naturally present in the soil.
		Set<Pathogen> microbes,
		// Seeds of various weeds present in the soil.
		Set<Species> weeds,
		// Whether it's hidden in creative tabs or database
		boolean hidden
) implements DataRegistry.Entry{
	public Soil(ResourceLocation name, SoilShape shape, int tint, int fluidCapacity, SubstanceSet nutrients, Set<Pathogen> microbes, Set<Species> weeds){
		this(name, shape, tint, fluidCapacity, nutrients, microbes, weeds, false);
	}

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
