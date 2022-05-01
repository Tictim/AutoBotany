package tictim.autobotany.util;

import net.minecraft.util.Mth;

/**
 * Temperature, measured in kelvin. 0.15 units of inaccuracy is discarded on kelvin-to-celsius conversion. The existence of fahrenheit is discarded.
 */
public class Temps{
	/**
	 * Minimum value the temperature can be.
	 */
	public static final int ABSOLUTE_ZERO = 0;
	/**
	 * Maximum value the temperature can be. The value might feel a bit tiny, but I find concepts of crops that can only grow inside sun's core quite dubious.
	 */
	public static final int MAX = 10000;

	/**
	 * 0 degree in celsius.
	 */
	public static final int CELSIUS = 273;

	/**
	 * 20 degree in celsius.
	 */
	public static final int ROOM_TEMPERATURE = 293;

	public static boolean isValid(int temp){
		return temp>=ABSOLUTE_ZERO&&temp<=MAX;
	}
	public static int clamp(int temp){
		return Mth.clamp(temp, ABSOLUTE_ZERO, MAX);
	}
}
