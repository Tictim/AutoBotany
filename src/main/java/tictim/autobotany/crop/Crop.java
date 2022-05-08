package tictim.autobotany.crop;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import tictim.autobotany.crop.condition.CropCondition;
import tictim.autobotany.crop.condition.CropConditions;
import tictim.autobotany.crop.factor.CropGrowthFactor;
import tictim.autobotany.data.AllSpecies;
import tictim.autobotany.data.Species;
import tictim.autobotany.loot.Loot;
import tictim.autobotany.util.CropScoreCalculation;
import tictim.autobotany.util.Rank;
import tictim.autobotany.util.SeedProperty;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public final class Crop{
	public final Species species;
	public int growth;
	public int cropTick;

	public final List<CropGrowthFactor<?>> factors = new ArrayList<>();

	public Crop(Species species){
		this.species = species;

		for(CropCondition<?> c : this.species.conditions())
			this.factors.add(c.createFactor());
		sortFactor();
	}
	public Crop(CompoundTag tag){
		this.species = AllSpecies.REGISTRY.getOrDefault(tag.getString("Species"));
		this.growth = tag.getInt("Growth");
		this.cropTick = tag.getInt("CropTick");
		ListTag list = tag.getList("Factors", Tag.TAG_COMPOUND);
		for(int i = 0; i<list.size(); i++){
			CompoundTag conditionTag = list.getCompound(i);
			CropCondition<?> condition = CropConditions.readFromNbt(conditionTag);
			CropGrowthFactor<?> factor = condition.createFactor();
			factor.read(conditionTag.getCompound("Factor"));
			this.factors.add(factor);
		}
		sortFactor();
	}
	private void sortFactor(){
		this.factors.sort(Comparator.comparing(CropGrowthFactor::updateOrder));
	}

	public boolean isFullyGrown(){
		return species.baseGrowthTime()<=growth;
	}

	public double growthPercent(){
		return (double)this.growth/this.species.baseGrowthTime();
	}

	@Nullable private long[] totalScoreCache;
	private long totalScore;

	public long totalScore(){
		long[] scores = new long[factors.size()];
		for(int i = 0; i<factors.size(); i++){
			scores[i] = factors.get(i).score();
		}
		if(!Arrays.equals(scores, totalScoreCache)){
			totalScoreCache = scores;
			totalScore = CropScoreCalculation.sum(scores);
		}
		return totalScore;
	}

	public boolean isWilting(){
		long minimumScore = Rank.getMinimumScore(species.targetScore(), Rank.C);
		if(isFullyGrown()) return totalScore()<minimumScore;
		return totalScore() < (long)(growthPercent()*minimumScore);
	}

	public CropResult createResult(ServerLevel level){
		long score = totalScore();
		return growth<=Loot.SEED_GIVEBACK_TIME&&
				Rank.fromScore(score, species.targetScore())==Rank.F ?
				CropResult.builder(species, score)
						.addYields(createSeed(1))
						.addSpecialFlags()
						.build() :
				CropResult.builder(species, score)
						.addGeneratedYields(level)
						.build();
	}

	public List<ItemStack> createSeed(int count){
		if(count<=0) return List.of();
		Item seed = species.seed().get();
		if(seed==null) return List.of();
		ItemStack stack = new ItemStack(seed, count);
		SeedProperty.create(species).save(stack);
		return List.of(stack); // TODO
	}

	public CompoundTag write(boolean save){
		CompoundTag tag = new CompoundTag();
		tag.putString("Species", species.name().toString());
		tag.putInt("Growth", growth);
		if(save){
			tag.putInt("CropTick", cropTick);
			ListTag list = new ListTag();
			for(CropGrowthFactor<?> f : this.factors){
				CompoundTag conditionTag = f.condition.write();
				conditionTag.put("Factor", f.write());
				list.add(list.size(), conditionTag);
			}
			tag.put("Factors", list);
		}
		return tag;
	}

	@Override public String toString(){
		return "Crop{"+
				"species="+species+
				", growth="+growth+
				", cropTick="+cropTick+
				", factors="+factors+
				'}';
	}
}
