package discraft.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;

import discraft.*;

import java.util.Optional;

@CommandInfo(
    name = "whitelistremove",
    description = "Removes themselves from whitelist"
)
public class WhitelistRemove extends Command {
    private final Bot bot;
    private final Discraft discraft;
    public WhitelistRemove(Bot bot, Discraft discraft) {
        this.name = "whitelistremove";
        this.bot = bot;
        this.discraft = discraft;
    }
    @Override
    protected void execute(CommandEvent event) {
        if (!this.bot.isAllowed(event.getMember()).booleanValue()) {
            event.replyInDm("Sorry you don't have permission to run this command");
            return;
        }

        Optional<String> mcusername = this.discraft.getMCUsername(event.getMember());
        if (mcusername.isPresent()) {
            discraft.whitelistRemove(mcusername.get());
            discraft.delWhitelist(event.getMember());
            event.reply(String.format("Unwhitelisted %s", mcusername.get()));
        } else {
            event.reply("Sorry but you are not currently whitelisted");
        }

    }
    
}
