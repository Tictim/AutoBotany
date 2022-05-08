package tictim.autobotany.loot;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;
import tictim.autobotany.data.Species;

import javax.annotation.Nullable;
import java.util.List;

import static tictim.autobotany.AutoBotanyMod.MODID;

public class Loot{
	public static final LootContextParam<Integer> RANK = new LootContextParam<>(new ResourceLocation(MODID, "rank"));
	public static final LootContextParam<Species> SPECIES = new LootContextParam<>(new ResourceLocation(MODID, "species"));
	public static final LootContextParamSet PARAMS = LootContextParamSet.builder()
			.required(RANK)
			.required(SPECIES)
			.optional(LootContextParams.ORIGIN).build();
	public static final int SEED_GIVEBACK_TIME = 30; // 30 sec

	public static LootContext createContext(ServerLevel level, int rank, Species species){
		return createContext(level, rank, species, (Vec3)null);
	}
	public static LootContext createContext(ServerLevel level, int rank, Species species, @Nullable BlockPos origin){
		return createContext(level, rank, species, origin!=null ? new Vec3(origin.getX(), origin.getY(), origin.getZ()) : null);
	}
	public static LootContext createContext(ServerLevel level, int rank, Species species, @Nullable Vec3 origin){
		LootContext.Builder b = new LootContext.Builder(level);
		b.withParameter(RANK, rank).withParameter(SPECIES, species);
		if(origin!=null) b.withParameter(LootContextParams.ORIGIN, origin);
		return b.create(PARAMS);
	}
	public static List<ItemStack> generateYield(Species species, ServerLevel level, int rank){
		return species.yields().getRandomItems(createContext(level, rank, species));
	}

	public static final class Conditions{
		public static final LootItemConditionType MIN_RANK = new LootItemConditionType(new RankCondition.RankSerializer(RankCondition.Min::new));
		public static final LootItemConditionType MAX_RANK = new LootItemConditionType(new RankCondition.RankSerializer(RankCondition.Max::new));
		public static final LootItemConditionType EXACT_RANK = new LootItemConditionType(new RankCondition.RankSerializer(RankCondition.Exact::new));
	}

	public static final class Functions{
		public static final LootItemFunctionType SEED = new LootItemFunctionType(new SeedFunction.SeedSerializer());
		public static final LootItemFunctionType YIELD = new LootItemFunctionType(new YieldFunction.YieldSerializer());
	}

	public static void register(){
		register("min_rank", Conditions.MIN_RANK);
		register("max_rank", Conditions.MAX_RANK);
		register("exact_rank", Conditions.EXACT_RANK);

		register("seed", Functions.SEED);
		register("yield", Functions.YIELD);
	}

	private static void register(String s, LootItemConditionType lootCondition){
		Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(MODID, s), lootCondition);
	}
	private static void register(String s, LootItemFunctionType lootCondition){
		Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(MODID, s), lootCondition);
	}
}
