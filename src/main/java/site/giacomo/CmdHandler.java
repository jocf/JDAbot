package site.giacomo;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;

import java.awt.*;
import java.util.List;

public class CmdHandler extends Bot{
    protected TextChannel adminChannel;
    protected TextChannel afkTextChannel;
    protected List<Emote> guildEmotes;
    protected PropParser parser;

    public CmdHandler(List<Emote> guildEmotes, PropParser parser){
        /*
        Guild emotes currently not in use as using text values from config file is currently easier.
        This is scheduled to change if server administrators require large emotes (15+) and a full parser will be implemented.
         */
        this.guildEmotes = guildEmotes;
        this.parser = parser;
    }

    public void setAdminChannel(TextChannel adminChannel){ this.adminChannel = adminChannel; }

    public void setAfkTextChannel(TextChannel AfkTextChannel){ this.afkTextChannel = AfkTextChannel; }

    // This is our afkCheck send method. This handles the !afk command
    public void preAfkCheck(User startUser){
        /*
        Sends the pre-afk start check message.
        This message includes VOID/CULT check.
        EmbedBuilder, this is used to build advanced messages to be sent in chat.
        */
        //adminChannel.sendMessage("Your guild has " + guildEmotes.size() + " emotes").queue();
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Afk-Check Options", null);
        eb.setColor(Color.orange);
        eb.addField("Please choose run type", "Select run type by reacting to either " + parser.getCultIcon() + " or " + parser.getVoidIcon() + ". Once you are done, react to " + parser.getStartIcon() + " to begin the check. " + "You can react to " + parser.getStopIcon() + " to cancel.", false);
        eb.addField("","Afk-Check was started by " + startUser.getName(),false);
        eb.setFooter("giacomo.site", "https://i.imgur.com/tiQUxJR.png");
        adminChannel.sendMessage(eb.build()).queue();

    }


    public void sendHelpMessage(TextChannel channel){


    }

    // This will be used to handle reactions to messages sent in chat.
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event){

    }
}
