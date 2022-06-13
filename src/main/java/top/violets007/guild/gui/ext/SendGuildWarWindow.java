package top.violets007.guild.gui.ext;

import top.violets007.guild.GuildPlugin;
import top.violets007.guild.gui.CustomWindow;
import top.violets007.guild.gui.WindowLoader;
import top.violets007.guild.gui.WindowManager;
import top.violets007.guild.gui.WindowType;
import top.violets007.guild.gui.temp.ConfirmWindow;
import top.violets007.guild.pojo.Guild;
import top.violets007.guild.pojo.GuildWar;
import top.violets007.guild.pojo.WarStatus;
import top.violets007.guild.utils.GuildUtils;
import top.violets007.guild.utils.VariableTemplate;
import top.violets007.guild.utils.WarHandler;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * @author violets007
 * @description:
 */
public class SendGuildWarWindow extends CustomWindow implements WindowLoader {

    public SendGuildWarWindow() {
        super(GuildUtils.getWindowConfigInfo("send-war-window-title"));
    }

    @Override
    public FormWindow init(Object... params) {
        Player player = (Player) params[0];
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        getElements().clear();
        List<String> list = GuildUtils.GUILDS.stream().filter(guild1 -> !guild1.equals(guild)).map(guild1 -> {
            String name = guild1.getName();
            String online = Server.getInstance().getPlayer(guild1.getPresident()) == null ? "离线" : "在线";
            return name + "-" + online;
        }).collect(Collectors.toList());
        getElements().add(new ElementDropdown(GuildUtils.getWindowConfigInfo("send-war-guild-list"), list));
        getElements().add(new ElementInput(GuildUtils.getWindowConfigInfo("send-war-declare-war-content"), ""));
        return this;
    }

    @Override
    public void onClicked(FormResponseCustom response, Player player) {
        String guildName = response.getDropdownResponse(0).getElementContent().split("-")[0];
        String content = response.getInputResponse(1);
        Guild guild = GuildUtils.GUILDS.stream().filter(guild1 -> guild1.getName().equals(guildName)).collect(Collectors.toList()).get(0);
        Guild sendGuild = GuildUtils.getGuildByPlayerName(player.getName());
        Player president = Server.getInstance().getPlayer(guild.getPresident());
        if (president == null) {
            player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("send-guild-war-president-offline"), WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, player)));
            return;
        }

        // 公会战冷却时间
        int configCDTime = GuildPlugin.getInstance().getConfig().getInt("guild-war-cd-time");
        // 当前时间加十分钟
        long warCDTime = System.currentTimeMillis() + configCDTime * 1000;
        if (WarHandler.sendWarWaitTime.containsKey(player.getName())) {
            long nowTime = System.currentTimeMillis();
            if (nowTime < WarHandler.sendWarWaitTime.get(player.getName())) {
                player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("send-guild-war-president-cd", new String[]{VariableTemplate.CD_TIME}, configCDTime), WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, player)));
                return;
            }
        }
        WarHandler.sendWarWaitTime.put(player.getName(), warCDTime);

        ConfirmWindow confirmWindow = (ConfirmWindow) WindowManager.getFormWindow(WindowType.CONFIRM_WINDOW,
                content,
                GuildUtils.getWindowConfigInfo("confirm-accept-war"),
                GuildUtils.getWindowConfigInfo("confirm-cancel-war"));
        confirmWindow.setTitle(GuildUtils.getWindowConfigInfo("confirm-guild-war-window-title"));
        confirmWindow.setButtonClickedListener(new BiConsumer<Boolean, Player>() {
            @Override
            public void accept(Boolean confirm, Player player) {
                Config warConfig = GuildPlugin.getInstance().getWarConfig();
                Position position = GuildUtils.getPosByStr(warConfig.getString("preparing-birthplace"));
                Player president = Server.getInstance().getPlayer(sendGuild.getPresident());
                if (confirm) {

                    if (president == null) return;

                    player.teleport(position);
                    president.teleport(position);

                    GuildWar guildWar = new GuildWar(sendGuild, guild, null, WarStatus.WAIT);
                    WarHandler.guildWarCache.add(guildWar);
                } else {
                    player.showFormWindow(
                            WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, player)
                    );

                    president.showFormWindow(
                            WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("guild-war-cancel", new String[]{VariableTemplate.GUILD_NAME}, GuildUtils.getGuildByPlayerName(player.getName()).getName()), WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, president)
                            ));
                }
            }
        });

        president.showFormWindow(confirmWindow);
    }


    @Override
    public void onClosed(Player player) {
        super.onClosed(player);
    }
}
