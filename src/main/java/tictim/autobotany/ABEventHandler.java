package tictim.autobotany;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import tictim.autobotany.data.AllSpecies;
import tictim.autobotany.util.SeedProperty;

import static tictim.autobotany.AutoBotanyMod.MODID;

@EventBusSubscriber(modid = MODID)
public class ABEventHandler{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onUse(RightClickBlock event){
		ItemStack stack = event.getItemStack();
		if(!stack.isEmpty()&&SeedProperty.has(stack)&&AllSpecies.fromSeedItem(stack.getItem())!=null)
			event.setUseItem(Event.Result.DENY);
	}
}
