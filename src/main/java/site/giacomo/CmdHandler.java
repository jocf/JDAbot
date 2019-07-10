package site.giacomo;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

import java.awt.*;

public class CmdHandler {
    // Our EmbedBuilder, this is used to build advanced messages to be sent in chat.
    EmbedBuilder eb = new EmbedBuilder();
    // This is our afkCheck send method. This handles the !afk command
    public void sendAfkCheck(TextChannel afkChannel, TextChannel adminChannel, String runType){
        // if void or cult are not specified, then print a useful message
        if (!(runType.equals("void") || runType.equals("cult"))){
            adminChannel.sendMessage("```Please enter the command with the correct syntax.```").queue();

            eb.setTitle("Title", null);
            eb.setColor(Color.GREEN);
            eb.setDescription("Text");
            eb.addField("Title of field", "test of field", false);
            eb.addBlankField(false);
            eb.setAuthor("name", null, "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");
            eb.setFooter("Text", "https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/zekroBot_Logo_-_round_small.png");
            eb.setImage("https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");
            eb.setThumbnail("https://github.com/zekroTJA/DiscordBot/blob/master/.websrc/logo%20-%20title.png");
            adminChannel.sendMessage(eb.build()).queue();

            return;
        }
        // if the cult parameter is provided start a cult check
        else if (runType.equals("cult")){
            afkChannel.sendMessage("```cult started.```").queue();
            return;
        }
        // if the void parameter is provided start a void check
        else if (runType.equals("void")){
            afkChannel.sendMessage("```void started.```").queue();
            return;
        }

    }

    public void sendHelpMessage(TextChannel channel){

    }
}
