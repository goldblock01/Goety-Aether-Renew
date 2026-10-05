package github.goldblock.goety_aether.common.magic.spells;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.WandUtil;
import com.aetherteam.aether.client.AetherSoundEvents;
import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.init.ModItems;
import github.goldblock.goety_aether.config.GoetyAetherConfig;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class LevitationSpell extends Spell {
    private static final int BASE_DURATION = 200;
    private static final int DURATION_PER_DURATION = 40;

    @Override
    public SpellType getSpellType() {
        return GoetyAether.DIVINE;
    }

    @Override
    public int defaultSoulCost() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.LEVITATION_FOCUS_COST, 20);
    }

    @Override
    public int defaultCastDuration() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.LEVITATION_FOCUS_CAST_DURATION, 100);
    }

    @Override
    public int defaultSpellCooldown() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.LEVITATION_FOCUS_COOLDOWN, 600);
    }

    @Override
    public SoundEvent CastingSound() {
        return AetherSoundEvents.BLOCK_AETHER_PORTAL_TRIGGER.get();
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    @Override
    public boolean conditionsMet(ServerLevel worldIn, LivingEntity caster) {
        return caster.isShiftKeyDown() || caster.isCrouching() || this.getTarget(caster) != null;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        int durationLevel = 0;
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getPotencyLevel(caster);
            durationLevel = WandUtil.getLevels(ModEnchantments.DURATION.get(), caster);
        }

        int duration = BASE_DURATION + durationLevel * DURATION_PER_DURATION;
        if (this.typeStaff(staff, GoetyAether.DIVINE)) {
            duration += 60;
        }
        LivingEntity target = caster.isShiftKeyDown() || caster.isCrouching() ? caster : this.getTarget(caster);
        if (target == null) {
            target = caster;
        }

        target.addEffect(new MobEffectInstance(MobEffects.LEVITATION, duration, potency, false, false, true));
        this.playSound(worldIn, caster, SoundEvents.SHULKER_SHOOT, 1.0F, 0.6F);
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
