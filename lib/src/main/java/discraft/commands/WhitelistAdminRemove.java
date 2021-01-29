package discraft.commands;

import discraft.*;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;

@CommandInfo(
    name = "whitelistadminremove",
    description = "Admins can remove anyone from whitelist"
)
public class WhitelistAdminRemove extends Command {
    private final Bot bot;
    private final Discraft discraft;
    public WhitelistAdminRemove(Bot bot, Discraft discraft) {
        this.name = "whitelistadminremove";
        this.bot = bot;
        this.discraft = discraft;
    }

    @Override
    protected void execute(CommandEvent event) {
        if (!this.bot.isAdmin(event.getMember()).booleanValue()) {
            event.replyInDm("Sorry you don't have permission to run this command");
            return;
        }

        discraft.whitelistRemove(event.getArgs());
        event.reply(String.format("Unwhitelisted %s", event.getArgs()));
    }
    
}
