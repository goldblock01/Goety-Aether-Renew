package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.common.entities.neutral.AbstractMonolith;
import com.Polarice3.Goety.common.entities.neutral.Owned;
import github.goldblock.goety_aether.compat.deep_aether.DeepAetherBridge;
import github.goldblock.goety_aether.compat.deep_aether.DeepAetherCompatManager;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class EOTSSController extends AbstractMonolith {
    private final List<Float> segmentHealths = new ArrayList<>();

    public EOTSSController(EntityType<? extends Owned> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 220.1D)
                .add(Attributes.FOLLOW_RANGE, 96.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D);
    }

    @Override
    public BlockState getState() {
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public ParticleOptions getParticles() {
        return ParticleTypes.CLOUD;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            ParticleOptions preFight = DeepAetherBridge.particle("eots_pre_fight") instanceof ParticleOptions options ? options : ParticleTypes.CLOUD;
            for (int i = 0; i < 2; i++) {
                this.level().addParticle(preFight,
                        this.getX() - 1.1D, this.getY() + 0.25D + this.random.nextFloat() * 2.0F, this.getZ() + 0.3D,
                        0.0D, 0.001D + this.random.nextFloat() * 0.002D, 0.0D);
            }
        }
    }

    @Override
    public int getAgeSpeed() {
        return 5;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.level().isClientSide && !this.isEmerging()) {
            this.releaseSegments();
        }
        return InteractionResult.sidedSuccess(this.level().isClientSide);
    }

    private void releaseSegments() {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }
        LivingEntity owner = this.getTrueOwner();
        EOTSServantSegment head = new EOTSServantSegment(DeepAetherCompatManager.EOTSSERVANT_SEGMENT.get(), serverLevel);
        head.moveTo(this.getX(), this.getY() + 6.0D, this.getZ(), this.getYRot(), 0.0F);
        serverLevel.addFreshEntity(head);
        head.setTrueOwner(owner);
        EOTSServantSegment prev = head;
        for (int i = 1; i < 20; i++) {
            EOTSServantSegment segment = new EOTSServantSegment(serverLevel, prev);
            segment.setTrueOwner(owner);
            prev = segment;
        }
        EOTSServantSegment cur = head;
        for (int i = 0; i < 20 && cur != null; i++) {
            if (i < this.segmentHealths.size()) {
                Float health = this.segmentHealths.get(i);
                if (health != null && health > 0.0F) {
                    cur.setHealth(health);
                }
            }
            cur = cur.getChild();
        }
        this.discard();
    }

    public void setSegmentHealths(List<Float> healths) {
        this.segmentHealths.clear();
        this.segmentHealths.addAll(healths);
    }

    public List<Float> getSegmentHealths() {
        return this.segmentHealths;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ListTag list = new ListTag();
        for (Float health : this.segmentHealths) {
            CompoundTag entry = new CompoundTag();
            entry.putFloat("Health", health);
            list.add(entry);
        }
        tag.put("SegmentHealths", list);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.segmentHealths.clear();
        ListTag list = tag.getList("SegmentHealths", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            this.segmentHealths.add(list.getCompound(i).getFloat("Health"));
        }
    }

    @Override
    public boolean shouldDespawnInPeaceful() {
        return false;
    }

    @Override
    public boolean isPersistenceRequired() {
        return true;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }
}
