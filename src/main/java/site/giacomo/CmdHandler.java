package site.giacomo;

import net.dv8tion.jda.core.EmbedBuilder;
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
        eb.setColor(Color.PINK);
        eb.addField("Please choose run type", "Select run type by reacting to either " + parser.getCultIcon() + " or " + parser.getVoidIcon() + ". Once you are done, react to " + parser.getStartIcon() + " to begin the check. " + "You can react to " + parser.getStopIcon() + " to cancel.", false);
        eb.addField("","Pre-afk-check was started by " + startUser.getName(),false);
        eb.setFooter("giacomo.site", "https://i.imgur.com/tiQUxJR.png");
        adminChannel.sendMessage(eb.build()).queue();
        return;

    }


    public void sendHelpMessage(){
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Bot Commands!", null);
        eb.setColor(Color.PINK);
        eb.addField("?afk","```Starts a pre-afk check! This will launch a menu in which the user can choose void or cult type and start the check.```",false);
        eb.addField("?help","```Prompts this help message!```",false);
        eb.addField("?changeafkchannel new-channel-name","```Changes the channel in which afk-checks will be sent to.```",false);
        eb.addField("?changeadminchannel new-channel-name","```Changes the channel in which administration commands such as ?afk and ?help can be sent in.```",false);
        eb.addField("?changeverifychannel new-channel-name","```Changes the verification channel.```",false);
        eb.addField("?verify","```User verification! Must be sent in the set verification channel. This allows users to verify themselves automatically.```",false);
        eb.setFooter("giacomo.site", "https://i.imgur.com/tiQUxJR.png");
        adminChannel.sendMessage(eb.build()).queue();
        return;

    }
}
