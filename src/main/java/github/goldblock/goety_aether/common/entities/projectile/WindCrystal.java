package github.goldblock.goety_aether.common.entities.projectile;

import com.aetherteam.aether.client.AetherSoundEvents;
import com.aetherteam.aether.entity.projectile.crystal.AbstractCrystal;
import com.Polarice3.Goety.utils.MobUtil;
import github.goldblock.goety_aether.compat.deep_aether.DeepAetherCompatManager;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class WindCrystal extends AbstractCrystal {
    private static final EntityDataAccessor<Boolean> IS_FRIENDLY = SynchedEntityData.defineId(WindCrystal.class, EntityDataSerializers.BOOLEAN);

    public WindCrystal(EntityType<? extends WindCrystal> type, Level level) {
        super(type, level);
    }

    public WindCrystal(Level level, Entity shooter, Vec3 direction) {
        this(DeepAetherCompatManager.WIND_CRYSTAL.get(), level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getY(), shooter.getZ());
        this.setDeltaMovement(direction);
        level.addFreshEntity(this);
    }

    public WindCrystal(Level level, Entity shooter, double x, double y, double z) {
        this(level, shooter, new Vec3(x, y, z));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_FRIENDLY, false);
    }

    public void setFriendly(boolean friendly) {
        this.entityData.set(IS_FRIENDLY, friendly);
    }

    public boolean isFriendly() {
        return this.entityData.get(IS_FRIENDLY);
    }

    private float getDamage() {
        return this.isFriendly() ? 6.0F : 10.0F;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        if (target instanceof LivingEntity living) {
            DamageSource source = this.damageSources().mobProjectile(this, this.getOwner() instanceof LivingEntity owner ? owner : null);
            if (living.hurt(source, this.getDamage())) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), this.getImpactExplosionSoundEvent(), SoundSource.HOSTILE, 2.0F, this.random.nextFloat() - this.random.nextFloat() * 0.2F + 1.2F);
                this.discard();
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        if (this.getOwner() != null && (target.isAlliedTo(this.getOwner()) || MobUtil.areAllies(this.getOwner(), target))) {
            return false;
        }
        return super.canHitEntity(target);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        this.markHurt();
        if (result.getDirection() == Direction.UP) {
            float offset = this.random.nextInt(200) / 1000.0F;
            new WindCrystal(this.level(), this, 0.3D + offset, 0.0D, 0.3D + offset);
            new WindCrystal(this.level(), this, -0.3D + offset, 0.0D, 0.3D - offset);
            new WindCrystal(this.level(), this, 0.3D - offset, 0.0D, -0.3D - offset);
            new WindCrystal(this.level(), this, -0.3D - offset, 0.0D, -0.3D + offset);
        }
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), AetherSoundEvents.ENTITY_ICE_CRYSTAL_EXPLODE.get(), SoundSource.HOSTILE, 1.0F, 1.0F);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    protected SoundEvent getImpactExplosionSoundEvent() {
        return AetherSoundEvents.ENTITY_ICE_CRYSTAL_EXPLODE.get();
    }

    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    protected ParticleOptions getExplosionParticle() {
        return ParticleTypes.CLOUD;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("isFriendly", this.isFriendly());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setFriendly(tag.getBoolean("isFriendly"));
    }
}
