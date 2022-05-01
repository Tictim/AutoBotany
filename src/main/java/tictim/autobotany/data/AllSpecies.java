package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootTable;
import tictim.autobotany.AutoBotanyMod;
import tictim.autobotany.contents.ModItems;
import tictim.autobotany.crop.condition.CropCondition;
import tictim.autobotany.crop.condition.IrrigationCondition;
import tictim.autobotany.crop.condition.NutrientCondition;
import tictim.autobotany.data.Species.Tier;
import tictim.autobotany.util.FluidMatch;
import tictim.autobotany.util.ObjRef;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.LootTable.lootTable;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static net.minecraft.world.level.storage.loot.providers.number.ConstantValue.exactly;
import static tictim.autobotany.AutoBotanyMod.MODID;
import static tictim.autobotany.data.SpeciesVisual.*;

public class AllSpecies{
	public static final DataRegistry<Species> REGISTRY = new DataRegistry<>(new Species(
			new ResourceLocation(MODID, "default"),
			Tier.UNCATEGORIZED,
			0,
			1,
			0,
			0,
			List.of(),
			LootTable.EMPTY,
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
				lootTable().withPool(
						lootPool()
								.setRolls(exactly(1))
								.add(lootTableItem(Items.RABBIT_HIDE))
				),
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
				lootTable().withPool(
						lootPool()
								.setRolls(exactly(1))
								.add(lootTableItem(Items.BLUE_SHULKER_BOX))
				),
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
				lootTable().withPool(
						lootPool()
								.setRolls(exactly(1))
								.add(lootTableItem(Items.PIG_SPAWN_EGG))
				),
				ObjRef.of(ModItems.C),
				defaultVisual(MODID, "c", 1));

		wheat = register("wheat",
				Tier.DOMESTICATED,
				1440,
				14400,
				1,
				15,
				List.of(
						new IrrigationCondition(new FluidMatch.TagMatch(FluidTags.WATER), 10, 10)
				),
				lootTable().withPool(
						lootPool()
								.setRolls(exactly(1))
								.add(lootTableItem(Items.WHEAT))
				),
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

	private static Species register(String name,
	                                Tier tier,
	                                int baseGrowthTime,
	                                long targetScore,
	                                int geneComplexity,
	                                int baseInfluence,
	                                List<CropCondition<?>> conditions,
	                                LootTable.Builder yields,
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
	                              LootTable.Builder yields,
	                              ObjRef<Item> seed,
	                              SpeciesVisual visual){
		return new Species(new ResourceLocation(MODID, name),
				tier,
				baseGrowthTime,
				targetScore,
				geneComplexity,
				baseInfluence,
				conditions,
				yields.build(),
				seed,
				visual);
	}

	@Nullable private static Map<ResourceLocation, Species> seedToItemMap;

	@Nullable public static Species fromSeedItem(Item item){
		if(seedToItemMap==null){
			seedToItemMap = new HashMap<>();
			for(Species species : REGISTRY.entries()){
				Item seed = species.seed().get();
				if(seed==null) continue;
				ResourceLocation seedName = seed.getRegistryName();
				if(seedName==null) continue;
				if(seedToItemMap.putIfAbsent(seedName, species)!=null)
					AutoBotanyMod.LOGGER.warn("Item {} is registered as seed for both {} and {}",
							seedName, seedToItemMap.get(seedName).name(), species.name());
			}
		}
		ResourceLocation seedName = item.getRegistryName();
		return seedName!=null ? seedToItemMap.get(seedName) : null;
	}
	public static void clearSeedToItemCache(){
		seedToItemMap = null;
	}
}
