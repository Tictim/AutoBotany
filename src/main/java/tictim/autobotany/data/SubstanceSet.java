package tictim.autobotany.data;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Set of substances paired with amount, measured in microliters.
 */
public class SubstanceSet{
	public static SubstanceSet.Mutable newMutable(){
		return newCapped(Integer.MAX_VALUE);
	}
	public static SubstanceSet.Mutable mutableCopy(SubstanceSet set){
		return cappedCopy(Integer.MAX_VALUE, set);
	}

	public static SubstanceSet.Mutable newCapped(int cap){
		return new SubstanceSet.Mutable(cap, new Object2IntOpenHashMap<>());
	}
	public static SubstanceSet.Mutable cappedCopy(int cap, SubstanceSet set){
		return new SubstanceSet.Mutable(cap, new Object2IntOpenHashMap<>(set.map));
	}

	private static final SubstanceSet EMPTY_IMMUTABLE = new SubstanceSet(Object2IntMaps.emptyMap());
	public static SubstanceSet empty(){
		return EMPTY_IMMUTABLE;
	}
	public static SubstanceSet immutableCopy(SubstanceSet set){
		return new SubstanceSet(new Object2IntOpenHashMap<>(set.map));
	}

	protected final Object2IntMap<Substance> map;
	@Nullable protected Object2IntMap<Substance> view;

	public SubstanceSet(Object2IntMap<Substance> map){
		this.map = map;
	}

	public Object2IntMap<Substance> getMap(){
		if(view==null)
			view = Object2IntMaps.unmodifiable(map);
		return view;
	}

	public int size(){
		return map.size();
	}
	public boolean isEmpty(){
		return map.isEmpty();
	}
	public int getAmount(Substance substance){
		return map.getInt(substance);
	}
	public ObjectSet<Object2IntMap.Entry<Substance>> entries(){
		return getMap().object2IntEntrySet();
	}
	public boolean has(Substance substance){
		return map.containsKey(substance);
	}

	/**
	 * @return SubstanceSet containing {@code percentage} amount of substances.
	 */
	public SubstanceSet split(double percentage){
		if(percentage==0) return empty();
		if(percentage==1) return this;
		SubstanceSet.Mutable mutable = newMutable();
		for(Object2IntMap.Entry<Substance> e : map.object2IntEntrySet()){
			int split = (int)(e.getIntValue()*percentage);
			if(split<=0) continue;
			mutable.put(e.getKey(), split);
		}
		return mutable;
	}

	public CompoundTag write(CompoundTag tag){
		writeToNbt(tag, this.map);
		return tag;
	}

	private static void writeToNbt(CompoundTag tag, Object2IntMap<Substance> map){
		for(Object2IntMap.Entry<Substance> e : map.object2IntEntrySet())
			tag.putInt(e.getKey().name().toString(), e.getIntValue());
	}

	private static void readFromNbt(CompoundTag tag, Object2IntMap<Substance> map){
		map.clear();
		for(String key : tag.getAllKeys()){
			if(tag.contains(key, Tag.TAG_INT)) continue;
			ResourceLocation res = ResourceLocation.tryParse(key);
			if(res==null) continue;
			Substance substance = AllSubstances.SUBSTANCES.getOrNull(res);
			if(substance==null) continue;
			map.put(substance, tag.getInt(key));
		}
	}

	@Override public String toString(){
		return "["+map.object2IntEntrySet().stream()
				.map(e -> e.getKey()+"*"+e.getIntValue())
				.collect(Collectors.joining(", "))+"]";
	}

	public static class Mutable extends SubstanceSet{
		private final int cap;

		public Mutable(int cap, Object2IntMap<Substance> map){
			super(map);
			if(cap<0) throw new IllegalArgumentException("cap");
			this.cap = cap;
		}

		public int cap(){
			return cap;
		}

		/**
		 * @return Previous value
		 */
		public int put(Substance substance, int amount){
			return amount<=0 ? map.removeInt(substance) : map.put(substance, Math.min(cap(), amount));
		}

		public void putAll(SubstanceSet set){
			map.putAll(set.map);
		}
		public void putAll(Map<? extends Substance, ? extends Integer> m){
			map.putAll(m);
		}

		/**
		 * @return Amount of substances accepted
		 */
		public int add(Substance substance, int amount, boolean simulate){
			if(amount<=0) return 0;
			int stored = map.getInt(substance);
			if(stored>=cap()) return 0;
			int added = Math.min(this.cap()-stored, amount);
			if(!simulate) map.put(substance, stored+added);
			return added;
		}

		public boolean addAll(SubstanceSet substance, boolean simulate){
			for(Object2IntMap.Entry<Substance> e : substance.entries()){
				Substance s = e.getKey();
				if(add(s, e.getIntValue(), true)!=e.getIntValue()) return false;
			}
			if(!simulate){
				for(Object2IntMap.Entry<Substance> e : substance.entries())
					add(e.getKey(), e.getIntValue(), true);
			}
			return true;
		}

		/**
		 * @return Amount of substances consumed
		 */
		public int consume(Substance substance, int amount, boolean simulate){
			if(amount<=0) return 0;
			int i = map.getInt(substance);
			if(i==0) return 0;
			if(!simulate){
				map.put(substance, amount<=i ? 0 : amount-i);
			}
			return Math.min(amount, i);
		}

		public boolean consumeAll(SubstanceSet substance, boolean simulate){
			for(Object2IntMap.Entry<Substance> e : substance.entries()){
				Substance s = e.getKey();
				if(consume(s, e.getIntValue(), true)!=e.getIntValue()) return false;
			}
			if(!simulate){
				for(Object2IntMap.Entry<Substance> e : substance.entries())
					consume(e.getKey(), e.getIntValue(), true);
			}
			return true;
		}

		public void clear(){
			map.clear();
		}

		/**
		 * @return SubstanceSet containing {@code percentage} amount of substances.
		 */
		public SubstanceSet splitAndConsume(double percentage){
			if(percentage==0) return empty();
			if(percentage==1){
				SubstanceSet copy = immutableCopy(this);
				clear();
				return copy;
			}
			SubstanceSet.Mutable mutable = newMutable();
			for(ObjectIterator<Object2IntMap.Entry<Substance>> it = map.object2IntEntrySet().iterator(); it.hasNext(); ){
				Object2IntMap.Entry<Substance> e = it.next();
				int split = (int)(e.getIntValue()*percentage);
				if(split<=0) continue;
				mutable.put(e.getKey(), split);
				int consumed = e.getIntValue()-split;
				if(consumed>0) e.setValue(consumed);
				else it.remove();
			}
			return mutable;
		}

		public SubstanceSet split(double percentage, boolean consume){
			return consume ? splitAndConsume(percentage) : split(percentage);
		}

		public void read(CompoundTag tag){
			SubstanceSet.readFromNbt(tag, map);
		}
	}
}
