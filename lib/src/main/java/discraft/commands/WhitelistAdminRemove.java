package discraft.commands;

import discraft.*;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;

@CommandInfo(
    name = "unwhitelist",
    description = "Admins can remove anyone from whitelist"
)
public class WhitelistAdminRemove extends Command {
    private final Bot bot;
    public WhitelistAdminRemove(Bot bot) {
        this.name = "unwhitelist";
        this.bot = bot;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!this.bot.isAdmin(event.getMember()).booleanValue()) {
            event.replyInDm("Sorry you don't have permission to run this command");
            return;
        }

        this.bot.getPlugin().runCommand(String.format("whitelist remove %s", event.getArgs()));
        event.reply(String.format("Unwhitelisted %s", event.getArgs()));
    }
    
}
