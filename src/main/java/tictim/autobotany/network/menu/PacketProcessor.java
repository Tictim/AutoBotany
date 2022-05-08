package tictim.autobotany.network.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;

@FunctionalInterface
public interface PacketProcessor<M extends AbstractContainerMenu>{
	void process(M menu, FriendlyByteBuf additionalData);
}
