package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import tictim.autobotany.contents.ModItems;
import tictim.autobotany.crop.condition.CropCondition;
import tictim.autobotany.crop.condition.IrrigationCondition;
import tictim.autobotany.crop.condition.NutrientCondition;
import tictim.autobotany.data.Species.Tier;
import tictim.autobotany.loot.SeedFunction;
import tictim.autobotany.util.FluidMatch;
import tictim.autobotany.util.ObjRef;
import tictim.autobotany.util.Rank;

import java.util.List;
import java.util.function.Supplier;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.LootTable.lootTable;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static net.minecraft.world.level.storage.loot.functions.SetItemCountFunction.setCount;
import static net.minecraft.world.level.storage.loot.providers.number.ConstantValue.exactly;
import static net.minecraft.world.level.storage.loot.providers.number.UniformGenerator.between;
import static tictim.autobotany.AutoBotanyMod.MODID;
import static tictim.autobotany.data.SpeciesVisual.*;
import static tictim.autobotany.loot.RankCondition.exactRank;
import static tictim.autobotany.loot.RankCondition.minRank;
import static tictim.autobotany.loot.YieldFunction.cropYieldSq;

public class AllSpecies{
	public static final DataRegistry<Species> REGISTRY = new DataRegistry<>(new Species(
			new ResourceLocation(MODID, "default"),
			Tier.UNCATEGORIZED,
			0,
			1,
			0,
			0,
			List.of(),
			() -> LootTable.EMPTY,
			ObjRef.none(),
			defaultVisual(MODID, "default", 0)
	));

	public static Species a;
	public static Species b;
	public static Species c;

	public static Species wheat;

	public static void register(){
		a = register("a",
				Tier.EXTRAGALACTIC,
				100,
				100,
				1,
				10,
				List.of(
						new NutrientCondition(AllSubstances.uranium, 1, 1)
				),
				() -> lootTable()
						.withPool(lootPool().add(lootTableItem(Items.RABBIT_HIDE).when(minRank(Rank.D)).apply(cropYieldSq(Rank.D))))
						.withPool(lootPool().add(lootTableItem(Items.LEATHER).when(minRank(Rank.A)).apply(cropYieldSq(Rank.A))))
						.withPool(lootPool().add(seed(ModItems.A::get).apply(setCount(exactly(3)))))
						.build(),
				ObjRef.of(ModItems.A),
				defaultVisual(MODID, "a", 1));
		b = register("b",
				Tier.EXTRAGALACTIC,
				100,
				100,
				1,
				10,
				List.of(
						new NutrientCondition(AllSubstances.plutonium, 1, 1)
				),
				() -> lootTable()
						.withPool(lootPool().add(lootTableItem(Items.RED_SHULKER_BOX).when(exactRank(Rank.F))))
						.withPool(lootPool().add(lootTableItem(Items.ORANGE_SHULKER_BOX).when(exactRank(Rank.D))))
						.withPool(lootPool().add(lootTableItem(Items.YELLOW_SHULKER_BOX).when(exactRank(Rank.C))))
						.withPool(lootPool().add(lootTableItem(Items.GREEN_SHULKER_BOX).when(exactRank(Rank.B))))
						.withPool(lootPool().add(lootTableItem(Items.BLUE_SHULKER_BOX).when(exactRank(Rank.A))))
						.withPool(lootPool().add(lootTableItem(Items.CYAN_SHULKER_BOX).when(exactRank(Rank.S))))
						.withPool(lootPool().add(lootTableItem(Items.PURPLE_SHULKER_BOX).when(minRank(Rank.S+1)).apply(cropYieldSq(Rank.S+1))))
						.withPool(lootPool().add(seed(ModItems.B::get).apply(setCount(exactly(1)))))
						.build(),
				ObjRef.of(ModItems.B),
				defaultVisual(MODID, "b", 1));
		c = register("c",
				Tier.EXTRAGALACTIC,
				100,
				100,
				1,
				10,
				List.of(
						new NutrientCondition(AllSubstances.literal_shit, 1, 1)
				),
				() -> lootTable()
						.withPool(lootPool().add(lootTableItem(Items.PIG_SPAWN_EGG).apply(setCount(exactly(3))).when(minRank(Rank.D)).apply(cropYieldSq(Rank.D))))
						.withPool(lootPool().add(lootTableItem(Items.COW_SPAWN_EGG).apply(setCount(exactly(2))).when(exactRank(Rank.C))))
						.withPool(lootPool().add(seed(ModItems.C::get).apply(setCount(exactly(2)))))
						.build(),
				ObjRef.of(ModItems.C),
				defaultVisual(MODID, "c", 1));

		wheat = register("wheat",
				Tier.DOMESTICATED,
				1440,
				14400,
				1,
				15,
				List.of(
						new IrrigationCondition(new FluidMatch.TagMatch(FluidTags.WATER), 3, 10)
				),
				() -> lootTable()
						.withPool(lootPool().add(lootTableItem(Items.WHEAT).when(minRank(Rank.D)).apply(cropYieldSq(Rank.D))))
						.withPool(lootPool().add(seed(Items.WHEAT_SEEDS).apply(setCount(between(0, 2)))))
						.build(),
				ObjRef.of(Items.WHEAT_SEEDS),
				visual().addStage(model("block/wheat_stage0").wilt(0xFF0000))
						.addStage(model("block/wheat_stage1").wilt(0xFF0000))
						.addStage(model("block/wheat_stage2").wilt(0xFF0000))
						.addStage(model("block/wheat_stage3").wilt(0xFF0000))
						.addStage(model("block/wheat_stage4").wilt(0xFF0000))
						.addStage(model("block/wheat_stage5").wilt(0xFF0000))
						.addStage(model("block/wheat_stage6").wilt(0xFF0000))
						.mature(model("block/wheat_stage7").wilt(0xFF0000))
						.build()
		);
	}

	private static LootPoolSingletonContainer.Builder<?> seed(ItemLike itemLike){
		return lootTableItem(itemLike).apply(SeedFunction::new);
	}

	private static Species register(String name,
	                                Tier tier,
	                                int baseGrowthTime,
	                                long targetScore,
	                                int geneComplexity,
	                                int baseInfluence,
	                                List<CropCondition<?>> conditions,
	                                Supplier<LootTable> yields,
	                                ObjRef<Item> seed,
	                                SpeciesVisual visual){
		return REGISTRY.register(create(name, tier, baseGrowthTime, targetScore, geneComplexity, baseInfluence, conditions, yields, seed, visual));
	}

	private static Species create(String name,
	                              Tier tier,
	                              int baseGrowthTime,
	                              long targetScore,
	                              int geneComplexity,
	                              int baseInfluence,
	                              List<CropCondition<?>> conditions,
	                              Supplier<LootTable> yields,
	                              ObjRef<Item> seed,
	                              SpeciesVisual visual){
		return new Species(new ResourceLocation(MODID, name),
				tier,
				baseGrowthTime,
				targetScore,
				geneComplexity,
				baseInfluence,
				conditions,
				yields,
				seed,
				visual);
	}
}
