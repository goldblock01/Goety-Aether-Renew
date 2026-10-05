package github.goldblock.goety_aether.common.magic.spells;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.WandUtil;
import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.entities.util.DivineFavorCloud;
import github.goldblock.goety_aether.common.init.ModItems;
import github.goldblock.goety_aether.config.GoetyAetherConfig;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class DivineFavorSpell extends Spell {
    private static final int BASE_RADIUS = 5;
    private static final int BASE_DURATION = 160;
    private static final int DURATION_PER_DURATION = 40;

    @Override
    public SpellType getSpellType() {
        return GoetyAether.DIVINE;
    }

    @Override
    public int defaultSoulCost() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.DIVINE_FAVOR_FOCUS_COST, 12);
    }

    @Override
    public int defaultCastDuration() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.DIVINE_FAVOR_FOCUS_CAST_DURATION, 60);
    }

    @Override
    public int defaultSpellCooldown() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.DIVINE_FAVOR_FOCUS_COOLDOWN, 300);
    }

    @Override
    public SoundEvent CastingSound() {
        return SoundEvents.AMETHYST_BLOCK_CHIME;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        list.add(ModEnchantments.RANGE.get());
        return list;
    }

    @Override
    public boolean conditionsMet(ServerLevel worldIn, LivingEntity caster) {
        return true;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        int range = spellStat.getRange();
        int durationLevel = 0;
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getPotencyLevel(caster);
            range += WandUtil.getLevels(ModEnchantments.RANGE.get(), caster);
            durationLevel = WandUtil.getLevels(ModEnchantments.DURATION.get(), caster);
        }

        int radius = BASE_RADIUS + range;
        int duration = BASE_DURATION + durationLevel * DURATION_PER_DURATION;

        DivineFavorCloud cloud = new DivineFavorCloud(worldIn, caster.getX(), caster.getY(), caster.getZ());
        cloud.setOwner(caster);
        cloud.setRadius(radius);
        cloud.setDuration(duration);
        cloud.setPotency(potency);
        worldIn.addFreshEntity(cloud);

        this.playSound(worldIn, caster, SoundEvents.PLAYER_LEVELUP, 0.7F, 1.2F);
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
