package cn.dawntribe.guild.gui.ext;

import cn.dawntribe.guild.gui.SimpleWindow;
import cn.dawntribe.guild.gui.WindowLoader;
import cn.dawntribe.guild.gui.WindowManager;
import cn.dawntribe.guild.gui.WindowType;
import cn.dawntribe.guild.pojo.Guild;
import cn.dawntribe.guild.utils.GuildUtils;
import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindow;

import java.util.ArrayList;

/**
 * @author violets007
 * @version 1.0
 * @description: 公会主窗口
 * @date 2022/2/27 2:50 AM
 */
public class GuildWindow extends SimpleWindow implements WindowLoader {

    public GuildWindow() {
        super(GuildUtils.getWindowConfigInfo("guild-title"), "");
    }

    @Override
    public FormWindow init(Object... objects) {
        Player player = (Player) objects[0];
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        ArrayList<String> buttonImgList = new ArrayList<>();
        ArrayList<String> buttonName = new ArrayList<>();

        if (guild == null) {
            buttonImgList.add(GuildUtils.getWindowConfigInfo("guild-create-img"));
            buttonImgList.add(GuildUtils.getWindowConfigInfo("guild-join-img"));
            buttonName.add(GuildUtils.getWindowConfigInfo("guild-create"));
            buttonName.add(GuildUtils.getWindowConfigInfo("guild-join"));
        } else {
            buttonImgList.add(GuildUtils.getWindowConfigInfo("guild-view-img"));
            buttonName.add(0, GuildUtils.getWindowConfigInfo("guild-view"));
        }

        if (guild != null && guild.getPresident().equals(player.getName())) {
            buttonImgList.add(GuildUtils.getWindowConfigInfo("guild-manage-img"));
            buttonName.add(0, GuildUtils.getWindowConfigInfo("guild-manage"));
        }

        buttonImgList.add(GuildUtils.getWindowConfigInfo("guild-Money-Ranking-img"));
        buttonImgList.add(GuildUtils.getWindowConfigInfo("guild-level-Ranking-img"));
        buttonImgList.add(GuildUtils.getWindowConfigInfo("guild-war-victory-ranking-img"));

        buttonName.add(GuildUtils.getWindowConfigInfo("guild-Money-Ranking"));
        buttonName.add(GuildUtils.getWindowConfigInfo("guild-level-Ranking"));
        buttonName.add(GuildUtils.getWindowConfigInfo("guild-war-victory-ranking"));

        WindowManager.buttonsAddImage(getButtons(), buttonImgList.toArray(new String[0]), buttonName.toArray(new String[0]));
        return this;
    }

    @Override
    public void onClicked(int id, Player player) {
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        switch (id) {
            case 0:
                if (guild == null) {
                    player.showFormWindow(WindowManager.getFormWindow(WindowType.CREATE_GUILD_WINDOW, this));
                } else if (guild.getPresident().equals(player.getName())) {
                    player.showFormWindow(WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, player));
                } else {
                    player.showFormWindow(WindowManager.getFormWindow(WindowType.MEMBER_WINDOW, player));
                }
                break;
            case 1:
                if (guild == null) {
                    player.showFormWindow(WindowManager.getFormWindow(WindowType.GUILD_LIST_WINDOW, this));
                } else if (guild.getPresident().equals(player.getName())) {
                    player.showFormWindow(WindowManager.getFormWindow(WindowType.MEMBER_WINDOW, player));
                }
                break;
            case 2:
                player.showFormWindow(WindowManager.getFormWindow(WindowType.GUILD_CONTRIBUTION_RANKING_WINDOW, this));
                break;
            case 3:
                player.showFormWindow(WindowManager.getFormWindow(WindowType.GUILD_LEVEL_RANKING_WINDOW, this));
                break;
            case 4:
                player.showFormWindow(WindowManager.getFormWindow(WindowType.GUILD_VICTORY_RANKING_WINDOW, this));
                break;
        }
    }
}
