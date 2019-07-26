package site.giacomo;

import net.dv8tion.jda.core.entities.User;
import org.apache.commons.lang3.RandomStringUtils;

public class UserVerifier{
    public String verificationCode;
    User startUser;

    public UserVerifier(User startUser){
        // Generate the users unique verification code.
        this.verificationCode = RandomStringUtils.random(8,true,false);
        this.startUser = startUser;
    }

    public int startVerify(){
        return 0;
    }

    public String getCode(){
        return verificationCode;
    }
}
