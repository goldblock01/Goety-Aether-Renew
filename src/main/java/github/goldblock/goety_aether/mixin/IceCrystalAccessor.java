package github.goldblock.goety_aether.mixin;

import com.aetherteam.aether.entity.projectile.crystal.IceCrystal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = IceCrystal.class, remap = false)
public interface IceCrystalAccessor {
    @Accessor("xPower")
    void setXPower(double value);

    @Accessor("zPower")
    void setZPower(double value);
}
