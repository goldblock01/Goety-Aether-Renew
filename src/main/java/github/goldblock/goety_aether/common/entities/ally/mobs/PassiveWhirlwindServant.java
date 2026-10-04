package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.aetherteam.aether.client.particle.AetherParticleTypes;
import com.aetherteam.aether.loot.AetherLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PassiveWhirlwindServant extends AbstractWhirlwindServant {
    public PassiveWhirlwindServant(EntityType<? extends PassiveWhirlwindServant> type, Level level) {
        super(type, level);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();
        if (item instanceof DyeItem dyeItem && player.isShiftKeyDown()) {
            this.setColorData(dyeItem.getDyeColor().getMapColor().col);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void spawnParticles() {
        for (int i = 0; i < 2; i++) {
            double d2 = this.getX() + this.getRandom().nextDouble() * 0.25D;
            double d5 = this.getY() + this.getBbHeight() + 0.125D;
            double d8 = this.getZ() + this.getRandom().nextDouble() * 0.25D;
            float f1 = this.getRandom().nextFloat() * 360.0F;
            this.level().addParticle(AetherParticleTypes.PASSIVE_WHIRLWIND.get(), d2, d5 - 0.25D, d8,
                    -Math.sin(0.0175F * f1) * 0.75D, 0.125D, Math.cos(0.0175F * f1) * 0.75D);
        }
    }

    @Override
    public ResourceLocation getLootLocation() {
        return AetherLoot.WHIRLWIND_JUNK;
    }

    @Override
    public int getDefaultColor() {
        return 0xFFFFFF;
    }
}
