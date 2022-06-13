package cn.dawntribe.guild.gui.ext;

import cn.dawntribe.guild.GuildPlugin;
import cn.dawntribe.guild.event.KickMemberEvent;
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

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author violets007
 * @version 1.0
 * @description: 管理成员列表
 * @date 2022/3/14 10:32 PM
 */
public class ManageMemberListWindow extends SimpleWindow implements WindowLoader {

    public ManageMemberListWindow() {
        super(GuildUtils.getWindowConfigInfo("manage-member-list"), "");
    }

    @Override
    public FormWindow init(Object... params) {
        Guild guild = (Guild) params[0];
        FormWindow formWindow = (FormWindow) params[1];
        if (formWindow != null) setParent(formWindow);
        List<String> memberList = guild.getMembers().stream().filter(member -> !member.getName().equals(guild.getPresident())).map(member -> member.getName()).collect(Collectors.toList());
        WindowManager.addButtonByNames(getButtons(), memberList.toArray(new String[0]));
        return this;
    }

    @Override
    public void onClicked(int id, Player player) {
        String playerName = getResponse().getClickedButton().getText();
        Guild guild = GuildUtils.getGuildByPlayerName(playerName);
        if (guild == null) return;
        ConfirmWindow confirmWindow = (ConfirmWindow) WindowManager.getFormWindow(WindowType.CONFIRM_WINDOW,
                GuildUtils.getWindowConfigInfo("confirm-remove-member-info",
                        new String[]{
                                VariableTemplate.PLAYER_NAME
                        }
                        , playerName),
                GuildUtils.getWindowConfigInfo("confirm-kick-player", new String[]{VariableTemplate.PLAYER_NAME}, playerName),
                GuildUtils.getWindowConfigInfo("confirm-kick-player-cancel", new String[]{VariableTemplate.PLAYER_NAME}, playerName));

        confirmWindow.setButtonClickedListener(new BiConsumer<Boolean, Player>() {
            @Override
            public void accept(Boolean confirm, Player player) {
                if (confirm) {
                    GuildPlugin.getInstance().getServer().getPluginManager().callEvent(new KickMemberEvent(playerName, player, guild));
                } else {
                    player.showFormWindow(
                            WindowManager.getFormWindow(WindowType.MANAGE_MEMBER_LIST_WINDOW,
                                    guild, WindowManager.getFormWindow(WindowType.GUILD_WINDOW, player)
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
