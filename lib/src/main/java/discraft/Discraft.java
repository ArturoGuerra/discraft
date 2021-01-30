/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package discraft;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;

import javax.security.auth.login.LoginException;

import java.util.Optional;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

import discraft.database.DatabaseHandler;

public class Discraft extends JavaPlugin {
    private FileConfiguration config = getConfig();
    private Logger logger = getLogger();
    private Optional<Bot> bot;

    public Discraft() {
        this.bot = Optional.empty();
    }

    @Override
    public void onEnable() {
        defaultConfig();

        DatabaseHandler db;
        try {
            db = new DatabaseHandler(this, new File(getDataFolder(), "discraft.db"), logger);
        } catch (SQLException | IOException | ClassNotFoundException e) {
            logger.info(e.getMessage());
            return;
        }

        try {
            logger.info("Starting bot..");
            this.bot = Optional.of(new Bot(this, db, config, logger));
        } catch (LoginException | InterruptedException e) {
            this.logger.info(e.getMessage());
        }
    }

    @Override
    public void onDisable() {
        if (this.bot.isPresent()) this.bot.get().stop();
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
    }

    public void runCommand(String command) {
        getServer().getScheduler().runTask(this, () -> {
            getServer().dispatchCommand(getServer().getConsoleSender(), command);
        });
    }

}
