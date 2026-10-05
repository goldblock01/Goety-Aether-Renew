package github.goldblock.goety_aether.client.render.block;

import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.client.render.model.PlushieModel;
import github.goldblock.goety_aether.common.blocks.PlushieBlock;
import github.goldblock.goety_aether.common.blocks.entities.PlushieBlockEntity;
import github.goldblock.goety_aether.client.render.GoetyAetherModelLayers;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class PlushieBlockEntityRenderer implements BlockEntityRenderer<PlushieBlockEntity> {
    protected static final ResourceLocation TEXTURE = new ResourceLocation(GoetyAether.MOD_ID, "textures/entity/plushie/0.png");

    public PlushieBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PlushieBlockEntity blockEntity, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BlockState blockstate = blockEntity.getBlockState();
        float f1 = 22.5F * blockstate.getValue(PlushieBlock.ROTATION);
        renderPlushie(blockEntity, partialTicks, f1, matrixStack, buffer, combinedLight);
    }

    public static void renderPlushie(PlushieBlockEntity blockEntity, float partialTicks, float rotateY, PoseStack matrixStack, MultiBufferSource buffer, int light) {
        PlushieModel plushieModel = new PlushieModel(Minecraft.getInstance().getEntityModels().bakeLayer(GoetyAetherModelLayers.PLUSHIE));
        matrixStack.pushPose();
        matrixStack.translate(0.5D, 0.0D, 0.5D);

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        float f = blockEntity.getAnimation(partialTicks);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCullZOffset(getTexture(blockEntity.getBlockState())));
        plushieModel.setupAnim(f, rotateY, 0.0F);
        plushieModel.renderToBuffer(matrixStack, consumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }

    public static void renderItemPlushie(ItemStack stack, BlockState blockState, float rotateY, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight) {
        PlushieModel plushieModel = new PlushieModel(Minecraft.getInstance().getEntityModels().bakeLayer(GoetyAetherModelLayers.PLUSHIE));
        matrixStack.pushPose();
        matrixStack.translate(0.5D, 0.0D, 0.5D);

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        matrixStack.scale(0.5F, 0.5F, 0.5F);
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(buffer, RenderType.entityTranslucent(getTexture(blockState)), true, stack.hasFoil());
        plushieModel.setupAnim(0, rotateY, 0.0F);
        plushieModel.renderToBuffer(matrixStack, vertexConsumer, combinedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }

    public static ResourceLocation getTexture(BlockState blockState) {
        ResourceLocation texture = TEXTURE;
        if (blockState.getBlock() instanceof PlushieBlock plushieBlock) {
            texture = new ResourceLocation(GoetyAether.MOD_ID, "textures/entity/plushie/" + plushieBlock.getPlushieType() + ".png");
        }
        return texture;
    }
}
