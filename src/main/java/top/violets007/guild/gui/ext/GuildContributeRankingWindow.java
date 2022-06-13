package top.violets007.guild.gui.ext;

import top.violets007.guild.gui.RankingType;
import top.violets007.guild.gui.SimpleWindow;
import top.violets007.guild.gui.WindowLoader;
import top.violets007.guild.pojo.Guild;
import top.violets007.guild.utils.GuildUtils;
import top.violets007.guild.utils.VariableTemplate;
import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindow;

import java.util.List;

/**
 * @author violets007
 * @version 1.0
 * @description: 贡献榜
 * @date 2022/3/31 9:41 PM
 */
public class GuildContributeRankingWindow extends SimpleWindow implements WindowLoader {

    public GuildContributeRankingWindow() {
        super(GuildUtils.getWindowConfigInfo("guild-contribution-ranking-title"), "");
    }

    @Override
    public FormWindow init(Object... params) {
        FormWindow formWindow = (FormWindow) params[0];
        if (formWindow != null) setParent(formWindow);
        List<Guild> guildList = GuildUtils.getGuildList(RankingType.CONTRIBUTION);
        StringBuilder rankingList = new StringBuilder();
        for (Guild guild : guildList) {
            rankingList.append(GuildUtils.getWindowConfigInfo("guild-contribution-ranking-info",
                    new String[]{VariableTemplate.GUILD_NAME, VariableTemplate.PRESIDENT, VariableTemplate.CONTRIBUTE},
                    guild.getName(), guild.getPresident(), guild.getContribute() + "\n")
            );

        }
        setContent(rankingList.toString());
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
