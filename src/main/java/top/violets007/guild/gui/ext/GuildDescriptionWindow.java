package top.violets007.guild.gui.ext;

import top.violets007.guild.gui.SimpleWindow;
import top.violets007.guild.gui.WindowLoader;
import top.violets007.guild.gui.WindowManager;
import top.violets007.guild.gui.WindowType;
import top.violets007.guild.pojo.Guild;
import top.violets007.guild.utils.GuildUtils;
import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindow;

/**
 * @author violets007
 * @version 1.0
 * @description:
 * @date 2022/4/23 12:00 AM
 */
public class GuildDescriptionWindow extends SimpleWindow implements WindowLoader {


    public GuildDescriptionWindow() {
        super(GuildUtils.getWindowConfigInfo("president-description-info-title"), "");
    }

    @Override
    public FormWindow init(Object... params) {
        Player player = (Player) params[0];
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        setContent(guild.getDescription());
        return this;
    }

    @Override
    public void onClosed(Player player) {
        player.showFormWindow(WindowManager.getFormWindow(WindowType.MEMBER_WINDOW, player));
    }
}
