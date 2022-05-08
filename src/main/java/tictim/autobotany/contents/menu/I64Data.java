package tictim.autobotany.contents.menu;

import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;

public class I64Data{
	private final ContainerData data = new SimpleContainerData(4);

	public ContainerData rawData(){
		return data;
	}

	public long get(){
		return ((long)data.get(0)<<48&0xFFFF_0000_0000_0000L)
				|((long)data.get(1)<<32&0xFFFF_0000_0000L)
				|((long)data.get(2)<<16&0xFFFF_0000L)
				|((long)data.get(3)&0xFFFFL);
	}
	public void set(long l){
		data.set(0, (int)(l >> 48));
		data.set(1, (int)(l >> 32));
		data.set(2, (int)(l >> 16));
		data.set(3, (int)l);
	}
}
