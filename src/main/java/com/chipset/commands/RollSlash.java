package com.chipset.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class RollSlash extends ListenerAdapter {
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("roll")) {
            Random rand = new Random();

            int count = (int) Objects.requireNonNull(event.getOption("count")).getAsLong();
            int sides = (int) Objects.requireNonNull(event.getOption("sides")).getAsLong();

            List<Integer> result = new ArrayList<>();
            int total = 0;

            for (int i=0; i<count; i++) {
                int res = rand.nextInt((sides - 1) + 1) + 1;
                result.add(res);
                total += res;
            }

            String rolls = String.format("**result:** %s", result.toString().replaceAll("[,\\[,\\]]",""));
            rolls = rolls.replaceAll("1", "**1**");
            rolls = rolls.replaceAll(String.valueOf(sides), "**"+sides+"**");

            String msg = String.format("🎲 %sd%s %n", count, sides) + rolls + String.format("\n**total:** %d", total);

            event.reply(msg).queue();
        }
    }
}
