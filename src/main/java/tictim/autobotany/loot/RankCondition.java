package tictim.autobotany.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import tictim.autobotany.util.Rank;

import java.util.function.IntFunction;

public abstract class RankCondition implements LootItemCondition{
	public static LootItemCondition.Builder minRank(int rank){
		return () -> new Min(rank);
	}
	public static LootItemCondition.Builder maxRank(int rank){
		return () -> new Max(rank);
	}
	public static LootItemCondition.Builder exactRank(int rank){
		return () -> new Exact(rank);
	}

	public final int rank;

	public RankCondition(int rank){
		this.rank = rank;
	}

	@Override public boolean test(LootContext lootContext){
		Integer rank = lootContext.getParamOrNull(Loot.RANK);
		return rank!=null&&test(rank);
	}

	protected abstract boolean test(int rank);

	public static class Min extends RankCondition{
		public Min(int rank){
			super(rank);
		}
		@Override protected boolean test(int rank){
			return this.rank<=rank;
		}
		@Override public LootItemConditionType getType(){
			return Loot.Conditions.MIN_RANK;
		}
	}

	public static class Max extends RankCondition{
		public Max(int rank){
			super(rank);
		}
		@Override protected boolean test(int rank){
			return this.rank>=rank;
		}
		@Override public LootItemConditionType getType(){
			return Loot.Conditions.MAX_RANK;
		}
	}

	public static class Exact extends RankCondition{
		public Exact(int rank){
			super(rank);
		}
		@Override protected boolean test(int rank){
			return this.rank==rank;
		}
		@Override public LootItemConditionType getType(){
			return Loot.Conditions.EXACT_RANK;
		}
	}

	public static class RankSerializer implements Serializer<RankCondition>{
		private final IntFunction<RankCondition> constructor;

		public RankSerializer(IntFunction<RankCondition> constructor){
			this.constructor = constructor;
		}

		@Override public void serialize(JsonObject json, RankCondition rankCondition, JsonSerializationContext ctx){
			json.addProperty("rank", Rank.toString(rankCondition.rank));
		}
		@Override public RankCondition deserialize(JsonObject json, JsonDeserializationContext ctx){
			int rank = Rank.fromJsonString(GsonHelper.getAsString(json, "rank"));
			return constructor.apply(rank);
		}
	}
}
