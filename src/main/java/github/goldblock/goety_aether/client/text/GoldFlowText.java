package github.goldblock.goety_aether.client.text;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.Mth;

public class GoldFlowText {
    private static final long FLOW_PERIOD = 3000L;

    public static int flowingColor(int index) {
        float time = (Util.getMillis() % FLOW_PERIOD) / (float) FLOW_PERIOD;
        float wave = 0.5F + 0.5F * Mth.sin(index * 0.9F - time * (float) (Math.PI * 2));
        int r = (int) Mth.lerp(wave, 170.0F, 255.0F);
        int g = (int) Mth.lerp(wave, 120.0F, 235.0F);
        int b = (int) Mth.lerp(wave, 20.0F, 120.0F);
        return -16777216 | r << 16 | g << 8 | b;
    }

    public static Component flowingGold(String text) {
        MutableComponent result = Component.empty();
        String clean = ChatFormatting.stripFormatting(text);

        for (int i = 0; clean != null && i < clean.length(); i++) {
            int color = flowingColor(i);
            result.append(Component.literal(String.valueOf(clean.charAt(i))).withStyle(style -> style.withColor(TextColor.fromRgb(color))));
        }

        return result;
    }
}
