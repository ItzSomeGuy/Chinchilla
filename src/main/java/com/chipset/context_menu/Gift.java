package com.chipset.context_menu;

import com.jagrosh.jdautilities.command.UserContextMenu;
import com.jagrosh.jdautilities.command.UserContextMenuEvent;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Gift extends UserContextMenu {
    public Gift() {
        this.name = "Give Gift";
    }

    @Override
    protected void execute(@NotNull UserContextMenuEvent event) {
        String url = "https://imgur.com/IgGYsqs";
        User target = Objects.requireNonNull(event.getTargetMember()).getUser();

        target.openPrivateChannel()
                        .flatMap(channel -> channel.sendMessage(url))
                                .queue();

        event.reply("it is done.").setEphemeral(true).queue();
    }
}
