package me.borawski.hcf;import java.io.File;import java.util.UUID;import org.bukkit.Bukkit;import org.bukkit.ChatColor;import org.bukkit.command.CommandExecutor;import org.bukkit.configuration.file.YamlConfiguration;import org.bukkit.entity.Player;import org.bukkit.event.player.PlayerJoinEvent;import org.bukkit.plugin.Plugin;import org.bukkit.plugin.java.JavaPlugin;import com.sk89q.worldedit.bukkit.WorldEditPlugin;import me.borawski.hcf.api.FileHandler;import me.borawski.hcf.api.LangHandler;import me.borawski.hcf.barrier.TagHandler;import me.borawski.hcf.command.CustomCommandHandler;import me.borawski.hcf.command.commands.base.LivesCommand;import me.borawski.hcf.command.commands.base.RankCommand;import me.borawski.hcf.connection.MongoWrapper;import me.borawski.hcf.listener.ChatListener;import me.borawski.hcf.listener.CombatListener;import me.borawski.hcf.listener.ConnectionListener;import me.borawski.hcf.listener.ListenerManager;import me.borawski.hcf.listener.MovementListener;import me.borawski.hcf.manual.ManualManager;import me.borawski.hcf.punishment.PunishmentHandler;import me.borawski.hcf.session.AchievementManager;import me.borawski.hcf.session.FactionSessionHandler;import me.borawski.hcf.session.RegionHandler;import me.borawski.hcf.session.SessionHandler;import me.borawski.hcf.util.ItemDb;import me.borawski.hcf.util.ManualUtil;/** * Created by Ethan on 3/8/2017. */public class Core extends JavaPlugin implements CommandExecutor {    private static final UUID CONSOLE = UUID.fromString("6afde421-9fbf-4dab-a9c3-87698280b2c5");    /**     * Instance     */    private static Core instance;    /**     * Variables     */    private static ListenerManager listenerManager;    private static CustomCommandHandler customCommandHandler;    private static PunishmentHandler punishmentHandler;    private static SessionHandler sessionHandler;    private static AchievementManager achievementManager;    private static ManualManager manualManager;    private static MongoWrapper mongoWrapper;    private static LangHandler lang;    private static FileHandler config;    private static RegionHandler regionHandler;    private static ItemDb itemHandler;        /**     * Components     */    private Components components;    @Override    public void onEnable() {        instance = this;        saveDefaultConfig();        saveResource("lang.yml", false);        config = new FileHandler(getConfig());        lang = new LangHandler(YamlConfiguration.loadConfiguration(new File(getDataFolder(), "lang.yml")));        saveResource("items.csv", false);        itemHandler = new ItemDb();        mongoWrapper = new MongoWrapper();        SessionHandler.initialize();        PunishmentHandler.initialize();        FactionSessionHandler.initialize();        RegionHandler.initialize();        TagHandler.initialize();        listenerManager = new ListenerManager();        listenerManager.addListener(new ConnectionListener());        listenerManager.addListener(new ChatListener());        listenerManager.addListener(new MovementListener());        listenerManager.addListener(new CombatListener());        customCommandHandler = new CustomCommandHandler();        registerCommands();        achievementManager = new AchievementManager();        manualManager = new ManualManager();        registerManuals();        this.components = new Components();        components.onEnable();        for (Player p : Bukkit.getOnlinePlayers()) {            Bukkit.getPluginManager().callEvent(new PlayerJoinEvent(p, ""));        }    }    /**     * Gets the singleton instance of the Core class.     *      * @return     */    public static Core getInstance() {        return instance;    }    public void registerCommands() {        customCommandHandler.registerCommand(new RankCommand());        customCommandHandler.registerCommand(new LivesCommand());    }    public void registerManuals() {        ManualUtil.initializeManuals(manualManager.getManualMap());    }    /**     * Gets the instance of MongoDB maintained by the plugin.     *      * @return     */    public MongoWrapper getMongoWrapper() {        return mongoWrapper;    }    /**     * Gets the instance of the listener manager. Used to add more listeners or     * update existing ones.     *      * @return     */    public ListenerManager getListenerManager() {        return listenerManager;    }    public CustomCommandHandler getCommandHandler() {        return customCommandHandler;    }    public PunishmentHandler getPunishmentHandler() {        return punishmentHandler;    }    public SessionHandler getSessionHandler() {        return sessionHandler;    }    public AchievementManager getAchievementManager() {        return achievementManager;    }    public ManualManager getManualManager() {        return manualManager;    }    public static LangHandler getLangHandler() {        return lang;    }    public static FileHandler getConfigHandler() {        return config;    }    public static RegionHandler getRegionHandler() {        return regionHandler;    }    public String getPrefix() {        return ChatColor.DARK_RED + "" + ChatColor.BOLD + "DesireHCF" + ChatColor.RESET + "" + ChatColor.GRAY + " ";    }    public static UUID getConsoleUUID() {        return CONSOLE;    }    public static WorldEditPlugin getWorldEdit() {        Plugin p = Bukkit.getPluginManager().getPlugin("WorldEdit");        if (p == null) {            System.out.println("This code would crash if that was null. If this message runs there is a serious problem. Kappa.");            return null;        }        return (WorldEditPlugin) p;    }    public static ItemDb getItemHandler() {        return itemHandler;    }}