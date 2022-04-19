package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;

import static tictim.autobotany.AutoBotanyMod.MODID;

public class AllSubstances{
	public static final DataRegistry<Substance> SUBSTANCES = new DataRegistry<>(new Substance(
			new ResourceLocation(MODID, "default"),
			0xFFFFFF,
			0,
			7,
			true
	));

	private static Substance uranium;
	private static Substance plutonium;
	private static Substance literal_shit;

	public static void register(){
		uranium = SUBSTANCES.register(new Substance(
				new ResourceLocation(MODID, "uranium"),
				0xFFFFFF,
				0,
				7));
		plutonium = SUBSTANCES.register(new Substance(
				new ResourceLocation(MODID, "plutonium"),
				0xFFFFFF,
				0,
				7));
		literal_shit = SUBSTANCES.register(new Substance(
				new ResourceLocation(MODID, "literal_shit"),
				0xFFFFFF,
				0,
				7));
	}
}
