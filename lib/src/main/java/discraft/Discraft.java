/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package discraft;


import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;
import java.util.logging.Logger;
import java.util.Optional;
import java.util.UUID;
import java.util.HashMap;

public class Discraft extends JavaPlugin {
    private final String whitelistKey = "whitelist.%s";
    private FileConfiguration config = getConfig();
    private Logger logger = getLogger();
    private Bot bot;

    public Discraft() {
        this.bot = null;
    }

    @Override
    public void onEnable() {
        defaultConfig();
        this.bot = new Bot(this, config, logger);
        this.bot.run();
    }

    @Override
    public void onDisable() {
        if (this.bot == null) return;
        this.bot.shutdown();
        // pain
    }

    private void defaultConfig() {
        config.addDefault("token", "discord token");
        config.addDefault("prefix", "!");
        config.addDefault("logs-channel", "discord channel id");
        config.addDefault("owner", "owner discord id");
        config.addDefault("admin-roles", List.of("role id"));
        config.addDefault("allowed-roles", List.of("role id"));
        config.addDefault("whitelisted", new HashMap<String, String>());
        config.options().copyDefaults(true);
        saveConfig();
        // pain
    }

    public void runCommand(String command) {
        getServer().getScheduler().runTask(this, () -> {
            getServer().dispatchCommand(getServer().getConsoleSender(), command);
        });
    }

    /*
    Adds user to whitelist
    */
    public void whitelistAdd(String name) {
        runCommand(String.format("whitelist add %s", name));
    }

    /*
    Removes user from whitelist
    */
    public void whitelistRemove(String name) {
        runCommand(String.format("whitelist remove %s", name));
    }
    

    @SuppressWarnings("deprecation")
    public Optional<String> getMCUsername(Member member) {
        String dbkey = String.format(whitelistKey, member.getId());
        if (dbkey == null) return Optional.empty();
        String strUUID = config.getString(dbkey);
        if (strUUID == null) return Optional.empty();
        OfflinePlayer player = getServer().getOfflinePlayer(strUUID);
        return (player == null) ? Optional.empty() : Optional.of(player.getName());
    }

    @SuppressWarnings("deprecation")
    public Boolean setMCUsername(Member member, String username) {
        String dbkey = String.format(whitelistKey, member.getId());
        if (config.getString(dbkey) == null) {
            OfflinePlayer player = getServer().getOfflinePlayer(username);
            if (player != null) {
                UUID playerid = player.getUniqueId();
                config.set(dbkey, playerid.toString());
                saveConfig();
                return true;
            }
        }

        return false;
    }

    public void delWhitelist(Member member) {
        config.set(String.format(whitelistKey, member.getId()), null);
        saveConfig();
    }
}