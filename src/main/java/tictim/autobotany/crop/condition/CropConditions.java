package tictim.autobotany.crop.condition;

import net.minecraft.nbt.CompoundTag;
import tictim.autobotany.util.ABNBT;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CropConditions{
	private static final Map<String, Deserializer> deserializers = new HashMap<>();

	static{
		CropConditions.registerType(IrrigationCondition.TYPE, IrrigationCondition::read);
		CropConditions.registerType(NutrientCondition.TYPE, NutrientCondition::read);
		CropConditions.registerType(pHCondition.TYPE, pHCondition::read);
		CropConditions.registerType(TemperatureCondition.TYPE, TemperatureCondition::read);
	}

	public static void registerType(String type, Deserializer deserializer){
		if(type.isEmpty()) throw new IllegalStateException("Empty type is not allowed");
		if(deserializers.putIfAbsent(type, Objects.requireNonNull(deserializer))!=null){
			throw new IllegalStateException("Duplicated crop condition type '"+type+"'");
		}
	}

	public static CropCondition<?> readFromNbt(CompoundTag tag){
		String type = tag.getString("Type");
		if(!type.isEmpty()){
			Deserializer deserializer = deserializers.get(type);
			if(deserializer!=null){
				CropCondition<?> condition = deserializer.read(tag);
				if(condition!=null) return condition;
			}else if(ABNBT.REPORT_NBT_ERROR)
				ABNBT.logNbtReadError("Invalid crop condition type '"+type+'\'');
		}
		return NoCondition.INSTANCE;
	}

	@FunctionalInterface
	public interface Deserializer{
		@Nullable CropCondition<?> read(CompoundTag tag);
	}
}
