package tictim.autobotany.crop.factor;

import net.minecraft.nbt.CompoundTag;
import tictim.autobotany.crop.CropAccess;
import tictim.autobotany.crop.condition.CropCondition;

public abstract class CropGrowthFactor<C extends CropCondition<C>>{
	public final C condition;

	protected CropGrowthFactor(C condition){
		this.condition = condition;
	}

	public abstract UpdateOrder updateOrder();

	public abstract void update(CropAccess cropAccess);

	private boolean scoreCached = false;
	private long scoreCache;

	public final long score(){
		if(!scoreCached){
			scoreCached = true;
			scoreCache = calculateScore();
		}
		return scoreCache;
	}

	protected abstract long calculateScore();

	protected void markScoreUpdated(){
		this.scoreCached = false;
	}

	public CompoundTag write(){
		CompoundTag tag = new CompoundTag();
		write(tag);
		return tag;
	}
	public abstract void write(CompoundTag tag);
	public abstract void read(CompoundTag tag);

	public enum UpdateOrder{
		DAMAGE, // Calculate damage before irrigation/nutrient is consumed
		IRRIGATION, // Amount of accepted irrigations also determines nutrient consumption, so do that first
		NUTRIENT,
		ELSE
	}
}
