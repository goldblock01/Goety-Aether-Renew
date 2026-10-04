package github.goldblock.goety_aether.common.entities.projectile;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.utils.MathHelper;
import com.aetherteam.aether.data.resources.registries.AetherDamageTypes;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import github.goldblock.goety_aether.mixin.IceCrystalAccessor;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class IceCrystal extends com.aetherteam.aether.entity.projectile.crystal.IceCrystal {
    private float bonusDamage;
    private boolean freezing;

    public IceCrystal(EntityType<? extends com.aetherteam.aether.entity.projectile.crystal.IceCrystal> type, Level level) {
        super(type, level);
    }

    public IceCrystal(Level level, Entity shooter, float bonusDamage, boolean freezing) {
        this(ModEntityTypes.ICE_CRYSTAL.get(), level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getY(), shooter.getZ());
        this.bonusDamage = bonusDamage;
        this.freezing = freezing;
        float rotation = this.random.nextFloat() * 360.0F;
        double xPower = Mth.cos(rotation) * 0.2D;
        double zPower = -Mth.sin(rotation) * 0.2D;
        ((IceCrystalAccessor) this).setXPower(xPower);
        ((IceCrystalAccessor) this).setZPower(zPower);
        this.setDeltaMovement(xPower, 0.0D, zPower);
    }

    public IceCrystal(Level level, LivingEntity shooter, float bonusDamage, boolean freezing, Vec3 direction) {
        this(ModEntityTypes.ICE_CRYSTAL.get(), level);
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1D, shooter.getZ());
        this.bonusDamage = bonusDamage;
        this.freezing = freezing;
        double xPower = direction.x * 0.2D;
        double zPower = direction.z * 0.2D;
        ((IceCrystalAccessor) this).setXPower(xPower);
        ((IceCrystalAccessor) this).setZPower(zPower);
        this.setDeltaMovement(xPower, 0.0D, zPower);
    }

    @Override
    public void doDamage(Entity entity) {
        if (this.getOwner() != entity && entity instanceof LivingEntity living) {
            if (living.hurt(AetherDamageTypes.indirectEntityDamageSource(this.level(), AetherDamageTypes.ICE_CRYSTAL, this, this.getOwner()), 7.0F + this.bonusDamage)) {
                this.damageWithWeakness(this, living, this.random);
                if (this.freezing) {
                    living.addEffect(new MobEffectInstance(GoetyEffects.FREEZING.get(), MathHelper.secondsToTicks(2)));
                }
            }
        }
    }
}
