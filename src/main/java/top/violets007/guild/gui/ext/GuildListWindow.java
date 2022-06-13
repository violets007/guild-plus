package top.violets007.guild.gui.ext;

import top.violets007.guild.GuildPlugin;
import top.violets007.guild.event.PlayerApplyGuildEvent;
import top.violets007.guild.gui.SimpleWindow;
import top.violets007.guild.gui.WindowLoader;
import top.violets007.guild.gui.WindowManager;
import top.violets007.guild.gui.WindowType;
import top.violets007.guild.gui.temp.ConfirmWindow;
import top.violets007.guild.pojo.Guild;
import top.violets007.guild.utils.GuildUtils;
import top.violets007.guild.utils.VariableTemplate;
import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindow;

import java.util.ArrayList;
import java.util.function.BiConsumer;

/**
 * @author violets007
 * @version 1.0
 * @description: 公会列表
 * @date 2022/3/31 8:42 PM
 */
public class GuildListWindow extends SimpleWindow implements WindowLoader {

    public GuildListWindow() {
        super(GuildUtils.getWindowConfigInfo("guild-list-title"), "");
    }

    @Override
    public FormWindow init(Object... params) {
        FormWindow formWindow = (FormWindow) params[0];
        if (formWindow != null) setParent(formWindow);
        ArrayList<String> guildSimpleInfo = new ArrayList<>();
        for (Guild guild : GuildUtils.GUILDS) {
            guildSimpleInfo.add(GuildUtils.getWindowConfigInfo("guild-list-info",
                    new String[]{VariableTemplate.GUILD_NAME, VariableTemplate.LEVEL, VariableTemplate.PRESIDENT},
                    guild.getName(), guild.getLevel(), guild.getPresident()));
        }
        WindowManager.addButtonByNames(getButtons(), guildSimpleInfo.toArray(new String[0]));
        return this;
    }

    @Override
    public void onClicked(int id, Player player) {
        Guild guild = GuildUtils.GUILDS.get(id);
        if (guild == null) return;

        ConfirmWindow confirmWindow = (ConfirmWindow) WindowManager.getFormWindow(WindowType.CONFIRM_WINDOW,
                GuildUtils.getWindowConfigInfo("confirm-join-guild-info",
                        new String[]{
                                VariableTemplate.GUILD_NAME, VariableTemplate.LEVEL, VariableTemplate.DESCRIPTION}
                        , guild.getName(), guild.getLevel(), guild.getDescription()),
                GuildUtils.getWindowConfigInfo("confirm-join-guild-true"),
                GuildUtils.getWindowConfigInfo("confirm-join-guild-false"));

        confirmWindow.setButtonClickedListener(new BiConsumer<Boolean, Player>() {
            @Override
            public void accept(Boolean confirm, Player player) {
                if (confirm) {
                    GuildPlugin.getInstance().getServer().getPluginManager().callEvent(new PlayerApplyGuildEvent(player, guild));
                } else {
                    player.showFormWindow(
                            WindowManager.getFormWindow(WindowType.GUILD_LIST_WINDOW,
                                    WindowManager.getFormWindow(WindowType.GUILD_WINDOW, player)
                            ));
                }
            }
        });

        player.showFormWindow(confirmWindow);
    }

    @Override
    public void onClosed(Player player) {
        if (getParent() != null) player.showFormWindow(getParent());
    }
}
