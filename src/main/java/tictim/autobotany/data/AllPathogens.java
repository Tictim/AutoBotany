package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;

import static tictim.autobotany.AutoBotanyMod.MODID;

public class AllPathogens{
	public static final DataRegistry<Pathogen> PATHOGENS = new DataRegistry<>(new Pathogen(
			new ResourceLocation(MODID, "default"),
			0xFFFFFF,
			SubstanceSet.empty(),
			null,
			true
	));
}
