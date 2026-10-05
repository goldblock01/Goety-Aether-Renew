package github.goldblock.goety_aether.client.events;

import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.client.render.BlueSwetServantRenderer;
import github.goldblock.goety_aether.client.render.CockatriceServantRenderer;
import github.goldblock.goety_aether.client.render.CreeperServantRenderer;
import github.goldblock.goety_aether.client.render.FireMinionServantRenderer;
import github.goldblock.goety_aether.client.render.GoldenSwetServantRenderer;
import github.goldblock.goety_aether.client.render.MountaineerRenderer;
import github.goldblock.goety_aether.client.render.WhirlwindServantRenderer;
import github.goldblock.goety_aether.client.render.SkyWolfRenderer;
import github.goldblock.goety_aether.client.render.RainPoisonDartRenderer;
import github.goldblock.goety_aether.client.render.SunSpiritServantRenderer;
import github.goldblock.goety_aether.client.render.GoetyAetherModelLayers;
import github.goldblock.goety_aether.client.render.SentryBombModel;
import github.goldblock.goety_aether.client.render.SentryBombRenderer;
import github.goldblock.goety_aether.client.render.SentryServantRenderer;
import github.goldblock.goety_aether.client.render.SliderRenderer;
import github.goldblock.goety_aether.client.render.ValkyrieServantRenderer;
import github.goldblock.goety_aether.client.render.WindCallerRenderer;
import github.goldblock.goety_aether.client.render.ZephyrServantRenderer;
import github.goldblock.goety_aether.client.render.ZombieValkyrieQueenServantRenderer;
import github.goldblock.goety_aether.client.render.block.EternalNightBeaconRenderer;
import github.goldblock.goety_aether.client.render.block.PlushieBlockEntityRenderer;
import github.goldblock.goety_aether.client.render.model.PlushieModel;
import github.goldblock.goety_aether.common.init.ModBlockEntities;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import github.goldblock.goety_aether.compat.deep_aether.client.DeepAetherCompatClient;
import github.goldblock.goety_aether.compat.genesis.client.GenesisCompatClient;
import github.goldblock.goety_aether.compat.lost_aether.client.LostAetherCompatClient;
import github.goldblock.goety_aether.compat.legendary_monsters.client.LegendaryMonstersCompatClient;
import github.goldblock.goety_aether.compat.mod.AetherDeepAetherCompat;
import github.goldblock.goety_aether.compat.mod.AetherGenesisCompat;
import github.goldblock.goety_aether.compat.mod.AetherLostAetherCompat;
import github.goldblock.goety_aether.compat.mod.AetherReduxCompat;
import github.goldblock.goety_aether.compat.mod.LegendaryMonstersCompat;
import github.goldblock.goety_aether.compat.redux.client.ReduxCompatClient;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

import java.nio.file.Path;

