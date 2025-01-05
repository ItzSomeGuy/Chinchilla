package com.chipset.Listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {
    // 78702161663361024L
    // 264817386547445760L

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember();
        long target = 78702161663361024L;
        TextChannel textChannel = guild.getTextChannelById(1186464227259580487L);
        // 1186464227259580487L

        if (member.getIdLong() == target) {
            textChannel.sendMessage(member.getAsMention()).queue(message -> {
                message.delete().queue();
            });
        }
    }
}
