package com.chipset.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;

public class FlipSlash extends ListenerAdapter {
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("flip")) {
            Random rand = new Random();

            boolean res = rand.nextBoolean();
            String result = (res) ? "heads" : "tails";

            event.reply(result).queue();
        }
    }
}
