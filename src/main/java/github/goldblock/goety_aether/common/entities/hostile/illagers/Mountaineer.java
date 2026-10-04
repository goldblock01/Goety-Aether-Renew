package github.goldblock.goety_aether.common.entities.hostile.illagers;

import com.Polarice3.Goety.common.entities.ai.ModMeleeAttackGoal;
import com.Polarice3.Goety.common.entities.ai.path.ModClimberNavigation;
import com.Polarice3.Goety.common.entities.hostile.illagers.HuntingIllagerEntity;
import com.Polarice3.Goety.init.ModTags;
import com.Polarice3.Goety.common.items.ModItems;
import com.Polarice3.Goety.common.items.equipment.IceAxeItem;
import com.Polarice3.Goety.init.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.function.Predicate;

public class Mountaineer extends HuntingIllagerEntity {
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(Mountaineer.class, EntityDataSerializers.BYTE);

    public Mountaineer(EntityType<? extends HuntingIllagerEntity> type, Level level) {
        super(type, level);
        this.xpReward = 10;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MountaineerBreakDoorGoal(this));
        this.goalSelector.addGoal(4, new MountaineerMeleeAttackGoal(this));
    }

    @Override
    public void extraGoals() {
    }

    public static AttributeSupplier.Builder setCustomAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.FOLLOW_RANGE, 12.0D)
                .add(Attributes.MAX_HEALTH, 28.0D)
                .add(Attributes.ARMOR, 0.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS_ID, (byte) 0);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new ModClimberNavigation(this, level);
    }

    @Override
    public AbstractIllager.IllagerArmPose getArmPose() {
        return this.isAggressive() ? AbstractIllager.IllagerArmPose.ATTACKING : AbstractIllager.IllagerArmPose.NEUTRAL;
    }

    @Override
    public void applyRaidBuffs(int wave, boolean isFinalWave) {
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return null;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag tag) {
        SpawnGroupData data = super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData, tag);
        RandomSource random = level.getRandom();
        this.populateDefaultEquipmentSlots(random, difficulty);
        this.populateDefaultEquipmentEnchantments(random, difficulty);
        return data;
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.IRON_ICE_AXE.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.085F);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.MOUNTAINEER_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return ModSounds.MOUNTAINEER_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.MOUNTAINEER_DEATH.get();
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return ModSounds.MOUNTAINEER_CELEBRATE.get();
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean climbing) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }
        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    public boolean isClimbableBlock(BlockPos blockPos) {
        BlockState blockState = this.level().getBlockState(blockPos);
        return (blockState.is(BlockTags.ICE)
                || blockState.is(Tags.Blocks.STONE)
                || blockState.is(Tags.Blocks.COBBLESTONE)
                || blockState.is(BlockTags.DIRT)
                || blockState.is(BlockTags.SNOW))
                && blockState.isSolidRender(this.level(), blockPos);
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            github.goldblock.goety_aether.common.entities.ally.mobs.CreeperReplacementBridge.tryReplace(this);
            boolean shouldClimb = this.horizontalCollision && !this.isStuckAtCeiling();
            this.setClimbing(shouldClimb);
        }
    }

    private boolean isStuckAtCeiling() {
        BlockPos above = this.blockPosition().above(2);
        return this.level().getBlockState(above).isSolidRender(this.level(), above) && this.getDeltaMovement().y <= 0.01D;
    }

    public boolean isMainWeapon(ItemStack itemStack) {
        return itemStack.getItem() instanceof IceAxeItem || itemStack.is(ModTags.Items.MOUNTAINEER_WEAPONS);
    }

    static class MountaineerBreakDoorGoal extends BreakDoorGoal {
        private static final Predicate<Difficulty> DOOR_BREAKING_PREDICATE = (difficulty) -> difficulty == Difficulty.NORMAL || difficulty == Difficulty.HARD;

        public MountaineerBreakDoorGoal(Mountaineer mountaineer) {
            super(mountaineer, 6, DOOR_BREAKING_PREDICATE);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canContinueToUse() {
            Mountaineer mountaineer = (Mountaineer) this.mob;
            return mountaineer.hasActiveRaid() && super.canContinueToUse();
        }

        @Override
        public boolean canUse() {
            Mountaineer mountaineer = (Mountaineer) this.mob;
            return mountaineer.hasActiveRaid() && mountaineer.random.nextInt(reducedTickDelay(10)) == 0 && super.canUse();
        }

        @Override
        public void start() {
            super.start();
            this.mob.setNoActionTime(0);
        }
    }

    static class MountaineerMeleeAttackGoal extends ModMeleeAttackGoal {
        public MountaineerMeleeAttackGoal(Mountaineer mountaineer) {
            super(mountaineer, 1.0D, false);
        }
    }
}
