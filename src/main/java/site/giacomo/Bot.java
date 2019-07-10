package site.giacomo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.dv8tion.jda.client.entities.Group;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.PermissionException;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import java.awt.Color;

import javax.security.auth.login.LoginException;

public class Bot extends ListenerAdapter {
    // Bot startup in main method. authToken is passed from the config file which is parsed in the Main class.
    // Creating our parser object.
    PropParser parser = new PropParser();
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
    public void onMessageReceived(MessageReceivedEvent event){
        // Generating our file properties.
        parser.generateProperties();
        // Create our CmdHandler object to handle commands.
        CmdHandler handler = new CmdHandler();
        // Fetching the bot object from the event received.
        JDA bot = event.getJDA();
        // Now, we will check if the event is coming from a text channel within the discord guild.
        if (event.isFromType(ChannelType.TEXT)){
            // Next, we need to determine if the channel is the specified adminChannel that commands should be sent to.
            // If this was in an admin channel, we will be handling ALL admin commands here.
            if (event.getChannel().getName().equals(parser.getAdminChannel())){
                // If not bot print user entered commands.
                if (!event.getAuthor().isBot()){
                    System.out.println("Message " + event.getMessage().getContentDisplay() + " was sent in admin channel by the user " + event.getAuthor().getName());
                }
                if (event.getMessage().getContentDisplay().split(" ")[0].equals("!afk")){
                    // Should link to the AfkCheck class. To be implemented later.
                    // We need to fetch the AfkCheck channel name here as well.
                    // Call sendAfkCheck method.
                    TextChannel AfkTextChannel = event.getGuild().getTextChannelsByName(parser.getAfkCheckChannel(),true).get(0);
                    if (event.getMessage().getContentDisplay().split(" ").length >= 2) {
                        handler.sendAfkCheck(AfkTextChannel, event.getTextChannel(), event.getMessage().getContentDisplay().split(" ")[1]);
                    }else{
                        event.getTextChannel().sendMessage("```Please enter the command with the correct syntax.```").queue();
                    }

                }else if(event.getMessage().getContentDisplay().equals("!help")){
                    TextChannel adminTextChannel = event.getTextChannel();
                    // Call sendHelpMessage method.
                    handler.sendHelpMessage(adminTextChannel);
                }
            }
        }
    }

}
