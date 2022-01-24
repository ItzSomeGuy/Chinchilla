package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Choose extends SlashCommand {
    public Choose() {
        this.name = "choose";
        this.help = "choose between two options";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "option1", "the first option").setRequired(true));
        options.add(new OptionData(OptionType.STRING, "option2", "the second option").setRequired(true));

        this.options = options;
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        List<OptionMapping> options = event.getOptions();
        ArrayList<String> choices = new ArrayList<>();

        for (OptionMapping option : options) {
            choices.add(option.getAsString());
        }

        SecureRandom random = new SecureRandom();

        String choice = choices.get(random.nextInt(choices.size()));


        // print options to console
        event.reply("I think you should go with "+choice).queue();
    }
}
