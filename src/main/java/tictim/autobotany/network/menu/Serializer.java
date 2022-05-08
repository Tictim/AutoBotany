package tictim.autobotany.network.menu;

import net.minecraft.network.FriendlyByteBuf;

public final class Serializer{
	private Serializer(){}

	@FunctionalInterface
	public interface P1<A>{
		void serialize(FriendlyByteBuf buf, A a);
	}
	@FunctionalInterface
	public interface P2<A, B>{
		void serialize(FriendlyByteBuf buf, A a, B b);
	}
	@FunctionalInterface
	public interface P3<A, B, C>{
		void serialize(FriendlyByteBuf buf, A a, B b, C c);
	}
	@FunctionalInterface
	public interface P4<A, B, C, D>{
		void serialize(FriendlyByteBuf buf, A a, B b, C c, D d);
	}
}
