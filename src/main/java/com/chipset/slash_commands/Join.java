package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Collections;
import java.util.Objects;

public class Join extends SlashCommand {
    public Join() {
        this.name = "join";
        this.help = "Allows bot to join voice channel";
        this.options = Collections.singletonList(new OptionData(OptionType.CHANNEL, "target", "channel you want the bot to join").setRequired(true));
    }
    @Override
    public void execute(SlashCommandEvent event){
        OptionMapping target = event.getOption("target");
        assert target != null;

        VoiceChannel vc = (VoiceChannel) target.getAsGuildChannel();

        AudioManager manager = Objects.requireNonNull(event.getGuild()).getAudioManager();
        manager.openAudioConnection(vc);

        event.reply("joined "+vc.getName()).setEphemeral(true).queue();
    }

}

