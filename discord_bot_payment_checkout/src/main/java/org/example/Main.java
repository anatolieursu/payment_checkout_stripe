package org.example;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.example.Commands.CreateCommand;
import org.example.Commands.GetPayments;


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
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();
        Guild guild = jda.getGuildById(1096672836153782362L);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getMessage().getAuthor().isBot()) return;
        if(event.getMessage().getContentRaw() .equals("create")) {
            System.out.println("create");
        }
    }
}