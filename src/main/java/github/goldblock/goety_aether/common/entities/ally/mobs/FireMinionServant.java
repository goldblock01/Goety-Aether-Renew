package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import com.aetherteam.aether.client.AetherSoundEvents;
import com.aetherteam.aether.entity.ai.goal.ContinuousMeleeAttackGoal;
import com.aetherteam.aether.entity.monster.dungeon.FireMinion;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.level.Level;

public class FireMinionServant extends Summoned {
    public FireMinionServant(EntityType<? extends Owned> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return FireMinion.createMobAttributes();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new ContinuousMeleeAttackGoal(this, 1.5D, true));
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            for (int i = 0; i < 1; ++i) {
                double d0 = this.random.nextFloat() - 0.5F;
                double d1 = this.random.nextFloat();
                double d2 = this.random.nextFloat() - 0.5F;
                this.level().addParticle(ParticleTypes.FLAME, this.getX() + d0 * d1, this.getBoundingBox().minY + d1 + 0.5D, this.getZ() + d2 * d1, 0.0D, -0.075D, 0.0D);
            }
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (pSource.getDirectEntity() instanceof Snowball) {
            pAmount += 3.0F;
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return AetherSoundEvents.ENTITY_FIRE_MINION_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return AetherSoundEvents.ENTITY_FIRE_MINION_DEATH.get();
    }
}
