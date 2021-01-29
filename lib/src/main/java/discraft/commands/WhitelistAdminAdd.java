package discraft.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;

import discraft.*;

@CommandInfo(
    name = "whitelistadminadd",
    description = "Admins can whitelist whoever they want"
)
public class WhitelistAdminAdd extends Command {
    private final Bot bot;
    private final Discraft discraft;
    public WhitelistAdminAdd(Bot bot, Discraft discraft) {
        this.name = "whitelistadminadd";
        this.bot = bot;
        this.discraft = discraft;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!this.bot.isAdmin(event.getMember()).booleanValue()) {
            event.replyInDm("Sorry you don't have permission to run this command");
            return;
        }
        discraft.whitelistAdd(event.getArgs());
        event.reply(String.format("Whitelisted %s", event.getArgs()));
    }    
}
