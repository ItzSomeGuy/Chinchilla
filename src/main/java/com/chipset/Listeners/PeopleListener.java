package com.chipset.Listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class PeopleListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        TextChannel channel = event.getGuild().getTextChannelById(740820510811881493L);
        if (guild.getId().equals("193117152709050368")) {
            channel.sendMessage(event.getUser().getAsMention() + " has joined the chipset.").queue();
        }
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        Guild guild = event.getGuild();
        TextChannel channel = event.getGuild().getTextChannelById(740820510811881493L);
        if (guild.getId().equals("193117152709050368")) {
            channel.sendMessage(event.getUser().getAsMention() + " has left the chipset.").queue();
        }
    }
}
