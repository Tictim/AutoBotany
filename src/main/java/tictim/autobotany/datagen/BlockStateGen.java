package tictim.autobotany.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import tictim.autobotany.contents.ModBlocks;

import static tictim.autobotany.AutoBotanyMod.MODID;

public class BlockStateGen extends BlockStateProvider{
	public BlockStateGen(DataGenerator gen, ExistingFileHelper exFileHelper){
		super(gen, MODID, exFileHelper);
	}

	@Override protected void registerStatesAndModels(){
		horizontalBlock(ModBlocks.WOODEN_TRAY.get(), models().getExistingFile(new ResourceLocation(MODID, "block/wooden_tray")));
	}
}
