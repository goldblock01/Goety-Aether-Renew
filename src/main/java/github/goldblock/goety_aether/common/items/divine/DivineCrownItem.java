package github.goldblock.goety_aether.common.items.divine;

import com.Polarice3.Goety.common.items.curios.MagicCrownItem;
import com.aetherteam.aether.item.AetherItems;
import github.goldblock.goety_aether.GoetyAether;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class DivineCrownItem extends MagicCrownItem {
    public DivineCrownItem() {
        super((new Properties()).stacksTo(1).fireResistant().rarity(AetherItems.AETHER_LOOT), GoetyAether.DIVINE);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.translatable("info.goety_aether.divine_crown").withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("info.goety_aether.divine_crown_cast").withStyle(ChatFormatting.BLUE));
    }
}
