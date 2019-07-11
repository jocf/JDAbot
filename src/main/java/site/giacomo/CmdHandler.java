package site.giacomo;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.entities.Emote;


import java.awt.*;

public class CmdHandler {
    protected TextChannel adminChannel;
    protected TextChannel afkTextChannel;


    public void setAdminChannel(TextChannel adminChannel){
        this.adminChannel = adminChannel;
    }

    public void setAfkTextChannel(TextChannel AfkTextChannel){
        this.afkTextChannel = AfkTextChannel;
    }

    // This is our afkCheck send method. This handles the !afk command
    public void preAfkCheck(User startUser){
        // Sends the pre-afk start check message.
        // This message includes VOID/CULT check.
        // Our EmbedBuilder, this is used to build advanced messages to be sent in chat.
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Afk-Check Options", null);
        eb.setColor(Color.GREEN);
        eb.addField("Please choose run type", "Please selected run type by reacting to either :void: or :malus:. Once you are done, react to :whitebag~1: to begin the check.", false);
        //eb.addBlankField(false);
        eb.setFooter("giacomo.site", "https://i.imgur.com/tiQUxJR.png");
        adminChannel.sendMessage(eb.build()).queue();

    }

    public void startAfkCheck(String runType, User startUser){
        // Our EmbedBuilder, this is used to build advanced messages to be sent in chat.
        EmbedBuilder eb = new EmbedBuilder();
        if (!(runType.equals("void") || runType.equals("cult"))){

            eb.setTitle("Afk-Check Options", null);
            eb.setColor(Color.orange);
            eb.addField("How to use", "Please react to either the void/cult symbol, select classes, and then press start.", false);
            eb.addBlankField(false);
            eb.setFooter("giacomo.site", "https://i.imgur.com/tiQUxJR.png");
            eb.setThumbnail("https://i.imgur.com/tiQUxJR.png");
            afkTextChannel.sendMessage(eb.build()).queue();

            return;
        }
        // if the cult parameter is provided start a cult check
        else if (runType.equals("cult")){
            afkTextChannel.sendMessage("```cult started.```").queue();
            return;
        }
        // if the void parameter is provided start a void check
        else if (runType.equals("void")){
            afkTextChannel.sendMessage("```void started.```").queue();
            return;
        }
    }

    public void sendHelpMessage(TextChannel channel){

    }
}
