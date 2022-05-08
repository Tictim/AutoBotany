package tictim.autobotany.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

public class SeedFunction implements LootItemFunction{
	@Override public LootItemFunctionType getType(){
		return Loot.Functions.SEED;
	}
	@Override public ItemStack apply(ItemStack stack, LootContext lootContext){
		// TODO
		return stack;
	}
	public static class SeedSerializer implements Serializer<SeedFunction>{
		@Override public void serialize(JsonObject json, SeedFunction seedFunction, JsonSerializationContext ctx){}
		@Override public SeedFunction deserialize(JsonObject json, JsonDeserializationContext ctx){
			return new SeedFunction();
		}
	}
}
