package github.goldblock.goety_aether.client.events;

import github.goldblock.goety_aether.GoetyAether;
import github.goldblock.goety_aether.client.text.GoldFlowText;
import github.goldblock.goety_aether.common.init.ModPlushies;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = GoetyAether.MOD_ID, value = Dist.CLIENT)
public class TooltipFlowEvents {
    private static final String DIVINE_NAME_KEY = "spell.goety.divine";

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        try {
            List<Component> tooltip = event.getToolTip();

            if (isGildedItem(event.getItemStack()) && !tooltip.isEmpty()) {
                Component name = tooltip.get(0);
                String text = name.getString();
                String suffix = null;
                if (text.endsWith("Plushie")) {
                    suffix = "Plushie";
                } else if (text.endsWith("毛绒玩偶")) {
                    suffix = "毛绒玩偶";
                }
                if (suffix != null && text.length() > suffix.length()) {
                    tooltip.set(0, Component.literal("")
                            .append(GoldFlowText.flowingGold(text.substring(0, text.length() - suffix.length())))
                            .append(Component.literal(suffix).withStyle(name.getStyle())));
                } else {
                    tooltip.set(0, GoldFlowText.flowingGold(text));
                }
            }

            for (int i = 0; i < tooltip.size(); i++) {
                tooltip.set(i, gildDivine(tooltip.get(i)));
            }
        } catch (Throwable ignored) {
        }
    }

    private static boolean isGildedItem(net.minecraft.world.item.ItemStack stack) {
        return stack.getItem() instanceof github.goldblock.goety_aether.common.items.divine.DivineCrownItem
                || stack.getItem() instanceof github.goldblock.goety_aether.common.items.divine.DivineRobeItem
                || stack.getItem() instanceof github.goldblock.goety_aether.common.items.magic.DivineStaff
                || stack.getItem() == ModPlushies.GOLDEN_PLUSHIE.get().asItem()
                || stack.getItem() == ModPlushies.YOYEYE_PLUSHIE.get().asItem();
    }

    private static Component gildDivine(Component component) {
        if (isDivineName(component)) {
            return GoldFlowText.flowingGold(component.getString());
        }

        MutableComponent result = null;

        if (component.getContents() instanceof TranslatableContents contents && contents.getArgs().length > 0) {
            Object[] args = contents.getArgs().clone();
            boolean changed = false;
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Component arg) {
                    Component gilded = gildDivine(arg);
                    if (gilded != arg) {
                        args[i] = gilded;
                        changed = true;
                    }
                }
            }
            if (changed) {
                result = Component.translatable(contents.getKey(), args).withStyle(component.getStyle());
            }
        }

        if (!component.getSiblings().isEmpty()) {
            MutableComponent copy = result != null ? result : component.copy();
            List<Component> siblings = copy.getSiblings();
            List<Component> mapped = new ArrayList<>(siblings.size());
            boolean changed = false;
            for (Component sibling : siblings) {
                Component gilded = gildDivine(sibling);
                mapped.add(gilded);
                if (gilded != sibling) {
                    changed = true;
                }
            }
            if (changed) {
                siblings.clear();
                siblings.addAll(mapped);
                result = copy;
            }
        }

        return result != null ? result : component;
    }

    private static boolean isDivineName(Object object) {
        return object instanceof Component component
                && component.getContents() instanceof TranslatableContents contents
                && DIVINE_NAME_KEY.equals(contents.getKey());
    }
}
