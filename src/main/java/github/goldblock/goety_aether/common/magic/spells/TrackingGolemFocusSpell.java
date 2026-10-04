package github.goldblock.goety_aether.common.magic.spells;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.SummonSpell;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import github.goldblock.goety_aether.common.entities.ally.mobs.TrackingGolem;
import github.goldblock.goety_aether.common.init.ModItems;
import github.goldblock.goety_aether.compat.genesis.GenesisCompatManager;
import github.goldblock.goety_aether.compat.mod.AetherGenesisCompat;
import github.goldblock.goety_aether.config.GoetyAetherConfig;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;

public class TrackingGolemFocusSpell extends SummonSpell {

    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setDuration(1);
    }

    @Override
    public int defaultSoulCost() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.TRACKING_GOLEM_FOCUS_COST, 24);
    }

    @Override
    public int defaultCastDuration() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.TRACKING_GOLEM_FOCUS_CAST_DURATION, 20);
    }

    @Override
    public int SummonDownDuration() {
        return 100;
    }

    @Override
    public int defaultSpellCooldown() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.TRACKING_GOLEM_FOCUS_COOLDOWN, 300);
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
        return livingEntity -> livingEntity instanceof TrackingGolem;
    }

    @Override
    public int summonLimit() {
        return 5;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        if (!AetherGenesisCompat.isGenesisLoaded()) {
            return;
        }
        this.commonResult(worldIn, caster);
        int potency = spellStat.getPotency();
        int duration = spellStat.getDuration();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getPotencyLevel(caster);
            duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster) + 1;
        }
        if (!isShifting(caster)) {
            int i = 1;
            if (rightStaff(staff)) {
                i = 3;
            }
            if (GeoPower(caster) && CuriosFinder.hasGeoRobe(caster)) {
                i = 4 + (caster.getRandom().nextBoolean() ? 1 : 0);
            }
            for (int i1 = 0; i1 < i; ++i1) {
                TrackingGolem summonedentity = new TrackingGolem(GenesisCompatManager.TRACKING_GOLEM.get(), worldIn);
                summonedentity.setTrueOwner(caster);
                summonedentity.moveTo(BlockFinder.SummonRadius(caster.blockPosition(), summonedentity, worldIn), 0.0F, 0.0F);
                MobUtil.moveDownToGround(summonedentity);
                summonedentity.setPersistenceRequired();
                summonedentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                summonedentity.finalizeSpawn(worldIn, caster.level().getCurrentDifficultyAt(caster.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                this.buffSummon(caster, summonedentity, potency);
                this.SummonSap(caster, summonedentity);
                this.setTarget(caster, summonedentity);
                if (worldIn.addFreshEntity(summonedentity)) {
                    this.uponSummon(worldIn, caster, staff, summonedentity);
                }
                this.summonAdvancement(caster, summonedentity);
            }
            this.SummonDown(caster);
            this.playSound(worldIn, caster, ModSounds.SUMMON_SPELL.get());
        }
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
