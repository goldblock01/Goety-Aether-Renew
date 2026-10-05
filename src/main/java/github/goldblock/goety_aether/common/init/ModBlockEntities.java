package github.goldblock.goety_aether.common.init;

import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.blocks.PlushieBlock;
import github.goldblock.goety_aether.common.blocks.entities.EternalNightBeaconBlockEntity;
import github.goldblock.goety_aether.common.blocks.entities.HolyDarkAltarBlockEntity;
import github.goldblock.goety_aether.common.blocks.entities.HolyPedestalBlockEntity;
import github.goldblock.goety_aether.common.blocks.entities.PlushieBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, GoetyAether.MOD_ID);

    public static final RegistryObject<BlockEntityType<EternalNightBeaconBlockEntity>> ETERNAL_NIGHT_BEACON =
            BLOCK_ENTITIES.register("eternal_night_beacon",
                    () -> BlockEntityType.Builder.of(EternalNightBeaconBlockEntity::new, ModBlocks.ETERNAL_NIGHT_BEACON.get()).build(null));

    public static final RegistryObject<BlockEntityType<PlushieBlockEntity>> PLUSHIE =
            BLOCK_ENTITIES.register("plushie", () -> {
                Block[] blocks = ModPlushies.BLOCKS.getEntries().stream()
                        .map(RegistryObject::get)
                        .filter(block -> block instanceof PlushieBlock)
                        .toArray(Block[]::new);
                return BlockEntityType.Builder.of(PlushieBlockEntity::new, blocks).build(null);
            });

    public static final RegistryObject<BlockEntityType<HolyPedestalBlockEntity>> PEDESTAL_HOLYSTONE =
            BLOCK_ENTITIES.register("pedestal_holystone",
                    () -> BlockEntityType.Builder.of(HolyPedestalBlockEntity::new, ModBlocks.PEDESTAL_HOLYSTONE.get()).build(null));

    public static final RegistryObject<BlockEntityType<HolyDarkAltarBlockEntity>> DARK_ALTAR_HOLYSTONE =
            BLOCK_ENTITIES.register("dark_altar_holystone",
                    () -> BlockEntityType.Builder.of(HolyDarkAltarBlockEntity::new, ModBlocks.DARK_ALTAR_HOLYSTONE.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
