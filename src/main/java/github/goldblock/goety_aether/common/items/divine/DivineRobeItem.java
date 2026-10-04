package github.goldblock.goety_aether.common.items.divine;

import com.Polarice3.Goety.common.items.curios.MagicRobeItem;
import com.Polarice3.Goety.utils.ColorUtil;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import com.aetherteam.aether.item.AetherItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class DivineRobeItem extends MagicRobeItem {
    public DivineRobeItem() {
        super();
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return AetherItems.AETHER_LOOT;
    }

    @Override
    public boolean isFireResistant() {
        return true;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof Player player) {
            if (CuriosFinder.hasCurio(player, itemStack -> itemStack.getItem() instanceof DivineRobeItem) && !player.isSpectator()) {
                Vec3 vector3d = player.getDeltaMovement();
                if (player.hasEffect(MobEffects.SLOW_FALLING)) {
                    player.removeEffect(MobEffects.SLOW_FALLING);
                }
                if (!player.onGround() && vector3d.y < 0.0D
                        && !player.isNoGravity()
                        && !player.getAbilities().flying
                        && !player.onClimbable()
                        && !player.isInFluidType()
                        && !player.isInWater()
                        && !player.isInLava()
                        && player.fallDistance >= 2.0F) {
                    if (worldIn instanceof ServerLevel serverLevel) {
                        ColorUtil color = ColorUtil.WHITE;
                        ServerParticleUtil.windParticle(serverLevel, color, 1.0F + serverLevel.random.nextFloat() * 0.5F, 0.0F, player.getId(), player.position());
                        ServerParticleUtil.circularParticles(serverLevel, ParticleTypes.CLOUD, player, 1.0F);
                    }
                    if (!player.isCrouching() && !player.isShiftKeyDown()) {
                        player.setDeltaMovement(vector3d.multiply(1.0D, 0.875D, 1.0D));
                    } else {
                        player.setDeltaMovement(vector3d.multiply(1.0D, 0.99D, 1.0D));
                    }
                }
            }
        }

        super.inventoryTick(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(Component.translatable("info.goety_aether.divine_robe").withStyle(ChatFormatting.DARK_PURPLE));
        tooltip.add(Component.translatable("info.goety_aether.divine_robe_flight").withStyle(ChatFormatting.BLUE));
    }
}
