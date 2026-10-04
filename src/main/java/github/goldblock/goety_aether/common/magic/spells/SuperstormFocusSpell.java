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
import github.goldblock.goety_aether.common.entities.ally.mobs.EOTSServantSegment;
import github.goldblock.goety_aether.common.init.ModItems;
import github.goldblock.goety_aether.compat.deep_aether.DeepAetherCompatManager;
import github.goldblock.goety_aether.config.GoetyAetherConfig;
import github.goldblock.goety_aether.GoetyAether;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SuperstormFocusSpell extends SummonSpell {
    private static final int BASE_SEGMENTS = 7;

    @Override
    public SpellType getSpellType() {
        return GoetyAether.DIVINE;
    }

    @Override
    public int defaultSoulCost() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.SUPERSTORM_FOCUS_COST, 500);
    }

    @Override
    public int defaultCastDuration() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.SUPERSTORM_FOCUS_CAST_DURATION, 400);
    }

    @Override
    public int defaultSpellCooldown() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.SUPERSTORM_FOCUS_COOLDOWN, 2400);
    }

    @Override
    public int SummonDownDuration() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.SUPERSTORM_FOCUS_SUMMON_DOWN, 400);
    }

    @Override
    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SUMMON.get();
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
        return livingEntity -> livingEntity instanceof EOTSServantSegment;
    }

    @Override
    public int summonLimit() {
        return 40;
    }

    private int getSegmentCount(LivingEntity caster, ItemStack staff) {
        int count = BASE_SEGMENTS;
        if (staff.getItem() == com.Polarice3.Goety.common.items.ModItems.WIND_STAFF.get()) {
            count += 2;
        }
        if (staff.getItem() == ModItems.DIVINE_STAFF.get()) {
            count += 4;
        }
        if (CuriosFinder.hasCurio(caster, com.Polarice3.Goety.common.items.ModItems.WIND_CROWN.get())) {
            count += 2;
        }
        if (CuriosFinder.hasCurio(caster, com.Polarice3.Goety.common.items.ModItems.WIND_ROBE.get())) {
            count += 2;
        }
        if (CuriosFinder.hasCurio(caster, ModItems.DIVINE_CROWN.get())) {
            count += 4;
        }
        if (CuriosFinder.hasCurio(caster, ModItems.DIVINE_ROBE.get())) {
            count += 5;
        }
        return count;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        this.commonResult(worldIn, caster);
        int potency = spellStat.getPotency();
        int duration = spellStat.getDuration();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getPotencyLevel(caster);
            duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster) + 1;
        }
        if (!isShifting(caster)) {
            int count = this.getSegmentCount(caster, staff);
            EOTSServantSegment head = DeepAetherCompatManager.EOTSSERVANT_SEGMENT.get().create(worldIn);
            if (head != null) {
                BlockPos blockPos = BlockFinder.SummonFlyingRadius(caster.blockPosition(), head, worldIn, 15);
                int lifespan = MobUtil.getSummonLifespan(worldIn) * duration;
                head.setTrueOwner(caster);
                head.moveTo(blockPos, 0.0F, 0.0F);
                head.setPersistenceRequired();
                head.setLimitedLife(lifespan);
                head.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null, null);
                this.buffSummon(caster, head, potency);
                this.SummonSap(caster, head);
                this.setTarget(caster, head);
                if (worldIn.addFreshEntity(head)) {
                    this.uponSummon(worldIn, caster, staff, head);
                    EOTSServantSegment prev = head;
                    for (int i = 1; i < count; ++i) {
                        EOTSServantSegment segment = new EOTSServantSegment(worldIn, prev);
                        segment.setPersistenceRequired();
                        segment.setLimitedLife(lifespan);
                        this.buffSummon(caster, segment, potency);
                        this.SummonSap(caster, segment);
                        this.setTarget(caster, segment);
                        prev = segment;
                    }
                }
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
