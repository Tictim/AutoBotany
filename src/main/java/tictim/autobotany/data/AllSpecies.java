package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootTable;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.LootTable.lootTable;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static net.minecraft.world.level.storage.loot.providers.number.ConstantValue.exactly;
import static tictim.autobotany.AutoBotanyMod.MODID;

public class AllSpecies{
	public static final DataRegistry<Species> REGISTRY = new DataRegistry<>(new Species(
			new ResourceLocation(MODID, "default"),
			0,
			0,
			0,
			0,
			0,
			LootTable.EMPTY,
			true
	));

	public static Species a;
	public static Species b;
	public static Species c;

	public static void register(){
		a = REGISTRY.register(new Species(
				new ResourceLocation(MODID, "a"),
				0,
				100*20,
				1,
				10,
				1,
				lootTable().withPool(
						lootPool()
								.setRolls(exactly(1))
								.add(lootTableItem(Items.RABBIT_HIDE))
				)));
		b = REGISTRY.register(new Species(
				new ResourceLocation(MODID, "b"),
				0,
				100*20,
				1,
				10,
				1,
				lootTable().withPool(
						lootPool()
								.setRolls(exactly(1))
								.add(lootTableItem(Items.BLUE_SHULKER_BOX))
				)));
		c = REGISTRY.register(new Species(
				new ResourceLocation(MODID, "c"),
				0,
				100*20,
				1,
				10,
				1,
				lootTable().withPool(
						lootPool()
								.setRolls(exactly(1))
								.add(lootTableItem(Items.PIG_SPAWN_EGG))
				)));
	}
}
