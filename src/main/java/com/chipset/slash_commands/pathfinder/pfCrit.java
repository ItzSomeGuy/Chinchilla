package com.chipset.slash_commands.pathfinder;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class pfCrit extends SlashCommand {
    public pfCrit() {
        this.name = "pfcrit";
        this.help = "calulates a critical hit in pathfinder 2e";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "calc", "the damage roll").setRequired(true));
        options.add(new OptionData(OptionType.INTEGER, "mod", "the modifier").setRequired(false));
        options.add(new OptionData(OptionType.INTEGER, "multiplier", "crit multiplier").setRequired(false));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event) {

        // do calc
        SecureRandom rand = new SecureRandom();

        String calc = event.getOption("calc").getAsString();

        if (calc.startsWith("d")) {
            calc = 1 + calc;
        }

        String[] split = calc.split("d");

        int count = Integer.parseInt(split[0]);
        int sides = Integer.parseInt(split[1]);

        int mod = 0;
        String modString = "";
        try {
            mod = event.getOption("mod").getAsInt();
            modString = mod >=0 ? "+"+mod : Integer.toString(mod);
        } catch (NullPointerException ignored) {}

        List<Integer> result = new ArrayList<>();
        int total = 0;
        int res;

        for (int i=0; i<count; i++) {
            res = rand.nextInt(sides) + 1;
            result.add(res);
            total += res;
        }

        total += mod;

        int multiplier;
        try {
            multiplier = event.getOption("multiplier").getAsInt();
        } catch (NullPointerException e) {
            multiplier = 2;
        }
        total *= multiplier;

        String rolls = String.format("**rolled:** %s %s => %s", result.toString().replaceAll("[,\\[\\]]",""), modString, total/multiplier);
        rolls = rolls.replaceAll(" 1 ", " *1* ");
        rolls = rolls.replaceAll(String.valueOf(sides), "**"+sides+"**");

        String msg = String.format("🎲 %sd%s%s %n", count, sides, modString) + rolls;

        msg += String.format("\n**crit damage:** %d", total);

        event.reply(msg).queue();
    }
}
