package cn.dawntribe.guild.gui.ext;

import cn.dawntribe.guild.GuildPlugin;
import cn.dawntribe.guild.gui.CustomWindow;
import cn.dawntribe.guild.gui.WindowLoader;
import cn.dawntribe.guild.gui.WindowManager;
import cn.dawntribe.guild.gui.WindowType;
import cn.dawntribe.guild.pojo.Guild;
import cn.dawntribe.guild.utils.GuildUtils;
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
