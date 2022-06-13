package cn.dawntribe.guild.gui.ext.admin;

import cn.dawntribe.guild.GuildPlugin;
import cn.dawntribe.guild.gui.SimpleWindow;
import cn.dawntribe.guild.gui.WindowLoader;
import cn.dawntribe.guild.gui.WindowManager;
import cn.dawntribe.guild.gui.WindowType;
import cn.dawntribe.guild.utils.GuildUtils;
import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.utils.Config;

/**
 * @author violets007
 * @description:
 */
public class GuildAdminWindow extends SimpleWindow implements WindowLoader {


    public GuildAdminWindow() {
        super(GuildUtils.getWindowConfigInfo("guild-admin-window-title"), "");
    }

    @Override
    public FormWindow init(Object... params) {

        WindowManager.buttonsAddImage(getButtons(),
                new String[]{
                        GuildUtils.getWindowConfigInfo("guild-admin-preparing-birthplace-img"),
                        GuildUtils.getWindowConfigInfo("guild-admin-sparring-points-img"),
                        GuildUtils.getWindowConfigInfo("guild-admin-sparring-points1-img"),
                        GuildUtils.getWindowConfigInfo("guild-admin-gameover-points-img")
                },
                new String[]{
                        GuildUtils.getWindowConfigInfo("guild-admin-preparing-birthplace"),
                        GuildUtils.getWindowConfigInfo("guild-admin-sparring-points"),
                        GuildUtils.getWindowConfigInfo("guild-admin-sparring-points1"),
                        GuildUtils.getWindowConfigInfo("guild-admin-gameover-points")
                });

        return this;
    }

    @Override
    public void onClicked(int id, Player player) {
        Config warConfig = GuildPlugin.getInstance().getWarConfig();

        switch (id) {
            case 0:
                int floorX = player.getPosition().getFloorX();
                int floorY = player.getPosition().getFloorY();
                int floorZ = player.getPosition().getFloorZ();
                String levelName = player.getLevel().getName();
                String join = String.join(",", floorX + "", floorY + "", floorZ + "", levelName);
                warConfig.set("preparing-birthplace", join);
                warConfig.save();
                player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("set-preparing-birthplace-success"), WindowManager.getFormWindow(WindowType.GUILD_ADMIN_WINDOW, player)));
                break;
            case 1:
                floorX = player.getPosition().getFloorX();
                floorY = player.getPosition().getFloorY();
                floorZ = player.getPosition().getFloorZ();
                levelName = player.getLevel().getName();
                join = String.join(",", floorX + "", floorY + "", floorZ + "", levelName);
                warConfig.set("sparring-points", join);
                warConfig.save();
                player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("set-sparring-points-success"), WindowManager.getFormWindow(WindowType.GUILD_ADMIN_WINDOW, player)));
                break;
            case 2:
                floorX = player.getPosition().getFloorX();
                floorY = player.getPosition().getFloorY();
                floorZ = player.getPosition().getFloorZ();
                levelName = player.getLevel().getName();
                join = String.join(",", floorX + "", floorY + "", floorZ + "", levelName);
                warConfig.set("sparring-points1", join);
                warConfig.save();
                player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("set-sparring-points1-success"), WindowManager.getFormWindow(WindowType.GUILD_ADMIN_WINDOW, player)));
                break;
            case 3:
                floorX = player.getPosition().getFloorX();
                floorY = player.getPosition().getFloorY();
                floorZ = player.getPosition().getFloorZ();
                levelName = player.getLevel().getName();
                join = String.join(",", floorX + "", floorY + "", floorZ + "", levelName);
                warConfig.set("gameover-points", join);
                warConfig.save();
                player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("set-gameover-point-success"), WindowManager.getFormWindow(WindowType.GUILD_ADMIN_WINDOW, player)));
                break;
        }
    }


}
