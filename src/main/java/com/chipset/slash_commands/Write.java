package com.chipset.slash_commands;

import com.chipset.dumbo.AudioReceiveHandler;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Widget;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Write extends SlashCommand {
    public Write() {
        this.name = "write";
        this.help = "writes down what the user says in voice";
    }
    @Override
    protected void execute(SlashCommandEvent slashCommandEvent) {
        Guild guild = slashCommandEvent.getGuild();

        VoiceChannel vc = slashCommandEvent.getMember().getVoiceState().getChannel().asVoiceChannel();
        TextChannel tc = guild.getTextChannelById(740820510811881493L);
        Member member = slashCommandEvent.getMember();

        // join VC
        AudioManager manager = Objects.requireNonNull(slashCommandEvent.getGuild()).getAudioManager();
        manager.openAudioConnection(vc);

        // listen?
        slashCommandEvent.deferReply().queue();
        tc.sendMessage("Recording..").queue();

        AudioReceiveHandler listener = new AudioReceiveHandler();
        manager.setReceivingHandler(listener);

        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                manager.closeAudioConnection();
                byte[] recordedAudio = listener.getRecordedAudio();

                try (FileOutputStream fos = new FileOutputStream("recorded_audio.pcm")) {
                    fos.write(recordedAudio);
                    fos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 10, TimeUnit.SECONDS);

        slashCommandEvent.reply("done!").queue();
    }
}
