package github.goldblock.goety_aether.compat.jade;

import com.Polarice3.Goety.compat.jade.HostileIndicatorProvider;
import com.Polarice3.Goety.compat.jade.ServantModeProvider;
import com.Polarice3.Goety.compat.jade.SummonLifespanProvider;
import com.Polarice3.Goety.compat.jade.SummonOwnerProvider;
import github.goldblock.goety_aether.common.entities.ally.mobs.CockatriceServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.SentryServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.Slider;
import github.goldblock.goety_aether.common.entities.ally.mobs.ValkyrieServant;
import github.goldblock.goety_aether.common.entities.ally.mobs.ZombieValkyrieQueenServant;
import github.goldblock.goety_aether.common.entities.ally.neutral.AbstractSwetServant;
import github.goldblock.goety_aether.compat.mod.AetherGenesisCompat;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class GoetyAetherJadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(ServantModeProvider.INSTANCE, SentryServant.class);
        registration.registerEntityDataProvider(HostileIndicatorProvider.INSTANCE, SentryServant.class);
        registration.registerEntityDataProvider(SummonOwnerProvider.INSTANCE, SentryServant.class);
        registration.registerEntityDataProvider(ServantModeProvider.INSTANCE, CockatriceServant.class);
        registration.registerEntityDataProvider(HostileIndicatorProvider.INSTANCE, CockatriceServant.class);
        registration.registerEntityDataProvider(SummonOwnerProvider.INSTANCE, CockatriceServant.class);
        registration.registerEntityDataProvider(SummonLifespanProvider.INSTANCE, CockatriceServant.class);
        registration.registerEntityDataProvider(ServantModeProvider.INSTANCE, Slider.class);
        registration.registerEntityDataProvider(HostileIndicatorProvider.INSTANCE, Slider.class);
        registration.registerEntityDataProvider(SummonOwnerProvider.INSTANCE, Slider.class);
        registration.registerEntityDataProvider(ServantModeProvider.INSTANCE, ValkyrieServant.class);
        registration.registerEntityDataProvider(HostileIndicatorProvider.INSTANCE, ValkyrieServant.class);
        registration.registerEntityDataProvider(SummonOwnerProvider.INSTANCE, ValkyrieServant.class);
        registration.registerEntityDataProvider(ServantModeProvider.INSTANCE, ZombieValkyrieQueenServant.class);
        registration.registerEntityDataProvider(HostileIndicatorProvider.INSTANCE, ZombieValkyrieQueenServant.class);
        registration.registerEntityDataProvider(SummonOwnerProvider.INSTANCE, ZombieValkyrieQueenServant.class);
        registration.registerEntityDataProvider(ServantModeProvider.INSTANCE, AbstractSwetServant.class);
        registration.registerEntityDataProvider(HostileIndicatorProvider.INSTANCE, AbstractSwetServant.class);
        registration.registerEntityDataProvider(SummonOwnerProvider.INSTANCE, AbstractSwetServant.class);
        registration.registerEntityDataProvider(SummonLifespanProvider.INSTANCE, SentryServant.class);
        registration.registerEntityDataProvider(SummonLifespanProvider.INSTANCE, Slider.class);
        registration.registerEntityDataProvider(SummonLifespanProvider.INSTANCE, ValkyrieServant.class);
        registration.registerEntityDataProvider(SummonLifespanProvider.INSTANCE, ZombieValkyrieQueenServant.class);
        registration.registerEntityDataProvider(SummonLifespanProvider.INSTANCE, AbstractSwetServant.class);
        if (AetherGenesisCompat.isGenesisLoaded()) {
            GenesisJadeCompat.register(registration);
        }
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(ServantModeProvider.INSTANCE, SentryServant.class);
        registration.registerEntityComponent(HostileIndicatorProvider.INSTANCE, SentryServant.class);
        registration.registerEntityComponent(ServantModeProvider.INSTANCE, CockatriceServant.class);
        registration.registerEntityComponent(HostileIndicatorProvider.INSTANCE, CockatriceServant.class);
        registration.registerEntityComponent(SummonLifespanProvider.INSTANCE, CockatriceServant.class);
        registration.registerEntityComponent(ServantModeProvider.INSTANCE, Slider.class);
        registration.registerEntityComponent(HostileIndicatorProvider.INSTANCE, Slider.class);
        registration.registerEntityComponent(ServantModeProvider.INSTANCE, ValkyrieServant.class);
        registration.registerEntityComponent(HostileIndicatorProvider.INSTANCE, ValkyrieServant.class);
        registration.registerEntityComponent(ServantModeProvider.INSTANCE, ZombieValkyrieQueenServant.class);
        registration.registerEntityComponent(HostileIndicatorProvider.INSTANCE, ZombieValkyrieQueenServant.class);
        registration.registerEntityComponent(ServantModeProvider.INSTANCE, AbstractSwetServant.class);
        registration.registerEntityComponent(HostileIndicatorProvider.INSTANCE, AbstractSwetServant.class);
        registration.registerEntityComponent(SummonLifespanProvider.INSTANCE, SentryServant.class);
        registration.registerEntityComponent(SummonLifespanProvider.INSTANCE, Slider.class);
        registration.registerEntityComponent(SummonLifespanProvider.INSTANCE, ValkyrieServant.class);
        registration.registerEntityComponent(SummonLifespanProvider.INSTANCE, ZombieValkyrieQueenServant.class);
        registration.registerEntityComponent(SummonLifespanProvider.INSTANCE, AbstractSwetServant.class);
        if (AetherGenesisCompat.isGenesisLoaded()) {
            GenesisJadeCompat.registerClient(registration);
        }
    }
}
