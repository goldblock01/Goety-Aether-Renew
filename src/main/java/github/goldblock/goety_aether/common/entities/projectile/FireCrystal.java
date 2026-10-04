package github.goldblock.goety_aether.common.entities.projectile;

import com.aetherteam.aether.data.resources.registries.AetherDamageTypes;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import github.goldblock.goety_aether.mixin.FireCrystalAccessor;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class FireCrystal extends com.aetherteam.aether.entity.projectile.crystal.FireCrystal {
    private float bonusDamage;

    public FireCrystal(EntityType<? extends com.aetherteam.aether.entity.projectile.crystal.FireCrystal> type, Level level) {
        super(type, level);
    }

    public FireCrystal(Level level, Entity shooter, float bonusDamage) {
        this(ModEntityTypes.FIRE_CRYSTAL.get(), level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getY() + 1.0D, shooter.getZ());
        this.bonusDamage = bonusDamage;
        float rotation = this.random.nextFloat() * 360.0F;
        double xPower = Mth.cos(rotation) * 0.5D;
        double zPower = -Mth.sin(rotation) * 0.5D;
        double yPower = Mth.cos(this.random.nextFloat() * 360.0F) * 0.45D;
        double verticalOffset = 1.0D - Math.abs(yPower);
        xPower *= verticalOffset;
        zPower *= verticalOffset;
        ((FireCrystalAccessor) this).setXPower(xPower);
        ((FireCrystalAccessor) this).setYPower(yPower);
        ((FireCrystalAccessor) this).setZPower(zPower);
        this.setDeltaMovement(xPower, yPower, zPower);
    }

    public FireCrystal(Level level, LivingEntity shooter, float bonusDamage, Vec3 direction) {
        this(ModEntityTypes.FIRE_CRYSTAL.get(), level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
        this.bonusDamage = bonusDamage;
        double xPower = direction.x * 0.5D;
        double yPower = direction.y * 0.5D;
        double zPower = direction.z * 0.5D;
        ((FireCrystalAccessor) this).setXPower(xPower);
        ((FireCrystalAccessor) this).setYPower(yPower);
        ((FireCrystalAccessor) this).setZPower(zPower);
        this.setDeltaMovement(xPower, yPower, zPower);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity living) {
            if (living.hurt(AetherDamageTypes.indirectEntityDamageSource(this.level(), AetherDamageTypes.FIRE_CRYSTAL, this, this.getOwner()), 15.0F + this.bonusDamage)) {
                living.setSecondsOnFire(6);
                SoundEvent sound = this.getImpactExplosionSoundEvent();
                if (sound != null) {
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), sound, SoundSource.HOSTILE, 2.0F, this.random.nextFloat() - this.random.nextFloat() * 0.2F + 1.2F);
                }
                if (!this.level().isClientSide()) {
                    this.discard();
                }
            }
        }
    }
}
