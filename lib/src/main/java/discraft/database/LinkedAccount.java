package discraft.database;

public class LinkedAccount {
    private final String discord;
    private final String minecraft;

    public LinkedAccount(String discord, String minecraft) {
        this.discord = discord;
        this.minecraft = minecraft;
    }

    public String getDiscord() {
        return this.discord;
    }

    public String getMinecraft() {
        return this.minecraft;
    }
    
}
