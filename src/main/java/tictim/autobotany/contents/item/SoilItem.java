package tictim.autobotany.contents.item;

import net.minecraftforge.client.IItemRenderProperties;
import tictim.autobotany.data.AllSoils;
import tictim.autobotany.data.Soil;

import java.util.function.Consumer;

public class SoilItem extends DataItem<Soil>{
	public SoilItem(Properties properties){
		super(properties, AllSoils.SOILS);
	}

	@Override public void initializeClient(Consumer<IItemRenderProperties> consumer){
		consumer.accept(new RenderProperties(soil -> soil.shape().itemModel()));
	}
}
