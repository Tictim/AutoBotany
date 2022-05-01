package tictim.autobotany.crop.factor;

import tictim.autobotany.crop.CropAccess;
import tictim.autobotany.crop.condition.TemperatureCondition;

public class TemperatureFactor extends DamageFactor<TemperatureCondition>{
	public TemperatureFactor(TemperatureCondition condition){
		super(condition);
	}

	@Override protected boolean shouldApplyDamage(CropAccess cropAccess){
		return !condition.isInRange(cropAccess.temperature());
	}
}
