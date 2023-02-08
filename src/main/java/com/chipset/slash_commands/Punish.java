package com.chipset.slash_commands;

import com.chipset.Lavaplayer.PlayerManager;
import com.jagrosh.jdautilities.command.SlashCommand;
import com.jagrosh.jdautilities.command.SlashCommandEvent;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Punish extends SlashCommand {

    public Punish() {
        this.name = "punish";
        this.help = "punishes a user";
        this.options = Collections.singletonList(new OptionData(OptionType.USER, "target", "the soon to be damned").setRequired(true));
    }

    @Override
    public void execute(SlashCommandEvent event) {

        //List of possible channel names
        String[] names = {"Literal Hell", "The Void",
                "The Shadow Realm ", "The Firing Squad",
                "Guantanamo Bay", "6 Feet Under",
                "The Gulag", "Back to Canada",
                "The Crucifixion Cross", "The Midnight Facility",
                "dono's toe sucking dungeon",
                "J's Furry Fun-house", "K's Horse Ranch",
                "The Inside of J's Thiccc Cheeks", "An Uncomfortably Warm Room",
                "A Pile of Hungry Hamsters Just Nibbling on You", "A Room Full of Forks Just Sticking Up on the Ground",
                "Nicole's Trypophobia Terrace", "Between the Slices of Laura's Sandwich",
                "You're Gay/Not Gay Now. Boo. Scary.", "Charlie's Dog is Taking Chunks Out of Your Leg"
        };

        Member target = Objects.requireNonNull(event.getOption("target")).getAsMember(); //Defines the target as whoever the user selects
        VoiceChannel currentChannel = event.getMember().getVoiceState().getChannel().asVoiceChannel();
        TextChannel channel = Objects.requireNonNull(event.getTextChannel());

        assert target != null; //target cannot be null
        if (Objects.requireNonNull(target.getVoiceState()).inAudioChannel()) { //if statement checks if target is in voice channel
            Random nameArr = new Random();
            String randomName = names[nameArr.nextInt(names.length)];
            Guild guild = event.getGuild();
            assert guild != null;

            AudioManager manager = Objects.requireNonNull(event.getGuild()).getAudioManager();
            manager.openAudioConnection(currentChannel);
            event.reply(target.getAsMention()+" was punished").setEphemeral(true).queue();

            String[] tracks = {
                    "https://www.youtube.com/watch?v=JPbFSwMb4vc",
                    "https://www.youtube.com/watch?v=8uAEXzrpfj8",
                    "https://www.youtube.com/watch?v=C-v0kAjBapc",
                    "https://www.youtube.com/watch?v=SuqlriC3O2k"
            };

            Random rand = new Random();
            int index = rand.nextInt(tracks.length);
            String trackurl = tracks[index];

            PlayerManager.getInstance().loadAndPlay(channel, trackurl, false, 120);

           guild.createVoiceChannel(randomName).queue(response -> {

               try {
                   TimeUnit.SECONDS.sleep(4);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }

               guild.moveVoiceMember(target, response).queue();    //Moves target to punishment voice channel

               manager.closeAudioConnection();

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

              while (true) {
                   List<Member> listMembers = response.getMembers();
                   int memberAmount = listMembers.size();

                  if (memberAmount == 0) {
                   Objects.requireNonNull(target.getGuild().getVoiceChannelById(response.getIdLong())).delete().queue();     //Gets the target's current channel (which should be the punishment channel) and proceeds to delete that channel
                       break;
                    }
                }
           });
        }
    }
}
