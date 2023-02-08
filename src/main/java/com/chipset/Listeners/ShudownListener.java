package com.chipset.Listeners;

import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ShudownListener extends ListenerAdapter {
    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        System.out.println(event.getJDA().getSelfUser().getName() + " is offline!");
    }
}
