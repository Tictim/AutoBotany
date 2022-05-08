package tictim.autobotany.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import tictim.autobotany.AutoBotanyMod;
import tictim.autobotany.data.AllSpecies;
import tictim.autobotany.data.Species;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

// TODO
public final class SeedProperty{
	private static final String KEY = "ABSeed";

	public final Species species;

	private SeedProperty(Species species){
		this.species = Objects.requireNonNull(species);
	}

	public void addTooltip(List<Component> list, @Nullable Player player, TooltipFlag flags){
		list.add(new TextComponent("ligma balls"));
	}

	public void load(CompoundTag tag){
	}

	public void save(ItemStack stack){
		stack.getOrCreateTag().put(KEY, write());
	}

	public CompoundTag write(){
		CompoundTag tag = new CompoundTag();
		return tag;
	}

	public static SeedProperty create(Species species){
		return new SeedProperty(species);
	}
	public static boolean hasOverride(ItemStack stack){
		if(speciesFromSeedItem(stack)==null) return false;
		CompoundTag tag = stack.getTag();
		return tag!=null&&tag.contains(KEY, Tag.TAG_COMPOUND);
	}
	@Nullable public static SeedProperty get(ItemStack stack){
		Species species = speciesFromSeedItem(stack);
		if(species==null) return null;
		SeedProperty seedProperty = new SeedProperty(species);
		CompoundTag tag = stack.getTag();
		if(tag!=null&&tag.contains(KEY, Tag.TAG_COMPOUND)) seedProperty.load(tag.getCompound(KEY));
		return seedProperty;
	}
	public static void removeOverride(ItemStack stack){
		CompoundTag tag = stack.getTag();
		if(tag!=null) tag.remove(KEY);
	}

	@Nullable private static Map<ResourceLocation, Species> seedToItemMap;

	@Nullable public static Species speciesFromSeedItem(ItemStack stack){
		return stack.isEmpty() ? null : speciesFromSeedItem(stack.getItem());
	}
	@Nullable public static Species speciesFromSeedItem(Item item){
		if(seedToItemMap==null){
			seedToItemMap = new HashMap<>();
			for(Species species : AllSpecies.REGISTRY.entries()){
				Item seed = species.seed().get();
				if(seed==null) continue;
				ResourceLocation seedName = seed.getRegistryName();
				if(seedName==null) continue;
				if(seedToItemMap.putIfAbsent(seedName, species)!=null)
					AutoBotanyMod.LOGGER.warn("Item {} is registered as seed for both {} and {}",
							seedName, seedToItemMap.get(seedName).name(), species.name());
			}
		}
		ResourceLocation seedName = item.getRegistryName();
		return seedName!=null ? seedToItemMap.get(seedName) : null;
	}
	public static void clearSeedToItemCache(){
		seedToItemMap = null;
	}
}
