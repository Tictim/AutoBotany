package tictim.autobotany.contents.menu;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import tictim.autobotany.AutoBotanyMod;
import tictim.autobotany.contents.ModMenus;
import tictim.autobotany.contents.blockentity.TrayBlockEntity;
import tictim.autobotany.crop.Crop;
import tictim.autobotany.loot.Loot;
import tictim.autobotany.network.menu.*;
import tictim.autobotany.util.Rank;

import javax.annotation.Nullable;

public class TrayMenu extends AbstractContainerMenu implements MenuPacketHandler{
	private static final MenuPacketContract.P0<TrayMenu> packetTestContract = MenuPacketContract.forServer(0, (m, buf) -> {
		AutoBotanyMod.LOGGER.info("Join my patreon for upcoming sex mod compat");
	});
	private static final MenuPacketContract.P1<TrayMenu, Integer> generateLootContract = MenuPacketContract.forServer(1,
			(buf, rank) -> buf.writeUtf(Rank.toString(rank)),
			(m, buf) -> {
				if(m.trayBlockEntity==null||!(m.trayBlockEntity.getLevel() instanceof ServerLevel sl)) return;
				Crop crop = m.trayBlockEntity.getTray().crop();
				if(crop==null) return;
				int rank = Rank.fromString(buf.readUtf());
				if(rank<0) return;
				AutoBotanyMod.LOGGER.info("Generating yield {}", Rank.toString(rank));
				for(ItemStack s : Loot.generateYield(crop.species, sl, rank)){
					m.trayBlockEntity.getItems().addItem(s);
				}
			});

	private final I64Data growth = new I64Data();
	private final Inventory inventory;
	@Nullable private final TrayBlockEntity trayBlockEntity;

	private final SimpleMenuPacketHandler<TrayMenu> packetHandler;

	public final Sender.P0 packetTest;
	public final Sender.P1<Integer> generateYield;

	public TrayMenu(int containerId, Inventory inventory){
		this(containerId, inventory, null);
	}
	public TrayMenu(int containerId, Inventory inventory, @Nullable TrayBlockEntity trayBlockEntity){
		super(ModMenus.TRAY.get(), containerId);
		this.inventory = inventory;
		this.trayBlockEntity = trayBlockEntity;

		SimpleContainer c = trayBlockEntity!=null ? trayBlockEntity.getItems() : new SimpleContainer(TrayBlockEntity.INVENTORY_SIZE);
		for(int i = 0; i<18; i++){
			addSlot(new Slot(c, i, 8+i%9*18, 80+i/9*18));
		}

		for(int y = 0; y<3; ++y){
			for(int x = 0; x<9; ++x){
				addSlot(new Slot(inventory, x+y*9+9, 8+x*18, 130+y*18));
			}
		}

		for(int i = 0; i<9; ++i){
			addSlot(new Slot(inventory, i, 8+i*18, 188));
		}

		addDataSlots(growth.rawData());

		this.packetHandler = new SimpleMenuPacketHandler<>(this, inventory.player);
		this.packetTest = packetHandler.register(packetTestContract);
		this.generateYield = packetHandler.register(generateLootContract);
	}

	public long getGrowth(){
		return growth.get();
	}
	public void setGrowth(long growth){
		this.growth.set(growth);
	}

	public Inventory getInventory(){
		return inventory;
	}
	@Nullable public TrayBlockEntity getTrayBlockEntity(){
		return trayBlockEntity;
	}

	@Override public boolean stillValid(Player p){
		return true;
	}

	@Override public ItemStack quickMoveStack(Player p, int i){
		ItemStack s = ItemStack.EMPTY;
		Slot slot = this.slots.get(i);
		if(slot.hasItem()){
			ItemStack s1 = slot.getItem();
			s = s1.copy();
			if(i<TrayBlockEntity.INVENTORY_SIZE){
				if(!this.moveItemStackTo(s1, TrayBlockEntity.INVENTORY_SIZE, this.slots.size(), true)) return ItemStack.EMPTY;
			}else{
				if(!this.moveItemStackTo(s1, 0, TrayBlockEntity.INVENTORY_SIZE, false)) return ItemStack.EMPTY;
			}

			if(s1.isEmpty()) slot.set(ItemStack.EMPTY);
			else slot.setChanged();
		}

		return s;
	}

	@Override public void handle(MenuPacket packet){
		this.packetHandler.handle(packet);
	}
}
