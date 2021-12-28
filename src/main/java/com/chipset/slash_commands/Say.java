package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Say extends SlashCommand {
    public Say() {
        this.name = "say"; // must be lowercase
        this.help = "just says shit";

        List<OptionData> options = new ArrayList<>();
        // this.options = Collections.singletonList(new OptionData(OptionType.STRING, "text", "The text to say.").setRequired(true)); <- single option version
        options.add(new OptionData(OptionType.STRING, "text", "the text you want the bot to say"));
        options.add(new OptionData(OptionType.USER, "target", "the person you want to yell at"));

        this.options = options;
    }

    @Override
    public void execute(SlashCommandEvent event) {
        OptionMapping optionText = event.getOption("text");
        OptionMapping optionTarget = event.getOption("target");

        if (optionText == null) {
            event.reply("joe sucks!").queue();
            return;
        } else if (optionTarget == null) {
            event.reply(optionText.getAsString()).queue();
            return;
        }
        event.reply(Objects.requireNonNull(optionTarget.getAsMember()).getAsMention()+" "+optionText.getAsString()).queue();
    }
}
