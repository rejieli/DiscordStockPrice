package bot;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;


public class Controller {
    public static String prefix = "$";

    public static void main(String[] args) throws LoginException {
    	JDA jda = JDABuilder.createDefault("ODA4MDYxOTc0MTI5ODAzMzI0.YCBEWw.FrvdF2TZk3Loqr8gJ1f-gJfu0fk").build();
    	jda.getPresence().setStatus(OnlineStatus.ONLINE);
    	System.out.println("BOOTING...");
    	jda.getPresence().setActivity(Activity.watching("Stocks"));
    	jda.addEventListener(new Commands());
    }
}
