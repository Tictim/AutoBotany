package tictim.autobotany.util;

import net.minecraft.util.Mth;

/**
 * Naming convention can suck my dick
 */
public class pHs{
	public static final double ACID = 0;
	public static final double NEUTRAL = 7;
	public static final double BASE = 14;

	public static boolean isValid(double pH){
		return pH>=ACID&&pH<=BASE;
	}
	public static double clamp(double pH){
		return Double.isNaN(pH) ? NEUTRAL : Mth.clamp(pH, ACID, BASE);
	}
}
