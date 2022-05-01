package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootTable;
import tictim.autobotany.crop.condition.CropCondition;
import tictim.autobotany.util.ObjRef;

import java.util.List;

/**
 * Species of crops.
 */
public record Species(
		ResourceLocation name,
		// Numeric tier for convenience.
		Tier tier,
		// Base growth time in "crop ticks"; base is 1 sec = 1 crop tick.
		int baseGrowthTime,
		long targetScore,
		// Genetic complexity of the species.
		int geneComplexity,
		// TODO "influence" yeah cool name
		// Crops with higher influence can slow down weeds more effectively.
		int baseInfluence,
		List<CropCondition<?>> conditions,
		// Amount of yields - TODO is it possible to control everything with loot table options? that'll be pretty poggers
		LootTable yields,
		ObjRef<Item> seed,
		SpeciesVisual visual
) implements DataRegistry.Entry{
	@Override
	public boolean equals(Object o){
		return this==o||o instanceof Species species&&name.equals(species.name);
	}
	@Override
	public int hashCode(){
		return name.hashCode();
	}
	@Override
	public String toString(){
		return name.toString();
	}

	public enum Tier{
		UNCATEGORIZED,
		WILD,
		DOMESTICATED,
		EXOTIC,
		EXTRATERRESTRIAL,
		EXTRAGALACTIC
	}
}
