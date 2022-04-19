package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;

import static java.util.Collections.emptySet;
import static tictim.autobotany.AutoBotanyMod.MODID;

public class AllSoils{
	public static final DataRegistry<Soil> SOILS = new DataRegistry<>(new Soil(
			new ResourceLocation(MODID, "default"),
			SoilShape.REGISTRY.defaultEntry(),
			0xFFFFFF,
			0,
			SubstanceSet.empty(),
			emptySet(),
			emptySet(),
			true
	));

	public static Soil funny_dirt;
	public static Soil unfunny_dirt;

	public static void register(){
		funny_dirt = SOILS.register(new Soil(
				new ResourceLocation(MODID, "funny_dirt"),
				SoilShape.FUNNY_DIRT,
				0xFFFFFF,
				1000,
				SubstanceSet.empty(),
				emptySet(),
				emptySet()
		));
		unfunny_dirt = SOILS.register(new Soil(
				new ResourceLocation(MODID, "unfunny_dirt"),
				SoilShape.UNFUNNY_DIRT,
				0xFFFFFF,
				10000,
				SubstanceSet.empty(),
				emptySet(),
				emptySet()
		));
	}
}
