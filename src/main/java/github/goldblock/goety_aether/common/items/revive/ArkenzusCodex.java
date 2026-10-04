package github.goldblock.goety_aether.common.items.revive;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.init.ModTags;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import com.aetherteam.aether.client.AetherSoundEvents;
import github.goldblock.goety_aether.common.entities.ally.mobs.ZombieValkyrieQueenServant;
import github.goldblock.goety_aether.common.init.ModEntityTypes;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ArkenzusCodex extends ReviveServantItem {
    public ArkenzusCodex() {
        super(new Properties().rarity(com.aetherteam.aether.item.AetherItems.AETHER_LOOT).setNoRepair().stacksTo(1));
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        Level level = player.getCommandSenderWorld();
        Entity entity;
        if (getSummon(stack, level) != null) {
            entity = getSummon(stack, level);
        } else {
            entity = new ZombieValkyrieQueenServant(ModEntityTypes.ZOMBIE_VALKYRIE_QUEEN_SERVANT.get(), level);
            IOwned owned = (IOwned) entity;
            owned.setTrueOwner(player);
        }
        if (entity instanceof ZombieValkyrieQueenServant valkyrieQueen) {
            if (target.getType().is(ModTags.EntityTypes.ZOMBIE_SERVANTS)) {
                if (valkyrieQueen.getTrueOwner() == player) {
                    valkyrieQueen.setHealth(valkyrieQueen.getMaxHealth());
                    valkyrieQueen.setPos(target.getX(), target.getY(), target.getZ());
                    valkyrieQueen.lookAt(EntityAnchorArgument.Anchor.EYES, player.position());
                    if (level.addFreshEntity(valkyrieQueen)) {
                        valkyrieQueen.spawnAnim();
                        if (level instanceof ServerLevel serverLevel) {
                            for (int i = 0; i < 8; ++i) {
                                ServerParticleUtil.addParticlesAroundSelf(serverLevel, ParticleTypes.POOF, valkyrieQueen);
                                ServerParticleUtil.addParticlesAroundSelf(serverLevel, ParticleTypes.SMOKE, valkyrieQueen);
                            }
                        }
                        valkyrieQueen.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 0.5F);
                        valkyrieQueen.playSound(AetherSoundEvents.ENTITY_VALKYRIE_QUEEN_INTERACT.get(), 2.0F, 0.5F);
                        target.discard();
                        player.swing(hand);
                        stack.shrink(1);
                    }
                }
            }
        }
        return super.interactLivingEntity(stack, player, target, hand);
    }
}
