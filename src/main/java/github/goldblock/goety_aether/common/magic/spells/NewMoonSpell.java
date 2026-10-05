package github.goldblock.goety_aether.common.magic.spells;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.WandUtil;
import com.aetherteam.aether.client.AetherSoundEvents;
import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.entities.util.NewMoonAttractor;
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
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class NewMoonSpell extends Spell {
    private static final double MAX_CAST_DISTANCE = 20.0D;
    private static final int BASE_DURATION = 60;
    private static final int DURATION_PER_DURATION = 40;
    private static final float BASE_RADIUS = 8.0F;
    private static final float RADIUS_PER_RANGE = 2.0F;

    @Override
    public SpellType getSpellType() {
        return GoetyAether.DIVINE;
    }

    @Override
    public int defaultSoulCost() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.NEW_MOON_FOCUS_COST, 20);
    }

    @Override
    public int defaultCastDuration() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.NEW_MOON_FOCUS_CAST_DURATION, 140);
    }

    @Override
    public int defaultSpellCooldown() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.NEW_MOON_FOCUS_COOLDOWN, 300);
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
        list.add(ModEnchantments.RANGE.get());
        return list;
    }

    @Override
    public boolean conditionsMet(ServerLevel worldIn, LivingEntity caster) {
        return caster.pick(MAX_CAST_DISTANCE, 1.0F, false).getType() != HitResult.Type.MISS;
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

        HitResult hitResult = caster.pick(MAX_CAST_DISTANCE, 1.0F, false);
        Vec3 pos = hitResult.getLocation();
        int duration = BASE_DURATION + durationLevel * DURATION_PER_DURATION;
        float radius = BASE_RADIUS + range * RADIUS_PER_RANGE;
        double strength = 1.0D + potency * 0.15D;

        NewMoonAttractor attractor = new NewMoonAttractor(worldIn, pos.x, pos.y, pos.z);
        attractor.setOwner(caster);
        attractor.setDuration(duration);
        attractor.setRadius(radius);
        attractor.setStrength(strength);
        worldIn.addFreshEntity(attractor);

        this.playSound(worldIn, caster, SoundEvents.BEACON_ACTIVATE, 1.0F, 0.6F);
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
