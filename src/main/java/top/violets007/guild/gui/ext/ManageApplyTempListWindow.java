package top.violets007.guild.gui.ext;

import top.violets007.guild.GuildPlugin;
import top.violets007.guild.event.PlayerJoinGuildEvent;
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

import java.util.HashMap;
import java.util.function.BiConsumer;

/**
 * @author violets007
 * @version 1.0
 * @description: 临时申请列表
 * @date 2022/3/14 10:32 PM
 */
public class ManageApplyTempListWindow extends SimpleWindow implements WindowLoader {

    public ManageApplyTempListWindow() {
        super(GuildUtils.getWindowConfigInfo("apply-temp-list-title"), "");
    }

    @Override
    public FormWindow init(Object... params) {
        Guild guild = (Guild) params[0];
        FormWindow formWindow = (FormWindow) params[1];
        if (formWindow != null) setParent(formWindow);
        WindowManager.addButtonByNames(getButtons(), guild.getTempApply().toArray(new String[0]));
        return this;
    }

    @Override
    public void onClicked(int id, Player player) {
        String playerName = getResponse().getClickedButton().getText();
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        if (guild == null) return;
        ConfirmWindow confirmWindow = (ConfirmWindow) WindowManager.getFormWindow(WindowType.CONFIRM_WINDOW,
                GuildUtils.getWindowConfigInfo("confirm-player-join",
                        new String[]{
                                VariableTemplate.PLAYER_NAME
                        }
                        , playerName),
                GuildUtils.getWindowConfigInfo("confirm-accept-join"),
                GuildUtils.getWindowConfigInfo("confirm-cancel-join"));

        confirmWindow.setButtonClickedListener(new BiConsumer<Boolean, Player>() {
            @Override
            public void accept(Boolean confirm, Player player) {
                HashMap<String, Object> guildLevelMap = (HashMap<String, Object>) GuildPlugin.getInstance().getConfig().get("guildLevel");
                HashMap<String, Object> levelCondition = (HashMap<String, Object>) guildLevelMap.get(guild.getLevel() + "");
                int count = Integer.parseInt(levelCondition.get("count").toString());
                if (guild.getMembers().size() == count) {
                    player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("apply-guild-exceeds-max", new String[]{VariableTemplate.MEMBER_COUNT}, guild.getMembers().size()), WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, player)));
                    return;
                }
                if (confirm) {
                    GuildPlugin.getInstance().getServer().getPluginManager().callEvent(new PlayerJoinGuildEvent(playerName, player, guild));
                } else {
                    player.showFormWindow(
                            WindowManager.getFormWindow(WindowType.MANAGE_APPLY_TEMP_LIST_WINDOW,
                                    guild, WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, player)
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
