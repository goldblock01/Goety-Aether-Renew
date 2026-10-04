package github.goldblock.goety_aether.common.magic.spells;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.EverChargeSpell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.WandUtil;
import github.goldblock.goety_aether.common.entities.projectile.RainPoisonDart;
import github.goldblock.goety_aether.common.init.ModItems;
import github.goldblock.goety_aether.config.GoetyAetherConfig;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class PoisonDartRainSpell extends EverChargeSpell {

    @Override
    public SpellType getSpellType() {
        return SpellType.WILD;
    }

    @Override
    public int defaultSoulCost() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.POISON_DART_RAIN_COST, 5);
    }

    @Override
    public int defaultCastUp() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.POISON_DART_RAIN_CAST_DURATION, 20);
    }

    @Override
    public int defaultSpellCooldown() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.POISON_DART_RAIN_COOLDOWN, 400);
    }

    @Override
    public int shotsNumber() {
        return 100;
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RANGE.get());
        list.add(ModEnchantments.BURNING.get());
        list.add(ModEnchantments.VELOCITY.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        int potency = spellStat.getPotency();
        float velocity = spellStat.getVelocity();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getPotencyLevel(caster);
            velocity += WandUtil.getLevels(ModEnchantments.VELOCITY.get(), caster) / 2.0F;
        }
        if (caster instanceof Player player && (player.isSprinting() || player.isCrouching()) && hasGatlingCharm(player)) {
            Vec3 lookVec = caster.getViewVector(1.0F);
            Vec3 spawnPos = caster.position().add(0.0, caster.getEyeHeight(), 0.0).add(lookVec.x, 0.0, lookVec.z);
            for (int j = 0; j < potency + 1; j++) {
                RainPoisonDart dart = new RainPoisonDart(worldIn);
                dart.setPos(spawnPos);
                dart.setOwner(caster);
                dart.getPersistentData().putBoolean("PoisonDartRain", true);
                float randomness = 1.0F + worldIn.random.nextFloat() * 0.1F;
                if (worldIn.random.nextFloat() < 0.1F) {
                    randomness = worldIn.random.nextFloat() * 0.5F;
                }
                dart.shoot(lookVec.x, lookVec.y, lookVec.z, velocity + 1.5F * worldIn.random.nextFloat(), randomness);
                if (worldIn.addFreshEntity(dart)) {
                    this.playSound(worldIn, caster, SoundEvents.CROSSBOW_SHOOT, 2.0F, 1.0F / (worldIn.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
                }
            }
            return;
        }

        Vec3 lookVec = caster.getViewVector(1.0F);
        for (int j = 0; j < potency + 1; j++) {
            RainPoisonDart dart = new RainPoisonDart(worldIn);
            double spawnX = (double) ((float) (caster.getRandom().nextInt(2) * 2 - 1) * caster.getRandom().nextFloat() * 3.0F);
            double spawnY = 12.0 + (double) caster.getRandom().nextInt(10);
            double spawnZ = (double) ((float) (caster.getRandom().nextInt(2) * 2 - 1) * caster.getRandom().nextFloat() * 3.0F);
            float randomness = 1.0F + worldIn.random.nextFloat() * 0.1F;
            if (worldIn.random.nextFloat() < 0.1F) {
                randomness = worldIn.random.nextFloat() * 0.5F;
            }
            dart.setPos(caster.getX() + spawnX, caster.getY() + spawnY, caster.getZ() + spawnZ);
            dart.setOwner(caster);
            dart.getPersistentData().putBoolean("PoisonDartRain", true);
            dart.shoot(lookVec.x, lookVec.y, lookVec.z, velocity + 1.5F * worldIn.random.nextFloat(), randomness);
            if (worldIn.addFreshEntity(dart)) {
                if (worldIn.random.nextBoolean()) {
                    worldIn.sendParticles(ParticleTypes.SWEEP_ATTACK, caster.getX() + spawnX, caster.getY() + spawnY, caster.getZ() + spawnZ, 1, 0.0, 0.0, 0.0, 0.0);
                }
                this.playSound(worldIn, caster, SoundEvents.CROSSBOW_SHOOT);
            }
        }
    }

    private static boolean hasGatlingCharm(Player player) {
        try {
            Class<?> gatlingCharmClass = Class.forName("com.k1sak1.goetyawaken.common.items.curios.GatlingCharmItem");
            Method hasItemMethod = gatlingCharmClass.getMethod("hasGatlingCharmItem", Player.class);
            return (Boolean) hasItemMethod.invoke(null, player);
        } catch (Exception e) {
            return false;
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
