package tictim.autobotany.crop.factor;

import net.minecraft.nbt.CompoundTag;
import tictim.autobotany.crop.CropAccess;
import tictim.autobotany.crop.condition.ScoreCondition;

public abstract class DamageFactor<C extends ScoreCondition<C>> extends CropGrowthFactor<C>{
	private int damage;

	protected DamageFactor(C condition){
		super(condition);
	}

	@Override public UpdateOrder updateOrder(){
		return UpdateOrder.DAMAGE;
	}

	@Override public void update(CropAccess cropAccess){
		if(shouldApplyDamage(cropAccess)) this.damage++;
	}

	protected abstract boolean shouldApplyDamage(CropAccess cropAccess);

	@Override public long calculateScore(){
		return (long)(-damage*condition.scoreModifier);
	}

	@Override public void write(CompoundTag tag){
		tag.putInt("Damage", damage);
	}
	@Override public void read(CompoundTag tag){
		this.damage = Math.max(0, tag.getInt("Damage"));
	}
}
