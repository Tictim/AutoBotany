package tictim.autobotany.contents.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import tictim.autobotany.crop.Tray;

import javax.annotation.Nullable;

import static tictim.autobotany.contents.block.TrayBlock.SOILED;

public class TrayBlockEntity extends BlockEntity{
	private final Tray tray = new Tray();

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
		tray.read(tag);
	}
	@Override protected void saveAdditional(CompoundTag tag){
		super.saveAdditional(tag);
		tray.write(tag, true);
	}
}
