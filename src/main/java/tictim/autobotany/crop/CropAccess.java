package tictim.autobotany.crop;

import net.minecraftforge.fluids.FluidStack;
import tictim.autobotany.data.Species;
import tictim.autobotany.data.SubstanceSet;

public interface CropAccess{
	Species species();
	int growth();

	/**
	 * Fluid provided. Don't modify this.
	 */
	FluidStack irrigation();

	int consumeIrrigation(int maxAmount, boolean simulate);

	/**
	 * @return Temperature
	 */
	int temperature();

	/**
	 * <br>
	 * (Probably should lazy calculate; since some plants just don't give a shit about pH, and pH calculation is not a fast one.)
	 *
	 * @return pH
	 */
	double pH();

	/**
	 * Get nutrient the crop can access. Modifying this SubstanceSet will directly affect amount of nutrients.<Br>
	 * This will get reset in next tick.<br>
	 * 'Base nutrients' provided by soils/ecosystem etc + nutrients provided by irrigation(should calculate portion of consumed irrigation) + whatever else provided by crop factor
	 */
	SubstanceSet.Mutable nutrients();
}
