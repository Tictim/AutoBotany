package tictim.autobotany;

import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.gui.OverlayRegistry;
import net.minecraftforge.client.model.ForgeModelBakery;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tictim.autobotany.capability.SolutionHandler;
import tictim.autobotany.client.DebugOverlay;
import tictim.autobotany.client.TrayRenderer;
import tictim.autobotany.contents.ModBlockEntities;
import tictim.autobotany.contents.ModBlocks;
import tictim.autobotany.contents.ModItems;
import tictim.autobotany.data.*;
import tictim.autobotany.datagen.BlockModelGen;
import tictim.autobotany.datagen.BlockStateGen;
import tictim.autobotany.datagen.ItemModelGen;
import tictim.autobotany.datagen.LootTableGen;

import static net.minecraft.world.inventory.InventoryMenu.BLOCK_ATLAS;

@Mod(AutoBotanyMod.MODID)
@Mod.EventBusSubscriber(modid = AutoBotanyMod.MODID, bus = Bus.MOD)
public class AutoBotanyMod{
	public static final String MODID = "autobotany";
	public static final Logger LOGGER = LogManager.getLogger("AutoBotany");

	public AutoBotanyMod(){
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		ModBlocks.REGISTER.register(eventBus);
		ModItems.REGISTER.register(eventBus);
		ModBlockEntities.REGISTER.register(eventBus);

		// AllHazards.register();
		AllSubstances.register();
		// AllGenes.register();
		AllSpecies.register();
		// AllEcosystems.register();
		AllSoils.register();
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event){
		event.enqueueWork(() -> {
			for(Species species : AllSpecies.REGISTRY.entries())
				species.seed().checkCache();
		});
	}

	@SubscribeEvent
	public static void registerCapabilities(RegisterCapabilitiesEvent event){
		event.register(SolutionHandler.class);
	}

	@SubscribeEvent
	public static void gatherData(GatherDataEvent event){
		DataGenerator generator = event.getGenerator();
		if(event.includeClient()){
			generator.addProvider(new BlockModelGen(generator, event.getExistingFileHelper()));
			generator.addProvider(new ItemModelGen(generator, event.getExistingFileHelper()));
			generator.addProvider(new BlockStateGen(generator, event.getExistingFileHelper()));
		}
		if(event.includeServer()){
			generator.addProvider(new LootTableGen(generator));
		}
	}

	@Mod.EventBusSubscriber(modid = AutoBotanyMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
	public static class Client{
		@SubscribeEvent
		public static void clientSetup(FMLClientSetupEvent event){
			BlockEntityRenderers.register(ModBlockEntities.WOODEN_TRAY.get(), TrayRenderer::new);
			event.enqueueWork(() -> {
				OverlayRegistry.registerOverlayTop("AutoBotany Overlay", new DebugOverlay());
			});
		}

		@SubscribeEvent
		public static void itemColorHandler(ColorHandlerEvent.Item event){
			event.getItemColors().register(
					(stack, tintIndex) -> ModItems.SOIL.get().getEntryOrDefault(stack).tint(),
					ModItems.SOIL.get());
		}

		@SubscribeEvent
		public static void modelRegistry(ModelRegistryEvent event){
			for(SoilShape soilShape : SoilShape.REGISTRY.entries()){
				ForgeModelBakery.addSpecialModel(soilShape.itemModel());
			}
			for(Species species : AllSpecies.REGISTRY.entries()){
				species.visual().forEachModels(ForgeModelBakery::addSpecialModel);
			}
		}

		@SubscribeEvent
		public static void beforeTextureStitch(TextureStitchEvent.Pre event){
			if(!event.getAtlas().location().equals(BLOCK_ATLAS)) return;
			for(SoilShape soilShape : SoilShape.REGISTRY.entries())
				event.addSprite(soilShape.soilTexture());
		}
	}
}
