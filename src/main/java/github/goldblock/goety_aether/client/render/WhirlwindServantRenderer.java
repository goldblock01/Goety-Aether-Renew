package github.goldblock.goety_aether.client.render;

import github.goldblock.goety_aether.common.entities.ally.mobs.AbstractWhirlwindServant;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;

public class WhirlwindServantRenderer<T extends AbstractWhirlwindServant> extends EntityRenderer<T> {
    public WhirlwindServantRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(T whirlwind) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
