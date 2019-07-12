package site.giacomo;

import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Bot extends ListenerAdapter {
    /*
     Bot startup in main method. authToken is passed from the config file which is parsed in the Main class.
     */
    // Creating our parser object.
    protected PropParser parser = new PropParser();
    //protected CmdHandler handler = new CmdHandler();
    public static void main(String authToken) {
        try{
            JDA bot = new JDABuilder(authToken).addEventListener(new Bot()).build();
            bot.awaitReady(); // Blocking guarantees that JDA will be completely loaded.
            System.out.println("Finished Building bot!");
        }
        catch(LoginException e) {
            System.out.println("A login exception has occurred.");
            e.printStackTrace();
        }
        catch(InterruptedException e){
            System.out.println("The connection was interrupted unexpectedly.");
            e.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // Generating our file properties.
        parser.generateProperties();
        // Fetching the bot object from the event received.
        JDA bot = event.getJDA();
        // Now, we will check if the event is coming from a text channel within the discord guild.
        if (event.isFromType(ChannelType.TEXT)) {


            //If the message is a user sent message from the administration channel

            if (!event.getAuthor().isBot() && event.getChannel().getName().equals(parser.getAdminChannel())) {
                // Creating CmdHandler object when a message is received and meets the criteria.
                // NOTE: Emotes are not currently in use. Scheduled to be in use in future updates.
                CmdHandler handler = new CmdHandler(event.getGuild().getEmotes(), parser);
                System.out.println("The user " + event.getAuthor().getName() + " sent the command " + event.getMessage().getContentDisplay() + ".");
                // Channel for final afk check to be sent to.
                TextChannel AfkTextChannel = event.getGuild().getTextChannelsByName(parser.getAfkCheckChannel(), true).get(0);
                // Pass the adminChannel and AfkTextChannel to the command handler.
                handler.setAdminChannel(event.getTextChannel());
                handler.setAfkTextChannel(AfkTextChannel);
                if (event.getMessage().getContentDisplay().equals("?afk")) {
                    /*
                    Should link to the AfkCheck class. To be implemented later.
                    We need to fetch the AfkCheck channel name here as well.
                    Call sendAfkCheck method and pass the starting user (user who entered the command).
                     */
                    User startUser = event.getAuthor();
                    handler.preAfkCheck(startUser);
                } else if (event.getMessage().getContentDisplay().equals("?help")) {
                    TextChannel adminTextChannel = event.getTextChannel();
                    // Call sendHelpMessage method.
                    handler.sendHelpMessage(adminTextChannel);
                } else if (event.getMessage().getContentDisplay().equals("?verify")) {
                    // Do verification stuff here.
                } else { // If some unhandled error occurs throw a syntax error.
                    event.getTextChannel().sendMessage("```Please enter the command with the correct syntax.```").queue();
                    return;
                }

            }


            /*
            If the entered message was from a bot, we will need to determine the purpose of the message and then select
            an appropriate response.
             */

            else if (event.getAuthor().isBot()){
                if(event.getChannel().getName().equals(parser.getAdminChannel()) && !event.getMessage().getEmbeds().isEmpty() && event.getMessage().getEmbeds().get(0).getTitle().equals("Afk-Check Options")){
                    /*
                     We need to trim the reaction id, it is currently in the form <:name:id>
                     and we need the id portion.
                     */

                    String startIcon = parser.getStartIcon();
                    startIcon = startIcon.replace("<","");
                    startIcon = startIcon.replace(">","");
                    String stopIcon = parser.getStopIcon();
                    stopIcon = stopIcon.replace("<","");
                    stopIcon = stopIcon.replace(">","");
                    String voidIcon = parser.getVoidIcon();
                    voidIcon = voidIcon.replace("<","");
                    voidIcon = voidIcon.replace(">","");
                    String cultIcon = parser.getCultIcon();
                    cultIcon = cultIcon.replace("<","");
                    cultIcon = cultIcon.replace(">","");

                    event.getMessage().addReaction(voidIcon).queue();
                    event.getMessage().addReaction(cultIcon).queue();
                    event.getMessage().addReaction(startIcon).queue();
                    event.getMessage().addReaction(stopIcon).queue();
                }

            }


        }
    }

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event){
        if(event.getChannel().getName().equals(parser.getAdminChannel())){
            // Fetching the original message that was sent in the channel to see what reactions have been checked.
            Message message = event.getChannel().getMessageById(event.getMessageId()).complete();
            // Fetching the message reactions to check who has reacted and how many reactions there are.
            List<MessageReaction> reactions = message.getReactions();

            if(event.getReactionEmote().getName().equals("start") && !event.getUser().isBot()){

                System.out.println("Run started by " + event.getUser().getName());
                // We now check to see if the user has forgotten to select a raid type

                for(MessageReaction e : message.getReactions()){

                    event.getChannel().sendMessage(e.getReactionEmote().getEmote().getName()).queue();
                }


                if ((message.getReactions().get(0).getCount() < 2) && (message.getReactions().get(1).getCount() < 2 )){

                    event.getChannel().sendMessage("```Please selected either cult or void and then re-react to the start button!```").queue();
                }else if ((message.getReactions().get(0).getCount() >= 2) && (message.getReactions().get(1).getCount() >= 2 )){

                    event.getChannel().sendMessage("```You cannot select two run types! Please select one and re-react to the start button!```").queue();
                }else{

                    event.getChannel().sendMessage("```Run started.```").queue();
                    // Fetch the AfkTextChannel in the guild to passed to the run start method.
                    TextChannel afkTextChannel = event.getGuild().getTextChannelsByName(parser.getAfkCheckChannel(), true).get(0);
                    String runType;
                    if (message.getReactions().get(0).getCount() >= 2){
                        runType = "void";
                    }else{
                        runType = "cult";
                    }
                    User startUser = event.getUser();

                    startAfkCheck(runType,startUser,afkTextChannel);

                }




            }else if(event.getReactionEmote().getName().equals("stop") && !event.getUser().isBot()){

                // Stop afk check by deleting the start message.
                System.out.println("Run canceled by " + event.getUser().getName());
                message.delete().queue();
            }

        }
    }

    public void startAfkCheck(String runType, User startUser, TextChannel afkTextChannel){
        // Our EmbedBuilder, this is used to build advanced messages to be sent in chat.
        EmbedBuilder eb = new EmbedBuilder();
        // if the cult parameter is provided start a cult check
        if (runType.equals("cult")){
            afkTextChannel.sendMessage("```cult started.```").queue();
            return;
        }
        // if the void parameter is provided start a void check
        else if (runType.equals("void")){
            afkTextChannel.sendMessage("```void started.```").queue();
            return;
        }
    }

}
