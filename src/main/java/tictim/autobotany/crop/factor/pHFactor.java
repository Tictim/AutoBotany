package tictim.autobotany.crop.factor;

import tictim.autobotany.crop.CropAccess;
import tictim.autobotany.crop.condition.pHCondition;

public class pHFactor extends DamageFactor<pHCondition>{
	public pHFactor(pHCondition pHCondition){
		super(pHCondition);
	}

	@Override protected boolean shouldApplyDamage(CropAccess cropAccess){
		return !condition.isInRange(cropAccess.pH());
	}
}
