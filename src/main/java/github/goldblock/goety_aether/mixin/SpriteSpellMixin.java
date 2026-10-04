package github.goldblock.goety_aether.mixin;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.magic.spells.storm.SpriteSpell;
import com.Polarice3.Goety.utils.ColorUtil;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import github.goldblock.goety_aether.GoetyAether;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpriteSpell.class)
public abstract class SpriteSpellMixin {
    @Inject(method = "getSpellType", at = @At("HEAD"), cancellable = true, remap = false)
    private void goety_aether$divineType(CallbackInfoReturnable<SpellType> cir) {
        cir.setReturnValue(GoetyAether.DIVINE);
    }

    @Redirect(method = "SpellResult",
            at = @At(value = "INVOKE",
                    target = "Lcom/Polarice3/Goety/common/magic/spells/storm/SpriteSpell;summonAdvancement(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;)V",
                    remap = false),
            remap = false)
    private void goety_aether$goldSummon(SpriteSpell instance, LivingEntity summoner, LivingEntity summoned) {
        instance.summonAdvancement(summoner, summoned);
        if (summoned.level() instanceof ServerLevel serverLevel) {
            ServerParticleUtil.summonUndeadParticles(serverLevel, summoned, new ColorUtil(0xffd700), 0xffd700, 0xfff2a0);
        }
    }
}
