package com.chipset.Listeners;

import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ModalListener extends ListenerAdapter {

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if (event.getModalId().equals("lfg_create")) {
            String game = Objects.requireNonNull(event.getValue("game")).getAsString();
            String desc = Objects.requireNonNull(event.getValue("desc")).getAsString();

            ForumChannel lfg_board = Objects.requireNonNull(event.getGuild()).getForumChannelById(1062841778270642308L);

            assert lfg_board != null;
            lfg_board.createForumPost(game, MessageCreateData.fromContent(desc)).queue(channel -> {
                channel.getThreadChannel().addThreadMember(Objects.requireNonNull(event.getMember())).queue();
            });

            event.reply("A LFG for " + game + " was created!").setEphemeral(true).queue();
        }
    }
}
