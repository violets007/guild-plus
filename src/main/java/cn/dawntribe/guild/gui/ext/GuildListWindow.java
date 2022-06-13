package cn.dawntribe.guild.gui.ext;

import cn.dawntribe.guild.GuildPlugin;
import cn.dawntribe.guild.event.PlayerApplyGuildEvent;
import cn.dawntribe.guild.gui.SimpleWindow;
import cn.dawntribe.guild.gui.WindowLoader;
import cn.dawntribe.guild.gui.WindowManager;
import cn.dawntribe.guild.gui.WindowType;
import cn.dawntribe.guild.gui.temp.ConfirmWindow;
import cn.dawntribe.guild.pojo.Guild;
import cn.dawntribe.guild.utils.GuildUtils;
import cn.dawntribe.guild.utils.VariableTemplate;
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
