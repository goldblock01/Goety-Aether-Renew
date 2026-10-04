package github.goldblock.goety_aether.common.events;

import github.goldblock.goety_aether.GoetyAether;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(modid = GoetyAether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class SpellScheduler {
    private static final List<Task> TASKS = new ArrayList<>();

    private SpellScheduler() {
    }

    public static void delay(int ticks, Runnable action) {
        TASKS.add(new Task(Math.max(1, ticks), action));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || TASKS.isEmpty()) {
            return;
        }
        Iterator<Task> iterator = TASKS.iterator();
        while (iterator.hasNext()) {
            Task task = iterator.next();
            if (--task.ticks <= 0) {
                iterator.remove();
                task.action.run();
            }
        }
    }

    private static final class Task {
        private int ticks;
        private final Runnable action;

        private Task(int ticks, Runnable action) {
            this.ticks = ticks;
            this.action = action;
        }
    }
}
