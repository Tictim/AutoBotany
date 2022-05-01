package tictim.autobotany.util;

/**
 * Rank indicating quality of crops.<br>
 * Note that this is not complete list of possible ranks. Some crops have special conditions that, when satisfied, would give an '+' tier.
 * Ranks beyond S (SS, SSS, SSSS etc.) are also present, but I don't feel like writing 1000+ constant decls today.
 * <table border="1">
 *   <tr>
 *     <th>Rank</th> <th>Min value</th>
 *   </tr>
 *   <tr>
 *     <td>F</td> <td>0 or less</td>
 *   </tr>
 *   <tr>
 *     <td>D</td> <td>1/4 of base score or less</td>
 *   </tr>
 *   <tr>
 *     <td>C</td> <td>1/2 of base score or less</td>
 *   </tr>
 *   <tr>
 *     <td>B</td> <td>base score or less</td>
 *   </tr>
 *   <tr>
 *     <td>A</td> <td>2 times base score or less</td>
 *   </tr>
 *   <tr>
 *     <td>S</td> <td>4 times base score or less</td>
 *   </tr>
 *   <tr>
 *     <td>SS~</td> <td>4*(number of S's) times base score or less</td>
 *   </tr>
 * </table>
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
	 * Optimal result with advanced setup with electronics.
	 */
	int A = 4;
	/**
	 * Optimal result with more advanced setup involving hydroponics and shit.<br>
	 * Yields are just doubled beyond this rank, so S or S+ should be hard limit before player reaches lategame/endgame stuff.
	 */
	int S = 5;

	static int fromScore(long baseScore, long score){
		if(score<=0) return F;
		if(baseScore<=0) return S;
		if(score<baseScore){
			if(score<baseScore/4) return D;
			if(score<baseScore/2) return C;
			return B;
		}else{
			if(score<baseScore*2) return A;
			int resultRank = S;
			while(true){
				baseScore *= 4;
				if(score<baseScore) return resultRank;
				resultRank++;
			}
		}
	}

	static long getMinimumScore(long baseScore, int rank){
		if(baseScore<=0||rank<=F) return 0;
		return switch(rank){
			case D -> baseScore/4;
			case C -> baseScore/2;
			case B -> baseScore;
			case A -> baseScore*2;
			case S -> baseScore*4;
			default -> baseScore*4*(rank-S+1);
		};
	}
}
