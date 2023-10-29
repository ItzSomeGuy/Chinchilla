package com.chipset.Listeners;

import com.chipset.main.Bot;
import com.chipset.slash_commands.poll.Poll;
import com.chipset.slash_commands.poll.PollHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PollListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        ButtonInteraction interaction = event.getInteraction();

        if (interaction.getButton().getId().startsWith("poll-o-")) { // if button belongs to a poll
            String vote = interaction.getButton().getLabel();
            Message msg = interaction.getMessage();
            MessageEmbed oldEmebd = msg.getEmbeds().get(0);

            /*
            add vote
            update total
            update graphic
             */

            PollHandler.Poll poll;
            try {
                poll = Bot.pollHandler.getPoll(oldEmebd.getTitle());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // identify vote
            boolean voted = poll.vote(vote, event.getMember());

            // update embed
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle(poll.getQuestion());

            for (String choice :
                    poll.getChoices()) {
                // do maths
                int votes = poll.getChoiceVoteCount(choice);
                int percent = Math.round((votes * 100.0f) / poll.getTotalVotes());
                String bar = createProgressBar(percent, votes, poll.getTop());
                // update embed
                embedBuilder.addField("**~** " + choice + " - " + poll.getChoiceVoteCount(choice) + " (" + percent + "%)", bar, false);
            }
            embedBuilder.setFooter("Total Votes: " + poll.getTotalVotes());
            MessageEmbed newEmbed = embedBuilder.build();

            msg.editMessageEmbeds(newEmbed).queue();
            event.reply("thanks for voting!").setEphemeral(true).queue();
        }
    }

    String createProgressBar(int percentage, int votes, int topPercentage) {
        if (percentage < 0) {
            percentage = 0;
        } else if (percentage > 100) {
            percentage = 100;
        }

        int segments = 10;
        int segmentLength = 100 / segments;
        int filledSegments = percentage / segmentLength;
        int emptySegments = segments - filledSegments;

        StringBuilder progressBar = new StringBuilder();

        String filledSegmentChar = "🟥";  // Red segment character
        String emptySegmentChar = "⬛";   // Default empty segment character

        // Check if this is the highest percentage
        if (votes == topPercentage) {
            filledSegmentChar = "🟩";  // Green segment character
        }

        progressBar.append(filledSegmentChar.repeat(filledSegments));

        progressBar.append(emptySegmentChar.repeat(emptySegments));

        return progressBar.toString();
    }
}
