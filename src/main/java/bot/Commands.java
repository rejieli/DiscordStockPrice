package bot;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bot.connector.PriceFetcher;
import bot.stock.Stock;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Commands extends ListenerAdapter {

	HashMap<Member, Long> mem = new HashMap<Member, Long>();

//	List<Member> mem = new ArrayList<Member>();

	public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
		try {
			// Timer
			checkTimer();

			String[] args = event.getMessage().getContentRaw().split("\\s+");
			// Stocks
			if (args[0].substring(0, 1).equals(Controller.prefix) && args.length == 1) {
				String ticker = args[0].substring(1, args[0].length());
				long startTime = System.currentTimeMillis();
				event.getChannel()
						.sendMessage(":white_check_mark: Searching for ticker " + ticker + " on Yahoo Finance!")
						.queue();

				Stock stock = PriceFetcher.getPrice(ticker);
				if (stock.getChange().equals("error")) {
					EmbedBuilder info = new EmbedBuilder();
					info.setTitle(":x: ERROR");
					info.setDescription("We're having trouble finding: " + ticker);
					info.setColor(Color.red);
					info.setFooter("Created by " + event.getMember().getEffectiveName(),
							event.getMember().getUser().getAvatarUrl());
					event.getChannel().sendMessage(info.build()).queue();
					info.clear();
				} else {
					EmbedBuilder info = new EmbedBuilder();
					info.setTitle(":white_check_mark: SUCCUESS");
					info.setDescription(
							stock.toString() + "\nTIME: " + (System.currentTimeMillis() - startTime) + "ms");
					info.setColor(Color.green);
					info.setFooter("Created by " + event.getMember().getEffectiveName(),
							event.getMember().getUser().getAvatarUrl());
					event.getChannel().sendMessage(info.build()).queue();
					info.clear();
				}

			}

			// @er
			if (args[0].equals("!") && args.length == 2) {
				List<Member> member = event.getMessage().getMentionedMembers();
				if (member.size() == 1) {
					// Check
					if (!checkMem(event.getMember())) {
						for (int i = 0; i < 2; i++) {
							event.getChannel()
									.sendMessage(member.get(0).getAsMention() + " " + member.get(0).getAsMention()
											+ "\n" + member.get(0).getAsMention() + " " + member.get(0).getAsMention()
											+ "\n" + member.get(0).getAsMention() + " " + member.get(0).getAsMention()
											+ "\n" + member.get(0).getAsMention() + " " + member.get(0).getAsMention()
											+ "\n" + member.get(0).getAsMention() + " " + member.get(0).getAsMention()
											+ "\n" + member.get(0).getAsMention() + " " + member.get(0).getAsMention())
									.queue();
						}
						mem.put(event.getMember(), System.currentTimeMillis());
						event.getChannel().sendMessage(event.getMember().getAsMention() + " NEEDS YOU!").queue();
					} else {
						event.getChannel().sendMessage("Please wait.. Don't Spam\n"
								+ (System.currentTimeMillis() - mem.get(event.getMember())) + "ms out of 10000ms")
								.queue();
					}
				} else {
					event.getChannel().sendMessage("Please @ 1 person").queue();
				}

			}

			// Audit
			if (args[0].equals("!audit")) {
				List<AuditLogEntry> audit = event.getGuild().retrieveAuditLogs().complete();
				EmbedBuilder info = new EmbedBuilder();
				StringBuilder sb = new StringBuilder();

				info.setTitle("AUDIT LOG (TOP IS MOST RECENT)");
				info.setColor(Color.blue);
				for (int i = 0; i < audit.size() && i < 5; i++) {
					// Formatting

					sb.append(
							"USER: " + audit.get(i).getUser().getName());
					sb.append("\nTYPE: " + audit.get(i).getType());
					sb.append("\nTARGET TYPE: " + audit.get(i).getTargetType());
					if (audit.get(i).getOptions().values().size() == 2) {
						sb.append("\nDETAILS: " + audit.get(i).getOptions().values().toArray()[1] + " -- "
								+ event.getGuild()
										.getGuildChannelById(Long
												.parseLong(audit.get(i).getOptions().values().toArray()[0].toString()))
										.getName());
					}
					sb.append("\n---------------------------------------------------\n");
					
				}
				info.setDescription(sb.toString());
				info.setFooter("REQUEST BY: " + event.getMember().getEffectiveName(),
						event.getMember().getUser().getAvatarUrl());
				event.getChannel().sendMessage(info.build()).queue();
				info.clear();
			}
		} catch (Exception e) {

		}
	}

	private void checkTimer() {
		for (Member me : mem.keySet()) {
			if ((System.currentTimeMillis() - (mem.get(me))) > 10000) {
				mem.remove(me);
			}
		}

	}

	private boolean checkMem(Member member) {
		for (Member me : mem.keySet()) {
			if (me.equals(member)) {
				return true;
			}
		}
		return false;
	}

}
