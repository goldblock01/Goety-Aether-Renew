package github.goldblock.goety_aether.common.entities.hostile;

import com.Polarice3.Goety.common.entities.neutral.Owned;
import github.goldblock.goety_aether.common.entities.ally.mobs.SkyWolfServant;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.level.Level;

public class SkyWolf extends SkyWolfServant implements Enemy {
    public SkyWolf(EntityType<? extends Owned> type, Level level) {
        super(type, level);
        this.setHostile(true);
    }
}
