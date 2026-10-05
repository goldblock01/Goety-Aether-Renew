package github.goldblock.goety_aether.common.init;

import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.blocks.PlushieBlock;
import github.goldblock.goety_aether.common.items.block.CurioISTERItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModPlushies {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GoetyAether.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GoetyAether.MOD_ID);

    public static final RegistryObject<PlushieBlock> GOLDEN_PLUSHIE = plushieRegister("plushie", PlushieBlock::new);
    public static final RegistryObject<PlushieBlock> YOYEYE_PLUSHIE = plushieRegister("plushie_1", () -> new PlushieBlock(1));

    private static <T extends Block> RegistryObject<T> plushieRegister(final String name, final Supplier<? extends T> supplier) {
        RegistryObject<T> block = BLOCKS.register(name, supplier);
        ITEMS.register(name, () -> new CurioISTERItem(block.get()));
        return block;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
