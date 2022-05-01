package tictim.autobotany.datagen;

import net.minecraft.client.renderer.block.model.BlockModel.GuiLight;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import tictim.autobotany.contents.ModItems;
import tictim.autobotany.data.SoilShape;

import java.util.Objects;

import static tictim.autobotany.AutoBotanyMod.MODID;

public class ItemModelGen extends ItemModelProvider{
	public ItemModelGen(DataGenerator generator, ExistingFileHelper existingFileHelper){
		super(generator, MODID, existingFileHelper);
	}

	@Override protected void registerModels(){
		simpleItem(ModItems.HERBARIUM.get());
		simpleHeld(ModItems.GARDEN_TROWEL.get());

		builtinEntity(ModItems.SOIL.get())
				.guiLight(GuiLight.FRONT);

		seed(ModItems.A);
		seed(ModItems.B);
		seed(ModItems.C);

		// TODO change to manual gen instead of blindly looping everything... or check namespace == autobotany?
		soilItem(SoilShape.REGISTRY.defaultEntry());
		soilItem(SoilShape.FUNNY_DIRT);
		soilItem(SoilShape.UNFUNNY_DIRT);
	}

	private void simpleItem(Item item){
		simpleItem(item, ITEM_FOLDER+"/generated");
	}
	private void simpleItem(String name){
		simpleItem(name, ITEM_FOLDER+"/generated");
	}
	private void simpleHeld(Item item){
		simpleItem(item, ITEM_FOLDER+"/handheld");
	}
	private void simpleHeld(String name){
		simpleItem(name, ITEM_FOLDER+"/handheld");
	}
	private void simpleItem(Item item, String parentName){
		ResourceLocation registryName = Objects.requireNonNull(item.getRegistryName());
		simpleItem(registryName.getPath(), parentName);
	}
	private void simpleItem(String name, String parentName){
		withExistingParent(ITEM_FOLDER+"/"+name, parentName)
				.texture("layer0", new ResourceLocation(MODID, ITEM_FOLDER+"/"+name));
	}

	private void soilItem(SoilShape soilShape){
		simpleItem("soil/"+soilShape.name().getPath());
	}

	private ItemModelBuilder builtinEntity(Item item){
		ResourceLocation registryName = Objects.requireNonNull(item.getRegistryName());
		return getBuilder(ITEM_FOLDER+"/"+registryName.getPath())
				.parent(new ModelFile.UncheckedModelFile("builtin/entity"));
	}

	private void seed(RegistryObject<Item> item){
		withExistingParent(ITEM_FOLDER+"/"+item.getId().getPath(), ITEM_FOLDER+"/generated")
				.texture("layer0", new ResourceLocation(MODID, ITEM_FOLDER+"/seed/"+item.getId().getPath().replaceAll("_seed$", "")));
	}
}
