package tictim.autobotany.capability;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import tictim.autobotany.data.SubstanceSet;

public interface SolutionHandler extends IFluidHandler{
	@Override int getTanks();

	@Override FluidStack getFluidInTank(int tank);
	/**
	 * @param tank Tank index
	 * @return Substance dissolved in fluid, empty substance if there's no fluid.
	 */
	SubstanceSet getSubstanceInTank(int tank);

	@Override int getTankCapacity(int tank);
	@Override boolean isFluidValid(int tank, FluidStack stack);

	FluidAndSubstance fill(FluidStack resource, SubstanceSet substances, FluidAction action);
	FluidStackAndSubstance drainWithResources(FluidStack resource, FluidAction action);
	FluidStackAndSubstance drainWithResources(int maxDrain, FluidAction action);

	@Override default int fill(FluidStack resource, FluidAction action){
		return fill(resource, SubstanceSet.empty(), action).fluidAmount();
	}
	@Override default FluidStack drain(FluidStack resource, FluidAction action){
		return drainWithResources(resource, action).fluidStack();
	}
	@Override default FluidStack drain(int maxDrain, FluidAction action){
		return drainWithResources(maxDrain, action).fluidStack();
	}

	record FluidAndSubstance(int fluidAmount, SubstanceSet substances){
		public static FluidAndSubstance NONE = new FluidAndSubstance(0, SubstanceSet.empty());
	}
	record FluidStackAndSubstance(FluidStack fluidStack, SubstanceSet substances){
		public static FluidStackAndSubstance NONE = new FluidStackAndSubstance(FluidStack.EMPTY, SubstanceSet.empty());
	}
}
