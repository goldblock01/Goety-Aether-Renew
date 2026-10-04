package github.goldblock.goety_aether;

import com.Polarice3.Goety.api.magic.SpellType;
import com.mojang.logging.LogUtils;
import github.goldblock.goety_aether.common.init.ModCreativeTab;
import github.goldblock.goety_aether.common.init.ModEffects;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import github.goldblock.goety_aether.common.init.ModItems;
import github.goldblock.goety_aether.common.init.ModAttributes;
import github.goldblock.goety_aether.compat.deep_aether.DeepAetherCompatManager;
import github.goldblock.goety_aether.compat.genesis.GenesisCompatManager;
import github.goldblock.goety_aether.compat.lost_aether.LostAetherCompatManager;
import github.goldblock.goety_aether.compat.legendary_monsters.LegendaryMonstersCompatManager;
import github.goldblock.goety_aether.compat.mod.AetherDeepAetherCompat;
import github.goldblock.goety_aether.compat.mod.AetherGenesisCompat;
import github.goldblock.goety_aether.compat.mod.AetherLostAetherCompat;
import github.goldblock.goety_aether.compat.mod.AetherReduxCompat;
import github.goldblock.goety_aether.compat.mod.LegendaryMonstersCompat;
import github.goldblock.goety_aether.compat.redux.ReduxCompatManager;
import github.goldblock.goety_aether.config.GoetyAetherConfig;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

@Mod(GoetyAether.MOD_ID)
public class GoetyAether {
    public static final String MOD_ID = "goety_aether";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final SpellType DIVINE = SpellType.create("DIVINE", "divine");

    public GoetyAether() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModEntityTypes.register(modEventBus);
        ModItems.register(modEventBus);
        ModCreativeTab.register(modEventBus);
        ModEffects.register(modEventBus);
        ModAttributes.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GoetyAetherConfig.SPEC, "goety_aether.toml");
        GoetyAetherConfig.loadConfig(GoetyAetherConfig.SPEC, FMLPaths.CONFIGDIR.get().resolve("goety_aether.toml").toString());

        modEventBus.addListener(ModEntityTypes::registerAttributes);

        if (AetherGenesisCompat.isGenesisLoaded()) {
            GenesisCompatManager.init();
        }
        if (AetherReduxCompat.isReduxLoaded()) {
            ReduxCompatManager.init();
        }
        if (AetherDeepAetherCompat.isDeepAetherLoaded()) {
            DeepAetherCompatManager.init();
        }
        if (AetherLostAetherCompat.isLostAetherLoaded()) {
            LostAetherCompatManager.init();
        }
        if (LegendaryMonstersCompat.isLegendaryMonstersLoaded()) {
            LegendaryMonstersCompatManager.init();
        }
        ModItems.registerValkyrieEgg();
        ModItems.registerFireMinionEgg();
    }
}
