package github.goldblock.goety_aether.client.render;

import github.goldblock.goety_aether.GoetyAether;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class GoetyAetherModelLayers {
    public static final ModelLayerLocation EOTS_SEGMENT = register("eots_segment");
    public static final ModelLayerLocation EOTS_CONTROLLER = register("eots_controller");
    public static final ModelLayerLocation BABY_ZEPHYR = register("baby_zephyr");
    public static final ModelLayerLocation SENTRY_GUARDIAN = register("sentry_guardian");
    public static final ModelLayerLocation TRACKING_GOLEM = register("tracking_golem");
    public static final ModelLayerLocation TEMPEST = register("tempest");
    public static final ModelLayerLocation TEMPEST_TRANSPARENCY = new ModelLayerLocation(new ResourceLocation(GoetyAether.MOD_ID, "tempest"), "transparency");
    public static final ModelLayerLocation BATTLE_SENTRY = register("battle_sentry");
    public static final ModelLayerLocation SENTRY_GOLEM = register("sentry_golem");
    public static final ModelLayerLocation HURRICANE_SERVANT = register("hurricane_servant");

    private static ModelLayerLocation register(String name) {
        return new ModelLayerLocation(new ResourceLocation(GoetyAether.MOD_ID, name), "main");
    }
}
