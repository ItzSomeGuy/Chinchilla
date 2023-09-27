package com.chipset.Listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ReactionListener extends ListenerAdapter {
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember(); assert member != null;
        ForumChannel forumChannel = guild.getForumChannelById(1062841778270642308L);
        ThreadChannel threadChannel = event.getChannel().asThreadChannel(); assert forumChannel != null;
        Message message = threadChannel.retrieveStartMessage().complete();

        if (threadChannel.getParentChannel().getIdLong() == forumChannel.getIdLong() && event.getMessageIdLong() == message.getIdLong()) {
            threadChannel.addThreadMember(member).queue();
        }
    }

    public void onMessageReactionRemove(MessageReactionRemoveEvent event) {
        Guild guild = event.getGuild();
        Member member = event.getMember(); assert member != null;
        ForumChannel forumChannel = guild.getForumChannelById(1062841778270642308L);
        ThreadChannel threadChannel = event.getChannel().asThreadChannel(); assert forumChannel != null;
        Message message = threadChannel.retrieveStartMessage() .complete();

        if (threadChannel.getParentChannel().getIdLong() == forumChannel.getIdLong() && event.getMessageIdLong() == message.getIdLong()) {
            threadChannel.removeThreadMember(member).queue();
        }
    }
}
