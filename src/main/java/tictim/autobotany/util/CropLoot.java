package tictim.autobotany.util;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import tictim.autobotany.data.Species;

import javax.annotation.Nullable;
import java.util.List;

import static tictim.autobotany.AutoBotanyMod.MODID;

public class CropLoot{
	public static final LootContextParam<Integer> RANK = new LootContextParam<>(new ResourceLocation(MODID, "rank"));

	public static final LootContextParamSet PARAMS = LootContextParamSet.builder()
			.required(RANK).optional(LootContextParams.ORIGIN).build();

	public static final int SEED_GIVEBACK_TIME = 30; // 30 sec

	public static LootContext createContext(ServerLevel level, int rank){
		return createContext(level, rank, (Vec3)null);
	}
	public static LootContext createContext(ServerLevel level, int rank, @Nullable BlockPos origin){
		return createContext(level, rank, origin!=null ? new Vec3(origin.getX(), origin.getY(), origin.getZ()) : null);
	}
	public static LootContext createContext(ServerLevel level, int rank, @Nullable Vec3 origin){
		LootContext.Builder b = new LootContext.Builder(level);
		b.withParameter(RANK, rank);
		if(origin!=null) b.withParameter(LootContextParams.ORIGIN, origin);
		return b.create(PARAMS);
	}

	public static List<ItemStack> generateYield(Species species, ServerLevel level, int rank){
		return species.yields().getRandomItems(createContext(level, rank));
	}
}
