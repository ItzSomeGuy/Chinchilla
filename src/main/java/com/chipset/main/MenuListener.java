package com.chipset.main;

import com.chipset.Lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuListener extends ListenerAdapter {

    @Override
    public void onEntitySelectInteraction(@NotNull EntitySelectInteractionEvent event) {
        if (Objects.equals(event.getInteraction().getSelectMenu().getId(), "lfg_abort")) {
            ForumChannel lfg_board = Objects.requireNonNull(event.getGuild()).getForumChannelById(1062841778270642308L);

            ThreadChannel tc = (ThreadChannel) event.getInteraction().getMentions().getChannels().get(0);
            ForumChannel fc = tc.getParentChannel().asForumChannel();

            assert lfg_board != null;
            if (fc.getId().equals(lfg_board.getId())) {
                tc.delete().queue();

                event.reply(tc.getName() + " has been deleted!").setEphemeral(true).queue();
            }

            event.reply("You picked an invalid channel.").setEphemeral(true).queue();
        }
    }

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        // Menu:Message:Reminder
        if (Objects.requireNonNull(event.getInteraction().getSelectMenu().getId()).substring(0,9).equals("reminder-")) {
            String input = event.getInteraction().getValues().get(0);
            String id = event.getInteraction().getSelectMenu().getId().substring(9);

            event.getInteraction().editMessage("sure thing! I'll remind you about this in " + input).queue();
            event.getInteraction().editSelectMenu(null).queue();

            // Extract the number and the time unit from the input
            Pattern pattern = Pattern.compile("(\\d+)\\s(\\w+)");
            Matcher matcher = pattern.matcher(input);
            matcher.find();
            int delay = Integer.parseInt(matcher.group(1));
            String timeUnit = matcher.group(2);

            // Convert the time unit string to a TimeUnit enum
            TimeUnit delayUnit;
            switch (timeUnit) {
                case "second":
                case "seconds":
                    delayUnit = TimeUnit.SECONDS;
                    break;
                case "minute":
                case "minutes":
                    delayUnit = TimeUnit.MINUTES;
                    break;
                case "hour":
                case "hours":
                    delayUnit = TimeUnit.HOURS;
                    break;
                case "day":
                    delayUnit = TimeUnit.DAYS;
                    break;
                default:
                    throw new IllegalArgumentException("Invalid time unit: " + timeUnit);
            }

            ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            Runnable task = () -> {
                User user = event.getUser();
                event.getChannel().retrieveMessageById(id).queue(message -> {
                    String content = message.getContentDisplay();
                    String out = "**Here's that message you wanted me to remind you about:**\n\n"+content+"\n\n...bitch.";

                    String url_pattern = "^(http:\\/\\/www\\.|https:\\/\\/www\\.|http:\\/\\/|https:\\/\\/)?[a-z0-9]+([\\-\\.]{1}[a-z0-9]+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";

                    Pattern r = Pattern.compile(url_pattern);
                    Matcher m = r.matcher(content);
                    if (!m.find( )) {
                        List<MessageEmbed> embeds = message.getEmbeds();

                        event.getUser().openPrivateChannel().flatMap(privateChannel ->
                                {
                                    return privateChannel.sendMessage(out).addEmbeds(embeds);
                                })
                                .queue();
                    } else {
                        event.getUser().openPrivateChannel().flatMap(privateChannel ->
                                {
                                    return privateChannel.sendMessage(out);
                                })
                                .queue();
                    }

                });
            };
            executor.schedule(task, delay, delayUnit);
        // Slash:LoFi
        } else if (event.getInteraction().getSelectMenu().getId().equals("menu:vibeCheck")) {
            String vibe = event.getInteraction().getValues().get(0);

            event.getInteraction().editMessage("enjoy the "+ vibe +" vibes!").queue();
            event.getInteraction().editSelectMenu(null).queue();

            VoiceChannel vc = event.getMember().getVoiceState().getChannel().asVoiceChannel();
            TextChannel tc = event.getChannel().asTextChannel();

            AudioManager manager = Objects.requireNonNull(event.getGuild()).getAudioManager();
            manager.openAudioConnection(vc);

            String link = null;

            switch (vibe) {
                case "relax":
                case "study":
                    link = "https://www.youtube.com/watch?v=jfKfPfyJRdk";
                    break;
                case "sleep":
                case "chill":
                    link = "https://www.youtube.com/watch?v=rUxyKA_-grg";
                    break;
            }

            PlayerManager.getInstance().loadAndPlay(tc, link, false, true, 20);
        }
    }
}
