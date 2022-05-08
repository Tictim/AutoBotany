package tictim.autobotany.network.menu;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.fml.LogicalSide;

public sealed class MenuPacketContract<M extends AbstractContainerMenu>{
	public static <M extends AbstractContainerMenu> P0<M> forClient(int id, PacketProcessor<M> packetProcessor){
		return new P0<>(id, LogicalSide.CLIENT, packetProcessor);
	}
	public static <M extends AbstractContainerMenu, A> P1<M, A> forClient(int id, Serializer.P1<A> serializer,  PacketProcessor<M> packetProcessor){
		return new P1<>(id, LogicalSide.CLIENT, serializer, packetProcessor);
	}
	public static <M extends AbstractContainerMenu, A, B> P2<M, A, B> forClient(int id, Serializer.P2<A, B> serializer, PacketProcessor<M> packetProcessor){
		return new P2<>(id, LogicalSide.CLIENT, serializer, packetProcessor);
	}
	public static <M extends AbstractContainerMenu, A, B, C> P3<M, A, B, C> forClient(int id, Serializer.P3<A, B, C> serializer, PacketProcessor<M> packetProcessor){
		return new P3<>(id, LogicalSide.CLIENT, serializer, packetProcessor);
	}
	public static <M extends AbstractContainerMenu, A, B, C, D> P4<M, A, B, C, D> forClient(int id, Serializer.P4<A, B, C, D> serializer, PacketProcessor<M> packetProcessor){
		return new P4<>(id, LogicalSide.CLIENT, serializer, packetProcessor);
	}

	public static <M extends AbstractContainerMenu> P0<M> forServer(int id, PacketProcessor<M> packetProcessor){
		return new P0<>(id, LogicalSide.SERVER, packetProcessor);
	}
	public static <M extends AbstractContainerMenu, A> P1<M, A> forServer(int id, Serializer.P1<A> serializer,  PacketProcessor<M> packetProcessor){
		return new P1<>(id, LogicalSide.SERVER, serializer, packetProcessor);
	}
	public static <M extends AbstractContainerMenu, A, B> P2<M, A, B> forServer(int id, Serializer.P2<A, B> serializer, PacketProcessor<M> packetProcessor){
		return new P2<>(id, LogicalSide.SERVER, serializer, packetProcessor);
	}
	public static <M extends AbstractContainerMenu, A, B, C> P3<M, A, B, C> forServer(int id, Serializer.P3<A, B, C> serializer, PacketProcessor<M> packetProcessor){
		return new P3<>(id, LogicalSide.SERVER, serializer, packetProcessor);
	}
	public static <M extends AbstractContainerMenu, A, B, C, D> P4<M, A, B, C, D> forServer(int id, Serializer.P4<A, B, C, D> serializer, PacketProcessor<M> packetProcessor){
		return new P4<>(id, LogicalSide.SERVER, serializer, packetProcessor);
	}

	public final int id;
	public final LogicalSide receiverSide;
	public final PacketProcessor<M> packetProcessor;

	public MenuPacketContract(int id, LogicalSide receiverSide, PacketProcessor<M> packetProcessor){
		this.id = id;
		this.receiverSide = receiverSide;
		this.packetProcessor = packetProcessor;
	}

	public static final class P0<M extends AbstractContainerMenu> extends MenuPacketContract<M>{
		public P0(int id, LogicalSide receiverSide, PacketProcessor<M> packetProcessor){
			super(id, receiverSide, packetProcessor);
		}
	}
	public static sealed class Parameterized<M extends AbstractContainerMenu, S> extends MenuPacketContract<M>{
		public final S serializer;
		public Parameterized(int id, LogicalSide receiverSide, S serializer, PacketProcessor<M> packetProcessor){
			super(id, receiverSide, packetProcessor);
			this.serializer = serializer;
		}
	}
	public static final class P1<M extends AbstractContainerMenu, A> extends Parameterized<M, Serializer.P1<A>>{
		public P1(int id, LogicalSide receiverSide, Serializer.P1<A> serializer, PacketProcessor<M> packetProcessor){
			super(id, receiverSide, serializer, packetProcessor);
		}
	}
	public static final class P2<M extends AbstractContainerMenu, A, B> extends Parameterized<M, Serializer.P2<A, B>>{
		public P2(int id, LogicalSide receiverSide, Serializer.P2<A, B> serializer, PacketProcessor<M> packetProcessor){
			super(id, receiverSide, serializer, packetProcessor);
		}
	}
	public static final class P3<M extends AbstractContainerMenu, A, B, C> extends Parameterized<M, Serializer.P3<A, B, C>>{
		public P3(int id, LogicalSide receiverSide, Serializer.P3<A, B, C> serializer, PacketProcessor<M> packetProcessor){
			super(id, receiverSide, serializer, packetProcessor);
		}
	}
	public static final class P4<M extends AbstractContainerMenu, A, B, C, D> extends Parameterized<M, Serializer.P4<A, B, C,  D>>{
		public P4(int id, LogicalSide receiverSide, Serializer.P4<A, B, C, D> serializer, PacketProcessor<M> packetProcessor){
			super(id, receiverSide, serializer, packetProcessor);
		}
	}
}
