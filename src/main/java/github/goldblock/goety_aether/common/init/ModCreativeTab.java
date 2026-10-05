package github.goldblock.goety_aether.common.init;

import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.compat.deep_aether.DeepAetherCompatManager;
import github.goldblock.goety_aether.compat.genesis.GenesisCompatManager;
import github.goldblock.goety_aether.compat.legendary_monsters.LegendaryMonstersCompatManager;
import github.goldblock.goety_aether.compat.lost_aether.LostAetherCompatManager;
import github.goldblock.goety_aether.compat.redux.ReduxCompatManager;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ModCreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GoetyAether.MOD_ID);

    private static final List<Supplier<RegistryObject<? extends Item>>> EGG_ORDER = Arrays.asList(
            () -> ModItems.SENTRY_SERVANT_SPAWN_EGG,
            () -> GenesisCompatManager.BATTLE_SENTRY_SERVANT_SPAWN_EGG,
            () -> GenesisCompatManager.TRACKING_GOLEM_SPAWN_EGG,
            () -> GenesisCompatManager.SENTRY_GOLEM_SPAWN_EGG,
            () -> ModItems.SLIDER_SPAWN_EGG,
            () -> ReduxCompatManager.BLIGHTBUNNY_SERVANT_SPAWN_EGG,
            () -> GenesisCompatManager.SENTRY_GUARDIAN_SPAWN_EGG,
            () -> DeepAetherCompatManager.BABY_ZEPHYR_SERVANT_SPAWN_EGG,
            () -> DeepAetherCompatManager.VENOMITE_SERVANT_SPAWN_EGG,
            () -> DeepAetherCompatManager.EOTSSERVANT_SPAWN_EGG,
            () -> ModItems.VALKYRIE_SERVANT_SPAWN_EGG,
            () -> ModItems.COCKATRICE_SERVANT_SPAWN_EGG,
            () -> ModItems.SUN_SPIRIT_SERVANT_SPAWN_EGG,
            () -> ModItems.ZOMBIE_VALKYRIE_QUEEN_SERVANT_SPAWN_EGG,
            () -> ModItems.FIRE_MINION_SERVANT_SPAWN_EGG,
            () -> ModItems.GOLDEN_SWET_SERVANT_SPAWN_EGG,
            () -> ModItems.BLUE_SWET_SERVANT_SPAWN_EGG,
            () -> ReduxCompatManager.VANILLA_SWET_SERVANT_SPAWN_EGG,
            () -> GenesisCompatManager.DARK_SWET_SERVANT_SPAWN_EGG,
            () -> ModItems.ZEPHYR_SERVANT_SPAWN_EGG,
            () -> GenesisCompatManager.TEMPEST_SERVANT_SPAWN_EGG,
            () -> ModItems.WIND_CALLER_SPAWN_EGG,
            () -> ModItems.MOUNTAINEER_SPAWN_EGG,
            () -> ModItems.AECHOR_PLANT_SERVANT_SPAWN_EGG,
            () -> ModItems.PASSIVE_WHIRLWIND_SERVANT_SPAWN_EGG,
            () -> ModItems.EVIL_WHIRLWIND_SERVANT_SPAWN_EGG,
            () -> ModItems.CREEPER_SERVANT_SPAWN_EGG
    );

    public static final RegistryObject<CreativeModeTab> MAIN_TAB = CREATIVE_MODE_TABS.register("goety_aether",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(Items.GOLD_BLOCK))
                    .title(Component.translatable("itemGroup.goety_aether"))
                    .displayItems((parameters, output) -> {
                        List<Item> placed = new ArrayList<>();
                        output.accept(ModItems.DIVINE_STAFF.get());
                        placed.add(ModItems.DIVINE_STAFF.get());
                        output.accept(ModItems.DIVINE_CROWN.get());
                        placed.add(ModItems.DIVINE_CROWN.get());
                        output.accept(ModItems.DIVINE_ROBE.get());
                        placed.add(ModItems.DIVINE_ROBE.get());
                        output.accept(ModItems.ARKENZUS_CODEX.get());
                        placed.add(ModItems.ARKENZUS_CODEX.get());
                        output.accept(ModItems.SWOLLEN_SUN.get());
                        placed.add(ModItems.SWOLLEN_SUN.get());
                        output.accept(ModItems.UNBOUNDED_WATER_BUCKET.get());
                        placed.add(ModItems.UNBOUNDED_WATER_BUCKET.get());
                        output.accept(ModItems.AETHER_ACCESS_RING.get());
                        placed.add(ModItems.AETHER_ACCESS_RING.get());
                        output.accept(ModBlocks.ETERNAL_NIGHT_BEACON_ITEM.get());
                        placed.add(ModBlocks.ETERNAL_NIGHT_BEACON_ITEM.get());
                        output.accept(ModBlocks.PEDESTAL_HOLYSTONE_ITEM.get());
                        placed.add(ModBlocks.PEDESTAL_HOLYSTONE_ITEM.get());
                        output.accept(ModBlocks.DARK_ALTAR_HOLYSTONE_ITEM.get());
                        placed.add(ModBlocks.DARK_ALTAR_HOLYSTONE_ITEM.get());
                        output.accept(ModItems.SENTRY_FOCUS.get());
                        placed.add(ModItems.SENTRY_FOCUS.get());
                        output.accept(ModItems.BATTLE_SENTRY_FOCUS.get());
                        placed.add(ModItems.BATTLE_SENTRY_FOCUS.get());
                        output.accept(ModItems.TRACKING_GOLEM_FOCUS.get());
                        placed.add(ModItems.TRACKING_GOLEM_FOCUS.get());
                        output.accept(ModItems.SENTRY_GOLEM_FOCUS.get());
                        placed.add(ModItems.SENTRY_GOLEM_FOCUS.get());
                        output.accept(ModItems.PICKAXE_ATTACK_FOCUS.get());
                        placed.add(ModItems.PICKAXE_ATTACK_FOCUS.get());
                        if (DeepAetherCompatManager.BABY_ZEPHYR_FOCUS != null && DeepAetherCompatManager.BABY_ZEPHYR_FOCUS.isPresent()) {
                            output.accept(DeepAetherCompatManager.BABY_ZEPHYR_FOCUS.get());
                            placed.add(DeepAetherCompatManager.BABY_ZEPHYR_FOCUS.get());
                        }
                        output.accept(ModItems.ZEPHYR_FOCUS.get());
                        placed.add(ModItems.ZEPHYR_FOCUS.get());
                        output.accept(ModItems.SKY_WOLF_FOCUS.get());
                        placed.add(ModItems.SKY_WOLF_FOCUS.get());
                        output.accept(ModItems.VALKYRIE_FOCUS.get());
                        placed.add(ModItems.VALKYRIE_FOCUS.get());
                        output.accept(ModItems.COCKATRICE_FOCUS.get());
                        placed.add(ModItems.COCKATRICE_FOCUS.get());
                        output.accept(ModItems.POISON_DART_RAIN_FOCUS.get());
                        placed.add(ModItems.POISON_DART_RAIN_FOCUS.get());
                        if (DeepAetherCompatManager.WIND_CRYSTAL_FOCUS != null && DeepAetherCompatManager.WIND_CRYSTAL_FOCUS.isPresent()) {
                            output.accept(DeepAetherCompatManager.WIND_CRYSTAL_FOCUS.get());
                            placed.add(DeepAetherCompatManager.WIND_CRYSTAL_FOCUS.get());
                        }
                        if (DeepAetherCompatManager.SUPERSTORM_FOCUS != null && DeepAetherCompatManager.SUPERSTORM_FOCUS.isPresent()) {
                            output.accept(DeepAetherCompatManager.SUPERSTORM_FOCUS.get());
                            placed.add(DeepAetherCompatManager.SUPERSTORM_FOCUS.get());
                        }
                        if (GenesisCompatManager.TEMPEST_FOCUS != null && GenesisCompatManager.TEMPEST_FOCUS.isPresent()) {
                            output.accept(GenesisCompatManager.TEMPEST_FOCUS.get());
                            placed.add(GenesisCompatManager.TEMPEST_FOCUS.get());
                        }
                        output.accept(ModItems.THUNDER_CRYSTAL_FOCUS.get());
                        placed.add(ModItems.THUNDER_CRYSTAL_FOCUS.get());
                        output.accept(ModItems.FIRE_CRYSTAL_FOCUS.get());
                        placed.add(ModItems.FIRE_CRYSTAL_FOCUS.get());
                        output.accept(ModItems.ICE_CRYSTAL_FOCUS.get());
                        placed.add(ModItems.ICE_CRYSTAL_FOCUS.get());
                        if (LostAetherCompatManager.CLOUD_BREATH_FOCUS != null && LostAetherCompatManager.CLOUD_BREATH_FOCUS.isPresent()) {
                            output.accept(LostAetherCompatManager.CLOUD_BREATH_FOCUS.get());
                            placed.add(LostAetherCompatManager.CLOUD_BREATH_FOCUS.get());
                        }
                        if (LegendaryMonstersCompatManager.CLOUD_FALLING_FOCUS != null && LegendaryMonstersCompatManager.CLOUD_FALLING_FOCUS.isPresent()) {
                            output.accept(LegendaryMonstersCompatManager.CLOUD_FALLING_FOCUS.get());
                            placed.add(LegendaryMonstersCompatManager.CLOUD_FALLING_FOCUS.get());
                        }
                        output.accept(ModItems.SOLAR_FOCUS.get());
                        placed.add(ModItems.SOLAR_FOCUS.get());
                        output.accept(ModItems.DIVINE_FAVOR_FOCUS.get());
                        placed.add(ModItems.DIVINE_FAVOR_FOCUS.get());
                        output.accept(ModItems.LEVITATION_FOCUS.get());
                        placed.add(ModItems.LEVITATION_FOCUS.get());
                        output.accept(ModItems.NEW_MOON_FOCUS.get());
                        placed.add(ModItems.NEW_MOON_FOCUS.get());
                        output.accept(ModItems.REPEL_FOCUS.get());
                        placed.add(ModItems.REPEL_FOCUS.get());
                        for (Supplier<RegistryObject<? extends Item>> eggSupplier : EGG_ORDER) {
                            RegistryObject<? extends Item> egg = eggSupplier.get();
                            if (egg != null && egg.isPresent()) {
                                output.accept(egg.get());
                                placed.add(egg.get());
                            }
                        }
                        ModItems.ITEMS.getEntries().forEach(entry -> {
                            if (!placed.contains(entry.get())) {
                                output.accept(entry.get());
                            }
                        });
                        output.accept(ModPlushies.GOLDEN_PLUSHIE.get().asItem());
                        placed.add(ModPlushies.GOLDEN_PLUSHIE.get().asItem());
                        output.accept(ModPlushies.YOYEYE_PLUSHIE.get().asItem());
                        placed.add(ModPlushies.YOYEYE_PLUSHIE.get().asItem());
                    })
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
