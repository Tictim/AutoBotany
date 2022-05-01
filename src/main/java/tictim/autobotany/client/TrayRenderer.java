package tictim.autobotany.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import tictim.autobotany.contents.blockentity.TrayBlockEntity;
import tictim.autobotany.crop.Crop;
import tictim.autobotany.data.Soil;
import tictim.autobotany.data.SpeciesVisual;

import static net.minecraft.world.inventory.InventoryMenu.BLOCK_ATLAS;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.HORIZONTAL_FACING;

public class TrayRenderer implements BlockEntityRenderer<TrayBlockEntity>{
	public TrayRenderer(BlockEntityRendererProvider.Context ctx){}

	@Override public void render(TrayBlockEntity tray, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight, int packedOverlay){
		pose.pushPose();
		Direction dir = tray.getBlockState().getValue(HORIZONTAL_FACING);

		Box box = getInnerArea(tray);
		box.rotateHorizontally(dir);

		float y;

		// draw soil
		Soil soil = tray.getTray().soil();
		if(soil!=null){
			drawFlatQuad(entitySolid(buffer, soil.shape().soilTexture()), pose, box, y = box.yMax-1/16f, soil.tint()|0xFF000000, packedLight, packedOverlay);
		}else{
			FluidStack fluid = tray.getTray().getSolutionContainer().getFluidInTank(0);
			if(!fluid.isEmpty()){
				drawFluid(tray, pose, buffer, box, y = getFluidLevel(tray, box, fluid), packedLight, packedOverlay, fluid);
			}else y = box.yMin;
		}

		Crop crop = tray.getTray().crop();
		if(crop!=null)
			drawCrop(pose, buffer, box, y, packedLight, packedOverlay, crop, dir);

		pose.popPose();
	}

	protected Box getInnerArea(TrayBlockEntity tray){
		//new Box(0/16f, 7/16f, 5/16f, 11/16f, 0/16f, 7/16f); test box
		return new Box(2/16f, 14/16f, 5/16f, 11/16f, 3/16f, 13/16f);
	}

	private static float getFluidLevel(TrayBlockEntity tray, Box box, FluidStack fluid){
		float minLevel = box.yMin+0.01f;
		float maxLevel = box.yMax-.5f/16;
		float percentage = (float)fluid.getAmount()/tray.getTray().getSolutionContainer().getTankCapacity();
		return (maxLevel-minLevel)*percentage+minLevel;
	}

	private static void drawFluid(TrayBlockEntity tray, PoseStack pose, MultiBufferSource buffer, Box box, float y, int packedLight, int packedOverlay, FluidStack fluid){
		FluidAttributes attr = fluid.getFluid().getAttributes();
		ResourceLocation tex;
		int color;
		//noinspection ConstantConditions
		if(tray!=null&&tray.getLevel()!=null){
			tex = attr.getStillTexture(tray.getLevel(), tray.getBlockPos());
			color = attr.getColor(tray.getLevel(), tray.getBlockPos());
		}else{
			tex = attr.getStillTexture(fluid);
			color = attr.getColor(fluid);
		}

		drawFlatQuad(entityTranslucent(buffer, tex), pose, box, y, color, packedLight, packedOverlay);
	}

	private static void drawCrop(PoseStack pose, MultiBufferSource buffer, Box box, float y, int packedLight, int packedOverlay, Crop crop, Direction dir){
		SpeciesVisual.CropModel cropModel = crop.species.visual().model(crop);
		ResourceLocation model = cropModel.model(crop.isWilting());
		int color = cropModel.color(crop.isWilting());
		float r = (color >> 16)/255f;
		float g = (color >> 8)/255f;
		float b = (color)/255f;

		Minecraft mc = Minecraft.getInstance();
		BakedModel bakedModel = mc.getModelManager().getModel(model);
		pose.pushPose();
		pose.translate(.5, y, .5); // todo align actual center
		pose.mulPose(Vector3f.YP.rotationDegrees(-dir.toYRot()));
		float cropScale = box.cropScale();
		pose.scale(cropScale, cropScale, cropScale);
		pose.translate(-.5, 0, -.5);

		mc.getBlockRenderer().getModelRenderer().renderModel(pose.last(), buffer.getBuffer(RenderType.entityCutout(BLOCK_ATLAS)), null, bakedModel,
				r, g, b, packedLight, packedOverlay, EmptyModelData.INSTANCE);
		pose.popPose();
	}

