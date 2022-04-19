package tictim.autobotany.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import tictim.autobotany.data.SubstanceSet;

public class SolutionContainer implements SolutionHandler{
	protected final int capacity;
	protected final SubstanceSet.Mutable substances;
	protected FluidStack fluid = FluidStack.EMPTY;

	public SolutionContainer(int capacity, int substanceCapacity){
		this.capacity = capacity;
		this.substances = SubstanceSet.newCapped(substanceCapacity);
	}

	@Override public int getTanks(){
		return 1;
	}
	@Override public FluidStack getFluidInTank(int tank){
		return fluid;
	}
	@Override public SubstanceSet getSubstanceInTank(int tank){
		return substances;
	}
	@Override public int getTankCapacity(int tank){
		return capacity;
	}
	@Override public boolean isFluidValid(int tank, FluidStack stack){
		return true;
	}
	@Override public FluidAndSubstance fill(FluidStack resource, SubstanceSet substances, FluidAction action){
		if(resource.isEmpty()||!isFluidValid(0, resource))
			return FluidAndSubstance.NONE;

		if(fluid.isEmpty()){
			int accepted = Math.min(capacity, resource.getAmount());
			SubstanceSet acceptedSubstance = substances.split((double)accepted/resource.getAmount());
			if(!this.substances.addAll(acceptedSubstance, true)) // TODO lol
				return FluidAndSubstance.NONE;
			if(action.execute()){
				this.fluid = new FluidStack(resource, accepted);
				this.substances.addAll(acceptedSubstance, false);
				onContentsChanged();
			}
			return new FluidAndSubstance(accepted, acceptedSubstance);
		}

		if(!fluid.isFluidEqual(resource)) return FluidAndSubstance.NONE;
		int filled = Math.min(capacity-fluid.getAmount(), resource.getAmount());
		SubstanceSet acceptedSubstance = substances.split((double)filled/resource.getAmount());
		if(!this.substances.addAll(acceptedSubstance, true)) // TODO lol
			return FluidAndSubstance.NONE;
		fluid.grow(filled);
		this.substances.addAll(acceptedSubstance, false);
		if(filled>0) onContentsChanged();
		return new FluidAndSubstance(filled, acceptedSubstance);
	}
	@Override public FluidStackAndSubstance drainWithResources(FluidStack resource, FluidAction action){
		if(resource.isEmpty()||!resource.isFluidEqual(fluid))
			return FluidStackAndSubstance.NONE;
		return drainWithResources(resource.getAmount(), action);
	}
	@Override public FluidStackAndSubstance drainWithResources(int maxDrain, FluidAction action){
		int drained = maxDrain;
		if(fluid.getAmount()<drained)
			drained = fluid.getAmount();
		if(drained==0) return FluidStackAndSubstance.NONE;

		FluidStack stack = new FluidStack(fluid, drained);
		SubstanceSet substances = this.substances.split((double)fluid.getAmount()/drained, action.execute());
		if(action.execute()&&drained>0){
			fluid.shrink(drained);
			onContentsChanged();
		}
		return new FluidStackAndSubstance(stack, substances);
	}

	protected void onContentsChanged(){}

	public CompoundTag write(){
		CompoundTag tag = new CompoundTag();
		this.fluid.writeToNBT(tag);
		this.substances.write(tag);
		return tag;
	}

	public void read(CompoundTag tag){
		this.fluid = FluidStack.loadFluidStackFromNBT(tag);
		this.substances.read(tag);
	}

	@Override public String toString(){
		return "SolutionContainer{"+
				"capacity="+capacity+
				", substances="+substances+
				", fluid="+fluid+
				'}';
	}
}
