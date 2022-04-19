package tictim.autobotany.contents;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tictim.autobotany.contents.item.GardenTrowelItem;
import tictim.autobotany.contents.item.HerbariumItem;
import tictim.autobotany.contents.item.SeedItem;
import tictim.autobotany.contents.item.SoilItem;

import static tictim.autobotany.AutoBotanyMod.MODID;

public class ModItems{
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
	public static final CreativeModeTab TAB_TOOLS = new CreativeModeTab(MODID+".tools"){
		@Override public ItemStack makeIcon(){
			return new ItemStack(HERBARIUM.get());
		}
	};
	public static final CreativeModeTab TAB_ENTRY = new CreativeModeTab(MODID+".entry"){
		@Override public ItemStack makeIcon(){
			return new ItemStack(SEED.get());
		}
	};

	public static final RegistryObject<Item> HERBARIUM = REGISTER.register("herbarium", () -> new HerbariumItem(tools()));
	public static final RegistryObject<Item> GARDEN_TROWEL = REGISTER.register("garden_trowel", () -> new GardenTrowelItem(tools()));

	public static final RegistryObject<BlockItem> WOODEN_TRAY = REGISTER.register("wooden_tray", () -> new BlockItem(ModBlocks.WOODEN_TRAY.get(), tools()));

	public static final RegistryObject<SeedItem> SEED = REGISTER.register("seed", () -> new SeedItem(entry()));
	public static final RegistryObject<SoilItem> SOIL = REGISTER.register("soil", () -> new SoilItem(entry()));

	private static Item.Properties tools(){
		return new Item.Properties().tab(TAB_TOOLS);
	}
	private static Item.Properties entry(){
		return new Item.Properties().tab(TAB_ENTRY);
	}
}
