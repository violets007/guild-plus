package cn.dawntribe.guild;

import cn.dawntribe.guild.command.AdminCommand;
import cn.dawntribe.guild.command.GuildCommand;
import cn.dawntribe.guild.gui.WindowType;
import cn.dawntribe.guild.gui.ext.*;
import cn.dawntribe.guild.gui.ext.admin.GuildAdminWindow;
import cn.dawntribe.guild.gui.temp.ConfirmWindow;
import cn.dawntribe.guild.gui.temp.MessageWindow;
import cn.dawntribe.guild.listener.GuildListener;
import cn.dawntribe.guild.listener.ResponseLister;
import cn.dawntribe.guild.listener.WarListener;
import cn.dawntribe.guild.task.Watchdog;
import cn.dawntribe.guild.utils.GuildUtils;
import cn.dawntribe.guild.utils.Language;
import cn.dawntribe.guild.utils.Utils;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;

/**
 * @author violets007
 * @version 1.0
 * @description: 公会插件基类
 * @date 2022/2/24 12:17 AM
 */
public class GuildPlugin extends PluginBase {

    public static final String VERSION = "1.0";
    private static GuildPlugin instance = null;
    private Config config;
    private Config warConfig;
    private Language lang;
    private Config windowConfig;

    @Override
    public void onEnable() {
        if (instance == null) instance = this;
        registerListener();
        loadConfig();
        registerCommand();
        registerWindow();
        getLogger().info("当前插件版本: " + VERSION + " 作者: violets007");
        getServer().getScheduler().scheduleRepeatingTask(new Watchdog(this), 20);
    }

    public void loadConfig() {
        String configPath = getDataFolder() + File.separator + "config.yml";
        checkConfigVersion(configPath, VERSION, "config.yml");
        this.config = new Config(configPath, Config.YAML);

        String langPath = getDataFolder() + File.separator + "lang" + File.separator + this.config.getString("zh_CN.yml", "zh_CN.yml");
        checkConfigVersion(langPath, VERSION, "zh_CN.yml");
        String windowConfigPath = getDataFolder() + File.separator + "window-config.yml";
        checkConfigVersion(windowConfigPath, VERSION, "window-config.yml");
        String warConfigPath = getDataFolder() + File.separator + "guild-war.yml";
        checkConfigVersion(warConfigPath, VERSION, "guild-war.yml");


        this.lang = new Language(new Config(langPath, Config.YAML));
        this.windowConfig = new Config(windowConfigPath, Config.YAML);
        this.warConfig = new Config(warConfigPath, Config.YAML);
        GuildUtils.loadGuild();
    }

    public void checkConfigVersion(String path, String version, String fileName) {
        File file = new File(path);
        if (!file.exists()) {
            saveResource(fileName, true);
        } else {
            Config config = new Config(path, Config.YAML);
            if (!config.getString(version).equals(fileName)) {

                DumperOptions dumperOptions = new DumperOptions();
                dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
                Yaml yaml = new Yaml(dumperOptions);
                StringBuilder content = new StringBuilder(yaml.dump(config));
                File bakFile = new File(file.getParentFile().getAbsolutePath() + File.separator + fileName + ".bak");
                Utils.createJsonFile(content.toString(), bakFile.getAbsolutePath());

                getLogger().info("检测到: " + fileName + " 版本号不匹配, 将会被覆盖,已经备份到: " + bakFile.getAbsolutePath());
                saveResource(fileName, true);

            }
        }

    }


    public void registerListener() {
        getServer().getPluginManager().registerEvents(new GuildListener(), this);
        getServer().getPluginManager().registerEvents(new ResponseLister(), this);
        getServer().getPluginManager().registerEvents(new WarListener(), this);
    }

    public void registerCommand() {
        String adminCommandStr = this.config.getString("admin-command");
        String guildCommandStr = this.config.getString("guild-command");

        getServer().getCommandMap().register("", new AdminCommand(adminCommandStr));
        getServer().getCommandMap().register("", new GuildCommand(guildCommandStr));
    }

