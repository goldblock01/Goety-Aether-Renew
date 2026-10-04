package github.goldblock.goety_aether.common.items.magic;

import com.Polarice3.Goety.api.magic.ISpell;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.items.magic.DarkStaff;
import com.aetherteam.aether.item.AetherItems;
import github.goldblock.goety_aether.GoetyAether;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;

import java.util.List;

public class DivineStaff extends DarkStaff {

    public DivineStaff(double damage) {
        super(damage, SpellType.NONE);
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return AetherItems.AETHER_LOOT;
    }

    @Override
    public SpellType getSpellType() {
        return GoetyAether.DIVINE;
    }

    @Override
    public List<SpellType> getSpellTypes() {
        return List.of(GoetyAether.DIVINE);
    }

    @Override
    public float getWandVisualHeight(Level level, LivingEntity entity, ItemStack stack) {
        return 0.8F;
    }

    @Override
    public void useParticles(Level worldIn, LivingEntity livingEntity, ItemStack stack, ISpell iSpell) {        if (!worldIn.isClientSide()) {
            return;
        }
        RandomSource random = livingEntity.getRandom();
        Vector3f holyGold = new Vector3f(1.0F, 0.85F, 0.2F);
        Vector3f holyGoldBright = new Vector3f(1.0F, 0.96F, 0.6F);
        Vector3f holyWhite = new Vector3f(0.95F, 0.98F, 1.0F);
        double tipX = livingEntity.getX() + livingEntity.getLookAngle().x;
        double tipZ = livingEntity.getZ() + livingEntity.getLookAngle().z;
        double tipY = livingEntity.getY() + (double) livingEntity.getEyeHeight() + 0.1D;
        double time = (double) worldIn.getGameTime();
        double localT = time * 0.02D;
        for (int i = 0; i < 3; ++i) {
            double baseAngle = localT * 2.6D + Math.PI * 2.0D * (double) i / 3.0D;
            double radius = 0.6D + Math.sin(time * 0.1D + (double) i * 2.0D) * 0.25D;
            double px = livingEntity.getX() + Math.cos(baseAngle) * radius;
            double pz = livingEntity.getZ() + Math.sin(baseAngle) * radius;
            double py = livingEntity.getY() + 0.4D + (0.4D - Math.cos(baseAngle + localT)) * 0.5D;
            double dx = tipX - px;
            double dy = tipY - py;
            double dz = tipZ - pz;
            double dist = Math.max(0.35D, Math.sqrt(dx * dx + dy * dy + dz * dz));
            double pull = 0.055D;
            Vector3f color = (i % 2 == 0) ? holyGold : (random.nextInt(3) == 0 ? holyWhite : holyGoldBright);
            float size = 0.5F + random.nextFloat() * 0.5F;
            worldIn.addParticle(new DustParticleOptions(color, size), px, py, pz, dx * pull / dist, dy * pull / dist + 0.35D, dz * pull / dist);
        }
        for (int i = 0; i < 2; ++i) {
            double angle = random.nextDouble() * Math.PI * 2.0D;
            double px = tipX + Math.cos(angle) * 0.12D;
            double pz = tipZ + Math.sin(angle) * 0.12D;
            double py = tipY + random.nextDouble() * 0.35D;
            boolean sparkle = random.nextInt(4) == 0;
            worldIn.addParticle(sparkle ? ParticleTypes.END_ROD : new DustParticleOptions(holyWhite, 0.28F + random.nextFloat() * 0.18F), px, py, pz, 0.0D, 0.03D, 0.0D);
        }
    }

    public int getStaffLevel() {
        return 2;
    }
}
