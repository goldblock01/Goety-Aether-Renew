package github.goldblock.goety_aether.common.entities.projectile;

import com.Polarice3.Goety.client.particles.CircleExplodeParticleOption;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.client.particles.SphereExplodeParticleOption;
import com.Polarice3.Goety.common.entities.util.CameraShake;
import com.Polarice3.Goety.utils.ColorUtil;
import com.Polarice3.Goety.utils.MobUtil;
import github.goldblock.goety_aether.common.entities.ally.mobs.SentryGolem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class SentryBomb extends ThrowableProjectile {
    private static final float EXPLOSION_RADIUS = 1.0F;

    public SentryBomb(EntityType<SentryBomb> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public SentryBomb(EntityType<SentryBomb> pEntityType, double pX, double pY, double pZ, Level pLevel) {
        super(pEntityType, pX, pY, pZ, pLevel);
    }

    public SentryBomb(EntityType<SentryBomb> pEntityType, LivingEntity pShooter, Level pLevel) {
        super(pEntityType, pShooter, pLevel);
    }

    @Override
    protected void defineSynchedData() {
    }

    @Override
    protected boolean canHitEntity(Entity pTarget) {
        return super.canHitEntity(pTarget) && !(pTarget instanceof SentryGolem)
                && !(this.getOwner() != null && MobUtil.areAllies(this.getOwner(), pTarget));
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        if (!this.level().isClientSide()) {
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if (!this.level().isClientSide()) {
            this.explode();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (!this.level().isClientSide()) {
            this.explode();
        }
    }

    private void explode() {
        this.allySafeExplosion(EXPLOSION_RADIUS);
        this.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 0.2F * (this.random.nextFloat() - this.random.nextFloat()) + 1.0F);
        if (this.level() instanceof ServerLevel serverLevel) {
            float explosionPower = EXPLOSION_RADIUS * 5.0F;
            ColorUtil colorUtil = new ColorUtil(0x7EC8F2);
            serverLevel.sendParticles(new CircleExplodeParticleOption(colorUtil.red, colorUtil.green, colorUtil.blue, explosionPower, 1), this.getX(), this.getY(), this.getZ(), 0, 0.0D, 0.0D, 0.0D, 0);
            serverLevel.sendParticles(new SphereExplodeParticleOption(colorUtil.red, colorUtil.green, colorUtil.blue, explosionPower, 1), this.getX(), this.getY(), this.getZ(), 0, 0.0D, 0.0D, 0.0D, 0);
            for (int i = 0; i < 32; ++i) {
                ColorUtil colorUtil1 = new ColorUtil(0xac9b8f);
                serverLevel.sendParticles(ModParticleTypes.BIG_CULT_SPELL.get(), this.getRandomX(1.0F), this.getRandomY(), this.getRandomZ(1.0F), 0, colorUtil1.red, colorUtil1.green, colorUtil1.blue, 1.0F);
            }
            CameraShake.cameraShake(this.level(), this.position(), explosionPower * 2, 0.1F, 0, 20);
        }
    }

    private void allySafeExplosion(float pRadius) {
        Explosion explosion = new Explosion(this.level(), this, this.getX(), this.getY(), this.getZ(), pRadius, false, Explosion.BlockInteraction.KEEP);
        float f = pRadius * 2.0F;
        Vec3 center = new Vec3(this.getX(), this.getY(), this.getZ());
        for (Entity entity : this.level().getEntities(this, this.getBoundingBox().inflate(f), target -> target.isAlive() && !this.isFriendlyToward(target))) {
            double d1 = entity.getX() - this.getX();
            double d2 = entity.getY() + entity.getBbHeight() * 0.5D - this.getY();
            double d3 = entity.getZ() - this.getZ();
            double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
            if (d4 != 0.0D) {
                d1 /= d4;
                d2 /= d4;
                d3 /= d4;
                double d5 = Math.max(1.0D - d4 / f, 0.0D) * Explosion.getSeenPercent(center, entity);
                entity.hurt(this.damageSources().explosion(explosion), (float) ((d5 * d5 + d5) / 2.0D * 7.0D * f + 1.0D));
                double d6 = d5;
                if (entity instanceof LivingEntity living) {
                    living.invulnerableTime = 0;
                    d6 = ProtectionEnchantment.getExplosionKnockbackAfterDampener(living, d5);
                }
                entity.setDeltaMovement(entity.getDeltaMovement().add(d1 * d6, d2 * d6, d3 * d6));
            }
        }
    }

    private boolean isFriendlyToward(Entity target) {
        Entity owner = this.getOwner();
        if (owner != null) {
            return target == owner || MobUtil.areAllies((LivingEntity) owner, target);
        }
        return false;
    }
}
