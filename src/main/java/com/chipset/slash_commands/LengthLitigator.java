package com.chipset.slash_commands;

import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.security.SecureRandom;
import java.util.Objects;

public class LengthLitigator extends SlashCommand {
    public LengthLitigator() {
        this.name = "litigate";
        this.help = "the measuring stick of champions";
    }

    @Override
    protected void execute(SlashCommandEvent event) {
        SecureRandom rand = new SecureRandom();
        Guild guild = event.getGuild();
        assert guild != null;
        TextChannel tc = guild.getTextChannelById(1073670115847045160L);
        assert tc != null;
        MessageHistory history = tc.getHistory();

        history.retrievePast(100).queue(messages -> {
            boolean existing = false;

            Member tester = event.getMember(); assert tester != null;
            User testerUser = tester.getUser();

            for (Message m : messages) {
                if (m.getMentions().getUsers().get(0).equals(testerUser)) {
                    existing = true;
                }
            }

            int length = rand.nextInt(51);

            String msg = tester.getAsMention() +
                    "\n8" +
                    new String(new char[length])
                            .replace("\0", "=") +
                    "D";

            if (event.getChannel().equals(tc)) {
                if (!existing) {
                    event.reply(msg).queue();
                } else {
                    event.reply("you've already been litigated")
                            .setEphemeral(true)
                            .queue();
                }
            } else {

                event.reply("this isn't the place for something like this..try " + tc.getAsMention())
                        .setEphemeral(true)
                        .queue();
            }
        });


    }


}
