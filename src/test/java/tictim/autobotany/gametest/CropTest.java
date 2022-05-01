package tictim.autobotany.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.gametest.GameTestHolder;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import tictim.autobotany.AutoBotanyMod;
import tictim.autobotany.crop.Crop;
import tictim.autobotany.crop.CropAccess;
import tictim.autobotany.crop.condition.CropCondition;
import tictim.autobotany.crop.factor.CropGrowthFactor;
import tictim.autobotany.data.Species;
import tictim.autobotany.data.SpeciesVisual;
import tictim.autobotany.util.ObjRef;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static tictim.autobotany.AutoBotanyMod.MODID;

@GameTestHolder(MODID)
public class CropTest{
	private static final List<Species> scoreTestSpecies = List.of(
			createTestSpecies(100, 200, 300),
			createTestSpecies(Long.MAX_VALUE),
			createTestSpecies(Long.MAX_VALUE, Long.MAX_VALUE, Long.MAX_VALUE),
			createTestSpecies(Long.MAX_VALUE, Long.MIN_VALUE, Long.MAX_VALUE),
			createTestSpecies(Long.MIN_VALUE, Long.MIN_VALUE, Long.MIN_VALUE),
			createTestSpecies(Long.MAX_VALUE/3*2, Long.MAX_VALUE/3*2),
			createTestSpecies(Long.MAX_VALUE/3*2, Long.MIN_VALUE/2),
			createTestSpecies(Long.MAX_VALUE/3*2, Long.MIN_VALUE/2, Long.MIN_VALUE/2, Long.MIN_VALUE/2,
					Long.MIN_VALUE/2, Long.MAX_VALUE/3*2, Long.MAX_VALUE/3*2, Long.MAX_VALUE/3*2, Long.MAX_VALUE/3*2)
	);

	@PrefixGameTestTemplate(false)
	@GameTest(template = "empty")
	public static void scoreAlgorithmTest(GameTestHelper helper){
		List<Crop> crops = scoreTestSpecies.stream().map(Crop::new).collect(Collectors.toList());

		for(int i = 0; i<crops.size(); i++){
			Crop crop = crops.get(i);
			long control = ScoreCalc.control(crop.factors);

			long c1 = ScoreCalc.c1(crop.factors);
			if(c1!=control)
				AutoBotanyMod.LOGGER.info("Algorithm c1 differs from control, crop: {}, c1: {}, control: {}", i+1, c1, control);
			long c2 = ScoreCalc.c2(crop.factors);
			if(c2!=control)
				AutoBotanyMod.LOGGER.info("Algorithm c2 differs from control, crop: {}, c2: {}, control: {}", i+1, c2, control);
		}

		helper.succeed();
	}


	@PrefixGameTestTemplate(false)
	@GameTest(template = "empty")
	public static void scoreAlgorithmPerformanceTest(GameTestHelper helper){
		List<Crop> crops = scoreTestSpecies.stream().map(Crop::new).collect(Collectors.toList());

		AutoBotanyMod.LOGGER.info("Performance test");
		for(int i = 0; i<crops.size(); i++){
			Crop crop = crops.get(i);

			long t = System.currentTimeMillis();
			for(int j = 0; j<10000; j++) ScoreCalc.c1(crop.factors);
			AutoBotanyMod.LOGGER.info("{}: c1, {} ms", i+1, System.currentTimeMillis()-t);

			t = System.currentTimeMillis();
			for(int j = 0; j<10000; j++) ScoreCalc.c2(crop.factors);
			AutoBotanyMod.LOGGER.info("{}: c2, {} ms", i+1, System.currentTimeMillis()-t);
		}
		helper.succeed();
	}

	private static Species createTestSpecies(long... scores){
		return createTestSpecies(Arrays.stream(scores).mapToObj(ScoreTestCondition::new).toArray(CropCondition[]::new));
	}
	private static Species createTestSpecies(){
		return createTestSpecies(new CropCondition[0]);
	}
	private static Species createTestSpecies(CropCondition<?>... conditions){
		return new Species(
				new ResourceLocation(MODID, "test_only"),
				Species.Tier.WILD,
				1440,
				14400,
				1,
				15,
				List.of(conditions),
				LootTable.EMPTY,
				ObjRef.none(),
				SpeciesVisual.defaultVisual(MODID, "test_only", 0)
		);
	}

	public static final class ScoreTestCondition extends CropCondition<ScoreTestCondition>{
		public final long score;
		public ScoreTestCondition(long score){
			this.score = score;
		}

		@Override public String type(){
			return "";
		}
		@Override public CropGrowthFactor<ScoreTestCondition> createFactor(){
			return new Factor(this);
		}
		@Override protected void writeInternal(CompoundTag tag){}

		public static final class Factor extends CropGrowthFactor<ScoreTestCondition>{
			public Factor(ScoreTestCondition condition){
				super(condition);
			}

			@Override public UpdateOrder updateOrder(){
				return UpdateOrder.ELSE;
			}
			@Override public void update(CropAccess cropAccess){}
			@Override public long calculateScore(){
				return condition.score;
			}
			@Override public void write(CompoundTag tag){}
			@Override public void read(CompoundTag tag){}
		}
	}
}
