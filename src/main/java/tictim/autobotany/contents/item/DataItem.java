package tictim.autobotany.contents.item;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.IItemRenderProperties;
import tictim.autobotany.client.DataItemRenderer;
import tictim.autobotany.data.DataRegistry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class DataItem<T extends DataRegistry.Entry> extends Item{
	private final DataRegistry<T> registry;

	public DataItem(Properties properties, DataRegistry<T> registry){
		super(properties);
		this.registry = registry;
	}

	public ItemStack createStack(T t){
		ItemStack stack = new ItemStack(this);
		stack.getOrCreateTag().putString("Entry", t.name().toString());
		return stack;
	}

	public T getEntryOrDefault(ItemStack stack){
		return getEntryOrDefault(stack, registry.defaultEntry());
	}
	public T getEntryOrDefault(ItemStack stack, T defaultEntry){
		T t = getEntryOrNull(stack);
		return t!=null ? t : defaultEntry;
	}

	@Nullable public T getEntryOrNull(ItemStack stack){
		CompoundTag tag = stack.getTag();
		if(tag==null||!tag.contains("Entry", Tag.TAG_STRING)) return null;
		return registry.getOrNull(tag.getString("Entry"));
	}

	@Override public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items){
		if(this.allowdedIn(category)){
			for(T t : registry.map().values()){
				if(t.hidden()) continue;
				items.add(createStack(t));
			}
		}
	}

	@Override public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> text, TooltipFlag isAdvanced){
		if(isAdvanced.isAdvanced()){
			T t = getEntryOrNull(stack);
			if(t!=null) text.add(new TextComponent(t.name().toString()));
			else text.add(new TextComponent("Fuck"));
		}
	}

	private final Map<T, String> descriptionIDs = new HashMap<>();

	private String getDescriptionId(T t){
		return descriptionIDs.computeIfAbsent(t, t1 -> {
			ResourceLocation res = this.getRegistryName();
			StringBuilder stb = new StringBuilder(Util.makeDescriptionId("item", res));
			if(res==null||!t1.name().getNamespace().equals(res.getNamespace()))
				stb.append(".").append(t1.name().getNamespace());
			return stb.append(".").append(t1.name().getPath().replace('/', '.')).toString();
		});
	}

	@Override public String getDescriptionId(ItemStack stack){
		return getDescriptionId(getEntryOrDefault(stack));
	}

	public class RenderProperties implements IItemRenderProperties{
		private final DataItemRenderer<T> itemRenderer;

		public RenderProperties(Function<T, ResourceLocation> variationSelector){
			this.itemRenderer = new DataItemRenderer<>(
					Minecraft.getInstance().getBlockEntityRenderDispatcher(),
					Minecraft.getInstance().getEntityModels(),
					DataItem.this,
					variationSelector
			);
			((ReloadableResourceManager)Minecraft.getInstance().getResourceManager()).registerReloadListener(itemRenderer);
		}

		@Override public BlockEntityWithoutLevelRenderer getItemStackRenderer(){
			return itemRenderer;
		}
	}
}
