package tictim.autobotany.crop;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import tictim.autobotany.data.Species;
import tictim.autobotany.util.CropLoot;
import tictim.autobotany.util.Rank;

import java.util.ArrayList;
import java.util.List;

public final class CropResult{
	public static Builder builder(Species species, long score){
		return new Builder(species, score);
	}

	private final Species species;
	private final long score;
	private final int specialFlags;
	private final List<ItemStack> yields;

	public CropResult(Species species, long score, int specialFlags, List<ItemStack> yields){
		this.species = species;
		this.score = score;
		this.specialFlags = specialFlags;
		this.yields = yields;
	}

	public Species species(){
		return species;
	}
	public long score(){
		return score;
	}
	public int specialFlags(){
		return specialFlags;
	}
	public List<ItemStack> yields(){
		return yields;
	}

	private boolean rankGenerated;
	private int rank;

	public int rank(){
		if(!this.rankGenerated){
			this.rank = Rank.fromScore(score, species.targetScore());
			this.rankGenerated = true;
		}
		return this.rank;
	}

	public String getRankString(){
		int rank = rank();
		if(rank<0) return "?";
		else return switch(rank){
			case Rank.F -> "F";
			case Rank.D -> "D";
			case Rank.C -> "C";
			case Rank.B -> "B";
			case Rank.A -> "A";
			case Rank.S -> "S";
			default -> {
				int chars = rank-Rank.S+1;
				StringBuilder stb = new StringBuilder(chars);
				for(; chars>0; chars--) stb.append('S');
				yield stb.toString();
			}
		};
	}

	public static final class Builder{
		private final Species species;
		private final long score;
		private int specialFlags;

		private final List<ItemStack> yields = new ArrayList<>();

		public Builder(Species species, long score){
			this.species = species;
			this.score = score;
		}

		public Builder addGeneratedYields(ServerLevel level){
			this.yields.addAll(CropLoot.generateYield(species, level, Rank.fromScore(score, species.baseGrowthTime())));
			return this;
		}
		public Builder addYields(List<ItemStack> yields){
			this.yields.addAll(yields);
			return this;
		}
		public Builder addSpecialFlags(){
			this.specialFlags++;
			return this;
		}

		public CropResult build(){
			return new CropResult(species, score, specialFlags, yields);
		}
	}
}
