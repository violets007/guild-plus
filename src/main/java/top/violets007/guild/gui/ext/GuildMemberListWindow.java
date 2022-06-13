package top.violets007.guild.gui.ext;

import top.violets007.guild.gui.SimpleWindow;
import top.violets007.guild.gui.WindowLoader;
import top.violets007.guild.pojo.Guild;
import top.violets007.guild.utils.GuildUtils;
import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindow;

/**
 * @author violets007
 * @version 1.0
 * @description:
 * @date 2022/3/14 10:32 PM
 */
public class GuildMemberListWindow extends SimpleWindow implements WindowLoader {

    public GuildMemberListWindow() {
        super(GuildUtils.getWindowConfigInfo("guild-member-list"), "");
    }

    @Override
    public FormWindow init(Object... params) {
        Guild guild = (Guild) params[0];
        FormWindow formWindow = (FormWindow) params[1];
        if (formWindow != null) setParent(formWindow);
        setContent(GuildUtils.getMemberListByGuild(guild));
        return this;
    }

    @Override
    public void onClicked(int id, Player player) {
        super.onClicked(id, player);
    }

    @Override
    public void onClosed(Player player) {
        if (getParent() != null) player.showFormWindow(getParent());
    }
}
