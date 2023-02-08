package com.chipset.Listeners;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ReadyListener extends ListenerAdapter {
    @Override
    public void onReady(@NotNull ReadyEvent event) {
        Message.suppressContentIntentWarning();
        System.out.println(event.getJDA().getSelfUser().getName() + " is online!");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        scheduler.schedule(new Task(), 1, TimeUnit.DAYS);
    }

    class Task implements Runnable {
        public void run() {
            // check DB for any birthdays
        }
    }
}
