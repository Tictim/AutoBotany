package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

/**
 * Species of crops.
 */
public record Species(
		ResourceLocation name,
		// Numeric tier for convenience.
		int tier,
		// Base growth time in ticks
		int baseGrowthTime,
		// Genetic complexity of the species.
		int geneComplexity,
		// TODO "influence" yeah cool name
		//
		int baseInfluence,
		// Number of growth stages. Visuals only.
		// 0 = crops look same from seed to matured form, 1 and onwards = {stages} number of stages when growing, mature form when matured
		int stages,
		// Amount of yields - TODO is it possible to control everything with loot table options? that'll be pretty poggers
		LootTable yields,
		// Whether it's hidden in creative tabs or database
		boolean hidden
) implements DataRegistry.Entry{
	public Species(ResourceLocation name, int tier, int baseGrowthTime, int geneComplexity, int baseInfluence, int stages, LootTable.Builder yields){
		this(name, tier, baseGrowthTime, geneComplexity, baseInfluence, stages, yields.build(), false);
	}

	public ResourceLocation growingCropModel(int stage){
		return new ResourceLocation(name.getNamespace(), "tray_crop/"+name.getPath()+"_"+stage);
	}

	public ResourceLocation matureCropModel(){
		return new ResourceLocation(name.getNamespace(), "tray_crop/"+name.getPath()+"_mature");
	}

	public ResourceLocation seedItemModel(){
		return new ResourceLocation(name.getNamespace(), "item/seed/"+name.getPath());
	}

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
}
