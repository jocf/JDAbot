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
import java.io.IOException;
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
    protected CmdHandler handler = null;
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
                handler = new CmdHandler(event.getGuild().getEmotes(), parser);

                String sentCommand = event.getMessage().getContentDisplay();
                System.out.println("The user " + event.getAuthor().getName() + " sent the command " + sentCommand + ".");

                // Channel for final afk check to be sent to.
                TextChannel AfkTextChannel = event.getGuild().getTextChannelsByName(parser.getAfkCheckChannel(), false).get(0);
                // Pass the adminChannel and AfkTextChannel to the command handler.
                handler.setAdminChannel(event.getTextChannel());
                handler.setAfkTextChannel(AfkTextChannel);
                if (sentCommand.equals("?afk")) {
                    /*
                    Should link to the AfkCheck class. To be implemented later.
                    We need to fetch the AfkCheck channel name here as well.
                    Call sendAfkCheck method and pass the starting user (user who entered the command).
                     */
                    User startUser = event.getAuthor();
                    handler.preAfkCheck(startUser);
                } else if (sentCommand.equals("?help")) {
                    TextChannel adminTextChannel = event.getTextChannel();
                    // Call sendHelpMessage method.
                    handler.sendHelpMessage();
                } else if (sentCommand.equals("?verify")) {
                    // Do verification stuff here.

                }else if(sentCommand.split(" ")[0].equals("?changeafkchannel")){
                    String[] fullCommand = sentCommand.split(" ");
                    if ((fullCommand.length == 2) && (event.getGuild().getTextChannelsByName(fullCommand[1],false).isEmpty() == false)){
                        try {
                            parser.setAfkCheckChannel(fullCommand[1]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String sentMessage = String.format("```Afk channel successfully changed to %s.```", fullCommand[1]);
                        event.getTextChannel().sendMessage(sentMessage).queue();
                    }else{
                        event.getTextChannel().sendMessage("```You did not specify a new afk channel, or the channel does not exist in the discord!```").queue();
                    }

                }else if(sentCommand.split(" ")[0].equals("?changeadminchannel")){
                    String[] fullCommand = sentCommand.split(" ");
                    if ((fullCommand.length == 2) && (event.getGuild().getTextChannelsByName(fullCommand[1],false).isEmpty() == false)){
                        try {
                            parser.setAdminChannel(fullCommand[1]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String sentMessage = String.format("```Admin channel successfully changed to %s.```", fullCommand[1]);
                        event.getTextChannel().sendMessage(sentMessage).queue();
                    }else{
                        event.getTextChannel().sendMessage("```You did not specify a new admin channel, or the channel does not exist in the discord!```").queue();
                    }

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

    /*
    This is where all message reaction handling takes place.
    This covers two scenarios:
    - Pre-Afk check
    - Afk-Check
     */

    protected int voidCount = 0;
    protected int cultCount = 0;

    @Override
    public void onGuildMessageReactionAdd(GuildMessageReactionAddEvent event){
        // We need to ensure that the only messages that we are performing reaction operations/checks on are those messages that are pre-afk-checks. I.e. not any random message that is in the admin channel.
        if(event.getChannel().getName().equals(parser.getAdminChannel()) && event.getReaction().getChannel().getMessageById(event.getMessageId()).complete().getEmbeds().get(0).getTitle().equals("Afk-Check Options")){
            // Fetching the original message that was sent in the channel to see what reactions have been checked.
            Message message = event.getChannel().getMessageById(event.getMessageId()).complete();
            // Fetching the message reactions to check who has reacted and how many reactions there are.
            List<MessageReaction> reactions = message.getReactions();

            /*
            We have to manually count our void and cult reactions here as the .count() method that is part of the JDA library does not work correctly.
             */

            if(event.getReactionEmote().getName().equals(parser.getVoidName())){
                voidCount++;
            }

            if(event.getReactionEmote().getName().equals(parser.getCultName())){
                cultCount++;
            }

            if(event.getReactionEmote().getName().equals(parser.getStartName()) && !event.getUser().isBot()){
                // We run this check on the off chance the bot lags and a user reacts to start before all reactions have been printed.
                if(!(message.getReactions().size() < 4)) {
                    System.out.println("Run started by " + event.getUser().getName());

                    // System.out.println(voidEmote.getCount());
                    // System.out.println(cultEmote.getCount());


                    // We now check to see if the user has forgotten to select a raid type


                    if ((voidCount < 2) && (cultCount < 2)) {
                        event.getChannel().sendMessage("```You did not select a run type!```").queue();
                        message.delete().queue();
                        voidCount = 0;
                        cultCount = 0;
                        handler.preAfkCheck(event.getUser());
                        return;
                    } else if ((voidCount >= 2) && (cultCount >= 2)) {

                        event.getChannel().sendMessage("```You cannot select two run types!```").queue();
                        message.delete().queue();
                        voidCount = 0;
                        cultCount = 0;
                        handler.preAfkCheck(event.getUser());
                        return;
                    } else {

                        // Fetch the AfkTextChannel in the guild to passed to the run start method.

                        TextChannel afkTextChannel = event.getGuild().getTextChannelsByName(parser.getAfkCheckChannel(), true).get(0);

                        // Run type to be sent to the run start method.

                    /*
                    WE NOW CREATE A NEW AFK CHECK OBJECT FOR EACH AFK CHECK.
                    IF A CURRENT AFK-CHECK OBJECT ALREADY EXISTS,
                    WE STILL CREATE A NEW ONE.
                     */
                        String runType;
                        User startUser = event.getUser();
                        if (voidCount >= 2) {
                            runType = "void";
                            event.getChannel().sendMessage("```Void run started!```").queue();
                            message.delete().queue();
                        } else {
                            runType = "cult";
                            event.getChannel().sendMessage("```Cult run started!```").queue();
                            message.delete().queue();
                        }
                        voidCount = 0;
                        cultCount = 0;

                        AfkCheck afkCheck = new AfkCheck(runType,startUser,afkTextChannel);

                        return;

                    }
                }else{
                    event.getChannel().sendMessage("```Please wait!```").queue();
                }


            }else if(event.getReactionEmote().getName().equals(parser.getStopName()) && !event.getUser().isBot()){

                // Stop afk check by deleting the start message.
                System.out.println("Run canceled by " + event.getUser().getName());
                message.delete().queue();
                event.getChannel().sendMessage("```Run canceled!```").queue();
                return;
            }

        }
    }

}
