package github.goldblock.goety_aether.mixin;

import com.aetherteam.aether.entity.projectile.crystal.FireCrystal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = FireCrystal.class, remap = false)
public interface FireCrystalAccessor {
    @Accessor("xPower")
    void setXPower(double value);

    @Accessor("yPower")
    void setYPower(double value);

    @Accessor("zPower")
    void setZPower(double value);
}
