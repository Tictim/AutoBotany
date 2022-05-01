package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;

public record Hazard(
		ResourceLocation name
) implements DataRegistry.Entry{
	@Override
	public boolean equals(Object o){
		return this==o||o instanceof Hazard h&&name.equals(h.name);
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
