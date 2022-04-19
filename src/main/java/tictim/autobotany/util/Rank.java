package tictim.autobotany.util;

/**
 * Rank indicating quality of crops.<br>
 * Note that this is not complete list of possible ranks. Some crops have special conditions that, when satisfied, would give an 'N+' tier.
 * Ranks beyond S (SS, SSS, SSSS etc.) are also present, but I don't feel like write 1000+ lines of constant decl today.
 */
public interface Rank{
	/**
	 * Score for any "plant dead" situation or bailing out.<br>
	 * Planting seed and then uprooting it in set timeframe should result in F+ and give back the seed.
	 */
	int F = 0;
	/**
	 * "Oh no I missed watering my crops" score.
	 */
	int D = 1;
	/**
	 * Most likely result with basic starter setup with wooden tray.
	 */
	int C = 2;
	/**
	 * Optimal result with advanced setup with wooden tray & tamed species.
	 */
	int B = 3;
	/**
	 * Optimal result with advanced setup with electronics.<br>
	 * Default growth rate with default scoring should be lower limit of A
	 */
	int A = 4;
	/**
	 * Optimal result with more advanced setup involving hydroponics and shit.<br>
	 * Yields are just doubled beyond this rank, so S or S+ should be hard limit before player reaches lategame/endgame stuff.
	 */
	int S = 5;
}
