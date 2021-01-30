package discraft.database;

import discraft.Discraft;

import java.util.UUID;
import java.util.logging.Logger;
import java.util.Optional;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseHandler {
    private final File dbFile;
    private final Discraft plugin;
    private final Connection conn;
    private final Logger logger;
    public DatabaseHandler(Discraft plugin, File dbFile, Logger logger) throws IOException, SQLException, ClassNotFoundException {
        this.dbFile = dbFile;
        this.plugin = plugin;
        this.logger = logger;

        if (!this.dbFile.exists() && !this.dbFile.createNewFile()) throw new IOException("Error creating Database file");
        
        Class.forName("org.sqlite.JDBC");

        this.conn = DriverManager.getConnection(String.format("jdbc:sqlite:%s", dbFile));
        initialize();
    }

    private void initialize() throws SQLException {
        String query = "CREATE TABLE IF NOT EXISTS mc_whitelisted (discord_id varchar(18) PRIMARY KEY, mc_uuid varchar(255) NOT NULL)";
        try (Statement stmt = this.conn.createStatement()) {
            stmt.execute(query);
        }
    }
    
    public Discraft getPlugin() {
        return this.plugin;
    }

    public Boolean link(String discordID, UUID mcID) {
        String query = String.format(
            "INSERT INTO mc_whitelisted (discord_id, mc_uuid)"
            + "VALUES (\"%s\", \"%s\")",
            discordID, mcID.toString());

        try (Statement stmt = this.conn.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            this.logger.info(e.getMessage());
            return false;
        }

        return true;
    }
    
    public Boolean unlink(String discordID) {
        String query = String.format(
            "DELETE FROM mc_whitelisted WHERE discord_id=\"%s\"",
            discordID
        );

        try (Statement stmt = this.conn.createStatement()) {
            stmt.execute(query);
        } catch (SQLException e) {
            this.logger.info(e.getMessage());
            return false;
        }

        return true;
    }
    
    public Optional<LinkedAccount> getAccount(String discordID) {
        String query = String.format(
            "SELECT * FROM mc_whitelisted WHERE discord_id=\"%s\"",
            discordID
        );

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {
                logger.info("Fetching user..");
                return Optional.of(new LinkedAccount(rs.getString("discord_id"), rs.getString("mc_uuid")));
            }
        } catch (SQLException e) {
            logger.info(e.getMessage());
        }

        return Optional.empty();
    }
}