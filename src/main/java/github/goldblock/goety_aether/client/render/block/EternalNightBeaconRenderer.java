package github.goldblock.goety_aether.client.render.block;

import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.blocks.entities.EternalNightBeaconBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.List;

public class EternalNightBeaconRenderer implements BlockEntityRenderer<EternalNightBeaconBlockEntity> {
    public static final ResourceLocation BEAM_LOCATION = new ResourceLocation(GoetyAether.MOD_ID, "textures/entity/eternal_night_beacon_beam.png");

    public EternalNightBeaconRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(EternalNightBeaconBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        long gameTime = blockEntity.getLevel().getGameTime();
        List<EternalNightBeaconBlockEntity.BeaconBeamSection> list = blockEntity.getBeamSections();
        int j = 0;

        for (int k = 0; k < list.size(); ++k) {
            EternalNightBeaconBlockEntity.BeaconBeamSection section = list.get(k);
            renderBeaconBeam(poseStack, buffer, partialTick, gameTime, j, k == list.size() - 1 ? 1024 : section.getHeight());
            j += section.getHeight();
        }
    }

    private static void renderBeaconBeam(PoseStack poseStack, MultiBufferSource buffer, float partialTick, long gameTime, int yOffset, int height) {
        renderBeaconBeam(poseStack, buffer, BEAM_LOCATION, partialTick, 1.0F, gameTime, yOffset, height, 0.2F, 0.25F);
    }

    public static void renderBeaconBeam(PoseStack poseStack, MultiBufferSource buffer, ResourceLocation beamLocation, float partialTick, float textureScale, long gameTime, int yOffset, int height, float beamRadius, float glowRadius) {
        int i = yOffset + height;
        poseStack.pushPose();
        poseStack.translate(0.5D, 0.0D, 0.5D);
        float f = (float) Math.floorMod(gameTime, 40) + partialTick;
        float f1 = height < 0 ? f : -f;
        float f2 = Mth.frac(f1 * 0.2F - (float) Mth.floor(f1 * 0.1F));
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(f * 2.25F - 45.0F));
        float f6;
        float f8;
        float f9 = -beamRadius;
        float f10;
        float f11;
        float f12 = -beamRadius;
        float f13;
        float f14 = 1.0F;
        float f15 = -1.0F + f2;
        float f16 = (float) height * textureScale * (0.5F / beamRadius) + f15;
        renderPart(poseStack, buffer.getBuffer(RenderType.beaconBeam(beamLocation, false)), 1.0F, yOffset, i, 0.0F, beamRadius, beamRadius, 0.0F, f9, 0.0F, 0.0F, f12, 0.0F, 1.0F, f16, f15);
        poseStack.popPose();
        f6 = -glowRadius;
        float f7 = -glowRadius;
        f8 = -glowRadius;
        f9 = -glowRadius;
        f13 = 0.0F;
        f14 = 1.0F;
        f15 = -1.0F + f2;
        f16 = (float) height * textureScale + f15;
        renderPart(poseStack, buffer.getBuffer(RenderType.beaconBeam(beamLocation, true)), 0.125F, yOffset, i, f6, f7, glowRadius, f8, f9, glowRadius, glowRadius, glowRadius, 0.0F, 1.0F, f16, f15);
        poseStack.popPose();
    }

    private static void renderPart(PoseStack poseStack, VertexConsumer vertexConsumer, float alpha, int yOffset, int height, float p_112164_, float p_112165_, float p_112166_, float p_112167_, float p_112168_, float p_112169_, float p_112170_, float p_112171_, float p_112172_, float p_112173_, float p_112174_, float p_112175_) {
        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();
        renderQuad(matrix4f, matrix3f, vertexConsumer, alpha, yOffset, height, p_112164_, p_112165_, p_112166_, p_112167_, p_112172_, p_112173_, p_112174_, p_112175_);
        renderQuad(matrix4f, matrix3f, vertexConsumer, alpha, yOffset, height, p_112170_, p_112171_, p_112168_, p_112169_, p_112172_, p_112173_, p_112174_, p_112175_);
        renderQuad(matrix4f, matrix3f, vertexConsumer, alpha, yOffset, height, p_112166_, p_112167_, p_112170_, p_112171_, p_112172_, p_112173_, p_112174_, p_112175_);
        renderQuad(matrix4f, matrix3f, vertexConsumer, alpha, yOffset, height, p_112168_, p_112169_, p_112164_, p_112165_, p_112172_, p_112173_, p_112174_, p_112175_);
    }

    private static void renderQuad(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float alpha, int yOffset, int height, float p_112129_, float p_112130_, float p_112131_, float p_112132_, float p_112133_, float p_112134_, float p_112135_, float p_112136_) {
        addVertex(matrix4f, matrix3f, vertexConsumer, alpha, height, p_112129_, p_112130_, p_112134_, p_112135_);
        addVertex(matrix4f, matrix3f, vertexConsumer, alpha, yOffset, p_112129_, p_112130_, p_112134_, p_112136_);
        addVertex(matrix4f, matrix3f, vertexConsumer, alpha, yOffset, p_112131_, p_112132_, p_112133_, p_112136_);
        addVertex(matrix4f, matrix3f, vertexConsumer, alpha, height, p_112131_, p_112132_, p_112133_, p_112135_);
    }

    private static void addVertex(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float alpha, int y, float x, float z, float u, float v) {
        vertexConsumer.vertex(matrix4f, x, (float) y, z).color(1.0F, 1.0F, 1.0F, alpha).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    @Override
    public boolean shouldRenderOffScreen(EternalNightBeaconBlockEntity blockEntity) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

    @Override
    public boolean shouldRender(EternalNightBeaconBlockEntity blockEntity, Vec3 cameraPos) {
        return Vec3.atCenterOf(blockEntity.getBlockPos()).multiply(1.0D, 0.0D, 1.0D)
                .closerThan(cameraPos.multiply(1.0D, 0.0D, 1.0D), this.getViewDistance());
    }
}
