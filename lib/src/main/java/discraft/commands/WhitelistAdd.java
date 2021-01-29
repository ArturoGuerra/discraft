package discraft.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;

import discraft.*;

@CommandInfo(
    name = "whitelistadd",
    description = "Adds user to whitelist"
)
public class WhitelistAdd extends Command {
    private final Bot bot;
    private final Discraft discraft;
    public WhitelistAdd(Bot bot, Discraft discraft) {
        this.name = "whitelistadd";
        this.bot = bot;
        this.discraft = discraft;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!this.bot.isAllowed(event.getMember()).booleanValue()) {
            event.replyInDm("Sorry you don't have permission to run this command");
            return;
        }

        String username = event.getArgs();
        if (username.length() == 0) {
            event.replyInDm("Invalid username");
            return;
        }

        if (this.discraft.setMCUsername(event.getMember(), username).booleanValue()) {
            discraft.whitelistAdd(event.getArgs());
            event.reply(String.format("Whitelisted: %s", username));
        } else {
            event.reply("It appear you have already whitelisted yourself, contact an admin if this is a mistake");
        }
    }
}
