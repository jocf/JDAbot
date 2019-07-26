package site.giacomo;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

import java.awt.*;

/*
THIS SECTION REQUIRES FULL IMPLEMENTATION
 */



public class AfkCheck extends Bot{

    protected String runType;
    protected User startUser;
    protected TextChannel afkTextChannel;
    protected PropParser parser;

    public AfkCheck(String runType, User startUser, TextChannel afkTextChannel, PropParser parser){
        this.runType = runType;
        this.startUser = startUser;
        this.afkTextChannel = afkTextChannel;
        this.parser = parser;
    }


    public void startCheck(){
        EmbedBuilder eb = new EmbedBuilder();
        if (runType == "void"){
            eb.setTitle("Void Run", null);
            Color purple = new Color(102,0,153);
            eb.setColor(purple);
            eb.addField("Please respond to the afk-check.", "Select run type by reacting to either " + parser.getCultIcon() + " or " + parser.getVoidIcon() + ". Once you are done, react to " + parser.getStartIcon() + " to begin the check. " + "You can react to " + parser.getStopIcon() + " to cancel.", false);
            eb.addField("","Void run started by " + startUser.getName(),false);
            eb.setFooter("giacomo.site", "https://i.imgur.com/tiQUxJR.png");
            afkTextChannel.sendMessage(eb.build()).queue();
        }else if(runType == "cult"){
            eb.setTitle("Cult Run", null);
            eb.setColor(Color.red);
            //eb.addField("Please choose run type", "Select run type by reacting to either " + parser.getCultIcon() + " or " + parser.getVoidIcon() + ". Once you are done, react to " + parser.getStartIcon() + " to begin the check. " + "You can react to " + parser.getStopIcon() + " to cancel.", false);
            eb.addField("","Cult run started by " + startUser.getName(),false);
            eb.setFooter("giacomo.site", "https://i.imgur.com/tiQUxJR.png");
            afkTextChannel.sendMessage(eb.build()).queue();
        }

    }






}
