package tictim.autobotany.contents;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tictim.autobotany.contents.menu.TrayMenu;

import static tictim.autobotany.AutoBotanyMod.MODID;

public class ModMenus{
	public static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.CONTAINERS, MODID);

	public static final RegistryObject<MenuType<TrayMenu>> TRAY = REGISTER.register("tray", () -> new MenuType<>(TrayMenu::new));
}
