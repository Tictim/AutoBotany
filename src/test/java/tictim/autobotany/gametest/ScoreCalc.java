package tictim.autobotany.gametest;

import com.google.common.math.LongMath;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import tictim.autobotany.crop.factor.CropGrowthFactor;

import java.math.BigInteger;
import java.util.List;

public class ScoreCalc{
	// The "cringe code", born between the rising sun and the sleep-deprived idiot.
	// Not just being visually retarded, this code also produces wrong results, so that's a kind of achievement I guess.
	// Also this code isn't that fast compared to "optimized bigint math" implementation below.
	// Though bigint math becomes much slower if the total sum exceeds Long.MAX_VALUE by magnitude, such case should be rare.
	// This shitshow is preserved line-by-line to allow future generations to study the effects of sleep deprivation.
	public static long c1(List<CropGrowthFactor<?>> factors){
		// i didn't wrote this down yet but i have a feeling that i will write some really unbased and cringe code
		long totalScore = 0;
		// To prevent overflow, two distinct modes are used:
		// At start, add shit over until overflows. If the operation finishes before overflowing, then that's that.
		// If it overflows, then it is handled in method below.
		for(int i = 0; i<factors.size(); i++){
			CropGrowthFactor<?> f = factors.get(i);
			long score = f.score();
			if(canAddWithoutOverflow(totalScore, score)){
				totalScore += score;
			}else return calculateFuckingScore(factors, i+1, totalScore, score);
		}
		return totalScore;
	}

	private static long calculateFuckingScore(List<CropGrowthFactor<?>> factors, int startIndex, long alreadyCalculatedSum, long lastSum){
		// i was right, this is fucking cringe
		LongList positives = new LongArrayList(), negatives = new LongArrayList();
		(alreadyCalculatedSum<0 ? negatives : positives).add(alreadyCalculatedSum);

		for(int i = startIndex; i<factors.size(); i++){
			CropGrowthFactor<?> f = factors.get(i);
			long score = f.score();
			if(score==0) continue;
			if(score>0!=lastSum>0||canAddWithoutOverflow(lastSum, score)){ // no overflow guaranteed
				lastSum += score;
			}else{
				(lastSum<0 ? negatives : positives).add(lastSum);
				lastSum = score;
			}
		}
		// is this even faster than bigint math? i need to fucking sleep man, gn everybody
		while(true){
			if(positives.isEmpty()) return saturatedSum(negatives, Long.MIN_VALUE);
			if(negatives.isEmpty()) return saturatedSum(positives, Long.MAX_VALUE);
			long n = positives.getLong(positives.size()-1)+negatives.getLong(negatives.size()-1);
			if(n==0){
				positives.removeLong(positives.size()-1);
				negatives.removeLong(negatives.size()-1);
			}else if(n<0){
				positives.removeLong(positives.size()-1);
				negatives.set(negatives.size()-1, n);
			}else{
				positives.set(positives.size()-1, n);
				negatives.removeLong(negatives.size()-1);
			}
		}
	}

	private static long saturatedSum(LongList longs, long overflow){
		long sum = 0;
		for(int i = 0; i<longs.size(); i++){
			//noinspection UnstableApiUsage
			sum = LongMath.saturatedAdd(sum, longs.getLong(i));
			if(sum==overflow) break;
		}
		return sum;
	}

	private static boolean canAddWithoutOverflow(long l1, long l2){
		long r = l1+l2;
		// HD 2-12 Overflow iff both arguments have the opposite sign of the result
		// stolen from Math.addExact lol
		return ((l1^r)&(l2^r))>=0;
	}

	// optimized bigint math
	public static long c2(List<CropGrowthFactor<?>> factors){
		BigInteger bi = null;
		long l = 0;
		for(CropGrowthFactor<?> factor : factors){
			long s = factor.score();
			if(canAddWithoutOverflow(l, s)){
				l += s;
			}else{
				if(bi==null) bi = BigInteger.valueOf(l);
				else bi = bi.add(BigInteger.valueOf(l));
				l = s;
			}
		}
		if(bi==null) return l;
		bi = bi.add(BigInteger.valueOf(l)); // fuck you java, you suck
		return castToLong(bi);
	}

	// brute-force bigint math
	public static long control(List<CropGrowthFactor<?>> factors){
		return castToLong(factors.stream().reduce(BigInteger.ZERO,
				(i, cropGrowthFactor) -> i.add(BigInteger.valueOf(cropGrowthFactor.score())),
				(i, i2) -> i));
	}

	private static final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);
	private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);

	private static long castToLong(BigInteger bi){
		if(bi.compareTo(LONG_MAX)>0) return Long.MAX_VALUE;
		else if(bi.compareTo(LONG_MIN)<0) return Long.MIN_VALUE;
		else return bi.longValue();
	}
}
