package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

public class Inform extends SlashCommand  {
    public Inform() {
        this.name = "inform";
        this.help = "informs a user of specified news";

        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.USER, "target", "the person whom you want to inform"));
        options.add(new OptionData(OptionType.STRING, "message", "the  news"));

        this.options = options;
    }
    @Override
    protected void execute(SlashCommandEvent event) {
        User user =  event.getOption("target").getAsUser();
        String message = event.getOption("message").getAsString();

        user.openPrivateChannel().queue(privateChannel -> {
            privateChannel.sendMessage(message).queue();
        });

        event.reply("it has been done").setEphemeral(true).queue();
    }
}
