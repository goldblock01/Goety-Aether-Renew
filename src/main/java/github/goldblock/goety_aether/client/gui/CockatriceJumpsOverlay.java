package github.goldblock.goety_aether.client.gui;

import github.goldblock.goety_aether.common.entities.ally.mobs.CockatriceServant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class CockatriceJumpsOverlay {
    private static final ResourceLocation TEXTURE_JUMPS = new ResourceLocation("aether", "textures/gui/jumps.png");

    public static final IGuiOverlay COCKATRICE_JUMPS = (gui, guiGraphics, partialTick, width, height) -> {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            Entity vehicle = player.getVehicle();
            if (vehicle instanceof CockatriceServant cockatrice) {
                int maxJumps = CockatriceServant.MAX_JUMPS;
                for (int jumpCount = 0; jumpCount < maxJumps; jumpCount++) {
                    int xPos = width / 2 + jumpCount * 8 - maxJumps * 8 / 2;
                    int yPos = 18;
                    if (jumpCount < cockatrice.getRemainingJumps()) {
                        guiGraphics.blit(TEXTURE_JUMPS, xPos, yPos, 0.0F, 0.0F, 9, 11, 256, 256);
                    } else {
                        guiGraphics.blit(TEXTURE_JUMPS, xPos, yPos, 10.0F, 0.0F, 9, 11, 256, 256);
                    }
                }
            }
        }
    };
}
