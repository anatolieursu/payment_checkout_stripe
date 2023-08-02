package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.channel.ChannelManager;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.Commands.CreateCommand;
import org.example.Commands.GetPayments;
import org.example.Commands.ticketPayments.CreateTicket;
import org.example.Commands.ticketPayments.UpdateTicket;


public class Main extends ListenerAdapter {
    public static JDA jda;
    public static void main( String[] args )
    {
        jda = JDABuilder.createDefault("TOKEN")
                .setActivity(Activity.playing("orice"))
                .addEventListeners(new Main())
                .addEventListeners(new CreateCommand())
                .addEventListeners(new AllCommands())
                .addEventListeners(new GetPayments())
                .addEventListeners(new CreateTicket())
                .addEventListeners(new UpdateTicket())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        Guild guild = jda.getGuildById(1096672836153782362L);
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Your Bot is ready");
    }

    public static void changeChannelName(TextChannel theChannel, String name) {
        ChannelManager manager = theChannel.getManager();
        String principalName = theChannel.getName().replace("payment-", "");
        principalName = principalName.replace("waiting-", "");
        manager.setName(name + "-" + principalName).queue();
    }
}