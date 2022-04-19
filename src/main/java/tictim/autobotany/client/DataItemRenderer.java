package tictim.autobotany.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemStack;
import tictim.autobotany.contents.item.DataItem;
import tictim.autobotany.data.DataRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DataItemRenderer<T extends DataRegistry.Entry> extends BlockEntityWithoutLevelRenderer{
	private final DataItem<T> metaItem;
	private final Function<T, ResourceLocation> variationSelector;
	private final Map<T, BakedModel> models = new HashMap<>();

	public DataItemRenderer(BlockEntityRenderDispatcher blockEntityRenderDispatcher,
	                        EntityModelSet entityModelSet,
	                        DataItem<T> metaItem,
	                        Function<T, ResourceLocation> variationSelector){
		super(blockEntityRenderDispatcher, entityModelSet);
		this.metaItem = metaItem;
		this.variationSelector = variationSelector;
	}

	private BakedModel getModel(ItemStack stack){
		return models.computeIfAbsent(metaItem.getEntryOrDefault(stack), t -> Minecraft.getInstance().getModelManager().getModel(variationSelector.apply(t)));
	}

	@Override public void onResourceManagerReload(ResourceManager pResourceManager){}

	@Override public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay){
		BakedModel model = getModel(stack);
		if(model.isCustomRenderer()) return; // nice infinite loop bro
		pose.translate(.5, .5, .5);
		Minecraft.getInstance().getItemRenderer().render(stack, transformType, false, pose, buffer, packedLight, packedOverlay, model);
	}
}