	private static void drawFlatQuad(VertexConsumer vc, PoseStack pose, Box xz, float y, int color, int packedLight, int packedOverlay){
		float a = (color >> 24)/255f;
		float r = (color >> 16)/255f;
		float g = (color >> 8)/255f;
		float b = (color)/255f;
		drawFlatQuadVertex(vc, pose, xz.xMin, y, xz.zMin, r, g, b, a, packedLight, packedOverlay);
		drawFlatQuadVertex(vc, pose, xz.xMin, y, xz.zMax, r, g, b, a, packedLight, packedOverlay);
		drawFlatQuadVertex(vc, pose, xz.xMax, y, xz.zMax, r, g, b, a, packedLight, packedOverlay);
		drawFlatQuadVertex(vc, pose, xz.xMax, y, xz.zMin, r, g, b, a, packedLight, packedOverlay);
	}

	private static void drawFlatQuadVertex(VertexConsumer vc, PoseStack pose, float x, float y, float z, float r, float g, float b, float a, int packedLight, int packedOverlay){
		Matrix4f mat = pose.last().pose();
		Vector4f vec = new Vector4f(x, y, z, 1);
		vec.transform(mat);
		Vector3f norm = new Vector3f(0, 1, 0);
		norm.transform(pose.last().normal());
		vc.vertex(vec.x(), vec.y(), vec.z(), r, g, b, a, x, z, packedOverlay, packedLight, norm.x(), norm.y(), norm.z());
	}

	private static VertexConsumer entitySolid(MultiBufferSource buffer, ResourceLocation texture){
		return applyTexture(buffer.getBuffer(RenderType.entitySolid(BLOCK_ATLAS)), texture);
	}
	private static VertexConsumer entityTranslucent(MultiBufferSource buffer, ResourceLocation texture){
		return applyTexture(buffer.getBuffer(RenderType.entityTranslucent(BLOCK_ATLAS)), texture);
	}

	private static VertexConsumer applyTexture(VertexConsumer vc, ResourceLocation texture){
		return Minecraft.getInstance().getTextureAtlas(BLOCK_ATLAS)
				.apply(texture)
				.wrap(vc);
	}

	public static class Box{
		public float xMin;
		public float xMax;
		public float yMin;
		public float yMax;
		public float zMin;
		public float zMax;

		public Box(float xMin, float xMax, float yMin, float yMax, float zMin, float zMax){
			this.xMin = xMin;
			this.xMax = xMax;
			this.yMin = yMin;
			this.yMax = yMax;
			this.zMin = zMin;
			this.zMax = zMax;
		}

		public void rotateHorizontally(Direction dir){
			switch(dir){
				case NORTH -> {
					float xMin2 = -xMin+1;
					this.xMin = -xMax+1;
					this.xMax = xMin2;
					float zMin2 = -zMin+1;
					this.zMin = -zMax+1;
					this.zMax = zMin2;
				}
				case WEST -> {
					float xMin2 = -xMin+1;
					float xMax2 = -xMax+1;
					this.xMin = zMin;
					this.xMax = zMax;
					this.zMin = xMax2;
					this.zMax = xMin2;
				}
				case EAST -> {
					float zMin2 = -zMin+1;
					float zMax2 = -zMax+1;
					this.zMin = xMin;
					this.zMax = xMax;
					this.xMin = zMax2;
					this.xMax = zMin2;
				}
			}
		}

		public float cropScale(){
			return Math.max(2/16f, Math.min(xMax-xMin, zMax-zMin));
		}

		public double xCenter(){
			return (xMax-xMin)/2;
		}
		public double zCenter(){
			return (zMax-zMin)/2;
		}
	}
}
