package tictim.autobotany.network;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import tictim.autobotany.network.menu.MenuPacket;
import tictim.autobotany.network.menu.MenuPacketHandler;

import static tictim.autobotany.AutoBotanyMod.MODID;

public class ModNet{
	public static final String VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "master"),
			() -> VERSION,
			VERSION::equals, VERSION::equals);

	public static void register(){
		CHANNEL.registerMessage(0, MenuPacket.class,
				MenuPacket::write, MenuPacket::read, (p, ctx) -> {
					NetworkEvent.Context context = ctx.get();
					context.setPacketHandled(true);
					switch(context.getDirection().getReceptionSide()){
						case CLIENT -> context.enqueueWork(() -> Client.handleMenuPacket(p));
						case SERVER -> {
							ServerPlayer sender = context.getSender();
							if(sender!=null) context.enqueueWork(() -> handleMenuPacket(p, sender));
						}
					}
				});
	}

	private static void handleMenuPacket(MenuPacket p, ServerPlayer sender){
		if(sender.containerMenu instanceof MenuPacketHandler handler)
			handler.handle(p);
	}

	private static class Client{
		private static void handleMenuPacket(MenuPacket p){
			Minecraft mc = Minecraft.getInstance();
			if(mc.player!=null&&mc.player.containerMenu instanceof MenuPacketHandler handler)
				handler.handle(p);
		}
	}
}
