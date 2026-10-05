package github.goldblock.goety_aether.common.init;

import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.blocks.EternalNightBeaconBlock;
import com.aetherteam.aether.item.AetherItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, GoetyAether.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, GoetyAether.MOD_ID);

    public static final RegistryObject<EternalNightBeaconBlock> ETERNAL_NIGHT_BEACON = BLOCKS.register("eternal_night_beacon",
            EternalNightBeaconBlock::new);

    public static final RegistryObject<BlockItem> ETERNAL_NIGHT_BEACON_ITEM = ITEMS.register("eternal_night_beacon",
            () -> new BlockItem(ETERNAL_NIGHT_BEACON.get(), new Item.Properties().rarity(AetherItems.AETHER_LOOT)));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
