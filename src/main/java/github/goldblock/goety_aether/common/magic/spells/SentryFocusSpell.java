package github.goldblock.goety_aether.common.magic.spells;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.SummonSpell;
import com.Polarice3.Goety.init.ModAttributes;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.SEHelper;
import com.Polarice3.Goety.utils.WandUtil;
import github.goldblock.goety_aether.common.entities.ally.mobs.SentryServant;
import github.goldblock.goety_aether.common.entities.projectile.SentryBomb;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import github.goldblock.goety_aether.common.init.ModItems;
import github.goldblock.goety_aether.config.GoetyAetherConfig;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;

public class SentryFocusSpell extends SummonSpell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setDuration(1).setVelocity(1.6F);
    }

    @Override
    public int defaultSoulCost() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.SENTRY_FOCUS_COST, 24);
    }

    @Override
    public int defaultCastDuration() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.SENTRY_FOCUS_CAST_DURATION, 20);
    }

    @Override
    public int SummonDownDuration() {
        return 60;
    }

    @Override
    public int defaultSpellCooldown() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.SENTRY_FOCUS_COOLDOWN, 240);
    }

    @Override
    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SUMMON.get();
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.GEOMANCY;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    @Override
    public Predicate<LivingEntity> summonPredicate() {
        return livingEntity -> livingEntity instanceof SentryServant;
    }

    @Override
    public int summonLimit() {
        return 32;
    }

    @Override
    public int castDuration(LivingEntity caster, ItemStack staff) {
        if (isShifting(caster)) {
            return 26 + 6 * this.summonCount(caster, staff);
        }
        double duration = this.defaultCastDuration();
        if (this.ReduceCastTime(caster)) {
            duration /= 2;
        }
        duration *= ModAttributes.getCastingSpeed(caster);
        return (int) duration;
    }

    @Override
    public void useSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, int castTime, SpellStat spellStat) {
        if (isShifting(caster)) {
            int i = this.summonCount(caster, staff);
            for (int k = 0; k < i; ++k) {
                if (castTime == 26 + 6 * k) {
                    SentryBomb bomb = new SentryBomb(ModEntityTypes.SENTRY_BOMB.get(), caster, worldIn);
                    bomb.shootFromRotation(caster, caster.getXRot(), caster.getYRot(), 0.0F, spellStat.getVelocity(), 0.0F);
                    bomb.setOwner(caster);
                    worldIn.addFreshEntity(bomb);
                    this.playSound(worldIn, caster, SoundEvents.BASALT_PLACE, 1.0F, this.projPitch(worldIn.getRandom()));
                }
            }
        }
    }

    @Override
    public void stopSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, ItemStack focus, int castTime, SpellStat spellStat) {
        if (isShifting(caster) && castTime >= 26 && caster instanceof Player player && !focus.isEmpty()) {
            if (SEHelper.getSoulsAmount(player, this.soulCost(caster, staff))) {
                SEHelper.decreaseSouls(player, this.soulCost(caster, staff));
                SEHelper.addCooldown(player, focus.getItem(), this.spellCooldown(caster));
                SEHelper.sendSEUpdatePacket(player);
            }
        }
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        if (!isShifting(caster)) {
            this.commonResult(worldIn, caster);
            int potency = spellStat.getPotency();
            int durationLevels = 0;
            if (WandUtil.enchantedFocus(caster)) {
                potency += WandUtil.getPotencyLevel(caster);
                durationLevels += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster);
            }
            LivingEntity aimTarget = this.getTarget(caster);
            BlockPos summonPos = aimTarget != null ? aimTarget.blockPosition() : caster.blockPosition();
            int i = this.summonCount(caster, staff);
            for (int i1 = 0; i1 < i; ++i1) {
                SentryServant summonedentity = new SentryServant(ModEntityTypes.SENTRY_SERVANT.get(), worldIn);
                summonedentity.setTrueOwner(caster);
                summonedentity.moveTo(BlockFinder.SummonRadius(summonPos, summonedentity, worldIn), 0.0F, 0.0F);
                MobUtil.moveDownToGround(summonedentity);
                summonedentity.setPersistenceRequired();
                int baseLife = 160 + summonedentity.getRandom().nextInt(141);
                summonedentity.setLimitedLife(baseLife + (160 + summonedentity.getRandom().nextInt(141)) * durationLevels);
                summonedentity.finalizeSpawn(worldIn, caster.level().getCurrentDifficultyAt(caster.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                this.buffSummon(caster, summonedentity, potency);
                this.SummonSap(caster, summonedentity);
                this.setTarget(caster, summonedentity);
                if (aimTarget != null && !MobUtil.areAllies(caster, aimTarget)) {
                    summonedentity.setTarget(aimTarget);
                }
                if (worldIn.addFreshEntity(summonedentity)) {
                    this.uponSummon(worldIn, caster, staff, summonedentity);
                }
                this.summonAdvancement(caster, summonedentity);
            }
            this.SummonDown(caster);
            this.playSound(worldIn, caster, ModSounds.SUMMON_SPELL.get());
        }
    }

    private int summonCount(LivingEntity caster, ItemStack staff) {
        int i = 2;
        if (rightStaff(staff)) {
            i = 4;
        }
        return i;
    }

    @Override
    public boolean rightStaff(ItemStack staff) {
        return super.rightStaff(staff) || staff.getItem() == ModItems.DIVINE_STAFF.get();
    }

    @Override
    public boolean ReduceCastTime(LivingEntity caster) {
        return super.ReduceCastTime(caster) || CuriosFinder.hasCurio(caster, ModItems.DIVINE_CROWN.get());
    }

    @Override
    public boolean SoulDiscount(LivingEntity caster) {
        return super.SoulDiscount(caster) || CuriosFinder.hasCurio(caster, ModItems.DIVINE_ROBE.get());
    }

    @Override
    public boolean FrostSoulDiscount(LivingEntity caster) {
        return super.FrostSoulDiscount(caster) || CuriosFinder.hasCurio(caster, ModItems.DIVINE_ROBE.get());
    }

    @Override
    public boolean WindSoulDiscount(LivingEntity caster) {
        return super.WindSoulDiscount(caster) || CuriosFinder.hasCurio(caster, ModItems.DIVINE_ROBE.get());
    }

    @Override
    public boolean GeoSoulDiscount(LivingEntity caster) {
        return super.GeoSoulDiscount(caster) || CuriosFinder.hasCurio(caster, ModItems.DIVINE_ROBE.get());
    }

    @Override
    public boolean StormSoulDiscount(LivingEntity caster) {
        return super.StormSoulDiscount(caster) || CuriosFinder.hasCurio(caster, ModItems.DIVINE_ROBE.get());
    }

    @Override
    public boolean WildSoulDiscount(LivingEntity caster) {
        return super.WildSoulDiscount(caster) || CuriosFinder.hasCurio(caster, ModItems.DIVINE_ROBE.get());
    }

    @Override
    public boolean NetherSoulDiscount(LivingEntity caster) {
        return super.NetherSoulDiscount(caster) || CuriosFinder.hasCurio(caster, ModItems.DIVINE_ROBE.get());
    }

    @Override
    public boolean AbyssSoulDiscount(LivingEntity caster) {
        return super.AbyssSoulDiscount(caster) || CuriosFinder.hasCurio(caster, ModItems.DIVINE_ROBE.get());
    }

    @Override
    public boolean NecroSoulDiscount(LivingEntity caster) {
        return super.NecroSoulDiscount(caster) || CuriosFinder.hasCurio(caster, ModItems.DIVINE_ROBE.get());
    }

    @Override
    public boolean VoidSoulDiscount(LivingEntity caster) {
        return super.VoidSoulDiscount(caster) || CuriosFinder.hasCurio(caster, ModItems.DIVINE_ROBE.get());
    }
}
