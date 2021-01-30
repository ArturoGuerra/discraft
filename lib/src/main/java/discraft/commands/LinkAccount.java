package discraft.commands;

import java.util.UUID;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.jagrosh.jdautilities.doc.standard.CommandInfo;

import org.bukkit.OfflinePlayer;

import discraft.*;

@CommandInfo(
    name = "link",
    description = "Adds user to whitelist"
)
public class LinkAccount extends Command {
    private final Bot bot;
    public LinkAccount(Bot bot) {
        this.name = "link";
        this.bot = bot;
    }

    @Override
    @SuppressWarnings("deprecation")
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

        OfflinePlayer player = this.bot.getPlugin().getServer().getOfflinePlayer(username);

        if (this.bot.getDB().link(event.getMember().getId(), player.getUniqueId()).booleanValue()) {
            player.setWhitelisted(true);
            event.reply(String.format("Whitelisted: %s", username));
        } else {
            event.reply("It appear you have already whitelisted yourself, contact an admin if this is a mistake");
        }
    }
}
