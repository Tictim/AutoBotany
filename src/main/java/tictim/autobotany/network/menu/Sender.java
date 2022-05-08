package tictim.autobotany.network.menu;

public final class Sender{
	private Sender(){}
	private static final P0 NOOP0 = () -> {};
	private static final P1<?> NOOP1 = (a) -> {};
	private static final P2<?, ?> NOOP2 = (a, b) -> {};
	private static final P3<?, ?, ?> NOOP3 = (a, b, c) -> {};
	private static final P4<?, ?, ?, ?> NOOP4 = (a, b, c, d) -> {};

	public static P0 noOp0(){
		return NOOP0;
	}
	@SuppressWarnings("unchecked") public static <A> P1<A> noOp1(){
		return (P1<A>)NOOP1;
	}
	@SuppressWarnings("unchecked") public static <A, B> P2<A, B> noOp2(){
		return (P2<A, B>)NOOP2;
	}
	@SuppressWarnings("unchecked") public static <A, B, C> P3<A, B, C> noOp3(){
		return (P3<A, B, C>)NOOP3;
	}
	@SuppressWarnings("unchecked") public static <A, B, C, D> P4<A, B, C, D> noOp4(){
		return (P4<A, B, C, D>)NOOP4;
	}

	@FunctionalInterface
	public interface P0{
		void send();
	}
	@FunctionalInterface
	public interface P1<A>{
		void send(A a);
	}
	@FunctionalInterface
	public interface P2<A, B>{
		void send(A a, B b);
	}
	@FunctionalInterface
	public interface P3<A, B, C>{
		void send(A a, B b, C c);
	}
	@FunctionalInterface
	public interface P4<A, B, C, D>{
		void send(A a, B b, C c, D d);
	}
}
