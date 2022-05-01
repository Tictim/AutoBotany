package tictim.autobotany.contents;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tictim.autobotany.contents.block.WoodenTrayBlock;

import static tictim.autobotany.AutoBotanyMod.MODID;

public class ModBlocks{
	public static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

	public static final RegistryObject<Block> WOODEN_TRAY = REGISTER.register("wooden_tray", () ->
			new WoodenTrayBlock(p(Material.WOOD).sound(SoundType.WOOD)));

	public static BlockBehaviour.Properties p(Material material){
		return BlockBehaviour.Properties.of(material);
	}
}
