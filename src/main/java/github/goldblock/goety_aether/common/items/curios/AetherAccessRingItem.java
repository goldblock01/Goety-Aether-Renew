package github.goldblock.goety_aether.common.items.curios;

import com.Polarice3.Goety.common.items.curios.RingItem;
import com.aetherteam.aether.item.AetherItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class AetherAccessRingItem extends RingItem {
    @Override
    public Rarity getRarity(ItemStack stack) {
        return AetherItems.AETHER_LOOT;
    }
}
