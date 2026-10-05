package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.common.entities.ally.Summoned;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomFlyingGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import teamrazor.deepaether.entity.VenomiteBubble;
import teamrazor.deepaether.init.DASounds;

public class VenomiteServant extends Summoned {
    private static final int TICKS_PER_FLAP = net.minecraft.util.Mth.ceil(1.4959966F);

    public VenomiteServant(EntityType<? extends Summoned> type, Level level) {
        super(type, level);
        this.xpReward = 0;
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.4D, true));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new FloatGoal(this));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, level);
        flyingpathnavigation.setCanFloat(true);
        return flyingpathnavigation;
    }

    @Override
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return level.getBlockState(pos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return !source.is(DamageTypes.DROWN) && super.hurt(source, amount);
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.5F;
    }

    @Override
    public boolean isFlapping() {
        return this.isFlying() && this.tickCount % TICKS_PER_FLAP == 0;
    }

    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public void onRemovedFromWorld() {
        if (!this.level().isClientSide && this.isAlive() && this.random.nextBoolean()) {
            VenomiteBubble bubble = new VenomiteBubble(this.level());
            bubble.setPos(this.getPosition(0.0F));
            this.level().addFreshEntity(bubble);
        }
        super.onRemovedFromWorld();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return DASounds.VENOMITE_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return DASounds.VENOMITE_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return DASounds.VENOMITE_DEATH.get();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    protected static class MeleeChaseGoal extends Goal {
        private final VenomiteServant venomite;

        public MeleeChaseGoal(VenomiteServant venomite) {
            this.venomite = venomite;
        }

        @Override
        public boolean canUse() {
            net.minecraft.world.entity.LivingEntity target = this.venomite.getTarget();
            return target != null && target.isAlive();
        }
    }
}
