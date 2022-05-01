package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;

import static tictim.autobotany.AutoBotanyMod.MODID;

public class AllGenes{
	public static final DataRegistry<Gene> GENE = new DataRegistry<>(new Gene(
			new ResourceLocation(MODID, "default")
	));
}
