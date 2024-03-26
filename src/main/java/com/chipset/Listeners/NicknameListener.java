package com.chipset.Listeners;

import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class NicknameListener extends ListenerAdapter {

    @Override
    public void onGuildMemberUpdateNickname(GuildMemberUpdateNicknameEvent event) {
        String userId = event.getUser().getId();

        String givenId = "78953526977368064"; // Replace with the ID you want to check
        List<String> duckNames = Arrays.asList(
                "Mallard", "Wood Duck", "Muscovy Duck", "Pekin Duck", "Rouen Duck",
                "Khaki Campbell", "Indian Runner Duck", "Crested Duck", "Saxony Duck",
                "Swedish Blue Duck", "Buff Duck", "Cayuga Duck", "Appleyard Duck",
                "Aylesbury Duck", "Black Swedish Duck", "Blue Swedish Duck", "Call Duck",
                "Magpie Duck", "Welsh Harlequin Duck", "Indian Runner Duck", "Canvasback Duck",
                "Gadwall Duck", "Northern Pintail", "Northern Shoveler", "American Wigeon",
                "Redhead Duck", "Canvasback Duck", "Greater Scaup", "Lesser Scaup",
                "Tufted Duck", "Ring-necked Duck", "Common Eider", "King Eider",
                "Harlequin Duck", "Common Goldeneye", "Barrow's Goldeneye",
                "Bufflehead Duck", "Common Merganser", "Hooded Merganser",
                "Red-breasted Merganser", "Ruddy Duck", "Falcated Duck",
                "Baikal Teal", "Garganey Duck", "Eurasian Wigeon", "American Black Duck",
                "Marbled Teal", "Green-winged Teal", "Blue-winged Teal", "Cinnamon Teal",
                "European Teal", "Yellow-billed Duck", "Pacific Black Duck",
                "Australian Shelduck", "Paradise Shelduck", "Radjah Shelduck",
                "Pink-eared Duck", "Wandering Whistling Duck", "West Indian Whistling Duck",
                "White-faced Whistling Duck", "Plumed Whistling Duck", "Spotted Whistling Duck",
                "Black-bellied Whistling Duck", "Fulvous Whistling Duck", "White-backed Duck",
                "Freckled Duck", "Comb Duck", "Crested Shelduck", "Baer's Pochard",
                "Madagascar Pochard", "Common Pochard", "Hardhead Duck", "Ringed Teal",
                "Andean Teal", "Chestnut Teal", "Silver Teal", "Speckled Teal",
                "Yellow-billed Pintail", "White-cheeked Pintail", "Chiloe Wigeon",
                "American White Pelican", "Australian Shoveler", "Black-headed Duck",
                "Black-necked Swan", "Blue-billed Duck", "Blue-winged Goose",
                "Bronze-winged Duck", "Cape Barren Goose", "Cape Teal", "Chiloe Wigeon",
                "Cotton Pygmy Goose", "Crested Screamer", "Eurasian Teal", "Fulvous Whistling Duck",
                "Garganey Teal", "Gray Teal", "Greater White-fronted Goose",
                "Green Pygmy Goose", "Kelp Goose", "Kerguelen Goose", "Knob-billed Duck",
                "Laysan Duck", "Lesser White-fronted Goose", "Magellanic Flightless Steamer Duck",
                "Magellanic Flightless Steamer Duck", "Madagascar Teal", "Mallard Duck",
                "Masked Duck", "Meller's Duck", "Muscovy Duck", "New Zealand Scaup",
                "Northern Shoveler", "Orinoco Goose", "Pacific Black Duck", "Puna Teal",
                "Red Shoveler", "Red-billed Duck", "Red-billed Pintail", "Red-billed Teal",
                "Red-crested Pochard", "Ring-necked Duck", "Rosy-billed Pochard",
                "Royal Spoonbill", "Ruddy Duck", "Silver Teal", "Southern Wigeon",
                "Spectacled Duck", "Spot-billed Duck", "Spot-billed Duck", "Spur-winged Goose",
                "Steamer Duck", "Steller's Eider", "Subantarctic Teal", "Swan Goose",
                "Tadorna Duck", "Torrent Duck", "Turquoise Duck", "Vulturine Guineafowl",
                "Wandering Whistling Duck", "West Indian Whistling Duck", "White-backed Duck",
                "White-cheeked Pintail", "White-faced Whistling Duck", "White-headed Duck",
                "White-winged Duck", "Yellow-billed Duck", "Yellow-billed Pintail", "Yellow-billed Teal"
        ); // Add more names as you like

        String givenName = duckNames.get(new Random().nextInt(duckNames.size()));

        if (userId.equals(givenId)) {
            event.getGuild().modifyNickname(event.getMember(), givenName).queue();
        }
    }
}
