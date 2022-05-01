package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;

public record Gene(
		ResourceLocation name
) implements DataRegistry.Entry{
	@Override
	public boolean equals(Object o){
		return this==o||o instanceof Gene g&&name.equals(g.name);
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
