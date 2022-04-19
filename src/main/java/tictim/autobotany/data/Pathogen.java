package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Various pathogens present in {@link Soil soils}. May provide disease or {@link Substance additional nutrients}.
 */
public record Pathogen(
		ResourceLocation name,
		int tint,
		SubstanceSet nutrients,
		@Nullable Disease disease,
		// Whether it's hidden in creative tabs or database
		boolean hidden
) implements DataRegistry.Entry{
	@Override
	public boolean equals(Object o){
		return this==o||o instanceof Pathogen p&&name.equals(p.name);
	}
	@Override
	public int hashCode(){
		return name.hashCode();
	}
	@Override
	public String toString(){
		return name.toString();
	}

	/**
	 * Disease that specific pathogen apply. One pathogen can only cause one disease.
	 */
	public static record Disease (
			// Immunity threshold - the disease will only affect crops with immunity stat lower than the threshold.
			int immunityThreshold,
			// Score penalty - the greater penalty is, the more total score will be fucked over.
			int scorePenalty
	){}
}
