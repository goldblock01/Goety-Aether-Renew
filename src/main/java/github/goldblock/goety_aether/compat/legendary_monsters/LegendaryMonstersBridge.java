package github.goldblock.goety_aether.compat.legendary_monsters;

import net.miauczel.legendary_monsters.entity.AnimatedMonster.Projectile.CloudEntity;
import net.miauczel.legendary_monsters.entity.AnimatedMonster.Projectile.Tornado;
import net.miauczel.legendary_monsters.entity.ModEntities;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.registries.ForgeRegistries;

public class LegendaryMonstersBridge {
    public static final String MOD_ID = "legendary_monsters";
    private static final double FALL_HEIGHT = 8.0D;

    public static SoundEvent sound(String path) {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(MOD_ID, path));
    }

    public static ParticleType<?> particle(String path) {
        return ForgeRegistries.PARTICLE_TYPES.getValue(new ResourceLocation(MOD_ID, path));
    }

    public static void spawnCloud(Mob owner, double x, double y, double z) {
        EntityType<CloudEntity> entityType = ModEntities.C.get();
        CloudEntity newEntity = entityType.create(owner.level());
        if (newEntity != null) {
            newEntity.setPos(x, y, z);
            newEntity.setOwner(owner);
            owner.level().addFreshEntity(newEntity);
        }
    }

    public static void spawnFallingCloud(LivingEntity owner, double x, double z, int damage, float range) {
        Level level = owner.level();
        int groundY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mth.floor(x), Mth.floor(z));
        spawnFallingCloudAt(owner, x, groundY + FALL_HEIGHT, z, damage, range);
    }

    public static void spawnFallingCloudAt(LivingEntity owner, double x, double y, double z, int damage, float range) {
        Level level = owner.level();
        EntityType<CloudEntity> entityType = ModEntities.C.get();
        CloudEntity cloud = entityType.create(level);
        if (cloud != null) {
            cloud.setPos(x, y, z);
            cloud.setOwner(owner);
            cloud.setDamage(damage);
            cloud.setAttackRange(range);
            cloud.setParticleOptimalization(true);
            level.addFreshEntity(cloud);
        }
    }

    public static void spawnTornado(Mob caster, float angle, float damage) {
        float rad = (float) Math.toRadians(angle);
        double dx = -Math.sin(rad);
        double dz = Math.cos(rad);
        Tornado tornado = new Tornado(caster, dx, 0.0D, dz, caster.level(), damage, angle, 120);
        double theta = caster.yBodyRot * (Math.PI / 180) + Math.PI / 2;
        double vecX = Math.cos(theta);
        double vecZ = Math.sin(theta);
        tornado.setPos(caster.getX() + vecX, caster.getY(0.15D), caster.getZ() + vecZ);
        caster.level().addFreshEntity(tornado);
    }
}
