package discraft;

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
    private JDA dClient;
    private JDABuilder builder;
    private String owner = "";
    private Logger logger;
    private List<String> adminRoles = new ArrayList<>();
    private List<String> allowedRoles = new ArrayList<>();
    public Bot(Discraft discraft, FileConfiguration config, Logger logger) {
        this.logger = logger;
        this.owner = config.getString("owner");
        this.adminRoles = config.getStringList("admin-roles");
        this.allowedRoles = config.getStringList("allowed-roles");

        CommandClientBuilder cClient = new CommandClientBuilder();

        cClient.setOwnerId("411323761116184578");
        cClient.setPrefix(config.getString("prefix"));

        cClient.addCommands(
            new WhitelistAdminAdd(this, discraft),
            new WhitelistAdminRemove(this, discraft),
            new WhitelistAdd(this, discraft),
            new WhitelistRemove(this, discraft));

        this.builder = JDABuilder.create(config.getString("token"), GatewayIntent.getIntents(GatewayIntent.ALL_INTENTS));
        this.builder.addEventListeners(cClient.build());
    }

    public void run() {
        try {
            this.dClient = this.builder.build();
            this.dClient.awaitReady();
        } catch (LoginException | InterruptedException e) {
            this.logger.info(e.getMessage());
        }
    }

    public void shutdown() {
        if (this.dClient == null) return;
        this.dClient.shutdownNow();
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