    public void registerWindow() {
        GuildUtils.addWindowClass(WindowType.GUILD_WINDOW, GuildWindow.class);
        GuildUtils.addWindowClass(WindowType.CREATE_GUILD_WINDOW, CreateGuildWindow.class);
        GuildUtils.addWindowClass(WindowType.PRESIDENT_WINDOW, PresidentWindow.class);
        GuildUtils.addWindowClass(WindowType.MEMBER_WINDOW, MemberWindow.class);
        GuildUtils.addWindowClass(WindowType.GUILD_LIST_WINDOW, GuildListWindow.class);
        GuildUtils.addWindowClass(WindowType.GUILD_CONTRIBUTION_RANKING_WINDOW, GuildContributeRankingWindow.class);
        GuildUtils.addWindowClass(WindowType.GUILD_LEVEL_RANKING_WINDOW, GuildLevelRankingWindow.class);
        GuildUtils.addWindowClass(WindowType.GUILD_VICTORY_RANKING_WINDOW, GuildVictoryRankingWindow.class);
        GuildUtils.addWindowClass(WindowType.MANAGE_APPLY_TEMP_LIST_WINDOW, ManageApplyTempListWindow.class);
        GuildUtils.addWindowClass(WindowType.MANAGE_WAREHOUSE_WINDOW, ManageWarehouseWindow.class);
        GuildUtils.addWindowClass(WindowType.SELECT_WAREHOUSE_WINDOW, SelectWarehouseWindow.class);
        GuildUtils.addWindowClass(WindowType.GUILD_MEMBER_LIST_WINDOW, GuildMemberListWindow.class);
        GuildUtils.addWindowClass(WindowType.MANAGE_MEMBER_LIST_WINDOW, ManageMemberListWindow.class);
        GuildUtils.addWindowClass(WindowType.MANAGE_MEMBER_POST_WINDOW, ManageMemberPostWindow.class);
        GuildUtils.addWindowClass(WindowType.CONTRIBUTION_WINDOW, ContributionWindow.class);
        GuildUtils.addWindowClass(WindowType.GUILD_PRODUCT_LIST_WINDOW, GuildProductListWindow.class);
        GuildUtils.addWindowClass(WindowType.EDIT_GUILD_DESCRIPTION, EditGuildDescriptionWindow.class);
        GuildUtils.addWindowClass(WindowType.GUILD_DESCRIPTION_WINDOW, GuildDescriptionWindow.class);
        GuildUtils.addWindowClass(WindowType.MANAGE_PRODUCT_WINDOW, ManageProductWindow.class);
        GuildUtils.addWindowClass(WindowType.UPLOAD_PRODUCT_WINDOW, UploadProductWindow.class);
        GuildUtils.addWindowClass(WindowType.UNLOAD_PRODUCT_WINDOW, UnloadProductWindow.class);
        GuildUtils.addWindowClass(WindowType.PRESIDENT_UNLOAD_PRODUCT_WINDOW, PresidentUnloadProductWindow.class);
        GuildUtils.addWindowClass(WindowType.SEND_GUILD_WAR_WINDOW, SendGuildWarWindow.class);
        GuildUtils.addWindowClass(WindowType.GUILD_ADMIN_WINDOW, GuildAdminWindow.class);
        GuildUtils.addWindowClass(WindowType.MESSAGE_WINDOW, MessageWindow.class);
        GuildUtils.addWindowClass(WindowType.CONFIRM_WINDOW, ConfirmWindow.class);
    }

    @Override
    public void onDisable() {
    }

    public static GuildPlugin getInstance() {
        return instance;
    }

    public static void setInstance(GuildPlugin instance) {
        GuildPlugin.instance = instance;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public Config getWarConfig() {
        return warConfig;
    }

    public void setWarConfig(Config warConfig) {
        this.warConfig = warConfig;
    }

    public Language getLang() {
        return lang;
    }

    public void setLang(Language lang) {
        this.lang = lang;
    }

    public Config getWindowConfig() {
        return windowConfig;
    }

    public void setWindowConfig(Config windowConfig) {
        this.windowConfig = windowConfig;
    }
}
