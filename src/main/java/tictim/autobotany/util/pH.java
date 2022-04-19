package tictim.autobotany.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import tictim.autobotany.data.Substance;
import tictim.autobotany.data.SubstanceSet;

/**
 * Naming convention can suck my dick
 */
public class pH{
	public static double calculate(SubstanceSet substances, long bufferVolume){
		if(substances.isEmpty()) return 7;
		double totalPH = 0;
		long totalVolume = bufferVolume;

		for(Object2IntMap.Entry<Substance> e : substances.entries()){
			totalVolume += e.getIntValue();
			Substance s = e.getKey();
			if(s.pH()<7){
				totalPH += Math.pow(10, -s.pH())*(e.getIntValue()/1000.0);
			}else if(s.pH()>7){
				totalPH -= Math.pow(10, -14+s.pH())*(e.getIntValue()/1000.0);
			}
		}
		double pH = -Math.log10(totalPH/(totalVolume/1000.0));
		return Double.isNaN(pH) ? 7 : pH;
	}
}
