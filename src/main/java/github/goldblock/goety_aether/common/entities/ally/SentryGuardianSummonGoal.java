package github.goldblock.goety_aether.common.entities.ally;

import github.goldblock.goety_aether.compat.genesis.GenesisBridge;
import github.goldblock.goety_aether.common.entities.ally.mobs.SentryServant;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ServerLevelAccessor;

public class SentryGuardianSummonGoal extends Goal {
    private final Mob mob;
    private int spawnDelay;

    public SentryGuardianSummonGoal(Mob pMob) {
        this.mob = pMob;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        return target != null && target.isAlive() && this.mob.level().getDifficulty() != Difficulty.PEACEFUL;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.mob.level().getRandom().nextInt(75) == 1 && this.mob.getTarget() != null) {
            this.spawnDelay = 10;
            this.mob.setDeltaMovement(0.0D, 0.5D, 0.0D);
        }
        this.spawnDelay--;
        if (this.spawnDelay == 0) {
            this.spawnSentry();
            this.spawnDelay = -1;
        }
    }

    public void spawnSentry() {
        if (!this.mob.level().isClientSide()) {
            SentryServant sentryServant = new SentryServant(ModEntityTypes.SENTRY_SERVANT.get(), this.mob.level());
            sentryServant.setTrueOwner(this.mob);
            sentryServant.setPos(this.mob.position());
            sentryServant.finalizeSpawn((ServerLevelAccessor) this.mob.level(), this.mob.level().getCurrentDifficultyAt(sentryServant.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            this.mob.level().addFreshEntity(sentryServant);
            sentryServant.setDeltaMovement(0.0D, 1.0D, 0.0D);
            sentryServant.fallDistance = -100.0F;
            sentryServant.setTarget(this.mob.getTarget());
            this.mob.level().playSound(this.mob, sentryServant.blockPosition(), GenesisBridge.sound("entity.sentry_guardian.summon"), SoundSource.AMBIENT, 2.0F, 1.0F);
        }
    }
}
