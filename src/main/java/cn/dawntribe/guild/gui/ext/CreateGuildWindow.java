package cn.dawntribe.guild.gui.ext;

import cn.dawntribe.guild.GuildPlugin;
import cn.dawntribe.guild.event.PlayerCreateGuildEvent;
import cn.dawntribe.guild.gui.CustomWindow;
import cn.dawntribe.guild.gui.WindowLoader;
import cn.dawntribe.guild.gui.WindowManager;
import cn.dawntribe.guild.gui.WindowType;
import cn.dawntribe.guild.pojo.Guild;
import cn.dawntribe.guild.utils.GuildUtils;
import cn.dawntribe.guild.utils.Language;
import cn.dawntribe.guild.utils.VariableTemplate;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.utils.TextFormat;
import me.onebone.economyapi.EconomyAPI;

import java.util.regex.Pattern;

/**
 * @author violets007
 * @version 1.0
 * @description: 创建公会信息
 * @date 2022/2/27 9:35 PM
 */
public class CreateGuildWindow extends CustomWindow implements WindowLoader {

    public CreateGuildWindow() {
        super(GuildUtils.getWindowConfigInfo("create-guild-title"));
    }

    @Override
    public FormWindow init(Object... params) {
        getElements().clear();
        if (params.length >= 1) {
            FormWindow formWindow = (FormWindow) params[0];
            setBack(true, formWindow);
        }
        addElement(new ElementInput("", GuildPlugin.getInstance().getLang().translateString("input-create-guild-name")));
        return this;
    }

    @Override
    public void onClicked(FormResponseCustom response, Player player) {
        String guildName = response.getInputResponse(0);
        guildName = TextFormat.clean(guildName.replaceAll(" ", ""));
        final String regEx = "[\n`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。， 、？]";
        guildName.replaceAll(regEx, "");
        Language lang = GuildPlugin.getInstance().getLang();
        int createGuildMoney = GuildPlugin.getInstance().getConfig().getInt("create-guild-money");
        if (EconomyAPI.getInstance().reduceMoney(player, createGuildMoney) != EconomyAPI.RET_SUCCESS) {
            double playerMoney = EconomyAPI.getInstance().myMoney(player);
            player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, lang.translateString("create-guild-error", new String[]{VariableTemplate.PLAYER_MONEY, VariableTemplate.CREATE_GUILD_MONEY}, playerMoney, createGuildMoney), this));
            return;
        }

        Guild guild = GuildUtils.createGuild(guildName, player.getName());
        GuildPlugin.getInstance().getServer().getPluginManager().callEvent(new PlayerCreateGuildEvent(player, guild));
    }

    @Override
    public void onClosed(Player player) {
        super.onClosed(player);
    }
}
