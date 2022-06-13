package cn.dawntribe.guild.gui.ext;

import cn.dawntribe.guild.GuildPlugin;
import cn.dawntribe.guild.event.PlayerQuitGuildEvent;
import cn.dawntribe.guild.gui.SimpleWindow;
import cn.dawntribe.guild.gui.WindowLoader;
import cn.dawntribe.guild.gui.WindowManager;
import cn.dawntribe.guild.gui.WindowType;
import cn.dawntribe.guild.gui.temp.ConfirmWindow;
import cn.dawntribe.guild.pojo.Guild;
import cn.dawntribe.guild.pojo.Warehouse;
import cn.dawntribe.guild.utils.GuildUtils;
import cn.dawntribe.guild.utils.VariableTemplate;
import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.level.Position;

import java.util.function.BiConsumer;

/**
 * @author violets007
 * @version 1.0
 * @description: 公会成员窗口
 * @date 2022/3/14 8:47 PM
 */
public class MemberWindow extends SimpleWindow implements WindowLoader {


    public MemberWindow() {
        super("", "");
    }


    @Override
    public FormWindow init(Object... params) {
        Player player = (Player) params[0];
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        setTitle(GuildUtils.getWindowConfigInfo("member-window-title", new String[]{VariableTemplate.GUILD_NAME}, guild.getName()));

        WindowManager.buttonsAddImage(getButtons(),
                new String[]{
                        GuildUtils.getWindowConfigInfo("member-list-img"),
                        GuildUtils.getWindowConfigInfo("member-warehouse-img"),
                        GuildUtils.getWindowConfigInfo("member-shop-img"),
                        GuildUtils.getWindowConfigInfo("member-contribute-img"),
                        GuildUtils.getWindowConfigInfo("member-position-img"),
                        GuildUtils.getWindowConfigInfo("member-info-img"),
                        GuildUtils.getWindowConfigInfo("member-quit-guild-img"),
                },
                new String[]{
                        GuildUtils.getWindowConfigInfo("member-list"),
                        GuildUtils.getWindowConfigInfo("member-warehouse"),
                        GuildUtils.getWindowConfigInfo("member-shop"),
                        GuildUtils.getWindowConfigInfo("member-contribute"),
                        GuildUtils.getWindowConfigInfo("member-position"),
                        GuildUtils.getWindowConfigInfo("member-info"),
                        GuildUtils.getWindowConfigInfo("member-quit-guild"),


                });

        return this;
    }

    @Override
    public void onClicked(int id, Player player) {
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        if (guild == null) return;
        switch (id) {
            case 0:
                player.showFormWindow(WindowManager.getFormWindow(WindowType.GUILD_MEMBER_LIST_WINDOW, guild, this));
                break;
            case 1:
                player.showFormWindow(WindowManager.getFormWindow(WindowType.SELECT_WAREHOUSE_WINDOW, player));
                break;
            case 2:
                player.showFormWindow(WindowManager.getFormWindow(WindowType.MANAGE_PRODUCT_WINDOW));
                break;
            case 3:
                player.showFormWindow(WindowManager.getFormWindow(WindowType.CONTRIBUTION_WINDOW, player));
                break;
            case 4:
                Position position = guild.getPosition();
                if (position == null) {
                    player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("guild-position-error"), WindowManager.getFormWindow(WindowType.MEMBER_WINDOW, player)));
                    return;
                }

                player.teleport(position);
                player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("guild-position-success", new String[]{VariableTemplate.GUILD_NAME}, guild.getName()), WindowManager.getFormWindow(WindowType.MEMBER_WINDOW, player)));
                break;
            case 5:
                player.showFormWindow(WindowManager.getFormWindow(WindowType.GUILD_DESCRIPTION_WINDOW, player));
                break;
            case 6:

                ConfirmWindow confirmWindow = (ConfirmWindow) WindowManager.getFormWindow(WindowType.CONFIRM_WINDOW,
                        GuildUtils.getWindowConfigInfo("member-quit-guild-tips", new String[]{VariableTemplate.GUILD_NAME}, guild.getName()),
                        GuildUtils.getWindowConfigInfo("confirm-quit-guild"),
                        GuildUtils.getWindowConfigInfo("confirm-quit-guild-cancel"));

                confirmWindow.setButtonClickedListener(new BiConsumer<Boolean, Player>() {
                    @Override
                    public void accept(Boolean confirm, Player player) {
                        if (confirm) {
                            if (guild.getPresident().equals(player.getName())) {
                                player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("president-quit-guild"), WindowManager.getFormWindow(WindowType.MEMBER_WINDOW, player)));
                                return;
                            }

                            GuildPlugin.getInstance().getServer().getPluginManager().callEvent(new PlayerQuitGuildEvent(player, guild));
                        } else {
                            player.showFormWindow(
                                    WindowManager.getFormWindow(WindowType.MEMBER_WINDOW, player)
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
