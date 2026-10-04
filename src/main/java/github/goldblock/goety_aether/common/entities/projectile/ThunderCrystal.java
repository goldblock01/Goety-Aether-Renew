package github.goldblock.goety_aether.common.entities.projectile;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.utils.MathHelper;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class ThunderCrystal extends com.aetherteam.aether.entity.projectile.crystal.ThunderCrystal {
    private boolean spasms;

    public ThunderCrystal(EntityType<? extends com.aetherteam.aether.entity.projectile.crystal.ThunderCrystal> type, Level level) {
        super(type, level);
    }

    public ThunderCrystal(Level level, Entity shooter, Entity target, boolean spasms) {
        super(ModEntityTypes.THUNDER_CRYSTAL.get(), level, shooter, target);
        this.spasms = spasms;
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.spasms && result.getEntity() instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(GoetyEffects.SPASMS.get(), MathHelper.secondsToTicks(2)));
        }
        super.onHitEntity(result);
    }
}
