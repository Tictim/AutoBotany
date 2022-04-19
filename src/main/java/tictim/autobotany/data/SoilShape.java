package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;

import static tictim.autobotany.AutoBotanyMod.MODID;

public record SoilShape(ResourceLocation name) implements DataRegistry.Entry{
	@Override public boolean hidden(){
		return false;
	}

	public ResourceLocation itemModel(){
		return new ResourceLocation(name.getNamespace(), "item/soil/"+name.getPath());
	}
	public ResourceLocation soilTexture(){
		return new ResourceLocation(name.getNamespace(), "tray_soil/"+name.getPath());
	}

	public static final DataRegistry<SoilShape> REGISTRY = new DataRegistry<>(new SoilShape(new ResourceLocation(MODID, "default")));

	public static final SoilShape FUNNY_DIRT = create("funny_dirt");
	public static final SoilShape UNFUNNY_DIRT = create("unfunny_dirt");

	public static SoilShape create(String name){
		return REGISTRY.register(new SoilShape(new ResourceLocation(MODID, name)));
	}
}
