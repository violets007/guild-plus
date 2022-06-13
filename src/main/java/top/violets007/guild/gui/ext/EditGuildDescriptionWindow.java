package top.violets007.guild.gui.ext;

import top.violets007.guild.GuildPlugin;
import top.violets007.guild.gui.CustomWindow;
import top.violets007.guild.gui.WindowLoader;
import top.violets007.guild.gui.WindowManager;
import top.violets007.guild.gui.WindowType;
import top.violets007.guild.pojo.Guild;
import top.violets007.guild.utils.GuildUtils;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindow;

/**
 * @author violets007
 * @version 1.0
 * @description:
 * @date 2022/4/22 11:39 PM
 */
public class EditGuildDescriptionWindow extends CustomWindow implements WindowLoader {


    public EditGuildDescriptionWindow() {
        super(GuildUtils.getWindowConfigInfo("president-description-title"));
    }

    @Override
    public FormWindow init(Object... params) {
        getElements().clear();
        getElements().add(new ElementInput("", GuildUtils.getWindowConfigInfo("president-description-input")));
        return this;
    }

    @Override
    public void onClicked(FormResponseCustom response, Player player) {
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        if (guild == null) return;
        String description = response.getInputResponse(0);
        guild.setDescription(description);
        GuildUtils.saveGuildConfig(guild);
        player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("guild-set-description"), WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, player)));
    }

    @Override
    public void onClosed(Player player) {
        super.onClosed(player);
    }
}
