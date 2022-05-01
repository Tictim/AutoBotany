package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

/**
 * Various things present in {@link Soil soils}, such as fungi, microbes, etc.
 * May provide disease or {@link Substance additional nutrients}.
 */
public record Ecosystem(
		ResourceLocation name,
		int tint,
		SubstanceSet nutrients,
		@Nullable Disease disease
) implements DataRegistry.Entry{
	@Override
	public boolean equals(Object o){
		return this==o||o instanceof Ecosystem p&&name.equals(p.name);
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
	 * Disease that specific pathogen apply. One ecosystem can only cause one disease.
	 */
	public static record Disease (
			// Immunity threshold - the disease will only affect crops with immunity stat lower than the threshold.
			int immunityThreshold,
			// Score penalty - the greater penalty is, the more total score will be fucked over.
			int scorePenalty
	){}
}
