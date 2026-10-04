package github.goldblock.goety_aether.client.render;

import com.aetherteam.aether.client.renderer.AetherModelLayers;
import com.aetherteam.aether.client.renderer.entity.model.FireMinionModel;
import github.goldblock.goety_aether.common.entities.ally.mobs.FireMinionServant;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;

public class FireMinionServantRenderer extends MobRenderer<FireMinionServant, FireMinionModel<FireMinionServant>> {
    private static final ResourceLocation FIRE_MINION_SERVANT_TEXTURE = new ResourceLocation("aether", "textures/entity/mobs/sun_spirit/sun_spirit.png");

    public FireMinionServantRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new FireMinionModel<>(pContext.bakeLayer(AetherModelLayers.FIRE_MINION)), 0.8F);
    }

    @Override
    public ResourceLocation getTextureLocation(FireMinionServant pEntity) {
        return FIRE_MINION_SERVANT_TEXTURE;
    }

    @Override
    protected void scale(FireMinionServant pLivingEntity, PoseStack pPoseStack, float pPartialTickTime) {
        pPoseStack.translate(0.0D, 0.35D, 0.0D);
    }

    @Override
    protected int getBlockLightLevel(FireMinionServant pEntity, BlockPos pPos) {
        return 15;
    }

    @Override
    protected int getSkyLightLevel(FireMinionServant pEntity, BlockPos pPos) {
        return 15;
    }
}
