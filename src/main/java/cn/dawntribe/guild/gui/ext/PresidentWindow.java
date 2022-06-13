package cn.dawntribe.guild.gui.ext;

import cn.dawntribe.guild.GuildPlugin;
import cn.dawntribe.guild.event.PlayerQuitGuildEvent;
import cn.dawntribe.guild.gui.SimpleWindow;
import cn.dawntribe.guild.gui.WindowLoader;
import cn.dawntribe.guild.gui.WindowManager;
import cn.dawntribe.guild.gui.WindowType;
import cn.dawntribe.guild.gui.temp.ConfirmWindow;
import cn.dawntribe.guild.inventorys.FakeInventory;
import cn.dawntribe.guild.pojo.Guild;
import cn.dawntribe.guild.pojo.Member;
import cn.dawntribe.guild.pojo.Warehouse;
import cn.dawntribe.guild.utils.GuildUtils;
import cn.dawntribe.guild.utils.VariableTemplate;
import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindow;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author violets007
 * @version 1.0
 * @description: 会长管理窗口
 * @date 2022/3/10 8:49 PM
 */
public class PresidentWindow extends SimpleWindow implements WindowLoader {

    public PresidentWindow() {
        super(GuildUtils.getWindowConfigInfo(""), "");
    }

    @Override
    public FormWindow init(Object... params) {
        Player player = (Player) params[0];
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        setTitle(GuildUtils.getWindowConfigInfo("president-window-title", new String[]{VariableTemplate.GUILD_NAME}, guild.getName()));
        WindowManager.buttonsAddImage(getButtons(),
                new String[]{
                        GuildUtils.getWindowConfigInfo("president-manage-member-img"),
                        GuildUtils.getWindowConfigInfo("president-manage-post-img"),
                        GuildUtils.getWindowConfigInfo("president-temp-apply-list-img"),
                        GuildUtils.getWindowConfigInfo("president-war-img"),
                        GuildUtils.getWindowConfigInfo("president-shop-img"),
                        GuildUtils.getWindowConfigInfo("president-warehouse-img"),
                        GuildUtils.getWindowConfigInfo("president-upgrade-img"),
                        GuildUtils.getWindowConfigInfo("president-position-img"),
                        GuildUtils.getWindowConfigInfo("president-info-img"),
                        GuildUtils.getWindowConfigInfo("president-dissolve-img"),
                },
                new String[]{
                        GuildUtils.getWindowConfigInfo("president-manage-member"),
                        GuildUtils.getWindowConfigInfo("president-manage-post"),
                        GuildUtils.getWindowConfigInfo("president-temp-apply-list"),
                        GuildUtils.getWindowConfigInfo("president-war"),
                        GuildUtils.getWindowConfigInfo("president-shop"),
                        GuildUtils.getWindowConfigInfo("president-warehouse"),
                        GuildUtils.getWindowConfigInfo("president-upgrade"),
                        GuildUtils.getWindowConfigInfo("president-position"),
                        GuildUtils.getWindowConfigInfo("president-info"),
                        GuildUtils.getWindowConfigInfo("president-dissolve"),

                });

        return this;
    }

    @Override
    public void onClicked(int id, Player player) {
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        if (guild == null) return;
        switch (id) {
            case 0:
                player.showFormWindow(WindowManager.getFormWindow(WindowType.MANAGE_MEMBER_LIST_WINDOW, guild, WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, player)));
                break;
            case 1:
                if (guild.getMembers().size() < 2) {
                    player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("set-member-post-fail1"), WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, player)));
                    return;
                }
                player.showFormWindow(WindowManager.getFormWindow(WindowType.MANAGE_MEMBER_POST_WINDOW, player));
                break;
            case 2:
                player.showFormWindow(WindowManager.getFormWindow(WindowType.MANAGE_APPLY_TEMP_LIST_WINDOW, guild, WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, player)));
                break;
            case 3:
                if (!GuildUtils.canGuildWar()) {
                    player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("guild-war-config-missing"), WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, player)));
                    return;
                }
                player.showFormWindow(WindowManager.getFormWindow(WindowType.SEND_GUILD_WAR_WINDOW, player));

                break;
            case 4:
                player.showFormWindow(WindowManager.getFormWindow(WindowType.PRESIDENT_UNLOAD_PRODUCT_WINDOW, player));
                break;
            case 5:
                player.showFormWindow(WindowManager.getFormWindow(WindowType.MANAGE_WAREHOUSE_WINDOW, player));
                break;
            case 6:
                HashMap<String, Object> guildLevelMap = (HashMap<String, Object>) GuildPlugin.getInstance().getConfig().get("guildLevel");
                HashMap<String, Object> guildLevelConditions = (HashMap<String, Object>) guildLevelMap.get(guild.getLevel() + "");
                Long needContribute = Long.parseLong(guildLevelConditions.get("needContribute").toString());
                if (guild.getContribute() < needContribute) {
                    player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("guild-upgrade-fail", new String[]{VariableTemplate.CONTRIBUTE}, needContribute), WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, player)));
                    return;
                }
                List<String> warehouseType = guild.getWarehouse().stream().map(warehouse -> warehouse.getType()).collect(Collectors.toList());
                List<String> allowWarehouses = (List<String>) guildLevelConditions.get("allowWarehouses");
                for (String type : allowWarehouses) {
                    if (!warehouseType.contains(type)) {
                        Warehouse warehouse = GuildUtils.createWarehouseByType(player, type);
                        guild.getWarehouse().add(warehouse);
                    }
                }
                GuildUtils.upgradeGuild(guild, needContribute);
                player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("guild-upgrade-success", new String[]{VariableTemplate.LEVEL}, guild.getLevel()), WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, player)));
                break;
            case 7:
                guild.setPosition(player.getPosition());
                player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("guild-set-position"), WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, player)));
                GuildUtils.saveGuildConfig(guild);
                break;
            case 8:
                player.showFormWindow(WindowManager.getFormWindow(WindowType.EDIT_GUILD_DESCRIPTION));
                break;
            case 9:
                ConfirmWindow confirmWindow = (ConfirmWindow) WindowManager.getFormWindow(WindowType.CONFIRM_WINDOW,
                        GuildUtils.getWindowConfigInfo("dissolve-guild-tips", new String[]{VariableTemplate.GUILD_NAME}, guild.getName()),
                        GuildUtils.getWindowConfigInfo("confirm-dissolve-guild"),
                        GuildUtils.getWindowConfigInfo("confirm-dissolve-guild-cancel"));

                confirmWindow.setButtonClickedListener(new BiConsumer<Boolean, Player>() {
                    @Override
                    public void accept(Boolean confirm, Player player) {
                        if (confirm) {
                            GuildUtils.terminationGuild(guild);
                        } else {
                            player.showFormWindow(
                                    WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, player)
                            );
                        }
                    }
                });

                player.showFormWindow(confirmWindow);
                break;
        }
    }

    @Override
    public void onClosed(Player player) {
        player.showFormWindow(WindowManager.getFormWindow(WindowType.GUILD_WINDOW, player));
    }

}
