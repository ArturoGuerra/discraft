package discraft.commands;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;

import org.bukkit.OfflinePlayer;

import discraft.database.LinkedAccount;
import discraft.Bot;
import java.util.Optional;
import java.util.UUID;

@CommandInfo(
    name = "unlink",
    description = "Removes themselves from whitelist"
)
public class UnlinkAccount extends Command {
    private final Bot bot;
    public UnlinkAccount(Bot bot) {
        this.name = "unlink";
        this.bot = bot;
    }
    @Override
    protected void execute(CommandEvent event) {
        if (!this.bot.isAllowed(event.getMember()).booleanValue()) {
            event.replyInDm("Sorry you don't have permission to run this command");
            return;
        }
        
        Optional<LinkedAccount> account = this.bot.getDB().getAccount(event.getMember().getId());
        if (account.isPresent()) {
            OfflinePlayer player = this.bot.getPlugin().getServer().getOfflinePlayer(UUID.fromString(account.get().getMinecraft()));
            player.setWhitelisted(false);

            this.bot.getDB().unlink(event.getMember().getId());
            event.reply("You have been unwhitelisted, you can now whitelist a different account!");
            return;
        }


        event.reply("Sorry but you are not currently whitelisted");

    }
    
}
