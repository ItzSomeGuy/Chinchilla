package com.chipset.main;

import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MenuListener extends ListenerAdapter {

    @Override
    public void onEntitySelectInteraction(@NotNull EntitySelectInteractionEvent event) {
        ForumChannel lfg_board = Objects.requireNonNull(event.getGuild()).getForumChannelById(1062841778270642308L);

        ThreadChannel tc = (ThreadChannel) event.getInteraction().getMentions().getChannels().get(0);
        ForumChannel fc = tc.getParentChannel().asForumChannel();

        assert lfg_board != null;
        if (fc.getId().equals(lfg_board.getId())) {
            tc.delete().queue();

            event.reply(tc.getName() + "has been deleted!").setEphemeral(true).queue();
        }

        event.reply("You picked an invalid channel.").setEphemeral(true).queue();
    }
}
