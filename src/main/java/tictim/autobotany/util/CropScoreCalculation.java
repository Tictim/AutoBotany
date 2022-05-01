package tictim.autobotany.util;

import java.math.BigInteger;

public class CropScoreCalculation{
	// optimized bigint math
	public static long sum(long[] scores){
		BigInteger bi = null;
		long l = 0;
		for(long s : scores){
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

	private static boolean canAddWithoutOverflow(long l1, long l2){
		long r = l1+l2;
		// HD 2-12 Overflow iff both arguments have the opposite sign of the result
		// stolen from Math.addExact lol
		return ((l1^r)&(l2^r))>=0;
	}

	private static final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);
	private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);

	private static long castToLong(BigInteger bi){
		if(bi.compareTo(LONG_MAX)>0) return Long.MAX_VALUE;
		else if(bi.compareTo(LONG_MIN)<0) return Long.MIN_VALUE;
		else return bi.longValue();
	}
}
