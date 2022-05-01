package tictim.autobotany.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;
import tictim.autobotany.AutoBotanyMod;
import tictim.autobotany.data.DataRegistry;

import javax.annotation.Nullable;
import java.util.Objects;

public abstract class ObjRef<T>{
	@SuppressWarnings("unchecked") public static <T> ObjRef<T> none(){
		return (ObjRef<T>)Empty.INSTANCE;
	}
	public static ObjRef<Item> item(ResourceLocation id){
		return new RegistryRef<>(ForgeRegistries.ITEMS, id);
	}
	public static <T extends IForgeRegistryEntry<T>> ObjRef<T> of(T entry){
		return new Direct<>(entry);
	}
	public static <T extends IForgeRegistryEntry<T>> ObjRef<T> of(RegistryObject<T> registryObject){
		return new RegistryObjectRef<>(registryObject);
	}

	private boolean cacheGenerated;
	@Nullable private T cache;

	@Nullable public T get(){
		checkCache();
		return cache;
	}

	public void checkCache(){
		if(!cacheGenerated){
			cacheGenerated = true;
			cache = search();
			if(cache==null) logError();
		}
	}

	public void clearCache(){
		cacheGenerated = false;
		cache = null;
	}

	@Nullable protected abstract T search();
	protected abstract void logError();

	public static class RegistryRef<T extends IForgeRegistryEntry<T>> extends ObjRef<T>{
		private final IForgeRegistry<T> registry;
		private final ResourceLocation id;

		public RegistryRef(IForgeRegistry<T> registry, ResourceLocation id){
			this.registry = Objects.requireNonNull(registry);
			this.id = id;
		}

		@Nullable @Override protected T search(){
			return registry.getValue(id);
		}
		@Override protected void logError(){
			AutoBotanyMod.LOGGER.error("No entry exists for registry entry {}/{}", registry.getRegistryName(), id);
		}
	}

	public static class RegistryObjectRef<T extends IForgeRegistryEntry<T>> extends ObjRef<T>{
		private final RegistryObject<T> registryObject;

		public RegistryObjectRef(RegistryObject<T> registryObject){
			this.registryObject = registryObject;
		}

		@Nullable @Override protected T search(){
			return registryObject.get();
		}
		@Override protected void logError(){
			ResourceKey<T> key = registryObject.getKey();
			if(key==null) AutoBotanyMod.LOGGER.error("Invalid registry object");
			else AutoBotanyMod.LOGGER.error("No entry exists for registry entry {}/{}", key.registry(), key.getRegistryName());
		}
	}

	public static class DataRef<T extends DataRegistry.Entry> extends ObjRef<T>{
		private final DataRegistry<T> registry;
		private final String registryName;
		private final ResourceLocation id;

		public DataRef(DataRegistry<T> registry, String registryName, ResourceLocation id){
			this.registry = Objects.requireNonNull(registry);
			this.registryName = Objects.requireNonNull(registryName);
			this.id = id;
		}

		@Nullable @Override protected T search(){
			return registry.getOrNull(id);
		}
		@Override protected void logError(){
			AutoBotanyMod.LOGGER.error("No entry exists for data {}/{}", registryName, id);
		}
	}

	public static class Direct<T> extends ObjRef<T>{
		private final T t;

		public Direct(T t){
			this.t = Objects.requireNonNull(t);
		}

		@Override protected T search(){
			return t;
		}
		@Override protected void logError(){}
	}

	public static final class Empty extends ObjRef<Object>{
		public static final Empty INSTANCE = new Empty();

		private Empty(){}
		@Nullable @Override protected Object search(){
			return null;
		}
		@Override protected void logError(){}
	}
}
