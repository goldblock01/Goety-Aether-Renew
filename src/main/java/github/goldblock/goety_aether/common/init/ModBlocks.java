package github.goldblock.goety_aether.common.init;

import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.blocks.EternalNightBeaconBlock;
import github.goldblock.goety_aether.common.blocks.HolyDarkAltarBlock;
import github.goldblock.goety_aether.common.blocks.HolyPedestalBlock;
import com.aetherteam.aether.block.AetherBlocks;
import com.aetherteam.aether.item.AetherItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
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

    public static final RegistryObject<HolyPedestalBlock> PEDESTAL_HOLYSTONE = BLOCKS.register("pedestal_holystone",
            () -> new HolyPedestalBlock(BlockBehaviour.Properties.copy(AetherBlocks.HOLYSTONE.get()).noOcclusion()));

    public static final RegistryObject<BlockItem> PEDESTAL_HOLYSTONE_ITEM = ITEMS.register("pedestal_holystone",
            () -> new BlockItem(PEDESTAL_HOLYSTONE.get(), new Item.Properties()));

    public static final RegistryObject<HolyDarkAltarBlock> DARK_ALTAR_HOLYSTONE = BLOCKS.register("dark_altar_holystone",
            () -> new HolyDarkAltarBlock(BlockBehaviour.Properties.copy(AetherBlocks.HOLYSTONE.get()).noOcclusion()));

    public static final RegistryObject<BlockItem> DARK_ALTAR_HOLYSTONE_ITEM = ITEMS.register("dark_altar_holystone",
            () -> new BlockItem(DARK_ALTAR_HOLYSTONE.get(), new Item.Properties()));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
        ITEMS.register(eventBus);
    }
}
