package tictim.autobotany.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.minecraftforge.fluids.FluidStack;
import tictim.autobotany.capability.SolutionContainer;
import tictim.autobotany.contents.blockentity.TrayBlockEntity;

public class DebugOverlay implements IIngameOverlay{
	@Override public void render(ForgeIngameGui gui, PoseStack pose, float partialTick, int width, int height){
		Minecraft mc = Minecraft.getInstance();
		if(mc.screen!=null) return;
		Player player = mc.player;
		if(player==null||!player.isAlive()) return;

		if(mc.hitResult==null||mc.hitResult.getType()!=HitResult.Type.BLOCK) return;
		if(!(player.level.getBlockEntity(((BlockHitResult)mc.hitResult).getBlockPos()) instanceof TrayBlockEntity tray)) return;

		mc.font.drawShadow(pose, "Crop: "+tray.getTray().crop(), 10, 10, 0xFFFFFFFF);
		mc.font.drawShadow(pose, "Soil: "+tray.getTray().soil(), 10, 20, 0xFFFFFFFF);
		SolutionContainer fluidTank = tray.getTray().getSolutionContainer();
		FluidStack fluid = fluidTank.getFluidInTank(0);
		mc.font.drawShadow(pose, "Irrigation: "+(fluid.isEmpty() ? "0" : fluid.getDisplayName().getString()+ ", "+fluid.getAmount())+" / "+fluidTank.getTankCapacity(0), 10, 30, 0xFFFFFFFF);
	}
}
