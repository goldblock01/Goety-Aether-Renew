package github.goldblock.goety_aether.common.entities.ally.mobs;

import github.goldblock.goety_aether.common.entities.hostile.illagers.Mountaineer;
import github.goldblock.goety_aether.common.entities.hostile.illagers.WindCaller;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

public final class CreeperReplacementBridge {
    public static final String GOETY_AWAKEN = "goetyawaken";
    public static final String GOETY_HOSTILITY = "goetyhostility";

    private CreeperReplacementBridge() {
    }

    public static boolean isAwakenLoaded() {
        return ModList.get().isLoaded(GOETY_AWAKEN);
    }

    public static boolean isHostilityLoaded() {
        return ModList.get().isLoaded(GOETY_HOSTILITY);
    }

    @Nullable
    private static EntityType<?> findType(String namespace, String path) {
        return ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(namespace, path));
    }

    @Nullable
    public static String replacementPathFor(Entity mob) {
        if (mob instanceof CreeperServant && isAwakenLoaded()) {
            return "creeper_servant";
        }
        if (isHostilityLoaded()) {
            if (mob instanceof WindCaller) {
                return "wind_caller";
            }
            if (mob instanceof Mountaineer) {
                return "mountaineer";
            }
        }
        return null;
    }

    public static void tryReplace(Entity mob) {
        if (mob.level().isClientSide()) {
            return;
        }
        String path = replacementPathFor(mob);
        if (path == null) {
            return;
        }
        String namespace = (mob instanceof CreeperServant) ? GOETY_AWAKEN : GOETY_HOSTILITY;
        EntityType<?> type = findType(namespace, path);
        if (type == null) {
            return;
        }
        Entity replacement = type.create(mob.level());
        if (!(replacement instanceof Mob replacementMob)) {
            return;
        }
        CompoundTag tag = new CompoundTag();
        mob.saveWithoutId(tag);
        replacementMob.moveTo(mob.getX(), mob.getY(), mob.getZ(), mob.getYRot(), mob.getXRot());
        replacementMob.load(tag);
        mob.level().addFreshEntity(replacementMob);
        mob.discard();
    }
}
