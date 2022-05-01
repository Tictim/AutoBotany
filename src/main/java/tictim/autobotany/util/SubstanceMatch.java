package tictim.autobotany.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import tictim.autobotany.data.Substance;

import java.util.*;
import java.util.stream.Collectors;

public final class SubstanceMatch{
	public static final SubstanceMatch NONE = new SubstanceMatch();

	public static SubstanceMatch of(Substance... substances){
		if(substances.length==0) return NONE;
		return new SubstanceMatch(substances);
	}
	public static SubstanceMatch read(CompoundTag tag){
		ListTag list = tag.getList("Substances", Tag.TAG_STRING);
		List<Substance> substances = new ArrayList<>();
		for(int i = 0; i<list.size(); i++){
			ResourceLocation res = ABNBT.readNamespace(list.getString(i));
			if(res!=null){
				Substance substance = ABNBT.readSubstance(list.getString(i));
				if(substance!=null) substances.add(substance);
			}
		}
		return of(substances.toArray(new Substance[0]));
	}

	private final Set<Substance> substances;

	public SubstanceMatch(Substance... substances){
		this.substances = new HashSet<>();
		Collections.addAll(this.substances, substances);
	}

	public boolean matches(Substance substance){
		return substances.contains(substance);
	}

	public CompoundTag write(){
		CompoundTag tag = new CompoundTag();
		ListTag list = new ListTag();
		for(Substance s : substances){
			list.add(list.size(), StringTag.valueOf(s.name().toString()));
		}
		tag.put("Substances", list);
		return tag;
	}

	@Override public String toString(){
		return "SubstanceMatch["+substances.stream().map(Substance::toString).collect(Collectors.joining(", "))+']';
	}

}
