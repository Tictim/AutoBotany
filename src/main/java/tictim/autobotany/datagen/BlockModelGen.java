package tictim.autobotany.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import tictim.autobotany.contents.ModBlocks;

import java.util.Objects;

import static tictim.autobotany.AutoBotanyMod.MODID;

public class BlockModelGen extends BlockStateProvider{
	public BlockModelGen(DataGenerator gen, ExistingFileHelper exFileHelper){
		super(gen, MODID, exFileHelper);
	}
	@Override protected void registerStatesAndModels(){
		simpleItemBlock(ModBlocks.WOODEN_TRAY.get());
	}

	private void simpleItemBlock(Block block){
		itemModels().getBuilder(Objects.requireNonNull(block.getRegistryName()).getPath())
				.parent(new ModelFile.UncheckedModelFile(block.getRegistryName().getNamespace()+":block/"+block.getRegistryName().getPath()));
	}
}
