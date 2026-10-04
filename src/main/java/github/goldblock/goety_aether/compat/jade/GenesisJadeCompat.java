package github.goldblock.goety_aether.compat.jade;

import com.Polarice3.Goety.compat.jade.HostileIndicatorProvider;
import com.Polarice3.Goety.compat.jade.ServantModeProvider;
import com.Polarice3.Goety.compat.jade.SummonLifespanProvider;
import github.goldblock.goety_aether.common.entities.ally.mobs.BattleSentryServant;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;

public class GenesisJadeCompat {
    public static void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(ServantModeProvider.INSTANCE, BattleSentryServant.class);
        registration.registerEntityDataProvider(HostileIndicatorProvider.INSTANCE, BattleSentryServant.class);
        registration.registerEntityDataProvider(SummonLifespanProvider.INSTANCE, BattleSentryServant.class);
    }

    public static void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(ServantModeProvider.INSTANCE, BattleSentryServant.class);
        registration.registerEntityComponent(HostileIndicatorProvider.INSTANCE, BattleSentryServant.class);
        registration.registerEntityComponent(SummonLifespanProvider.INSTANCE, BattleSentryServant.class);
    }
}
