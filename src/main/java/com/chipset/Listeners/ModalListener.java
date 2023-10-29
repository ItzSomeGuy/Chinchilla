package com.chipset.Listeners;

import com.chipset.main.Bot;
import com.chipset.slash_commands.poll.PollHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
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
        } else if (event.getModalId().startsWith("poll-custom-")) {
            String choice = Objects.requireNonNull(event.getValue("choice")).getAsString();
            String question = event.getModalId().substring(12);
            System.out.println(choice);

            PollHandler.Poll poll;
            try {
                poll = Bot.pollHandler.getPoll(question);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println(poll.getChoices());
            poll.addChoice(choice);
            System.out.println(poll.getChoices());
            poll.vote(choice, event.getMember());
            System.out.println(poll.getTotalVotes());

            // update embed
            Message msg = event.getMessage();
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle(poll.getQuestion());

            for (String c :
                    poll.getChoices()) {
                // do maths
                int votes = poll.getChoiceVoteCount(c);
                int percent = Math.round((votes * 100.0f) / poll.getTotalVotes());
                String bar = createProgressBar(percent, votes, poll.getTop());
                // update embed
                embedBuilder.addField("**~** " + c + " - " + poll.getChoiceVoteCount(c) + " (" + percent + "%)", bar, false);
            }
            embedBuilder.setFooter("Total Votes: " + poll.getTotalVotes());
            MessageEmbed newEmbed = embedBuilder.build();

            msg.editMessageEmbeds(newEmbed).queue();

            // update action row
            List<Button> buttons = msg.getButtons();
            buttons.remove(buttons.size()-1);
            buttons.add(Button.primary("poll-o-op"+buttons.size()+1, choice));
            System.out.println(buttons.size());
            if (buttons.size() < 5) {
                buttons.add(Button.secondary("poll-n-new", "something else?"));
            }

            msg.editMessageComponents(ActionRow.of(buttons)).queue();

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
