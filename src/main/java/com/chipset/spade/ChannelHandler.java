package com.chipset.spade;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChannelHandler extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(@NotNull GuildVoiceUpdateEvent event) {
        String spadeID = "735693277059219528";
        User user = event.getMember().getUser();

        if (event.getChannelJoined() != null && event.getChannelJoined().getId().equals(spadeID)) {
            Guild guild = event.getGuild();

            // create a private hole
            guild.createCategory(user.getName()).queue(category -> {
                // set permissions
                category.getPermissionContainer().getManager().putRolePermissionOverride(
                        guild.getPublicRole().getIdLong(),
                        null,
                        List.of(Permission.VIEW_CHANNEL)
                ).queue();

                // create a text channel
                guild.createTextChannel(user.getName()+"'s Rock", category).queue();

                // create a voice channel
                guild.createVoiceChannel(user.getName()+"'s Hole", category).queue(voiceChannel
                        -> guild.moveVoiceMember(event.getMember(), voiceChannel).queue());

                ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

                Runnable taskDelete = () -> {
                    VoiceChannel vc = category.getVoiceChannels().get(0);

                    if (vc.getMembers().isEmpty()) {
                        List<GuildChannel> channels = category.getChannels();

                        // delete channels
                        for (GuildChannel channel : channels) {
                            channel.delete().queue();
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        // delete category
                        category.delete().queue();

                        // clean up executor
                        ses.shutdown();
                    }
                };

                ses.scheduleAtFixedRate(taskDelete, 15, 15, TimeUnit.MINUTES);
            });
        }
    }
}
