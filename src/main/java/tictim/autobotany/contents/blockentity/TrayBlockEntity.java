package tictim.autobotany.contents.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import tictim.autobotany.contents.menu.TrayMenu;
import tictim.autobotany.crop.Tray;

import javax.annotation.Nullable;

import static tictim.autobotany.contents.block.TrayBlock.SOILED;

public class TrayBlockEntity extends BlockEntity implements MenuProvider{
	public static final int INVENTORY_SIZE = 9*2;

	private final Tray tray = new Tray();
	private final SimpleContainer items = new SimpleContainer(INVENTORY_SIZE);

	private boolean syncMarked;
	private int syncDelay;

	public TrayBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state){
		super(type, pos, state);
		tray.setOnChanged(this::onChanged);
		tray.setOnSync(this::sync);
	}

	public Tray getTray(){
		return this.tray;
	}
	public SimpleContainer getItems(){
		return items;
	}

	public void serverTick(Level level, BlockPos pos, BlockState state){
		if(!(level instanceof ServerLevel sl)) return;
		tray.serverTick(sl, pos, state);
		if(syncDelay>0) syncDelay--;
		if(syncMarked&&syncDelay<=0) sync(true);
	}

	protected void onChanged(){
		Level level = getLevel();
		if(level!=null){
			BlockState blockState = getBlockState();
			if(blockState.hasProperty(SOILED)){
				boolean value = blockState.getValue(SOILED);
				if(tray.hasSoil()!=value){
					level.setBlock(getBlockPos(), blockState.setValue(SOILED, !value), 3);
				}
			}
		}
		setChanged();
	}

	protected void sync(boolean immediately){
		if(immediately||syncDelay<=0){
			if(getLevel()!=null&&!getLevel().isClientSide){
				getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);
				syncMarked = false;
				syncDelay = 20;
			}
		}else syncMarked = true;
	}

	public void onBreak(){
		if(getLevel() instanceof ServerLevel sl)
			tray.harvestCrop(sl, getBlockPos());
	}

	@Nullable @Override public Packet<ClientGamePacketListener> getUpdatePacket(){
		return ClientboundBlockEntityDataPacket.create(this);
	}
	@Override public CompoundTag getUpdateTag(){
		CompoundTag tag = new CompoundTag();
		tray.write(tag, false);
		return tag;
	}

	@Override public void load(CompoundTag tag){
		super.load(tag);
		this.tray.read(tag);
		this.items.fromTag(tag.getList("Items", Tag.TAG_COMPOUND));
	}
	@Override protected void saveAdditional(CompoundTag tag){
		super.saveAdditional(tag);
		this.tray.write(tag, true);
		tag.put("Items", this.items.createTag());
	}

	@Override public Component getDisplayName(){
		return new TextComponent("Amogus"); // todo
	}
	@Nullable @Override public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player){
		return new TrayMenu(containerId, inventory, this);
	}
}
