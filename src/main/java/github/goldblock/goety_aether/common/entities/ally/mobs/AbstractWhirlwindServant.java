package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.common.entities.ally.Summoned;
import com.aetherteam.aether.AetherTags;
import com.aetherteam.aether.client.AetherSoundEvents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractWhirlwindServant extends Summoned {
    public static final EntityDataAccessor<Integer> DATA_COLOR_ID = SynchedEntityData.defineId(AbstractWhirlwindServant.class, EntityDataSerializers.INT);

    private int dropsTimer;
    private float movementAngle;
    private float movementCurve;
    private boolean isEvil = false;

    public AbstractWhirlwindServant(EntityType<? extends AbstractWhirlwindServant> type, Level level) {
        super(type, level);
        if (level.isClientSide()) {
            this.movementAngle = this.getRandom().nextFloat() * 360.0F;
            this.movementCurve = (this.getRandom().nextFloat() - this.getRandom().nextFloat()) * 0.1F;
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.025D)
                .add(Attributes.FOLLOW_RANGE, 16.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_COLOR_ID, this.getDefaultColor());
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide() && this.isInFluidType()) {
            this.discard();
        }
    }

    @Override
    public void aiStep() {
        if (!this.level().isClientSide()) {
            this.dropsTimer++;
            if (this.dropsTimer >= 128) {
                this.spawnDrops();
                this.dropsTimer = 0;
            }
        } else if (!github.goldblock.goety_aether.compat.redux.client.ReduxWhirlwindParticleGate.hideParticles()) {
            this.spawnParticles();
        }

        super.aiStep();

        List<Entity> entityList = this.level().getEntities(this, this.getBoundingBox().inflate(2.5D, 2.5D, 2.5D))
                .stream().filter(entity -> !entity.getType().is(AetherTags.Entities.WHIRLWIND_UNAFFECTED)).toList();
        for (Entity entity : entityList) {
            if (this.isAlliedTo(entity) || entity == this.getTrueOwner()) {
                continue;
            }
            double x = entity.getX();
            double y = entity.getY() - entity.getMyRidingOffset() * 0.6D;
            double z = entity.getZ();
            double distance = this.distanceTo(entity);
            double d1 = y - this.getY();

            if (distance <= 1.5D + d1) {
                entity.setDeltaMovement(entity.getDeltaMovement().x(), 0.15D, entity.getDeltaMovement().z());
                entity.resetFallDistance();

                if (d1 > 1.5D) {
                    entity.setDeltaMovement(entity.getDeltaMovement().x(), -0.45D + d1 * 0.35D, entity.getDeltaMovement().z());
                    distance += d1 * 1.5D;
                } else {
                    entity.setDeltaMovement(entity.getDeltaMovement().x(), 0.125D, entity.getDeltaMovement().z());
                }

                double d2 = Math.atan2(this.getX() - x, this.getZ() - z) / 0.0175D;
                d2 += 160.0D;
                entity.setDeltaMovement(-Math.cos(0.0175D * d2) * (distance + 0.25D) * 0.1D,
                        entity.getDeltaMovement().y,
                        Math.sin(0.0175D * d2) * (distance + 0.25D) * 0.1D);

                if (entity instanceof AbstractWhirlwindServant) {
                    entity.discard();
                }
            } else {
                double d3 = Math.atan2(this.getX() - x, this.getZ() - z) / 0.0175D;
                entity.setDeltaMovement(entity.getDeltaMovement().add(Math.sin(0.0175D * d3) * 0.01D,
                        entity.getDeltaMovement().y, Math.cos(0.0175D * d3) * 0.01D));
            }
        }
    }

    protected void spawnDrops() {
        if (this.level() instanceof ServerLevel serverLevel) {
            if (this.getRandom().nextInt(4) == 0) {
                LootParams parameters = new LootParams.Builder(serverLevel)
                        .withParameter(LootContextParams.ORIGIN, this.position())
                        .withParameter(LootContextParams.THIS_ENTITY, this)
                        .create(LootContextParamSets.SELECTOR);
                LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(this.getLootLocation());
                for (ItemStack itemstack : lootTable.getRandomItems(parameters)) {
                    serverLevel.playSound(null, this.blockPosition(), AetherSoundEvents.ENTITY_WHIRLWIND_DROP.get(), SoundSource.HOSTILE, 0.5F, 1.0F);
                    this.spawnAtLocation(itemstack, 1.0F);
                }
            }
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.getTrueOwner() != null && source.getEntity() == this.getTrueOwner()) {
            return false;
        }
        return super.hurt(source, amount);
    }

    @Override
    public void kill() {
        this.remove(Entity.RemovalReason.KILLED);
        this.gameEvent(GameEvent.ENTITY_DIE);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    public int getColorData() {
        return this.entityData.get(DATA_COLOR_ID);
    }

    public void setColorData(int color) {
        this.entityData.set(DATA_COLOR_ID, color);
    }

    @Override
    public void lifeSpanDamage() {
        if (!this.level().isClientSide() && this.isAlive()) {
            this.discard();
        }
    }

    public float getMovementAngle() {
        return this.movementAngle;
    }

    public void setMovementAngle(float movementAngle) {
        this.movementAngle = movementAngle;
    }

    public float getMovementCurve() {
        return this.movementCurve;
    }

    public void setMovementCurve(float movementCurve) {
        this.movementCurve = movementCurve;
    }

    public boolean isEvil() {
        return this.isEvil;
    }

    public void setEvil(boolean evil) {
        this.isEvil = evil;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Color", this.getColorData());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Color")) {
            this.setColorData(tag.getInt("Color"));
        }
    }

    public abstract void spawnParticles();

    public abstract ResourceLocation getLootLocation();

    public abstract int getDefaultColor();
}
