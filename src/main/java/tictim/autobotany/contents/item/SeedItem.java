package tictim.autobotany.contents.item;

import net.minecraftforge.client.IItemRenderProperties;
import tictim.autobotany.data.AllSpecies;
import tictim.autobotany.data.Species;

import java.util.function.Consumer;

public class SeedItem extends DataItem<Species>{
	public SeedItem(Properties properties){
		super(properties, AllSpecies.REGISTRY);
	}

	@Override public void initializeClient(Consumer<IItemRenderProperties> consumer){
		consumer.accept(new RenderProperties(Species::seedItemModel));
	}
}
