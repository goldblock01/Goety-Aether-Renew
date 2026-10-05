package github.goldblock.goety_aether.client.render.block;

import github.goldblock.goety_aether.common.blocks.PlushieBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ModISTER extends BlockEntityWithoutLevelRenderer {

    public ModISTER() {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext camera, PoseStack matrixStack, MultiBufferSource buffer, int light, int overlay) {
        Item item = stack.getItem();

        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof PlushieBlock) {
                if (camera == ItemDisplayContext.GUI) {
                    matrixStack.pushPose();
                    matrixStack.translate(0.5F, 0.5F, 0.5F);
                    matrixStack.mulPose(Axis.XP.rotationDegrees(30));
                    matrixStack.mulPose(Axis.YN.rotationDegrees(-45));
                    matrixStack.translate(-0.5F, -0.5F, -0.5F);
                    matrixStack.translate(0.0F, 0.25F, 0.0F);
                    PlushieBlockEntityRenderer.renderItemPlushie(stack, block.defaultBlockState(), 180.0F, matrixStack, buffer, light);
                    matrixStack.popPose();
                } else {
                    PlushieBlockEntityRenderer.renderItemPlushie(stack, block.defaultBlockState(), 180.0F, matrixStack, buffer, light);
                }
            }
        }
    }
}
