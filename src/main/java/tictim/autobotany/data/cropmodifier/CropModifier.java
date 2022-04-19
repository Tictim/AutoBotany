package tictim.autobotany.data.cropmodifier;

import tictim.autobotany.util.CropAccess;

public sealed abstract class CropModifier permits GrowthModifier{
	public abstract void tick(CropAccess cropAccess);
}

