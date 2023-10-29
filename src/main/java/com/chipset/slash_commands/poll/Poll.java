package com.chipset.slash_commands.poll;

import com.chipset.main.Bot;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.LayoutComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.ArrayList;
import java.util.List;

public class Poll extends SlashCommand {
    public Poll() {
        this.name = "poll";
        this.help = "charlie made me do this";
        this.children = new SlashCommand[] {
                new SubPollStart(),
                new SubPollStop()
        };
    }

    @Override
    protected void execute(SlashCommandEvent event) {}

    private static class SubPollStart extends SlashCommand {
        public SubPollStart() {
            this.name = "start";
            this.help = "create a poll";

            this.options = new ArrayList<>();

            options.add(new OptionData(OptionType.STRING, "question", "What are you asking?", true));
            options.add(new OptionData(OptionType.STRING, "op1", "Option 1", true));
            options.add(new OptionData(OptionType.STRING, "op2", "Option 2", true));

            for (int i = 3; i <= 5; i++) {
                options.add(new OptionData(OptionType.STRING, "op" + i, "Option " + i, false));
            }

        }
        @Override
        protected void execute(SlashCommandEvent event) {
            // create the poll...easy...
            String question = event.getOption("question").getAsString();
            ArrayList<String> choices = new ArrayList<>();

            for (OptionMapping option :
                    event.getOptions()) {
                if (option.getName().contains("op")) {
                    choices.add(option.getAsString());
                }
            }

            Bot.pollHandler.createPoll(question, choices);

            // create embed
            MessageEmbed pollEmbed = CreatePollEmbed(question, choices);

            // create action row
            List<Button> buttons = new ArrayList<>();
            int buttonCount = 0;

            for (String choice :
                    choices) {
                buttonCount++;
                buttons.add(Button.primary("poll-o-op"+buttonCount, choice));
            }
            //buttons.add(Button.secondary("poll-n-new", "something else?"));

            List<LayoutComponent> list = new ArrayList<>();
            list.add(ActionRow.of(buttons));

            // build message
            MessageCreateBuilder pollMessageBuilder = new MessageCreateBuilder();
            pollMessageBuilder.addContent("### "+"New Poll!");
            pollMessageBuilder.addEmbeds(pollEmbed);
            pollMessageBuilder.addActionRow(buttons);
            MessageCreateData msg = pollMessageBuilder.build();

            event.reply(msg).queue();
        }

        private MessageEmbed CreatePollEmbed(String question, ArrayList<String> choices) {
            EmbedBuilder embedBuilder = new EmbedBuilder();

            embedBuilder.setTitle(question);

            for (String choice :
                    choices) {
                embedBuilder.addField("**~** " + choice + " 0 - (0%)", "⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛", false);
            }
            embedBuilder.setFooter("Total Votes: 0");

            return embedBuilder.build();
        }
    }

    private static class SubPollStop extends SlashCommand {
        public SubPollStop() {
            this.name = "stop";
            this.help = "end a poll";
        }
        @Override
        protected void execute(SlashCommandEvent event) {
            // close the poll
        }
    }
}
