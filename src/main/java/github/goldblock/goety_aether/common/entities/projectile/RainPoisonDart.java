package github.goldblock.goety_aether.common.entities.projectile;

import com.aetherteam.aether.entity.projectile.dart.PoisonDart;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class RainPoisonDart extends PoisonDart {

    public RainPoisonDart(EntityType<? extends RainPoisonDart> type, Level level) {
        super(type, level);
    }

    public RainPoisonDart(Level level) {
        super(ModEntityTypes.RAIN_POISON_DART.get(), level);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (result.getEntity() instanceof LivingEntity living) {
            living.invulnerableTime = 0;
        }
        super.onHitEntity(result);
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (this.getPersistentData().getBoolean("PoisonDartRain")) {
            this.discard();
        }
    }
}
