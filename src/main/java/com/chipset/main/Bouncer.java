package com.chipset.main;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Bouncer extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        TextChannel tc = event.getGuild().getTextChannelsByName("hello-goodbye", true).get(0);
        String person = event.getMember().getAsMention();

        tc.sendMessage(person+"*** has joined!***").queue();
    }

    @Override
    public void onGuildMemberRemove(@NotNull GuildMemberRemoveEvent event) {
        TextChannel tc = event.getGuild().getTextChannelsByName("hello-goodbye", true).get(0);
        String person = Objects.requireNonNull(event.getMember()).getAsMention();

        tc.sendMessage(person+"*** went poof!***").queue();
    }
}
