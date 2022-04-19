package tictim.autobotany.contents.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import tictim.autobotany.capability.SolutionContainer;
import tictim.autobotany.contents.ModItems;
import tictim.autobotany.data.AllSoils;
import tictim.autobotany.data.AllSpecies;
import tictim.autobotany.data.Soil;
import tictim.autobotany.data.Species;
import tictim.autobotany.util.CropLoot;
import tictim.autobotany.util.Rank;

import javax.annotation.Nullable;
import java.util.List;

public class TrayBlockEntity extends BlockEntity{
	private static final int SEED_GIVEBACK_TIME = 20*30; // 30 sec

	@Nullable private SoilData soilData;
	@Nullable private CropData cropData;

	private boolean syncMarked;
	private int syncDelay;

	public TrayBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state){
		super(type, pos, state);
	}

	public boolean hasSoil(){
		return soilData!=null;
	}
	@Nullable public SoilData getSoilData(){
		return soilData;
	}
	public void setSoil(@Nullable Soil soil){
		this.soilData = soil==null ? null : new SoilData(soil);
		setChanged();
		sync();
	}

	public boolean hasCrop(){
		return cropData!=null;
	}
	@Nullable public CropData getCropData(){
		return cropData;
	}
	public void plant(Species species){
		this.cropData = new CropData(species);
		setChanged();
		sync();
	}

	public void harvestCrop(ServerLevel level, BlockPos pos){
		dropShit(level, pos);
		this.cropData = null;
		setChanged();
		sync();
	}

	public void serverTick(Level level, BlockPos pos, BlockState state){
		if(!(level instanceof ServerLevel)) return;
		boolean save = false;
		if(cropData!=null){
			if(cropData.species.baseGrowthTime()>cropData.growth){
				cropData.growth++;
				save = true;
			}
		}
		if(save){
			setChanged(level, pos, state);
			syncMarked = true;
		}
		if(syncDelay>0) syncDelay--;
		if(syncMarked&&syncDelay<=0) sync();
	}

	protected void dropShit(ServerLevel level, BlockPos pos){
		if(this.cropData!=null){
			List<ItemStack> yields = CropLoot.generateYield(this.cropData.species, level, this.cropData.rank());
			if(this.cropData.growth<SEED_GIVEBACK_TIME)
				yields.add(ModItems.SEED.get().createStack(this.cropData.species));
			for(ItemStack stack : yields){
				level.addFreshEntity(new ItemEntity(level, pos.getX()+.5, pos.getY()+.75, pos.getZ()+.5, stack));
			}
		}
	}

	protected void sync(){
		if(getLevel()!=null&&!getLevel().isClientSide){
			getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 0);
			syncMarked = false;
			syncDelay = 20;
		}
	}

	public void onBreak(){
		if(getLevel() instanceof ServerLevel sl)
			harvestCrop(sl, getBlockPos());
	}

	@Nullable @Override public Packet<ClientGamePacketListener> getUpdatePacket(){
		return ClientboundBlockEntityDataPacket.create(this);
	}
	@Override public CompoundTag getUpdateTag(){
		CompoundTag tag = new CompoundTag();
		write(tag);
		return tag;
	}

	@Override public void load(CompoundTag tag){
		super.load(tag);
		read(tag);
	}
	@Override protected void saveAdditional(CompoundTag tag){
		super.saveAdditional(tag);
		write(tag);
	}

	protected void write(CompoundTag tag){
		if(soilData!=null) tag.put("SoilData", soilData.write());
		if(cropData!=null) tag.put("CropData", cropData.write());
	}
	protected void read(CompoundTag tag){
		this.soilData = tag.contains("SoilData", Tag.TAG_COMPOUND) ? new SoilData(tag.getCompound("SoilData")) : null;
		this.cropData = tag.contains("CropData", Tag.TAG_COMPOUND) ? new CropData(tag.getCompound("CropData")) : null;
	}

	public static final class SoilData{
		public final Soil soil;
		public final SolutionContainer solutionContainer;

		public SoilData(Soil soil){
			this.soil = soil;
			this.solutionContainer = new SolutionContainer(soil.fluidCapacity(), Integer.MAX_VALUE);
		}
		public SoilData(CompoundTag tag){
			this(AllSoils.SOILS.getOrDefault(tag.getString("Soil")));
			this.solutionContainer.read(tag.getCompound("Solution"));
		}

		public CompoundTag write(){
			CompoundTag tag = new CompoundTag();
			tag.putString("Soil", soil.name().toString());
			tag.put("Solution", solutionContainer.write());
			return tag;
		}

		@Override public String toString(){
			return "SoilData{"+
					"soil="+soil+
					", solutionContainer="+solutionContainer+
					'}';
		}
	}

	public static final class CropData{
		public final Species species;
		public int growth;

		public CropData(Species species){
			this.species = species;
		}
		public CropData(CompoundTag tag){
			this.species = AllSpecies.REGISTRY.getOrDefault(tag.getString("Species"));
			this.growth = tag.getInt("Growth");
		}

		public int rank(){
			return isFullyGrown() ? Rank.A : Rank.F;
		}

		public boolean isFullyGrown(){
			return growth>=species.baseGrowthTime();
		}

		public CompoundTag write(){
			CompoundTag tag = new CompoundTag();
			tag.putString("Species", species.name().toString());
			tag.putInt("Growth", growth);
			return tag;
		}

		@Override public String toString(){
			return "CropData{"+
					"species="+species+
					", growth="+growth+
					'}';
		}
	}
}
