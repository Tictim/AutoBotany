package tictim.autobotany.crop;

import net.minecraftforge.fluids.FluidStack;
import tictim.autobotany.capability.SolutionHandler.FluidStackAndSubstance;
import tictim.autobotany.data.Soil;
import tictim.autobotany.data.Species;
import tictim.autobotany.data.SubstanceSet;
import tictim.autobotany.util.Temps;
import tictim.autobotany.util.pHCalculator;
import tictim.autobotany.util.pHs;

import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.EXECUTE;
import static net.minecraftforge.fluids.capability.IFluidHandler.FluidAction.SIMULATE;

public class TrayCropAccess implements CropAccess{
	private final Tray tray;
	private final Crop crop;

	private int totalIrrigationConsumed;

	private final SubstanceSet.Mutable nutrients;

	private boolean pHGenerated;
	private double pH;

	public TrayCropAccess(Tray tray, Crop crop){
		this.tray = tray;
		this.crop = crop;
		Soil soil = tray.soil();
		this.nutrients = soil!=null ?
				SubstanceSet.mutableCopy(soil.nutrients()) :
				SubstanceSet.newMutable();
	}

	@Override public Species species(){
		return crop.species;
	}
	@Override public int growth(){
		return crop.growth;
	}
	@Override public FluidStack irrigation(){
		return tray.getSolutionContainer().getFluidInTank(0);
	}
	@Override public int consumeIrrigation(int maxAmount, boolean simulate){
		Soil soil = tray.soil();
		FluidStackAndSubstance fluidStackAndSubstance = tray.getSolutionContainer().drainWithResources(
				soil!=null ?
						Math.min(maxAmount, soil.maxIrrigationPerTick()-this.totalIrrigationConsumed) :
						maxAmount,
				simulate ? SIMULATE : EXECUTE);
		if(!simulate){
			this.nutrients.addAll(fluidStackAndSubstance.substances(), false);
			this.totalIrrigationConsumed += fluidStackAndSubstance.fluidStack().getAmount();
		}
		return fluidStackAndSubstance.fluidStack().getAmount();
	}
	@Override public int temperature(){
		return Temps.ROOM_TEMPERATURE; // TODO
	}
	@Override public double pH(){
		if(!pHGenerated){
			pHGenerated = true;
			pHCalculator calc = new pHCalculator()
					.add(pHs.NEUTRAL, irrigation().getAmount())
					.add(tray.getSolutionContainer().getSubstanceInTank(0));
			Soil soil = tray.soil();
			if(soil!=null)
				calc.add(soil.pH(), 8000);
			pH = calc.calculate();
		}
		return pH;
	}
	@Override public SubstanceSet.Mutable nutrients(){
		return nutrients;
	}
}
