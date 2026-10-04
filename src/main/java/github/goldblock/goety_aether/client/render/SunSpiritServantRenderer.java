package github.goldblock.goety_aether.client.render;

import com.aetherteam.aether.client.renderer.AetherModelLayers;
import com.aetherteam.aether.client.renderer.entity.model.SunSpiritModel;
import github.goldblock.goety_aether.common.entities.ally.mobs.SunSpiritServant;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class SunSpiritServantRenderer extends MobRenderer<SunSpiritServant, SunSpiritModel<SunSpiritServant>> {
    private static final ResourceLocation SUN_SPIRIT_TEXTURE = new ResourceLocation("aether", "textures/entity/mobs/sun_spirit/sun_spirit.png");
    private static final ResourceLocation FROZEN_TEXTURE = new ResourceLocation("aether", "textures/entity/mobs/sun_spirit/frozen_sun_spirit.png");

    public SunSpiritServantRenderer(EntityRendererProvider.Context context) {
        super(context, new SunSpiritModel<>(context.bakeLayer(AetherModelLayers.SUN_SPIRIT)), 1.2F);
    }

    @Override
    protected void scale(SunSpiritServant entity, PoseStack poseStack, float partialTick) {
        poseStack.scale(1.5F, 1.5F, 1.5F);
        poseStack.translate(0.0, 0.3, 0.0);
    }

    @Override
    public ResourceLocation getTextureLocation(SunSpiritServant entity) {
        return entity.isFrozen() ? FROZEN_TEXTURE : SUN_SPIRIT_TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(SunSpiritServant entity, BlockPos pos) {
        return 15;
    }

    @Override
    protected int getSkyLightLevel(SunSpiritServant entity, BlockPos pos) {
        return 15;
    }
}
