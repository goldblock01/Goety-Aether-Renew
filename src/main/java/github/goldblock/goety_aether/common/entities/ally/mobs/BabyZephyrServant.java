package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.aetherteam.aether.client.AetherSoundEvents;
import com.aetherteam.aether.entity.projectile.ZephyrSnowball;
import com.aetherteam.aether.entity.projectile.crystal.CloudCrystal;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BabyZephyrServant extends ZephyrServant {

    public BabyZephyrServant(EntityType<? extends ZephyrServant> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true, false));
    }

    @Override
    protected void registerShootGoal() {
        this.goalSelector.addGoal(7, new BabyZephyrShootGoal(this));
    }

    @Override
    protected void registerMeleeGoal() {
        this.goalSelector.addGoal(4, new BabyZephyrMeleeGoal(this));
    }

    private void randomFloatPos() {
        double x = this.getX() + (this.getRandom().nextFloat() * 2.0F - 1.0F) * 4.0F;
        double y = this.getY() + (this.getRandom().nextFloat() * 2.0F - 1.0F) * 4.0F;
        double z = this.getZ() + (this.getRandom().nextFloat() * 2.0F - 1.0F) * 4.0F;
        this.getMoveControl().setWantedPosition(x, y, z, 1.5D);
    }

    static class BabyZephyrMeleeGoal extends Goal {
        private final BabyZephyrServant zephyr;
        private int cooldown = 20;
        private int time = 0;
        private boolean hasAttacked = false;

        BabyZephyrMeleeGoal(BabyZephyrServant zephyr) {
            this.zephyr = zephyr;
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.zephyr.getTarget();
            if (target == null || !target.isAlive() || !this.zephyr.canAttack(target)) {
                return false;
            }
            if (this.cooldown > 0) {
                this.cooldown--;
                return false;
            }
            this.cooldown = this.zephyr.getRandom().nextInt(20) + 20;
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            if (this.hasAttacked) {
                this.zephyr.randomFloatPos();
                return false;
            }
            if (this.time > 200) {
                return false;
            }
            LivingEntity target = this.zephyr.getTarget();
            if (target == null || !target.isAlive()) {
                return false;
            }
            return this.zephyr.isPathFinding();
        }

        @Override
        public void start() {
            this.time = 0;
            this.hasAttacked = false;
            LivingEntity target = this.zephyr.getTarget();
            if (target != null) {
                Vec3 targetPos = target.position().add(0.0D, 2.0D, 0.0D);
                this.zephyr.getMoveControl().setWantedPosition(targetPos.x, targetPos.y, targetPos.z, 2.0D);
            }
            this.zephyr.setAggressive(true);
        }

        @Override
        public void stop() {
            LivingEntity target = this.zephyr.getTarget();
            if (target != null && !target.isAlive()) {
                this.zephyr.setTarget(null);
            }
            this.zephyr.setAggressive(false);
            this.zephyr.randomFloatPos();
        }

        @Override
        public void tick() {
            this.time++;
            LivingEntity target = this.zephyr.getTarget();
            if (target != null) {
                Vec3 targetPos = target.position().add(0.0D, 2.0D, 0.0D);
                this.zephyr.getMoveControl().setWantedPosition(targetPos.x, targetPos.y, targetPos.z, 1.5D * (1.0D + this.time / 100.0D));
                if (this.zephyr.position().distanceToSqr(targetPos) < 1.3D) {
                    target.hurt(this.zephyr.damageSources().mobAttack(this.zephyr), 4.0F);
                    this.hasAttacked = true;
                }
            }
        }
    }

    static class BabyZephyrShootGoal extends Goal {
        private final BabyZephyrServant zephyr;

        BabyZephyrShootGoal(BabyZephyrServant zephyr) {
            this.zephyr = zephyr;
        }

        @Override
        public boolean canUse() {
            return this.zephyr.getTarget() != null;
        }

        @Override
        public void start() {
            this.zephyr.setChargeTime(0);
        }

        @Override
        public void stop() {
            this.zephyr.setChargeTime(0);
        }

        @Override
        public void tick() {
            LivingEntity target = this.zephyr.getTarget();
            if (target != null) {
                if (target.distanceToSqr(this.zephyr) < 1600.0D && this.zephyr.hasLineOfSight(target)) {
                    Level level = this.zephyr.level();
                    this.zephyr.setChargeTime(this.zephyr.getChargeTime() + 1);
                    if (this.zephyr.getChargeTime() == 2) {
                        this.zephyr.playSound(AetherSoundEvents.ENTITY_ZEPHYR_AMBIENT.get(), 3.0F, (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F);
                    } else if (this.zephyr.getChargeTime() == 12) {
                        Vec3 look = this.zephyr.getViewVector(1.0F);
                        double accelX = target.getX() - (this.zephyr.getX() + look.x() * 4.0D);
                        double accelY = target.getY(0.5D) - (0.5D + this.zephyr.getY(0.5D));
                        double accelZ = target.getZ() - (this.zephyr.getZ() + look.z() * 4.0D);
                        this.zephyr.playSound(AetherSoundEvents.ENTITY_ZEPHYR_SHOOT.get(), 3.0F, (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F);
                        if (this.zephyr.getRandom().nextBoolean()) {
                            ZephyrSnowball snowball = new ZephyrSnowball(level, this.zephyr, accelX * 2.0D, accelY * 2.0D, accelZ * 2.0D);
                            snowball.setPos(this.zephyr.getX() + look.x() * 4.0D, this.zephyr.getY(0.5D) + 0.5D, this.zephyr.getZ() + look.z() * 4.0D);
                            level.addFreshEntity(snowball);
                        } else {
                            CloudCrystal crystal = new CloudCrystal(level);
                            crystal.setOwner(this.zephyr);
                            crystal.setPos(this.zephyr.getX() + look.x() * 4.0D, this.zephyr.getY(0.5D) + 0.5D, this.zephyr.getZ() + look.z() * 4.0D);
                            double dx = target.getX() - crystal.getX();
                            double dy = target.getY(0.5D) - crystal.getY();
                            double dz = target.getZ() - crystal.getZ();
                            crystal.shoot(dx, dy, dz, 1.0F, 0.0F);
                            level.addFreshEntity(crystal);
                        }
                        this.zephyr.setChargeTime(-1);
                    }
                } else if (this.zephyr.getChargeTime() > 0) {
                    this.zephyr.setChargeTime(this.zephyr.getChargeTime() - 1);
                }
            }
        }
    }
}
