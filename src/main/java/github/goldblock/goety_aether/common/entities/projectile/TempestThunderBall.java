package github.goldblock.goety_aether.common.entities.projectile;

import com.Polarice3.Goety.common.entities.util.MagicLightningTrap;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import github.goldblock.goety_aether.compat.genesis.GenesisBridge;
import github.goldblock.goety_aether.compat.genesis.GenesisCompatManager;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;

public class TempestThunderBall extends AbstractHurtingProjectile {
    private int ticksInAir;

    public TempestThunderBall(EntityType<? extends TempestThunderBall> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
    }

    public TempestThunderBall(Level level, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(GenesisCompatManager.TEMPEST_THUNDERBALL.get(), shooter, accelX, accelY, accelZ, level);
        this.setNoGravity(true);
    }

    @Override
    public void tick() {
        if (!this.onGround()) {
            ++this.ticksInAir;
        }
        if (this.ticksInAir > 400 && !this.level().isClientSide) {
            this.discard();
        }
        if (this.level().isClientSide || (this.getOwner() == null || this.getOwner().isAlive()) && this.level().hasChunkAt(this.blockPosition())) {
            HitResult hitResult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitResult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitResult)) {
                this.onHit(hitResult);
            }
            this.checkInsideBlocks();
            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            float inertia = this.getInertia();
            if (this.isInWater()) {
                for (int i = 0; i < 4; ++i) {
                    this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D, d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
                }
                inertia = 0.8F;
            }
            this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale(inertia));
            double xOffset = this.position().x + this.level().getRandom().nextDouble() * 1.5D - 0.75D;
            double yOffset = this.position().y + this.level().getRandom().nextDouble() * 2.0D - 0.5D;
            double zOffset = this.position().z + this.level().getRandom().nextDouble() * 1.5D - 0.75D;
            if (this.level().isClientSide) {
                ParticleOptions electricity = GenesisBridge.particle("tempest_electricity") instanceof ParticleOptions options ? options : ParticleTypes.ELECTRIC_SPARK;
                this.level().addParticle(electricity, xOffset + 0.3D, yOffset + 0.3D, zOffset + 0.3D, 0.0D, 0.0D, 0.0D);
                this.level().addParticle(electricity, xOffset, yOffset, zOffset, 0.0D, 0.0D, 0.0D);
            }
            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }
    }

    @Override
    public void onHit(HitResult result) {
        if (result.getType() == HitResult.Type.BLOCK) {
            return;
        }
        if (this.level().isClientSide) {
            super.onHit(result);
            return;
        }
        if (result instanceof EntityHitResult entityHitResult) {
            this.onHitEntity(entityHitResult);
            this.spawnLightningTrap(entityHitResult.getEntity());
            this.spawnColdFire(entityHitResult.getEntity());
            this.discard();
        } else {
            super.onHit(result);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide) {
            Entity target = result.getEntity();
            target.hurt(this.damageSources().indirectMagic(this, this.getOwner()), 4.0F);
            if (this.getOwner() instanceof LivingEntity living) {
                this.doEnchantDamageEffects(living, target);
            }
        }
    }

    private void spawnLightningTrap(Entity target) {
        if (this.level() instanceof ServerLevel serverLevel) {
            MagicLightningTrap trap = new MagicLightningTrap(serverLevel, target.getX(), target.getY(), target.getZ());
            if (this.getOwner() instanceof LivingEntity living) {
                trap.setOwner(living);
            }
            trap.setDuration(1);
            serverLevel.addFreshEntity(trap);
        }
    }

    private void spawnColdFire(Entity target) {
        if (this.level() instanceof ServerLevel serverLevel) {
            LivingEntity owner = this.getOwner() instanceof LivingEntity living ? living : null;
            WandUtil.spawnIceBouquet(serverLevel, target.position(), owner, 0.0F, 0);
        }
    }

    @Override
    protected boolean canHitEntity(Entity target) {
        return super.canHitEntity(target) && !(this.getOwner() != null && MobUtil.areAllies(this.getOwner(), target));
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    protected boolean shouldBurn() {
        return false;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
