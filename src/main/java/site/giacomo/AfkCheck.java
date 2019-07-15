package site.giacomo;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class AfkCheck extends Bot{

    protected String runType;
    protected User startUser;
    protected TextChannel afkTextChannel;

    public AfkCheck(String runType, User startUser, TextChannel afkTextChannel){
        this.runType = runType;
        this.startUser = startUser;
        this.afkTextChannel = afkTextChannel;
    }






}
