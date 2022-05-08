package tictim.autobotany.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.loot.LootTable;
import tictim.autobotany.crop.condition.CropCondition;
import tictim.autobotany.util.ObjRef;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Species of crops.
 */
public final class Species implements DataRegistry.Entry{
	private final ResourceLocation name;
	// Numeric tier for convenience.
	private final Tier tier;
	// Base growth time in "crop ticks"; base is 1 sec = 1 crop tick.
	private final int baseGrowthTime;
	// Genetic complexity of the species.
	private final long targetScore;
	// TODO "influence" yeah cool name
	// Crops with higher influence can slow down weeds more effectively.
	private final int geneComplexity;
	private final int baseInfluence;
	private final List<CropCondition<?>> conditions;
	private final Supplier<LootTable> yieldsSupplier;
	@Nullable private LootTable yields;
	private final ObjRef<Item> seed;
	private final SpeciesVisual visual;

	public Species(ResourceLocation name, Tier tier, int baseGrowthTime, long targetScore, int geneComplexity, int baseInfluence, List<CropCondition<?>> conditions, Supplier<LootTable> yields, ObjRef<Item> seed, SpeciesVisual visual){
		this.name = Objects.requireNonNull(name);
		this.tier = Objects.requireNonNull(tier);
		this.baseGrowthTime = baseGrowthTime;
		this.targetScore = targetScore;
		this.geneComplexity = geneComplexity;
		this.baseInfluence = baseInfluence;
		this.conditions = Collections.unmodifiableList(conditions);
		this.yieldsSupplier = Objects.requireNonNull(yields);
		this.seed = Objects.requireNonNull(seed);
		this.visual = Objects.requireNonNull(visual);
	}

	public ResourceLocation name(){
		return name;
	}
	public Tier tier(){
		return tier;
	}
	public int baseGrowthTime(){
		return baseGrowthTime;
	}
	public long targetScore(){
		return targetScore;
	}
	public int geneComplexity(){
		return geneComplexity;
	}
	public int baseInfluence(){
		return baseInfluence;
	}
	public List<CropCondition<?>> conditions(){
		return conditions;
	}
	public LootTable yields(){
		if(yields==null)
			yields = yieldsSupplier.get();
		return yields;
	}
	public ObjRef<Item> seed(){
		return seed;
	}
	public SpeciesVisual visual(){
		return visual;
	}

	@Override
	public boolean equals(Object o){
		return this==o||o instanceof Species species&&name.equals(species.name);
	}
	@Override
	public int hashCode(){
		return name.hashCode();
	}
	@Override
	public String toString(){
		return name.toString();
	}

	public enum Tier{
		UNCATEGORIZED,
		WILD,
		DOMESTICATED,
		EXOTIC,
		EXTRATERRESTRIAL,
		EXTRAGALACTIC
	}
}
