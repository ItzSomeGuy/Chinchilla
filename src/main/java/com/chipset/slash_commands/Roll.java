package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Roll extends SlashCommand {
    public Roll() {
        this.name = "roll"; // must be lowercase
        this.help = "rolls some amount of die/dice";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.INTEGER, "count", "number of dice you are rolling").setRequired(true));
        options.add(new OptionData(OptionType.INTEGER, "sides", "how many sides your die has").setRequired(true));
        options.add(new OptionData(OptionType.STRING, "modifier", "modifier to add to the roll"));

        this.options = options;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        //Random rand = new Random(); // less random but faster
        SecureRandom rand = new SecureRandom(); // more random but slower

        int count = (int) Objects.requireNonNull(event.getOption("count")).getAsLong();
        int sides = (int) Objects.requireNonNull(event.getOption("sides")).getAsLong();
        String modifier = "0";

        // if modifier is not null
        if (event.getOption("modifier") != null) {
            modifier = Objects.requireNonNull(event.getOption("modifier")).getAsString();
        }

        // if modifier does not start with a + or -, set to 0
        if (!modifier.startsWith("+") && !modifier.startsWith("-")) {
            modifier = "+0";
        }

        int mod = Integer.parseInt(modifier);

        List<Integer> result = new ArrayList<>();
        int total = 0;

        for (int i=0; i<count; i++) {
            int res = rand.nextInt((sides - 1) + 1) + 1;
            result.add(res);
            total += res;
        }

        total += mod;

        String rolls = String.format("**result:** %s ", result.toString().replaceAll("[,\\[\\]]",""));
        rolls = rolls.replaceAll(" 1 ", " **1** ");
        rolls = rolls.replaceAll(String.valueOf(sides), "**"+sides+"**");

        String msg = String.format("🎲 %sd%s%s %n", count, sides, modifier) + rolls + String.format("\n**total:** %d", total);

        event.reply(msg).queue();
    }
}
