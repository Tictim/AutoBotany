package tictim.autobotany.util;

import net.minecraftforge.fluids.FluidStack;
import tictim.autobotany.data.Pathogen;
import tictim.autobotany.data.Species;
import tictim.autobotany.data.SubstanceSet;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public interface CropAccess{
	Species species();
	int currentGrowth();
	int totalGrowth();

	List<WeedAccess> weedStatus();

	SubstanceSet totalSubstances();

	Set<Pathogen> microbes();
	Set<Species> weedSeeds();
	@Nullable FluidStack baseSolvent();

	/**
	 * @return Celsius
	 */
	double temperature();

	/**
	 * Probably should lazy calculate; since some plants just don't give a shit about pH, and pH calculation is not a fast one.
	 *
	 * @return pH
	 */
	double pH();

	interface WeedAccess{
		Species species();
		int currentGrowth();
		int totalGrowth();
	}
}
