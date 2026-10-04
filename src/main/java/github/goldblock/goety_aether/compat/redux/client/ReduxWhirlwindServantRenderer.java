package github.goldblock.goety_aether.compat.redux.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import github.goldblock.goety_aether.common.entities.ally.mobs.AbstractWhirlwindServant;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.zepalesque.redux.client.render.ReduxModelLayers;
import net.zepalesque.redux.client.render.ReduxRenderTypes;
import net.zepalesque.redux.client.render.entity.IPostRenderer;

public class ReduxWhirlwindServantRenderer extends LivingEntityRenderer<AbstractWhirlwindServant, ReduxWhirlwindServantModel> implements IPostRenderer<AbstractWhirlwindServant> {
    private static final ResourceLocation WHIRLWIND_TEXTURE = new ResourceLocation("aether", "textures/entity/mobs/whirlwind/whirlwind.png");
    private static final ResourceLocation EVIL_WHIRLWIND_TEXTURE = new ResourceLocation("aether", "textures/entity/mobs/whirlwind/evil_whirlwind.png");

    public ReduxWhirlwindServantRenderer(EntityRendererProvider.Context context) {
        super(context, new ReduxWhirlwindServantModel(context.bakeLayer(ReduxModelLayers.WHIRLWIND)), 0.0F);
    }

    @Override
    public ResourceLocation getTextureLocation(AbstractWhirlwindServant entity) {
        return entity.isEvil() ? EVIL_WHIRLWIND_TEXTURE : WHIRLWIND_TEXTURE;
    }

    @Override
    public void render(AbstractWhirlwindServant entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
    }

    @Override
    public void internalRender(AbstractWhirlwindServant entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (!ReduxWhirlwindModelState.isModelEnabled()) {
            return;
        }
        float age = entity.tickCount + partialTicks;
        VertexConsumer consumer = buffer.getBuffer(this.renderType(this.getTextureLocation(entity), this.xOffset(entity, age) % 1.0F));
        poseStack.pushPose();
        this.model.setupAnim(entity, 0.0F, 0.0F, age, 0.0F, 0.0F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        this.scale(entity, poseStack, partialTicks);
        this.model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    @Override
    protected void scale(AbstractWhirlwindServant entity, PoseStack poseStack, float partialTicks) {
        if (entity.isEvil()) {
            poseStack.scale(1.25F, 1.25F, 1.25F);
        }
    }

    protected RenderType renderType(ResourceLocation texture, float xOffset) {
        return ReduxRenderTypes.whirlwindParticleTranslucency(texture, xOffset, 0.0F);
    }

    protected float xOffset(AbstractWhirlwindServant entity, float tickCount) {
        return tickCount * (entity.isEvil() ? 0.015F : 0.01F);
    }

    @Override
    protected void setupRotations(AbstractWhirlwindServant entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
    }
}
