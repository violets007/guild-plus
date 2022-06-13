package cn.dawntribe.guild.gui.ext;

import cn.dawntribe.guild.gui.RankingType;
import cn.dawntribe.guild.gui.SimpleWindow;
import cn.dawntribe.guild.gui.WindowLoader;
import cn.dawntribe.guild.pojo.Guild;
import cn.dawntribe.guild.utils.GuildUtils;
import cn.dawntribe.guild.utils.VariableTemplate;
import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindow;

import java.util.List;

/**
 * @author violets007
 * @version 1.0
 * @description: 等级榜
 * @date 2022/3/31 9:41 PM
 */
public class GuildLevelRankingWindow extends SimpleWindow implements WindowLoader {

    public GuildLevelRankingWindow() {
        super(GuildUtils.getWindowConfigInfo("guild-level-ranking-title"), "");
    }

    @Override
    public FormWindow init(Object... params) {
        FormWindow formWindow = (FormWindow) params[0];
        if (formWindow != null) setParent(formWindow);
        List<Guild> guildList = GuildUtils.getGuildList(RankingType.LEVEL);
        StringBuilder rankingList = new StringBuilder();
        for (Guild guild : guildList) {
            rankingList.append(GuildUtils.getWindowConfigInfo("guild-level-ranking-info",
                    new String[]{VariableTemplate.GUILD_NAME, VariableTemplate.PRESIDENT, VariableTemplate.LEVEL},
                    guild.getName(), guild.getPresident(), guild.getLevel() + "\n")
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
