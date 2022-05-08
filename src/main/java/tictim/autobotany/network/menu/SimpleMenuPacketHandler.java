package tictim.autobotany.network.menu;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.PacketDistributor;
import tictim.autobotany.AutoBotanyMod;
import tictim.autobotany.network.ModNet;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public final class SimpleMenuPacketHandler<M extends AbstractContainerMenu> implements MenuPacketHandler{
	private final M menu;
	private final Player player;
	@Nullable private final LogicalSide exclusiveReceiverSide;

	private final Int2ObjectOpenHashMap<MenuPacketContract<M>> contracts = new Int2ObjectOpenHashMap<>();

	public SimpleMenuPacketHandler(M menu, Player player){
		this.menu = menu;
		this.player = player;
		this.exclusiveReceiverSide = player.level.isClientSide ? LogicalSide.CLIENT : LogicalSide.SERVER;
	}

	public boolean canReceivePacketFrom(LogicalSide side){
		return exclusiveReceiverSide==null||exclusiveReceiverSide==side;
	}

	@Override public void handle(MenuPacket packet){
		if(!packet.menuType().equals(menu.getType().getRegistryName())) return;
		MenuPacketContract<M> c = contracts.get(packet.packetId());
		if(c!=null) c.packetProcessor.process(menu, packet.additionalDataOut());
	}

	public boolean register(MenuPacketContract<M> contract){
		if(this.contracts.putIfAbsent(contract.id, contract)==null) return true;
		AutoBotanyMod.LOGGER.info("Duplicated registration of MenuPacket {}#{}", menu.getType().getRegistryName(), contract.id);
		return false;
	}
	public Sender.P0 register(MenuPacketContract.P0<M> contract){
		return register((MenuPacketContract<M>)contract)&&!canReceivePacketFrom(contract.receiverSide) ?
				() -> send(contract.receiverSide, contract.id, null) : Sender.noOp0();
	}
	public <A> Sender.P1<A> register(MenuPacketContract.P1<M, A> contract){
		return register((MenuPacketContract<M>)contract)&&!canReceivePacketFrom(contract.receiverSide) ?
				(a) -> send(contract.receiverSide, contract.id, buf -> contract.serializer.serialize(buf, a)) :
				Sender.noOp1();
	}
	public <A, B> Sender.P2<A, B> register(MenuPacketContract.P2<M, A, B> contract){
		return register((MenuPacketContract<M>)contract)&&!canReceivePacketFrom(contract.receiverSide) ?
				(a, b) -> send(contract.receiverSide, contract.id, buf -> contract.serializer.serialize(buf, a, b)) :
				Sender.noOp2();
	}
	public <A, B, C> Sender.P3<A, B, C> register(MenuPacketContract.P3<M, A, B, C> contract){
		return register((MenuPacketContract<M>)contract)&&!canReceivePacketFrom(contract.receiverSide) ?
				(a, b, c) -> send(contract.receiverSide, contract.id, buf -> contract.serializer.serialize(buf, a, b, c)) :
				Sender.noOp3();
	}
	public <A, B, C, D> Sender.P4<A, B, C, D> register(MenuPacketContract.P4<M, A, B, C, D> contract){
		return register((MenuPacketContract<M>)contract)&&!canReceivePacketFrom(contract.receiverSide) ?
				(a, b, c, d) -> send(contract.receiverSide, contract.id, buf -> contract.serializer.serialize(buf, a, b, c, d)) :
				Sender.noOp4();
	}

	private void send(LogicalSide receiverSide, int id, @Nullable Consumer<FriendlyByteBuf> additionalData){
		switch(receiverSide){
			case CLIENT -> sendToClient(id, additionalData);
			case SERVER -> sendToServer(id, additionalData);
		}
	}
	private void sendToClient(int id, @Nullable Consumer<FriendlyByteBuf> additionalData){
		ResourceLocation n = menu.getType().getRegistryName();
		if(n==null) return;
		if(player instanceof ServerPlayer serverPlayer)
			ModNet.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new MenuPacket(n, menu.containerId, id, additionalData));
	}
	private void sendToServer(int id, @Nullable Consumer<FriendlyByteBuf> additionalData){
		ResourceLocation n = menu.getType().getRegistryName();
		if(n==null) return;
		ModNet.CHANNEL.sendToServer(new MenuPacket(n, menu.containerId, id, additionalData));
	}
}
