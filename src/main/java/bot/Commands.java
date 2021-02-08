package bot;

import java.awt.Color;

import bot.connector.PriceFetcher;
import bot.stock.Stock;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {
	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		try {
			String[] args = event.getMessage().getContentRaw().split("\\s+");
			if (args[0].substring(0, 1).equals(Controller.prefix) && args.length == 1) {
				String ticker = args[0].substring(1,args[0].length());
				long startTime = System.currentTimeMillis();
				event.getChannel().sendMessage(":white_check_mark: Searching for ticker " + ticker + " on Yahoo Finance!").queue();
				
				Stock stock = PriceFetcher.getPrice(ticker);
				if(stock.getChange().equals("error")) {
					EmbedBuilder info = new EmbedBuilder();
					info.setTitle(":x: ERROR");
					info.setDescription("We're having trouble finding: " + ticker);
					info.setColor(Color.red);
					info.setFooter("Created by zuck", event.getMember().getUser().getAvatarUrl());
					event.getChannel().sendMessage(info.build()).queue();
					info.clear();
				}else {
					EmbedBuilder info = new EmbedBuilder();
					info.setTitle(":white_check_mark: SUCCUESS");
					info.setDescription(stock.toString() + "\nTIME: " + (System.currentTimeMillis()-startTime) + "ms");
					info.setColor(Color.green);
					info.setFooter("Created by " + event.getMember().getAsMention(), event.getMember().getUser().getAvatarUrl());
					event.getChannel().sendMessage(info.build()).queue();
					info.clear();
				}
				
			}
		} catch (Exception e) {

		}
	}
}
