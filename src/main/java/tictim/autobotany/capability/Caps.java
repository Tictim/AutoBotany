package tictim.autobotany.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class Caps{
	public static final Capability<SolutionHandler> SOLUTION_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});
}
