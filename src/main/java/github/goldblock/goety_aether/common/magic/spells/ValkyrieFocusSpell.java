package github.goldblock.goety_aether.common.magic.spells;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.SummonSpell;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.ColorUtil;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import com.Polarice3.Goety.utils.WandUtil;
import github.goldblock.goety_aether.common.entities.ally.mobs.ValkyrieServant;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import github.goldblock.goety_aether.common.init.ModItems;
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

public class ValkyrieFocusSpell extends SummonSpell {

    @Override
    public void summonParticles(ServerLevel worldIn, LivingEntity caster, ItemStack staff, LivingEntity summoned) {
        if (this.getSpellType() == GoetyAether.DIVINE) {
            ServerParticleUtil.summonUndeadParticles(worldIn, summoned, new ColorUtil(0xffd700), 0xffd700, 0xfff2a0);
        } else {
            super.summonParticles(worldIn, caster, staff, summoned);
        }
    }

    @Override
    public SpellType getSpellType() {
        return GoetyAether.DIVINE;
    }

    @Override
    public int defaultSoulCost() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.VALKYRIE_FOCUS_COST, 48);
    }

    @Override
    public int defaultCastDuration() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.VALKYRIE_FOCUS_CAST_DURATION, 60);
    }

    @Override
    public int defaultSpellCooldown() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.VALKYRIE_FOCUS_COOLDOWN, 600);
    }

    @Override
    public int SummonDownDuration() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.VALKYRIE_FOCUS_SUMMON_DOWN, 200);
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
        return livingEntity -> livingEntity instanceof ValkyrieServant;
    }

    @Override
    public int summonLimit() {
        return 8;
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
            int count = 1;
            if (this.rightStaff(staff)) {
                count = 3;
            }
            for (int i = 0; i < count; ++i) {
                ValkyrieServant valkyrie = ModEntityTypes.VALKYRIE_SERVANT.get().create(worldIn);
                if (valkyrie != null) {
                    BlockPos blockPos = BlockFinder.SummonRadius(caster.blockPosition(), valkyrie, worldIn);
                    valkyrie.setTrueOwner(caster);
                    valkyrie.moveTo(blockPos, 0.0F, 0.0F);
                    valkyrie.setPersistenceRequired();
                    valkyrie.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                    valkyrie.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null, null);
                    this.buffSummon(caster, valkyrie, potency);
                    this.SummonSap(caster, valkyrie);
                    this.setTarget(caster, valkyrie);
                    if (worldIn.addFreshEntity(valkyrie)) {
                        this.uponSummon(worldIn, caster, staff, valkyrie);
                    }
                    this.summonAdvancement(caster, valkyrie);
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
