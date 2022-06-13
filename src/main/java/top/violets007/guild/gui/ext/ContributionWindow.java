package top.violets007.guild.gui.ext;

import top.violets007.guild.GuildPlugin;
import top.violets007.guild.gui.CustomWindow;
import top.violets007.guild.gui.WindowLoader;
import top.violets007.guild.gui.WindowManager;
import top.violets007.guild.gui.WindowType;
import top.violets007.guild.pojo.Guild;
import top.violets007.guild.utils.GuildUtils;
import top.violets007.guild.utils.Utils;
import top.violets007.guild.utils.VariableTemplate;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementLabel;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindow;
import me.onebone.economyapi.EconomyAPI;

/**
 * @author violets007
 * @version 1.0
 * @description:
 * @date 2022/4/18 9:31 PM
 */
public class ContributionWindow extends CustomWindow implements WindowLoader {

    public ContributionWindow() {
        super(GuildUtils.getWindowConfigInfo("contribute-window-title"));
    }

    @Override
    public FormWindow init(Object... params) {
        Player player = (Player) params[0];
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        double totalContribute = guild.getContribute();
        getElements().clear();
        getElements().add(new ElementLabel(GuildPlugin.getInstance().getLang().translateString("guild-contribute-money", new String[]{VariableTemplate.MONEY}, totalContribute)));
        getElements().add(new ElementInput("", GuildPlugin.getInstance().getLang().translateString("input-contribute-money")));

        return this;
    }

    @Override
    public void onClicked(FormResponseCustom response, Player player) {
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        String contributeMoneyStr = response.getInputResponse(1);
        double myMoney = EconomyAPI.getInstance().myMoney(player);

        if (!Utils.isNumber(contributeMoneyStr)) {
            player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("contribute-money-Nan"), WindowManager.getFormWindow(WindowType.CONTRIBUTION_WINDOW, player)));
            return;
        }

        double contributeMoney = Double.parseDouble(contributeMoneyStr);
        if (EconomyAPI.getInstance().reduceMoney(player, contributeMoney) != EconomyAPI.RET_SUCCESS) {
            player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("contribute-money-fail", new String[]{VariableTemplate.MONEY, VariableTemplate.CONTRIBUTE}, myMoney, contributeMoney), WindowManager.getFormWindow(WindowType.CONTRIBUTION_WINDOW, player)));
            return;
        }

        GuildUtils.guildContributeAdd(player, guild, contributeMoney);
        player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("contribute-money", new String[]{VariableTemplate.MONEY}, contributeMoney), WindowManager.getFormWindow(WindowType.CONTRIBUTION_WINDOW, player)));
    }


    @Override
    public void onClosed(Player player) {
        player.showFormWindow(WindowManager.getFormWindow(WindowType.MEMBER_WINDOW, player));
    }
}