@EventBusSubscriber(modid = GoetyAether.MOD_ID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientEvents {
    public static final ModelLayerLocation SENTRY_BOMB_LAYER = new ModelLayerLocation(

            new ResourceLocation(GoetyAether.MOD_ID, "sentry_bomb"), "main");

    @SubscribeEvent
    public static void clientSetup(net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent event) {
        event.enqueueWork(github.goldblock.goety_aether.client.render.CuriosRenderer::register);
        if (AetherReduxCompat.isReduxLoaded()) {
            ReduxCompatClient.registerEvents();
        }
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.SENTRY_SERVANT.get(), SentryServantRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SLIDER.get(), SliderRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.VALKYRIE_SERVANT.get(), ValkyrieServantRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.COCKATRICE_SERVANT.get(), CockatriceServantRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SUN_SPIRIT_SERVANT.get(), SunSpiritServantRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.ZOMBIE_VALKYRIE_QUEEN_SERVANT.get(), ZombieValkyrieQueenServantRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.FIRE_MINION_SERVANT.get(), FireMinionServantRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.WIND_CALLER.get(), WindCallerRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.MOUNTAINEER.get(), MountaineerRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SKY_WOLF_SERVANT.get(), SkyWolfRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SKY_WOLF.get(), SkyWolfRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.GOLDEN_SWET_SERVANT.get(), GoldenSwetServantRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.BLUE_SWET_SERVANT.get(), BlueSwetServantRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.ZEPHYR_SERVANT.get(), ZephyrServantRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.SENTRY_BOMB.get(), SentryBombRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.FIRE_CRYSTAL.get(),
                (net.minecraft.client.renderer.entity.EntityRendererProvider<github.goldblock.goety_aether.common.entities.projectile.FireCrystal>) (net.minecraft.client.renderer.entity.EntityRendererProvider) com.aetherteam.aether.client.renderer.entity.FireCrystalRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.ICE_CRYSTAL.get(),
                com.aetherteam.aether.client.renderer.entity.IceCrystalRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.THUNDER_CRYSTAL.get(),
                (net.minecraft.client.renderer.entity.EntityRendererProvider<github.goldblock.goety_aether.common.entities.projectile.ThunderCrystal>) (net.minecraft.client.renderer.entity.EntityRendererProvider) com.aetherteam.aether.client.renderer.entity.ThunderCrystalRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.RAIN_POISON_DART.get(), RainPoisonDartRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.DIVINE_FAVOR_CLOUD.get(), net.minecraft.client.renderer.entity.NoopRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.NEW_MOON_ATTRACTOR.get(), net.minecraft.client.renderer.entity.NoopRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.REPEL_ATTRACTOR.get(), net.minecraft.client.renderer.entity.NoopRenderer::new);
        event.registerEntityRenderer(ModEntityTypes.AECHOR_PLANT_SERVANT.get(), github.goldblock.goety_aether.client.render.AechorPlantServantRenderer::new);
        if (!AetherReduxCompat.isReduxLoaded()) {
            event.registerEntityRenderer(ModEntityTypes.PASSIVE_WHIRLWIND_SERVANT.get(), WhirlwindServantRenderer::new);
            event.registerEntityRenderer(ModEntityTypes.EVIL_WHIRLWIND_SERVANT.get(), WhirlwindServantRenderer::new);
        }
        event.registerEntityRenderer(ModEntityTypes.CREEPER_SERVANT.get(), CreeperServantRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ETERNAL_NIGHT_BEACON.get(), EternalNightBeaconRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.PLUSHIE.get(), PlushieBlockEntityRenderer::new);
        if (AetherGenesisCompat.isGenesisLoaded()) {
            GenesisCompatClient.registerRenderers(event);
        }
        if (AetherReduxCompat.isReduxLoaded()) {
            ReduxCompatClient.registerRenderers(event);
        }
        if (AetherDeepAetherCompat.isDeepAetherLoaded()) {
            DeepAetherCompatClient.registerRenderers(event);
        }
        if (AetherLostAetherCompat.isLostAetherLoaded()) {
            LostAetherCompatClient.registerRenderers(event);
        }
        if (LegendaryMonstersCompat.isLegendaryMonstersLoaded()) {
            LegendaryMonstersCompatClient.registerRenderers(event);
        }
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SENTRY_BOMB_LAYER, SentryBombModel::createBodyLayer);
        event.registerLayerDefinition(GoetyAetherModelLayers.EOTS_SEGMENT, github.goldblock.goety_aether.client.render.EOTSServantSegmentModel::createBodyLayer);
        event.registerLayerDefinition(GoetyAetherModelLayers.EOTS_CONTROLLER, github.goldblock.goety_aether.client.render.EOTSSControllerModel::createBodyLayer);
        event.registerLayerDefinition(GoetyAetherModelLayers.BABY_ZEPHYR, github.goldblock.goety_aether.client.render.BabyZephyrServantModel::createBodyLayer);
        event.registerLayerDefinition(GoetyAetherModelLayers.PLUSHIE, PlushieModel::createBodyLayer);
        if (AetherGenesisCompat.isGenesisLoaded()) {
            GenesisCompatClient.registerLayerDefinitions(event);
        }
        if (AetherDeepAetherCompat.isDeepAetherLoaded()) {
            DeepAetherCompatClient.registerLayerDefinitions(event);
        }
        if (LegendaryMonstersCompat.isLegendaryMonstersLoaded()) {
            LegendaryMonstersCompatClient.registerLayerDefinitions(event);
        }
    }

    @SubscribeEvent
    public static void registerOverlays(RegisterGuiOverlaysEvent event) {
        event.registerAboveAll("cockatrice_jumps", github.goldblock.goety_aether.client.gui.CockatriceJumpsOverlay.COCKATRICE_JUMPS);
    }

    @SubscribeEvent
    public static void addEinheriValkyrieQueenPack(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            Path resourcePath = ModList.get().getModFileById(GoetyAether.MOD_ID).getFile().findResource(new String[]{"packs/client/einheri_valkyrie_queen"});
            Pack.Info info = new Pack.Info(Component.translatable("resourcePack.goety_aether.einheri_valkyrie_queen.description"), 15, FeatureFlagSet.of());
            Pack pack = Pack.create(
                    "builtin/client/einheri_valkyrie_queen",
                    Component.translatable("resourcePack.goety_aether.einheri_valkyrie_queen.name"),
                    false,
                    path -> new PathPackResources(path, resourcePath, true),
                    info,
                    PackType.CLIENT_RESOURCES,
                    Pack.Position.TOP,
                    false,
                    PackSource.BUILT_IN
            );
            event.addRepositorySource(consumer -> consumer.accept(pack));
        }
    }
}
