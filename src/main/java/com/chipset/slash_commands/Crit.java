package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Crit extends SlashCommand {
    public Crit() {
        this.name = "crit";
        this.help = "calculates crit rolls";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "calc", "the damage roll").setRequired(true));
        options.add(new OptionData(OptionType.INTEGER, "mod", "the modifier").setRequired(true));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        String calc = Objects.requireNonNull(event.getOption("calc")).getAsString();
        int mod = (int) Objects.requireNonNull(event.getOption("mod")).getAsLong();

        String modString;

        if (mod > 0) {
            modString = "+" + mod;
        } else if (mod < 0) {
            modString = Integer.toString(mod);
        } else {
            modString = "";
        }

        TextChannel tc = event.getTextChannel();

        String[] rolls = calc.split("\\+");
        StringBuilder sb = new StringBuilder();

        //Random rand = new Random(); // less random but faster
        SecureRandom rand = new SecureRandom(); // more random but slower

        int grandTotal = 0;

        for (String r : rolls) {
            sb.append("[ ");
            String[] parts = r.split("d");

            int c = Integer.parseInt(parts[0]);
            int s = Integer.parseInt(parts[1]);

            // calculate roll for c amount of dice with s sides
            int total = 0;
            for (int i = 0; i < c; i++) {
                int roll = rand.nextInt(s) + 1;
                total += roll;
                sb.append(roll).append(" ");
            }

            total += c*s;
            sb.append("**").append(c * s).append("**").append(" ");
            sb.append("] ");
            grandTotal += total;
        }
        grandTotal += mod;

        event.reply("🎲 "+calc+modString+"\n"+sb+modString+"\n**Total:** " + grandTotal).queue();
    }
}
