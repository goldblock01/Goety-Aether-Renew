package github.goldblock.goety_aether.common.magic.spells;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.utils.CuriosFinder;
import com.Polarice3.Goety.utils.WandUtil;
import github.goldblock.goety_aether.common.events.SpellScheduler;
import github.goldblock.goety_aether.common.init.ModItems;
import github.goldblock.goety_aether.compat.legendary_monsters.LegendaryMonstersBridge;
import github.goldblock.goety_aether.config.GoetyAetherConfig;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class CloudFallingSpell extends Spell {
    private static final int[] RING_COUNTS = {6, 9, 12, 15, 18, 21, 24};
    private static final double[] RING_RADII = {2.5D, 3.5D, 4.5D, 5.5D, 6.5D, 8.5D, 7.5D};
    private static final int[] RING_DELAYS = {0, 1, 4, 7, 10, 13, 16};
    private static final int COLUMN_CLOUDS = 11;
    private static final double COLUMN_START = 4.0D;
    private static final double COLUMN_STEP = 2.0D;
    private static final int WIND_COLUMNS = 3;
    private static final double COLUMN_LATERAL = 4.0D;
    private static final float CLOUD_RANGE = 2.0F;
    private static final double FALL_HEIGHT = 8.0D;

    @Override
    public SpellType getSpellType() {
        return SpellType.WIND;
    }

    @Override
    public int defaultSoulCost() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.CLOUD_FALLING_FOCUS_COST, 40);
    }

    @Override
    public int defaultCastDuration() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.CLOUD_FALLING_FOCUS_CAST_DURATION, 100);
    }

    @Override
    public int defaultSpellCooldown() {
        return GoetyAetherConfig.spellValue(GoetyAetherConfig.CLOUD_FALLING_FOCUS_COOLDOWN, 200);
    }

    @Override
    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        return list;
    }

    @Override
    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        boolean divine = staff.getItem() == ModItems.DIVINE_STAFF.get();
        boolean wind = this.rightStaff(staff) && !divine;

        int bonus = spellStat.getPotency();
        if (WandUtil.enchantedFocus(caster)) {
            bonus += WandUtil.getPotencyLevel(caster);
        }
        final int damageBonus = bonus;

        if (divine) {
            for (int ring = 0; ring < RING_COUNTS.length; ++ring) {
                final int index = ring;
                SpellScheduler.delay(RING_DELAYS[ring], () -> this.dropRing(worldIn, caster, index, damageBonus));
            }
        } else {
            final int columns = wind ? WIND_COLUMNS : 1;
            for (int step = 0; step < COLUMN_CLOUDS; ++step) {
                final double progress = COLUMN_START + step * COLUMN_STEP;
                SpellScheduler.delay(step, () -> this.dropColumn(worldIn, caster, columns, progress, damageBonus));
            }
        }

        this.playSound(worldIn, caster, SoundEvents.ENCHANTMENT_TABLE_USE, 1.5F, 0.769F);
    }

    private void dropRing(ServerLevel level, LivingEntity caster, int index, int bonus) {
        if (!caster.isAlive() || caster.level() != level) {
            return;
        }
        int count = RING_COUNTS[index];
        double radius = RING_RADII[index];
        double phase = Math.PI * 2.0D / (5.0D * (index + 1));
        int damage = (index == 0 ? 7 : 8) + bonus;
        double y = caster.getY() + FALL_HEIGHT;
        for (int i = 0; i < count; ++i) {
            double angle = Math.PI * 2.0D * i / count + phase;
            double x = caster.getX() + Math.cos(angle) * radius;
            double z = caster.getZ() + Math.sin(angle) * radius;
            LegendaryMonstersBridge.spawnFallingCloudAt(caster, x, y, z, damage, CLOUD_RANGE);
        }
    }

    private void dropColumn(ServerLevel level, LivingEntity caster, int columns, double progress, int bonus) {
        if (!caster.isAlive() || caster.level() != level) {
            return;
        }
        Vec3 forward = this.aimDirection(caster);
        double sideX = -forward.z;
        double sideZ = forward.x;
        for (int column = 0; column < columns; ++column) {
            double lateral = (column - (columns - 1) / 2.0D) * COLUMN_LATERAL;
            double x = caster.getX() + forward.x * progress + sideX * lateral;
            double z = caster.getZ() + forward.z * progress + sideZ * lateral;
            LegendaryMonstersBridge.spawnFallingCloud(caster, x, z, 8 + bonus, CLOUD_RANGE);
        }
    }

    private Vec3 aimDirection(LivingEntity caster) {
        double dx;
        double dz;
        LivingEntity target = caster instanceof Mob mob ? mob.getTarget() : null;
        if (target != null && !target.isDeadOrDying()) {
            dx = target.getX() - caster.getX();
            dz = target.getZ() - caster.getZ();
        } else {
            Vec3 look = caster.getViewVector(1.0F);
            dx = look.x;
            dz = look.z;
        }
        double length = Math.sqrt(dx * dx + dz * dz);
        if (length < 1.0E-4D) {
            return new Vec3(0.0D, 0.0D, 1.0D);
        }
        return new Vec3(dx / length, 0.0D, dz / length);
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
