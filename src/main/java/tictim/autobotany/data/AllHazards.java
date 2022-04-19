package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;

import static tictim.autobotany.AutoBotanyMod.MODID;

public class AllHazards{
	public static final DataRegistry<Hazard> HAZARDS = new DataRegistry<>(new Hazard(
			new ResourceLocation(MODID, "default"),
			true
	));
}
