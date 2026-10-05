package github.goldblock.goety_aether.common.items.block;

import github.goldblock.goety_aether.client.render.block.ModISTERs;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.function.Consumer;

public class CurioISTERItem extends BlockItem implements ICurioItem {

    public CurioISTERItem(Block block) {
        super(block, new Properties());
    }

    public CurioISTERItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return ModISTERs.get();
            }
        });
    }
}
