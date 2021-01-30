package discraft;

import discraft.database.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import com.jagrosh.jdautilities.command.CommandClientBuilder;

import org.bukkit.configuration.file.FileConfiguration;

import discraft.commands.*;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.JDA;

public class Bot {
    private final String owner;
    private JDA client;
    private Logger logger;
    private Discraft plugin;
    private DatabaseHandler db;
    private List<String> adminRoles = new ArrayList<>();
    private List<String> allowedRoles = new ArrayList<>();

    public Bot(Discraft plugin, DatabaseHandler db, FileConfiguration config, Logger logger) throws LoginException, InterruptedException {
        this.plugin = plugin;
        this.logger = logger;
        this.db = db;
        this.owner = config.getString("owner");
        this.adminRoles = config.getStringList("admin-roles");
        this.allowedRoles = config.getStringList("allowed-roles");
        
        CommandClientBuilder commands = new CommandClientBuilder();

        commands.setOwnerId(config.getString("owner"));
        commands.setPrefix(config.getString("prefix"));

        commands.addCommands(
            new WhitelistAdminAdd(this),
            new WhitelistAdminRemove(this),
            new LinkAccount(this),
            new UnlinkAccount(this)
        );

        JDABuilder builder = JDABuilder.create(config.getString("token"), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));
        builder.addEventListeners(commands.build());

        this.client = builder.build();
        this.client.awaitReady();
        logger.info("Discord bot started");
    }
    
    public void stop() {
        this.client.shutdownNow();
    }
    
    public Discraft getPlugin() {
        return this.plugin;
    }

    public Logger getLogger() {
        return this.logger;
    }

    public JDA getJDA() {
        return this.client;
    }

    public DatabaseHandler getDB() {
        return this.db;
    }

    public Boolean isAllowed(Member member) {
        if (this.isAdmin(member).booleanValue() || this.isOwner(member).booleanValue()) return true;

        List<Role> roles = member.getRoles();
        for (int i = 0; i < roles.size(); i++) {
            if (this.allowedRoles.contains(roles.get(i).getId())) return true;
        }
    
        return false;
    }

    public Boolean isAdmin(Member member) {
        if (this.isOwner(member).booleanValue()) return true;

        List<Role> roles = member.getRoles();
        for (int i = 0; i < roles.size(); i++) {
            if (this.adminRoles.contains(roles.get(i).getId())) return true;
        }

        return false;
    }

    public Boolean isOwner(Member member) {
        if (owner.equals(member.getId())) return true;
        return false;
    }    
}
