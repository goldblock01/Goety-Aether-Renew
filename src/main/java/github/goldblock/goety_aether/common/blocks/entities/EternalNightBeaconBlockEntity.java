package github.goldblock.goety_aether.common.blocks.entities;

import com.Polarice3.Goety.common.blocks.entities.ChunkLoadBlockEntity;
import com.Polarice3.Goety.client.particles.ShockwaveParticleOption;
import com.Polarice3.Goety.utils.ColorUtil;
import com.aetherteam.aether.block.AetherBlocks;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import github.goldblock.goety_aether.common.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class EternalNightBeaconBlockEntity extends ChunkLoadBlockEntity {
    private static final ResourceLocation AETHER_DIMENSION_TYPE_ID = new ResourceLocation("aether", "the_aether");
    private static final long AETHER_MIDNIGHT_TIME = 54000L;
    private static final long AETHER_NOON_TIME = 18000L;

    List<BeaconBeamSection> beamSections = Lists.newArrayList();
    private List<BeaconBeamSection> checkingBeamSections = Lists.newArrayList();
    private boolean daylightTrue;
    private boolean isNight;
    private boolean hasPortal;
    private boolean isActive;
    private int lastCheckY;

    public EternalNightBeaconBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ETERNAL_NIGHT_BEACON.get(), pos, state);
    }

    @Override
    public boolean shouldChunkLoad() {
        return this.isActive;
    }

    @Override
    public int selfLoadRadius() {
        return 1;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, EternalNightBeaconBlockEntity blockEntity) {
        blockEntity.chunkLoadBlock();
        int i = pos.getX();
        int j = pos.getY();
        int k = pos.getZ();
        BlockPos blockpos;
        if (blockEntity.lastCheckY < j) {
            blockpos = pos;
            blockEntity.checkingBeamSections = Lists.newArrayList();
            blockEntity.lastCheckY = pos.getY() - 1;
        } else {
            blockpos = new BlockPos(i, blockEntity.lastCheckY + 1, k);
        }

        BeaconBeamSection beaconSection = blockEntity.checkingBeamSections.isEmpty()
                ? null
                : blockEntity.checkingBeamSections.get(blockEntity.checkingBeamSections.size() - 1);
        int l = level.getHeight(Heightmap.Types.WORLD_SURFACE, i, k);

        for (int i1 = 0; i1 < 10 && blockpos.getY() <= l; ++i1) {
            BlockState blockstate = level.getBlockState(blockpos);
            float[] afloat = blockstate.getBeaconColorMultiplier(level, blockpos, pos);
            if (afloat != null) {
                if (blockEntity.checkingBeamSections.size() <= 1) {
                    beaconSection = new BeaconBeamSection();
                    blockEntity.checkingBeamSections.add(beaconSection);
                } else if (beaconSection != null) {
                    beaconSection.increaseHeight();
                }
            } else {
                if (beaconSection == null || blockstate.getLightBlock(level, blockpos) >= 15 && !blockstate.is(Blocks.BEDROCK)) {
                    blockEntity.checkingBeamSections.clear();
                    blockEntity.lastCheckY = l;
                    break;
                }
                beaconSection.increaseHeight();
            }

            blockpos = blockpos.above();
            ++blockEntity.lastCheckY;
        }

        if (level.getGameTime() % 20L == 0L) {
            if (!blockEntity.beamSections.isEmpty()) {
                blockEntity.hasPortal = hasPortal(level, pos);
            }
        }

        if (level.getGameTime() % 80L == 0L) {
            if (blockEntity.isActive && !blockEntity.beamSections.isEmpty()) {
                playSound(level, pos, SoundEvents.BEACON_AMBIENT);
            }
        }

        if (level.getServer() != null) {
            if (blockEntity.isActive && !blockEntity.beamSections.isEmpty() && isAetherDimension(level)) {
                lockAetherNight((ServerLevel) level);
            }
        }

        if (blockEntity.lastCheckY >= l) {
            blockEntity.lastCheckY = level.getMinBuildHeight() - 1;
            blockEntity.beamSections = blockEntity.checkingBeamSections;
        }

        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            if (!blockEntity.isActive && !serverLevel.dimensionType().hasFixedTime() && !blockEntity.getBeamSections().isEmpty()) {
                playSound(level, pos, SoundEvents.BEACON_ACTIVATE);
                ColorUtil colorUtil = new ColorUtil(0x87cefa);
                serverLevel.sendParticles(new ShockwaveParticleOption(colorUtil.red, colorUtil.green, colorUtil.blue, 8.0F, 0, true),
                        i + 0.5F, j, k + 0.5F, 0, 0, 0, 0, 0);
                blockEntity.isActive = true;
            } else if (blockEntity.isActive && blockEntity.getBeamSections().isEmpty()) {
                playSound(level, pos, SoundEvents.BEACON_DEACTIVATE);
                blockEntity.isActive = false;
            }
        }
    }

    private static boolean hasPortal(Level level, BlockPos blockPos) {
        BlockState below = level.getBlockState(blockPos.below());
        BlockState below2 = level.getBlockState(blockPos.below().below());
        return below.is(Blocks.NETHER_PORTAL) || below.is(AetherBlocks.AETHER_PORTAL.get())
                || below2.is(Blocks.NETHER_PORTAL) || below2.is(AetherBlocks.AETHER_PORTAL.get());
    }

    private static boolean isAetherDimension(Level level) {
        return level.dimensionType().effectsLocation().equals(AETHER_DIMENSION_TYPE_ID);
    }

    private static void lockAetherNight(ServerLevel serverLevel) {
        serverLevel.setDayTime(AETHER_MIDNIGHT_TIME);
        for (ServerPlayer player : serverLevel.players()) {
            player.connection.send(new ClientboundSetTimePacket(serverLevel.getGameTime(), serverLevel.getDayTime(),
                    serverLevel.getGameRules().getBoolean(GameRules.RULE_DAYLIGHT)));
        }
    }

    private static void restoreAetherDay(ServerLevel serverLevel) {
        serverLevel.setDayTime(AETHER_NOON_TIME);
    }

    public void setDayLight(boolean daylightTrue) {
        this.daylightTrue = daylightTrue;
    }

    @Override
    public void setRemoved() {
        if (this.level != null) {
            playSound(this.level, this.worldPosition, SoundEvents.BEACON_DEACTIVATE);
            if (this.level.getServer() != null && this.isActive && !this.beamSections.isEmpty() && isAetherDimension(this.level)) {
                restoreAetherDay((ServerLevel) this.level);
            }
        }
        super.setRemoved();
    }

    public static void playSound(Level level, BlockPos pos, SoundEvent sound) {
        level.playSound((Player) null, pos, sound, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public List<BeaconBeamSection> getBeamSections() {
        return !this.hasPortal ? ImmutableList.of() : this.beamSections;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.daylightTrue = tag.getBoolean("daylightTrue");
        this.isNight = tag.getBoolean("isNight");
        this.hasPortal = tag.getBoolean("hasPortal");
        this.isActive = tag.getBoolean("isActive");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putBoolean("daylightTrue", this.daylightTrue);
        tag.putBoolean("isNight", this.isNight);
        tag.putBoolean("hasPortal", this.hasPortal);
        tag.putBoolean("isActive", this.isActive);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (pkt.getTag() != null) {
            this.load(pkt.getTag());
        }
        super.onDataPacket(net, pkt);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public AABB getRenderBoundingBox() {
        return BlockEntity.INFINITE_EXTENT_AABB;
    }

    @Override
    public void setLevel(Level level) {
        super.setLevel(level);
        this.lastCheckY = level.getMinBuildHeight() - 1;
    }

    public static class BeaconBeamSection {
        private int height;

        public BeaconBeamSection() {
            this.height = 1;
        }

        protected void increaseHeight() {
            ++this.height;
        }

        public int getHeight() {
            return this.height;
        }
    }
}
