package com.chipset.context_menu;

import com.jagrosh.jdautilities.command.UserContextMenu;
import com.jagrosh.jdautilities.command.UserContextMenuEvent;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.w3c.dom.Text;

import java.security.SecureRandom;

public class LengthLitigator extends UserContextMenu {
    public LengthLitigator() { this.name = "Length Litigator"; }

    @Override
    protected void execute(UserContextMenuEvent event) {
        // min=0, max=2X
        SecureRandom rand = new SecureRandom();
        Guild guild = event.getGuild();
        assert guild != null;
        TextChannel tc = guild.getTextChannelById(1073670115847045160L);
        assert tc != null;
        MessageHistory history = tc.getHistory();

        history.retrievePast(100).queue(messages -> {
            boolean existing = false;
            Member tester = event.getTargetMember();
            User testerUser = tester.getUser();

            for (Message m : messages) {
                if (m.getMentions().getUsers().get(0).equals(testerUser)) {
                    existing = true;
                }
            }

            int length = rand.nextInt(51);

            String msg = tester.getAsMention() +
                    " 8" +
                    new String(new char[length])
                            .replace("\0", "=") +
                    "D";

            if (!existing) {
                event.reply(msg).queue();
            } else {
                event.reply("you've already been litigated")
                        .setEphemeral(true)
                        .queue();
            }
        });


    }


}
