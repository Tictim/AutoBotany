package tictim.autobotany.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

// TODO
public final class SeedProperty{
	private static final String KEY = "ABSeed";

	private SeedProperty(@Nullable CompoundTag tag){
	}

	public void save(ItemStack stack){
		stack.getOrCreateTag().put(KEY, write());
	}

	public CompoundTag write(){
		CompoundTag tag = new CompoundTag();
		return tag;
	}

	public static SeedProperty create(){
		return new SeedProperty(null);
	}
	public static SeedProperty create(@Nullable CompoundTag tag){
		return new SeedProperty(tag);
	}
	public static boolean has(ItemStack stack){
		CompoundTag tag = stack.getTag();
		return tag!=null&&tag.contains(KEY, Tag.TAG_COMPOUND);
	}
	@Nullable public static SeedProperty get(ItemStack stack){
		CompoundTag tag = stack.getTag();
		return tag==null||!tag.contains(KEY, Tag.TAG_COMPOUND) ?
				null : new SeedProperty(tag.getCompound(KEY));
	}
	public static SeedProperty create(ItemStack stack){
		SeedProperty seedProperty = get(stack);
		if(seedProperty==null) seedProperty = new SeedProperty(null);
		return seedProperty;
	}
	public static void remove(ItemStack stack){
		CompoundTag tag = stack.getTag();
		if(tag!=null) tag.remove(KEY);
	}
}
