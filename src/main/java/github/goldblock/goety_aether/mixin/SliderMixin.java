package github.goldblock.goety_aether.mixin;

import com.aetherteam.aether.entity.monster.dungeon.boss.Slider;
import github.goldblock.goety_aether.common.init.ModEffects;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(Slider.class)
public abstract class SliderMixin {
    @Inject(method = "canDamageSlider", at = @At("HEAD"), cancellable = true, remap = false)
    private void goety_aether$pickaxeAttack(DamageSource source, CallbackInfoReturnable<Optional<LivingEntity>> cir) {
        LivingEntity attacker = null;
        Entity direct = source.getDirectEntity();
        if (direct instanceof LivingEntity living) {
            attacker = living;
        } else if (direct instanceof Projectile projectile && projectile.getOwner() instanceof LivingEntity owner) {
            attacker = owner;
        }
        if (attacker != null && attacker.hasEffect(ModEffects.PICKAXE_ATTACK.get())
                && ((Slider) (Object) this).level().getDifficulty() != Difficulty.PEACEFUL) {
            cir.setReturnValue(Optional.of(attacker));
        }
    }
}
