package github.goldblock.goety_aether.common.entities.ally;

import com.aetherteam.aether.data.resources.registries.AetherDamageTypes;
import com.aetherteam.aether.entity.monster.dungeon.boss.Slider;
import com.Polarice3.Goety.utils.MobUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SliderCollideGoal extends Goal {
    private final Slider slider;

    public SliderCollideGoal(Slider slider) {
        this.slider = slider;
    }

    @Override
    public boolean canUse() {
        if (!this.slider.isAwake() || this.slider.isRemoved()) {
            return false;
        }
        return this.slider.attackCooldown() <= 0 || this.slider.getDeltaMovement().lengthSqr() > 0.08D;
    }

    @Override
    public void tick() {
        AABB bounds = this.slider.getBoundingBox().inflate(0.1D);
        for (Entity entity : this.slider.level().getEntities(this.slider, bounds)) {
            if (MobUtil.areAllies(this.slider, entity)) {
                continue;
            }
            if (entity instanceof LivingEntity livingEntity) {
                if (livingEntity.hurt(AetherDamageTypes.entityDamageSource(this.slider.level(), AetherDamageTypes.CRUSH, this.slider), 6.0F)) {
                    if (livingEntity instanceof Player player && player.getUseItem().is(Items.SHIELD) && player.isBlocking()) {
                        player.getCooldowns().addCooldown(Items.SHIELD, 100);
                        player.disableShield(true);
                        this.slider.level().broadcastEntityEvent(player, (byte) 30);
                    }
                    entity.setDeltaMovement(entity.getDeltaMovement().multiply(4.0D, 1.0D, 4.0D).add(0.0D, 0.25D, 0.0D));
                    this.slider.setMoveDelay(this.slider.calculateMoveDelay());
                    this.slider.setAttackCooldown(20);
                    this.slider.setMoveDirection(null);
                    this.slider.playSound(this.slider.getCollideSound(), 2.5F, 1.0F / (this.slider.getRandom().nextFloat() * 0.2F + 0.9F));
                    this.slider.setDeltaMovement(Vec3.ZERO);
                    continue;
                }
            }
            if (!(entity instanceof Slider)) {
                entity.setDeltaMovement(this.slider.getDeltaMovement().multiply(4.0D, 1.0D, 4.0D).add(0.0D, 0.25D, 0.0D));
            }
        }
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
