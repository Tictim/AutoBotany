package tictim.autobotany;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
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
import tictim.autobotany.contents.ModBlockEntities;
import tictim.autobotany.contents.ModBlocks;
import tictim.autobotany.contents.ModItems;
import tictim.autobotany.data.*;
import tictim.autobotany.datagen.BlockModelGen;
import tictim.autobotany.datagen.ItemModelGen;
import tictim.autobotany.datagen.LootTableGen;

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
		// AllPathogens.register();
		AllSoils.register();
	}

	@SubscribeEvent
	public static void commonSetup(FMLCommonSetupEvent event){
		LOGGER.info("SUSSY AMONG US 2022 FUNNY MEME MOD!!!11111111!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
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
		}
		if(event.includeServer()){
			generator.addProvider(new LootTableGen(generator));
		}
	}

	@Mod.EventBusSubscriber(modid = AutoBotanyMod.MODID, bus = Bus.MOD, value = Dist.CLIENT)
	public static class Client{
		@SubscribeEvent
		public static void clientSetup(FMLClientSetupEvent event){
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
			for(SoilShape soilShape : SoilShape.REGISTRY.map().values()){
				ForgeModelBakery.addSpecialModel(soilShape.itemModel());
			}
			for(Species species : AllSpecies.REGISTRY.map().values()){
				ForgeModelBakery.addSpecialModel(species.seedItemModel());
				for(int i = 0; i<species.stages(); i++)
					ForgeModelBakery.addSpecialModel(species.growingCropModel(i));
				ForgeModelBakery.addSpecialModel(species.matureCropModel());
			}
		}
	}
}
