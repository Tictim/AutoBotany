package tictim.autobotany.network.menu;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.EmptyByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import tictim.autobotany.AutoBotanyMod;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public record MenuPacket(
		ResourceLocation menuType,
		int menuId,
		int packetId,
		@Nullable Consumer<FriendlyByteBuf> additionalDataInput,
		FriendlyByteBuf additionalDataOut
){
	public MenuPacket(ResourceLocation menuType, int menuId, int packetId, @Nullable Consumer<FriendlyByteBuf> additionalDataInput){
		this(menuType, menuId, packetId, additionalDataInput, EMPTY_BUFFER);
	}

	private static final int ADDITIONAL_DATA_MAX_SIZE = 65535;
	public static final FriendlyByteBuf EMPTY_BUFFER = new FriendlyByteBuf(new EmptyByteBuf(ByteBufAllocator.DEFAULT));

	public static MenuPacket read(FriendlyByteBuf buf){
		return new MenuPacket(buf.readResourceLocation(), buf.readVarInt(), buf.readVarInt(), null, readFriendlyByteBuf(buf));
	}

	private static FriendlyByteBuf readFriendlyByteBuf(FriendlyByteBuf buf){
		int size = buf.readUnsignedShort();
		if(size<=0) return EMPTY_BUFFER;
		return new FriendlyByteBuf(buf.copy(buf.readerIndex(), size));
	}

	public void write(FriendlyByteBuf buf){
		buf.writeResourceLocation(menuType);
		buf.writeVarInt(menuId);
		buf.writeVarInt(packetId);
		if(additionalDataInput!=null){
			int index = buf.writerIndex();
			buf.writeShort(0);
			int index2 = buf.writerIndex();
			additionalDataInput.accept(buf);
			int size = buf.writerIndex()-index2;
			if(size>ADDITIONAL_DATA_MAX_SIZE){
				AutoBotanyMod.LOGGER.error("Additional data of packet "+menuType+"#"+packetId+" exceeds max size of "+ADDITIONAL_DATA_MAX_SIZE+" (size "+size+")");
			}else{
				buf.setShort(index, size);
			}
		}else buf.writeShort(0);
	}
}
