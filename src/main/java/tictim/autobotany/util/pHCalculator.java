package tictim.autobotany.util;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import tictim.autobotany.data.Substance;
import tictim.autobotany.data.SubstanceSet;

public final class pHCalculator{
	private double totalPH = 0;
	private long totalVolume = 0;

	public pHCalculator add(double pH, double volume){
		this.totalVolume += volume;
		if(pH<pHs.NEUTRAL) this.totalPH += Math.pow(10, -pH)*volume;
		else if(pH>pHs.NEUTRAL) this.totalPH -= Math.pow(10, -14+pH)*volume;
		return this;
	}

	public pHCalculator addBuffer(double volume){
		return add(pHs.NEUTRAL, volume);
	}

	public pHCalculator add(SubstanceSet substances){
		for(Entry<Substance> e : substances.entries()){
			add(e.getKey().pH(), e.getIntValue()/1000.0);
		}
		return this;
	}

	public double calculate(){
		return pHs.clamp(-Math.log10(totalPH/(totalVolume/1000.0)));
	}
}
