package tictim.autobotany;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import tictim.autobotany.util.SeedProperty;

import java.util.ArrayList;
import java.util.List;

import static tictim.autobotany.AutoBotanyMod.MODID;

@EventBusSubscriber(modid = MODID)
public class ABEventHandler{
	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onUse(RightClickBlock event){
		ItemStack stack = event.getItemStack();
		if(SeedProperty.hasOverride(stack))
			event.setUseItem(Event.Result.DENY);
	}

	private static final SeedItemTooltip tooltip = DistExecutor.safeRunForDist(() -> SeedItemTooltip.Client::new, () -> SeedItemTooltip::new);

	@SubscribeEvent
	public static void onItemTooltip(ItemTooltipEvent event){
		ItemStack stack = event.getItemStack();
		if(stack.isEmpty()||SeedProperty.speciesFromSeedItem(stack.getItem())==null) return;
		SeedProperty seedProperty = SeedProperty.get(stack);
		if(seedProperty==null) return;
		tooltip.addTooltip(event, seedProperty);
	}

	public static class SeedItemTooltip{
		public void addTooltip(ItemTooltipEvent event, SeedProperty seedProperty){
			List<Component> list = new ArrayList<>();
			seedProperty.addTooltip(list, event.getPlayer(), event.getFlags());
			event.getToolTip().addAll(1, list);
		}

		public static final class Client extends SeedItemTooltip{
			@Override public void addTooltip(ItemTooltipEvent event, SeedProperty seedProperty){
				if(Screen.hasShiftDown()) super.addTooltip(event, seedProperty);
				else event.getToolTip().add(1, new TranslatableComponent("tooltip.autobotany.seed.collapsed").withStyle(ChatFormatting.DARK_GRAY));
			}
		}
	}
}
