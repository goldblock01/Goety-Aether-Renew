package github.goldblock.goety_aether.common.entities.ally.mobs;

import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.aetherteam.aether.client.AetherSoundEvents;
import com.aetherteam.aether.client.particle.AetherParticleTypes;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class EvilWhirlwindServant extends AbstractWhirlwindServant {
    public EvilWhirlwindServant(EntityType<? extends EvilWhirlwindServant> type, Level level) {
        super(type, level);
        this.setEvil(true);
    }

    @Override
    protected void spawnDrops() {
        if (this.level() instanceof ServerLevel serverLevel) {
            if (this.getRandom().nextInt(4) == 0) {
                LootParams parameters = new LootParams.Builder(serverLevel)
                        .withParameter(LootContextParams.ORIGIN, this.position())
                        .withParameter(LootContextParams.THIS_ENTITY, this)
                        .create(LootContextParamSets.SELECTOR);
                LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(this.getLootLocation());
                for (ItemStack itemstack : lootTable.getRandomItems(parameters)) {
                    if (itemstack.isEmpty()) {
                        this.summonCreeperServant();
                    } else {
                        serverLevel.playSound(null, this.blockPosition(), AetherSoundEvents.ENTITY_WHIRLWIND_DROP.get(), SoundSource.HOSTILE, 0.5F, 1.0F);
                        this.spawnAtLocation(itemstack, 1.0F);
                    }
                }
            }
        }
    }

    private void summonCreeperServant() {
        if (this.level() instanceof ServerLevel serverLevel) {
            CreeperServant servant = new CreeperServant(ModEntityTypes.CREEPER_SERVANT.get(), serverLevel);
            BlockPos blockPos = BlockFinder.SummonRadius(this.blockPosition(), servant, serverLevel);
            servant.setTrueOwner(this.getTrueOwner() != null ? this.getTrueOwner() : this);
            servant.moveTo(blockPos, 0.0F, 0.0F);
            MobUtil.moveDownToGround(servant);
            servant.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(this.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            serverLevel.addFreshEntity(servant);
        }
    }

    @Override
    public void spawnParticles() {
        for (int i = 0; i < 3; i++) {
            double d2 = this.getX() + this.getRandom().nextDouble() * 0.25D;
            double d5 = this.getY() + this.getBbHeight() + 0.125D;
            double d8 = this.getZ() + this.getRandom().nextDouble() * 0.25D;
            float f1 = this.getRandom().nextFloat() * 360.0F;
            this.level().addParticle(AetherParticleTypes.EVIL_WHIRLWIND.get(), d2, d5 - 0.25D, d8,
                    -Math.sin(0.0175F * f1) * 0.75D, 0.125D, Math.cos(0.0175F * f1) * 0.75D);
        }
    }

    @Override
    public ResourceLocation getLootLocation() {
        return new ResourceLocation("goety_aether", "selectors/evil_whirlwind_servant_junk");
    }

    @Override
    public int getDefaultColor() {
        return 0;
    }
}
