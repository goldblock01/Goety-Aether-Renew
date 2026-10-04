package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.entities.ally.BlackWolf;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class SkyWolfServant extends BlackWolf {
    public SkyWolfServant(EntityType<? extends Owned> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean flag = super.doHurtTarget(entity);
        if (flag && entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(GoetyEffects.SUN_ALLERGY.get(), 40, 0), this);
        }
        return flag;
    }
}
