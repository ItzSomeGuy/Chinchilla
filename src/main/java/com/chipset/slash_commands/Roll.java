package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
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
        options.add(new OptionData(OptionType.STRING, "calc", "the damage roll").setRequired(true));
        options.add(new OptionData(OptionType.INTEGER, "mod", "the modifier").setRequired(false));

        this.options = options;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        //Random rand = new Random(); // less random but faster
        SecureRandom rand = new SecureRandom(); // more random but slower

        String calc = Objects.requireNonNull(event.getOption("calc")).getAsString();

        if (calc.startsWith("d") ) {
            calc = "1"+calc;
        }

        String[] split = calc.split("d");

        int count = Integer.parseInt(split[0]);
        int sides = Integer.parseInt(split[1]);

        int mod = 0;
        String modString = "";
        try {
            mod = (int) Objects.requireNonNull(event.getOption("mod")).getAsLong();
            modString = mod >= 0 ? "+"+mod : Integer.toString(mod);
        } catch (NullPointerException e) {
            // do nothing
        }

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

        String msg = String.format("🎲 %sd%s%s %n", count, sides, modString) + rolls + String.format("\n**total:** %d", total);

        event.reply(msg).queue();
    }
}
