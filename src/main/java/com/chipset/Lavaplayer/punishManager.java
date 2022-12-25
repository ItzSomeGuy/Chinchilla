package com.chipset.Lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.Map;

public class punishManager {
    //NIGHTMARENIGHTMARENIGHTMARENIGHTMARENIGHTMARENIGHTMARENIGHTMARENIGHTMARENIGHTMARENIGHTMARENIGHTMARENIGHTMARENIGHTMARE
    public static punishManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager punishAudioPlayerManager;

    public punishManager() {
        this.musicManagers = new HashMap<>();
        this.punishAudioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.punishAudioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.punishAudioPlayerManager);
    }
    public GuildMusicManager getMusicManager(Guild guild){
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager punishGuildMusicManager = new GuildMusicManager(this.punishAudioPlayerManager);

            guild.getAudioManager().setSendingHandler(punishGuildMusicManager.getSendHandler());
           return punishGuildMusicManager;
        });
    }
    public void punishLoadAndPlay(TextChannel channel, String track){
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        this.punishAudioPlayerManager.loadItemOrdered(musicManager, track, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {

            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {

            }
        });
    }

    public static punishManager getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new punishManager();
        }
        return INSTANCE;
    }

}
