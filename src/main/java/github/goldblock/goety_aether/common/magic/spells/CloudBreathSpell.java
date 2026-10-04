package github.goldblock.goety_aether.common.magic.spells;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.BreathingSpell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import com.Polarice3.Goety.utils.WandUtil;
import github.goldblock.goety_aether.common.entities.util.HarmCloud;
import github.goldblock.goety_aether.common.init.ModItems;
import github.goldblock.goety_aether.compat.lost_aether.LostAetherBridge;
import github.goldblock.goety_aether.config.GoetyAetherConfig;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class CloudBreathSpell extends BreathingSpell {
    @Override
    public SpellStat defaultStats() {
        return super.defaultStats().setRange(8);
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.WIND;
    }

    @Override
    public int defaultSoulCost() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.CLOUD_BREATH_FOCUS_COST, 3);
    }

    @Override
    public int shotsNumber() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.CLOUD_BREATH_FOCUS_SHOTS, 100);
    }

    @Override
    public int defaultSpellCooldown() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.CLOUD_BREATH_FOCUS_COOLDOWN, 100);
    }

    @Override
    public SoundEvent CastingSound() {
        return null;
    }

    @Override
    public SoundEvent loopSound(LivingEntity caster) {
        return ModSounds.HEAVY_WOOSH.get();
    }

    @Override
    public void useParticle(Level worldIn, LivingEntity caster, ItemStack stack) {
        if (worldIn instanceof ServerLevel serverLevel) {
            ServerParticleUtil.addParticlesAroundMiddleSelf(serverLevel, ParticleTypes.CLOUD, caster);
        }
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RANGE.get());
        return list;
    }

    @Override
    public boolean conditionsMet(ServerLevel worldIn, LivingEntity caster, SpellStat spellStat) {
        if (caster instanceof Mob mob) {
            if (mob.getTarget() != null) {
                int range = spellStat.getRange();
                if (WandUtil.enchantedFocus(caster)) {
                    range += WandUtil.getRangeLevel(caster);
                }

                return mob.hasLineOfSight(mob.getTarget()) && mob.distanceTo(mob.getTarget()) <= (double) (range + 4);
            }
        }

        return super.conditionsMet(worldIn, caster, spellStat);
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        float potency = spellStat.getPotency();
        int range = spellStat.getRange();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getPotencyLevel(caster);
            range += WandUtil.getRangeLevel(caster);
        }

        float damage = 3.0F + potency;
        boolean boosted = this.rightStaff(staff);
        if (boosted) {
            damage += 1.0F;
        }

        for (Entity target : this.getBreathTarget(caster, range)) {
            LivingEntity living = MobUtil.getLivingTarget(target);
            if (living != null) {
                living.hurt(living.damageSources().indirectMagic(caster, caster), damage);
            }
        }
    }

    @Override
    public void showWandBreath(LivingEntity entityLiving, ItemStack staff, SpellStat spellStat) {
        int range = spellStat.getRange();
        if (WandUtil.enchantedFocus(entityLiving)) {
            range += WandUtil.getRangeLevel(entityLiving);
        }

        if (this.rightStaff(staff)) {
            this.dragonBreathAttack(ParticleTypes.CLOUD, entityLiving, (double) range / 10.0D * 0.5D);
        } else {
            this.dragonBreathAttack(ParticleTypes.CLOUD, entityLiving, 10, (double) range / 10.0D * 0.5D, 1.0D);
        }
    }

    @Override
    public void stopSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, ItemStack focus, int castTime, SpellStat spellStat) {
        if (castTime >= 5 && this.rightStaff(staff)) {
            float potency = spellStat.getPotency();
            if (WandUtil.enchantedFocus(caster)) {
                potency += WandUtil.getPotencyLevel(caster);
            }

            float damage = 3.0F + potency + 1.0F;

            Vec3 look = caster.getLookAngle();
            double x = caster.getX() + look.x * 2.0D;
            double z = caster.getZ() + look.z * 2.0D;
            double y = caster.getY();
            HarmCloud cloud = new HarmCloud(worldIn, x, y, z);
            cloud.setOwner(caster);
            cloud.setDamage(damage);
            cloud.setRadius(3.0F);
            cloud.setRadiusOnUse(-0.5F);
            cloud.setWaitTime(10);
            worldIn.addFreshEntity(cloud);
            SoundEvent puff = LostAetherBridge.sound("entity.cloud_shot.puff");
            this.playSound(worldIn, caster, puff != null ? puff : ModSounds.HEAVY_WOOSH.get(), 1.5F, 1.0F);
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
