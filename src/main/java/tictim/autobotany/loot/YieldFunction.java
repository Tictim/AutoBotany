package tictim.autobotany.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import tictim.autobotany.util.Rank;

public class YieldFunction implements LootItemFunction{
	public static Builder cropYieldSq(int baseRank){
		return new Builder(baseRank);
	}

	private final int baseRank;

	public YieldFunction(int baseRank){
		this.baseRank = baseRank;
	}

	@Override public LootItemFunctionType getType(){
		return Loot.Functions.YIELD;
	}
	@Override public ItemStack apply(ItemStack stack, LootContext lootContext){
		Integer rank = lootContext.getParamOrNull(Loot.RANK);
		if(rank!=null&&rank>baseRank){
			stack.setCount(stack.getCount()*(rank-baseRank+1));
		}
		return stack;
	}

	public static class YieldSerializer implements Serializer<YieldFunction>{
		@Override public void serialize(JsonObject json, YieldFunction yieldFunction, JsonSerializationContext ctx){
			json.addProperty("baseRank", Rank.toString(yieldFunction.baseRank));
		}
		@Override public YieldFunction deserialize(JsonObject json, JsonDeserializationContext ctx){
			int rank = Rank.fromJsonString(GsonHelper.getAsString(json, "baseRank"));
			return new YieldFunction(rank);
		}
	}

	public static class Builder implements LootItemFunction.Builder{
		private final int baseRank;

		public Builder(int baseRank){
			this.baseRank = baseRank;
		}

		@Override public LootItemFunction build(){
			return new YieldFunction(baseRank);
		}
	}
}
