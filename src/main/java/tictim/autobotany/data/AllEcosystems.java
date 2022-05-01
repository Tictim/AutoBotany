package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;

import static tictim.autobotany.AutoBotanyMod.MODID;

public class AllEcosystems{
	public static final DataRegistry<Ecosystem> ECOSYSTEMS = new DataRegistry<>(new Ecosystem(
			new ResourceLocation(MODID, "default"),
			0xFFFFFF,
			SubstanceSet.empty(),
			null
	));
}
