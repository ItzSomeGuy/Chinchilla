package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.managers.AudioManager;


import java.util.Objects;

public class Leave extends SlashCommand {
    public Leave() {
        this.name = "leave";
        this.help = "Allows bot to leave voice channel that it is currently connected to";
    }

    @Override
    public void execute(SlashCommandEvent event) {
        AudioManager manager = Objects.requireNonNull(event.getGuild()).getAudioManager();
        manager.closeAudioConnection();
        event.reply("left").setEphemeral(true).queue();

    }

}
