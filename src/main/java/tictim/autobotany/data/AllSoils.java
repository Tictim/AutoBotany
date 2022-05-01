package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;

import java.util.Set;

import static java.util.Collections.emptySet;
import static tictim.autobotany.AutoBotanyMod.MODID;

public class AllSoils{
	public static final DataRegistry<Soil> SOILS = new DataRegistry<>(new Soil(
			new ResourceLocation(MODID, "default"),
			SoilShape.REGISTRY.defaultEntry(),
			0xFFFFFF,
			0,
			7,
			SubstanceSet.empty(),
			emptySet(),
			emptySet(),
			true
	));

	public static Soil funny_dirt;
	public static Soil unfunny_dirt;

	public static Soil dirt;
	public static Soil coarse_dirt;
	public static Soil rooted_dirt;
	public static Soil grassy_dirt;
	public static Soil podzol;
	public static Soil mycelium;
	public static Soil sand;
	public static Soil red_sand;
	public static Soil gravel;
	public static Soil clay;
	public static Soil soul_sand;
	public static Soil soul_soil;

	public static void register(){
		funny_dirt = register("funny_dirt",
				SoilShape.FUNNY_DIRT,
				0xFFFFFF,
				100,
				8,
				SubstanceSet.builder()
						.with(AllSubstances.uranium, 1)
						.with(AllSubstances.plutonium, 1)
						.build(),
				emptySet(),
				emptySet());
		unfunny_dirt = register("unfunny_dirt",
				SoilShape.UNFUNNY_DIRT,
				0xFFFFFF,
				300,
				6,
				SubstanceSet.builder()
						.with(AllSubstances.literal_shit, 1)
						.build(),
				emptySet(),
				emptySet());
		dirt = register("dirt",
				SoilShape.DIRT,
				0xFFFFFF,
				100,
				7,
				SubstanceSet.empty(),
				emptySet(),
				emptySet());
		coarse_dirt = register("coarse_dirt",
				SoilShape.COARSE_DIRT,
				0xFFFFFF,
				100,
				7,
				SubstanceSet.empty(),
				emptySet(),
				emptySet());
		rooted_dirt = register("rooted_dirt",
				SoilShape.ROOTED_DIRT,
				0xFFFFFF,
				100,
				7,
				SubstanceSet.empty(),
				emptySet(),
				emptySet());
		grassy_dirt = register("grassy_dirt",
				SoilShape.GRASSY_DIRT,
				0xFFFFFF,
				100,
				7,
				SubstanceSet.empty(),
				emptySet(),
				emptySet());
		podzol = register("podzol",
				SoilShape.PODZOL,
				0xFFFFFF,
				100,
				7,
				SubstanceSet.empty(),
				emptySet(),
				emptySet());
		mycelium = register("mycelium",
				SoilShape.MYCELIUM,
				0xFFFFFF,
				100,
				7,
				SubstanceSet.empty(),
				emptySet(),
				emptySet());
		sand = register("sand",
				SoilShape.SAND,
				0xFFFFFF,
				100,
				7,
				SubstanceSet.empty(),
				emptySet(),
				emptySet());
		red_sand = register("red_sand",
				SoilShape.RED_SAND,
				0xFFFFFF,
				100,
				7,
				SubstanceSet.empty(),
				emptySet(),
				emptySet());
		gravel = register("gravel",
				SoilShape.GRAVEL,
				0xFFFFFF,
				100,
				7,
				SubstanceSet.empty(),
				emptySet(),
				emptySet());
		clay = register("clay",
				SoilShape.CLAY,
				0xFFFFFF,
				100,
				7,
				SubstanceSet.empty(),
				emptySet(),
				emptySet());
		soul_sand = register("soul_sand",
				SoilShape.SOUL_SAND,
				0xFFFFFF,
				100,
				7,
				SubstanceSet.empty(),
				emptySet(),
				emptySet());
		soul_soil = register("soul_soil",
				SoilShape.SOUL_SOIL,
				0xFFFFFF,
				100,
				7,
				SubstanceSet.empty(),
				emptySet(),
				emptySet());
	}

	private static Soil register(String name,
	                             SoilShape shape,
	                             int tint,
	                             int maxIrrigationPerTick,
	                             double pH,
	                             SubstanceSet nutrients,
	                             Set<Ecosystem> microbes,
	                             Set<Species> weeds){
		return SOILS.register(new Soil(new ResourceLocation(MODID, name),
				shape,
				tint,
				maxIrrigationPerTick,
				pH,
				nutrients,
				microbes,
				weeds,
				false));
	}
}
