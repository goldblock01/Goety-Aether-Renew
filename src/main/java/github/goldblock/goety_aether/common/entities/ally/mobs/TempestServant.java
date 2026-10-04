package github.goldblock.goety_aether.common.entities.ally.mobs;

import github.goldblock.goety_aether.compat.genesis.GenesisBridge;
import net.minecraft.core.particles.ParticleTypes;
import github.goldblock.goety_aether.common.entities.projectile.TempestThunderBall;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TempestServant extends ZephyrServant {

    public TempestServant(EntityType<? extends ZephyrServant> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return FlyingMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 25.0D)
                .add(Attributes.FOLLOW_RANGE, 50.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
    }

    @Override
    protected void registerShootGoal() {
        this.goalSelector.addGoal(5, new TempestShootGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        for (int i = 0; i < 3; ++i) {
            double xOffset = this.getRandomX(0.5D) + this.level().getRandom().nextDouble() * 1.5D - 0.75D;
            double yOffset = this.getRandomY() + this.level().getRandom().nextDouble() * 2.0D - 0.5D;
            double zOffset = this.getRandomZ(0.5D) + this.level().getRandom().nextDouble() * 1.5D - 0.75D;
            this.level().addParticle(GenesisBridge.particle("tempest_electricity") instanceof ParticleOptions options ? options : ParticleTypes.ELECTRIC_SPARK, xOffset, yOffset, zOffset, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return GenesisBridge.sound("entity.tempest.ambient");
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return GenesisBridge.sound("entity.tempest.hurt");
    }

    @Override
    protected SoundEvent getDeathSound() {
        return GenesisBridge.sound("entity.tempest.death");
    }

    static class TempestShootGoal extends Goal {
        private final TempestServant tempest;

        TempestShootGoal(TempestServant tempest) {
            this.tempest = tempest;
        }

        @Override
        public boolean canUse() {
            return this.tempest.getTarget() != null;
        }

        @Override
        public void start() {
            this.tempest.setChargeTime(0);
        }

        @Override
        public void stop() {
            this.tempest.setChargeTime(0);
        }

        @Override
        public void tick() {
            LivingEntity target = this.tempest.getTarget();
            if (target != null) {
                if (target.distanceToSqr(this.tempest) < 1600.0D && this.tempest.hasLineOfSight(target)) {
                    Level level = this.tempest.level();
                    this.tempest.setChargeTime(this.tempest.getChargeTime() + 1);
                    if (this.tempest.getChargeTime() == 10) {
                        this.tempest.playSound(this.tempest.getAmbientSound(), 0.75F, (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F);
                    } else if (this.tempest.getChargeTime() == 20) {
                        Vec3 look = this.tempest.getViewVector(1.0F);
                        double accelX = target.getX() - (this.tempest.getX() + look.x() * 4.0D);
                        double accelY = target.getY(0.5D) - (0.5D + this.tempest.getY(0.5D));
                        double accelZ = target.getZ() - (this.tempest.getZ() + look.z() * 4.0D);
                        this.tempest.playSound(GenesisBridge.sound("entity.tempest.shoot"), 0.75F, (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F);
                        TempestThunderBall thunderBall = new TempestThunderBall(level, this.tempest, accelX, accelY, accelZ);
                        thunderBall.setPos(this.tempest.getX() + look.x() * 4.0D, this.tempest.getY(0.5D) + 0.5D, this.tempest.getZ() + look.z() * 4.0D);
                        level.addFreshEntity(thunderBall);
                        this.tempest.setChargeTime(-40);
                    }
                } else if (this.tempest.getChargeTime() > 0) {
                    this.tempest.setChargeTime(this.tempest.getChargeTime() - 1);
                }
            }
        }
    }
}
