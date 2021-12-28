package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Roll extends SlashCommand {
    public Roll() {
        this.name = "roll"; // must be lowercase
        this.help = "rolls some amount of die/dice";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER, "count", "number of dice you are rolling"));
        options.add(new OptionData(OptionType.INTEGER, "sides", "how many sides your die has"));

        this.options = options;
    }

    @Override
    public void execute(SlashCommandEvent event) {
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

        String rolls = String.format(" **result:** %s ", result.toString().replaceAll("[,\\[\\]]",""));
        rolls = rolls.replaceAll(" 1 ", " **1** ");
        rolls = rolls.replaceAll(String.valueOf(sides), "**"+sides+"**");

        String msg = String.format("🎲 %sd%s %n", count, sides) + rolls + String.format("\n**total:** %d", total);

        event.reply(msg).queue();
    }
}
