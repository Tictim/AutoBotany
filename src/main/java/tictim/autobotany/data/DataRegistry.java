package tictim.autobotany.data;

import com.google.common.base.Preconditions;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DataRegistry<T extends DataRegistry.Entry>{
	private final Map<ResourceLocation, T> map = new HashMap<>();
	private final Map<ResourceLocation, T> view = Collections.unmodifiableMap(map);

	private final T defaultEntry;

	public DataRegistry(T defaultEntry){
		Preconditions.checkNotNull(defaultEntry, "Default entry must be specified for SimpleRegistry");
		this.defaultEntry = defaultEntry;
		map.put(defaultEntry.name(), defaultEntry);
	}

	public Map<ResourceLocation, T> map(){
		return view;
	}
	public T defaultEntry(){
		return defaultEntry;
	}

	public Collection<T> entries(){
		return view.values();
	}

	public T register(T newEntry){
		if(map.putIfAbsent(newEntry.name(), newEntry)!=null)
			throw new IllegalStateException("Duplicated registration of entry "+newEntry.name()+" ("+newEntry+")");
		return newEntry;
	}

	@Nullable public T getOrNull(String name){
		ResourceLocation res = ResourceLocation.tryParse(name);
		return res==null ? null : map.get(res);
	}
	public T getOrDefault(String name){
		return getOrDefault(name, defaultEntry);
	}
	public T getOrDefault(String name, T defaultSoil){
		ResourceLocation res = ResourceLocation.tryParse(name);
		return res==null ? defaultSoil : map.getOrDefault(res, defaultSoil);
	}

	@Nullable public T getOrNull(ResourceLocation name){
		return map.get(name);
	}
	public T getOrDefault(ResourceLocation name){
		return getOrDefault(name, defaultEntry);
	}
	public T getOrDefault(ResourceLocation name, T defaultSoil){
		return map.getOrDefault(name, defaultSoil);
	}

	public interface Entry{
		ResourceLocation name();
	}
}
