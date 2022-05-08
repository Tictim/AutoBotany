package tictim.autobotany.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import tictim.autobotany.contents.menu.TrayMenu;
import tictim.autobotany.util.Rank;

import static tictim.autobotany.AutoBotanyMod.MODID;

public class TrayScreen extends AbstractContainerScreen<TrayMenu>{
	public static final ResourceLocation TEXTURE = new ResourceLocation(MODID, "textures/gui/tray.png");

	private Button btn1;
	private Button btn2;

	private EditBox score;

	public TrayScreen(TrayMenu menu, Inventory inventory, Component title){
		super(menu, inventory, title);
		this.imageHeight = 212;
	}

	@Override public void containerTick(){
		super.containerTick();
		this.score.tick();
	}

	@Override public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers){
		if(pKeyCode==256) this.minecraft.player.closeContainer();
		return !this.score.keyPressed(pKeyCode, pScanCode, pModifiers)&&!this.score.canConsumeInput() ? super.keyPressed(pKeyCode, pScanCode, pModifiers) : true;
	}

	@Override protected void init(){
		super.init();
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
		this.btn1 = addRenderableWidget(new Button(leftPos+54, topPos, 64, 20, new TextComponent("test"), button -> {
			this.menu.packetTest.send();
		}));
		this.score = addRenderableWidget(new EditBox(this.font, leftPos+54, topPos+32, 64, 12, new TextComponent("among")));
		this.btn2 = addRenderableWidget(new Button(leftPos+54, topPos+32+12, 64, 20, new TextComponent("generate yield"), button -> {
			int rank = Rank.fromString(score.getValue());
			if(rank<0) return;
			this.menu.generateYield.send(rank);
		}));
		this.score.setValue("A");
		this.setInitialFocus(this.score);
	}

	@Override public void removed(){
		super.removed();
		this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
	}

	@Override public void render(PoseStack pose, int mouseX, int mouseY, float partialTick){
		renderBg(pose, partialTick, mouseX, mouseY);
		super.render(pose, mouseX, mouseY, partialTick);
		this.renderTooltip(pose, mouseX, mouseY);
	}

	@Override protected void renderBg(PoseStack pose, float partialTick, int mouseX, int mouseY){
		RenderSystem.setShaderTexture(0, TEXTURE);
		blit(pose, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);

		drawString(pose, font, menu.getGrowth()+"", leftPos+54, topPos, 0xFFFFFFFF);
	}
}
