package github.goldblock.goety_aether.compat.redux.client;

import com.mojang.blaze3d.vertex.PoseStack;
import github.goldblock.goety_aether.common.entities.ally.mobs.AbstractWhirlwindServant;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ReduxWhirlwindRenderListener {
    @SubscribeEvent
    public static void renderPost(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            return;
        }
        if (!ReduxWhirlwindModelState.isModelEnabled()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        ClientLevel level = minecraft.level;
        if (level == null || player == null) {
            return;
        }
        Frustum frustum = event.getFrustum();
        Camera camera = event.getCamera();
        EntityRenderDispatcher dispatcher = minecraft.getEntityRenderDispatcher();
        float partialTick = minecraft.getPartialTick();
        RenderBuffers buffers = minecraft.renderBuffers();
        Vec3 cameraPos = camera.getPosition();
        double camX = cameraPos.x();
        double camY = cameraPos.y();
        double camZ = cameraPos.z();
        PoseStack poseStack = event.getPoseStack();

        for (Entity entity : level.entitiesForRendering()) {
            if (!(entity instanceof AbstractWhirlwindServant)) {
                continue;
            }
            if (!dispatcher.shouldRender(entity, frustum, camX, camY, camZ) && !entity.hasIndirectPassenger(player)) {
                continue;
            }
            MultiBufferSource.BufferSource buffer = buffers.bufferSource();
            double x = Mth.lerp(partialTick, entity.xOld, entity.getX());
            double y = Mth.lerp(partialTick, entity.yOld, entity.getY());
            double z = Mth.lerp(partialTick, entity.zOld, entity.getZ());
            float yaw = Mth.rotLerp(partialTick, entity.yRotO, entity.getYRot());
            render(entity, x - camX, y - camY, z - camZ, yaw, partialTick, poseStack, buffer, dispatcher);
        }
    }

    private static <E extends Entity> void render(E entity, double x, double y, double z, float yaw, float partialTick, PoseStack poseStack, MultiBufferSource.BufferSource buffer, EntityRenderDispatcher dispatcher) {
        net.minecraft.client.renderer.entity.EntityRenderer<? super E> renderer = dispatcher.getRenderer(entity);
        if (renderer instanceof ReduxWhirlwindServantRenderer reduxRenderer) {
            Vec3 offset = renderer.getRenderOffset(entity, partialTick);
            poseStack.pushPose();
            poseStack.translate(x + offset.x(), y + offset.y(), z + offset.z());
            reduxRenderer.internalRender((AbstractWhirlwindServant) entity, yaw, partialTick, poseStack, buffer, dispatcher.getPackedLightCoords(entity, partialTick));
            buffer.endBatch();
            poseStack.popPose();
        }
    }
}
