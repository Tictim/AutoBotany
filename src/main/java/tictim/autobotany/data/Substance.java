package tictim.autobotany.data;

import com.google.common.base.Preconditions;
import net.minecraft.resources.ResourceLocation;
import tictim.autobotany.util.pHs;

public record Substance(
		ResourceLocation name,
		int tint,
		int fluidCapacity,
		double pH,
		// Whether it's hidden in creative tabs
		boolean hidden
) implements DataRegistry.Entry{
	public Substance(ResourceLocation name, int tint, int fluidCapacity, double pH){
		this(name, tint, fluidCapacity, pH, false);
	}

	public Substance{
		Preconditions.checkNotNull( name);
		Preconditions.checkArgument(fluidCapacity>=0);
		Preconditions.checkArgument(pHs.isValid(pH));
	}

	@Override
	public boolean equals(Object o){
		return this==o||o instanceof Substance s&&name.equals(s.name);
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
