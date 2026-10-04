package github.goldblock.goety_aether.common.magic.spells;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.WandUtil;
import github.goldblock.goety_aether.common.entities.projectile.IceCrystal;
import github.goldblock.goety_aether.common.init.ModItems;
import github.goldblock.goety_aether.config.GoetyAetherConfig;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;

public class IceCrystalFocusSpell extends Spell {

    @Override
    public SpellType getSpellType() {
        return SpellType.FROST;
    }

    @Override
    public int defaultSoulCost() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.ICE_CRYSTAL_FOCUS_COST, 12);
    }

    @Override
    public int defaultCastDuration() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.ICE_CRYSTAL_FOCUS_CAST_DURATION, 0);
    }

    @Override
    public int defaultSpellCooldown() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.ICE_CRYSTAL_FOCUS_COOLDOWN, 80);
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        float bonus = this.rightStaff(staff) ? 2.0F : 0.0F;
        if (WandUtil.enchantedFocus(caster)) {
            bonus += WandUtil.getPotencyLevel(caster);
        }
        IceCrystal crystal = new IceCrystal(worldIn, caster, bonus, this.rightStaff(staff), caster.getViewVector(1.0F));
        worldIn.addFreshEntity(crystal);
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
