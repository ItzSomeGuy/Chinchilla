package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Objects;

public class Join extends SlashCommand {
    public Join() {
        this.name = "join";
        this.help = "Allows bot to join voice channel";
    }
    @Override
    public void execute(SlashCommandEvent event){
      VoiceChannel vc = Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel();

      if (vc != null) {
          AudioManager manager = Objects.requireNonNull(event.getGuild()).getAudioManager();
          manager.openAudioConnection(vc);
      }
      event.reply("joined").setEphemeral(true).queue();
    }

}

