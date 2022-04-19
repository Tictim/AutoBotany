package tictim.autobotany.contents.item;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.Tags;

public class GardenTrowelItem extends ShovelItem{
	public static final Tier TIER = new ForgeTier(
			Tiers.IRON.getLevel(),
			Tiers.IRON.getUses(),
			Tiers.WOOD.getSpeed(),
			Tiers.WOOD.getAttackDamageBonus(),
			Tiers.IRON.getEnchantmentValue(),
			Tags.Blocks.NEEDS_WOOD_TOOL,
			Tiers.IRON::getRepairIngredient);

	public GardenTrowelItem(Properties p){
		super(TIER, 1.5f, -3f,  p);
	}

	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		return InteractionResult.PASS;
	}
}
