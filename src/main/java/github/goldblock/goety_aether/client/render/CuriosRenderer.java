package github.goldblock.goety_aether.client.render;

import com.Polarice3.Goety.client.render.ModModelLayer;
import com.Polarice3.Goety.client.render.WearRenderer;
import com.Polarice3.Goety.client.render.model.DarkHatModel;
import com.Polarice3.Goety.client.render.model.DarkRobeModel;
import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.common.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public class CuriosRenderer {
    public static String folderPath = "textures/models/curios/";

    public static ResourceLocation render(String textureName) {
        return new ResourceLocation(GoetyAether.MOD_ID, folderPath + textureName);
    }

    public static void register() {
        CuriosRendererRegistry.register(ModItems.DIVINE_CROWN.get(), () -> new WearRenderer(
                render("divine_crown.png"),
                new DarkHatModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModModelLayer.IRON_CROWN))));
        CuriosRendererRegistry.register(ModItems.DIVINE_ROBE.get(), () -> new WearRenderer(
                render("divine_robe.png"),
                new DarkRobeModel(Minecraft.getInstance().getEntityModels().bakeLayer(ModModelLayer.DARK_ROBE))));
    }
}
