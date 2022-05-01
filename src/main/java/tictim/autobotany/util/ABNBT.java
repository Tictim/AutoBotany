package tictim.autobotany.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;
import tictim.autobotany.AutoBotanyMod;
import tictim.autobotany.data.*;

import javax.annotation.Nullable;

// todo the fucking name
public class ABNBT{
	public static final boolean REPORT_NBT_ERROR = true;

	@Nullable public static ResourceLocation readNamespace(String s){
		ResourceLocation res = ResourceLocation.tryParse(s);
		if(res==null&&REPORT_NBT_ERROR)
			logNbtReadError("Invalid namespace "+s);
		return res;
	}
	public static ResourceLocation readNamespace(String s, ResourceLocation def){
		ResourceLocation parsed = readNamespace(s);
		return parsed==null ? def : parsed;
	}

	@Nullable public static Substance readSubstance(String s){
		return readDataRegistryEntry(s, AllSubstances.SUBSTANCES, "Invalid substance ");
	}
	@Nullable public static Soil readSoil(String s){
		return readDataRegistryEntry(s, AllSoils.SOILS, "Invalid soil ");
	}

	@Nullable private static <T extends DataRegistry.Entry> T readDataRegistryEntry(String s, DataRegistry<T> registry, String errorText){
		T t = registry.getOrNull(s);
		if(t==null&&REPORT_NBT_ERROR) logNbtReadError(errorText+s);
		return t;
	}

	@Nullable public static Fluid readFluid(String key){
		ResourceLocation res = readNamespace(key);
		if(res==null) return null;
		return readFluid(res);
	}
	@Nullable public static Fluid readFluid(ResourceLocation key){
		Fluid fluid = ForgeRegistries.FLUIDS.getValue(key);
		if(fluid==null&&REPORT_NBT_ERROR)
			logNbtReadError("Invalid fluid "+key);
		return fluid;
	}

	public static void logNbtReadError(String message){
		AutoBotanyMod.LOGGER.error("Error in NBT deserialization: {}", message, new Exception());
	}
}
