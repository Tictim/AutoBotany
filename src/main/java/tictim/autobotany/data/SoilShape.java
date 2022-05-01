package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Objects;

import static tictim.autobotany.AutoBotanyMod.MODID;

public record SoilShape(
		ResourceLocation name,
		ResourceLocation itemModel,
		ResourceLocation soilTexture
) implements DataRegistry.Entry{
	public SoilShape(ResourceLocation name, @Nullable ResourceLocation itemModel, @Nullable ResourceLocation soilTexture){
		this.name = Objects.requireNonNull(name);
		this.itemModel = itemModel!=null ? itemModel : new ResourceLocation(name.getNamespace(), "item/soil/"+name.getPath());
		this.soilTexture = soilTexture!=null ? soilTexture : new ResourceLocation(name.getNamespace(), "autobotany_soil/"+name.getPath());
	}
	public SoilShape(ResourceLocation name){
		this(name, null, null);
	}

	public static final DataRegistry<SoilShape> REGISTRY = new DataRegistry<>(new SoilShape(new ResourceLocation(MODID, "default")));

	public static final SoilShape FUNNY_DIRT = REGISTRY.register(new SoilShape(new ResourceLocation(MODID, "funny_dirt")));
	public static final SoilShape UNFUNNY_DIRT = REGISTRY.register(new SoilShape(new ResourceLocation(MODID, "unfunny_dirt")));

	public static final SoilShape DIRT = REGISTRY.register(new SoilShape(
			new ResourceLocation(MODID, "dirt"),
			new ResourceLocation("block/dirt"),
			new ResourceLocation("block/dirt")
	));
	public static final SoilShape COARSE_DIRT = REGISTRY.register(new SoilShape(
			new ResourceLocation(MODID, "coarse_dirt"),
			new ResourceLocation("block/coarse_dirt"),
			new ResourceLocation("block/coarse_dirt")
	));
	public static final SoilShape ROOTED_DIRT = REGISTRY.register(new SoilShape(
			new ResourceLocation(MODID, "rooted_dirt"),
			new ResourceLocation("block/rooted_dirt"),
			new ResourceLocation("block/rooted_dirt")
	));
	public static final SoilShape GRASSY_DIRT = REGISTRY.register(new SoilShape(
			new ResourceLocation(MODID, "grassy_dirt"),
			new ResourceLocation("block/grass_block"),
			new ResourceLocation("block/grass_block_top")
	));
	public static final SoilShape PODZOL = REGISTRY.register(new SoilShape(
			new ResourceLocation(MODID, "podzol"),
			new ResourceLocation("block/podzol"),
			new ResourceLocation("block/podzol_top")
	));
	public static final SoilShape MYCELIUM = REGISTRY.register(new SoilShape(
			new ResourceLocation(MODID, "mycelium"),
			new ResourceLocation("block/mycelium"),
			new ResourceLocation("block/mycelium_top")
	));
	public static final SoilShape SAND = REGISTRY.register(new SoilShape(
			new ResourceLocation(MODID, "sand"),
			new ResourceLocation("block/sand"),
			new ResourceLocation("block/sand")
	));
	public static final SoilShape RED_SAND = REGISTRY.register(new SoilShape(
			new ResourceLocation(MODID, "red_sand"),
			new ResourceLocation("block/red_sand"),
			new ResourceLocation("block/red_sand")
	));
	public static final SoilShape GRAVEL = REGISTRY.register(new SoilShape(
			new ResourceLocation(MODID, "gravel"),
			new ResourceLocation("block/gravel"),
			new ResourceLocation("block/gravel")
	));
	public static final SoilShape CLAY = REGISTRY.register(new SoilShape(
			new ResourceLocation(MODID, "clay"),
			new ResourceLocation("block/clay"),
			new ResourceLocation("block/clay")
	));
	public static final SoilShape SOUL_SAND = REGISTRY.register(new SoilShape(
			new ResourceLocation(MODID, "soul_sand"),
			new ResourceLocation("block/soul_sand"),
			new ResourceLocation("block/soul_sand")
	));
	public static final SoilShape SOUL_SOIL = REGISTRY.register(new SoilShape(
			new ResourceLocation(MODID, "soul_soil"),
			new ResourceLocation("block/soul_soil"),
			new ResourceLocation("block/soul_soil")
	));
}
