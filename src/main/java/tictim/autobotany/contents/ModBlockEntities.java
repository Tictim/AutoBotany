package tictim.autobotany.contents;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import tictim.autobotany.contents.blockentity.WoodenTrayBlockEntity;

import static tictim.autobotany.AutoBotanyMod.MODID;

public class ModBlockEntities{
	public static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MODID);

	public static final RegistryObject<BlockEntityType<WoodenTrayBlockEntity>> WOODEN_TRAY = REGISTER.register("wooden_tray", () ->
			BlockEntityType.Builder.of(WoodenTrayBlockEntity::new, ModBlocks.WOODEN_TRAY.get()).build(null));
}
