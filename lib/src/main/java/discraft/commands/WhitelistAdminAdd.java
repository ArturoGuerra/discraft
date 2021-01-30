package discraft.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;

import discraft.*;

@CommandInfo(
    name = "whitelist",
    description = "Admins can whitelist whoever they want"
)
public class WhitelistAdminAdd extends Command {
    private final Bot bot;
    public WhitelistAdminAdd(Bot bot) {
        this.name = "whitelist";
        this.bot = bot;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!this.bot.isAdmin(event.getMember()).booleanValue()) {
            event.replyInDm("Sorry you don't have permission to run this command");
            return;
        }
        this.bot.getPlugin().runCommand(String.format("whitelist add %s", event.getArgs()));
        event.reply(String.format("Whitelisted %s", event.getArgs()));
    }    
}
