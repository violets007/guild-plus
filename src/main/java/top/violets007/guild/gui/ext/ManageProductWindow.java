package top.violets007.guild.gui.ext;

import top.violets007.guild.GuildPlugin;
import top.violets007.guild.gui.SimpleWindow;
import top.violets007.guild.gui.WindowLoader;
import top.violets007.guild.gui.WindowManager;
import top.violets007.guild.gui.WindowType;
import top.violets007.guild.utils.GuildUtils;
import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindow;

/**
 * @author violets007
 * @version 1.0
 * @description: 玩家管理商品窗口
 * @date 2022/4/9 10:20 PM
 */
public class ManageProductWindow extends SimpleWindow implements WindowLoader {

    public ManageProductWindow() {
        super(GuildUtils.getWindowConfigInfo("guild-product-title"), "");
    }

    @Override
    public FormWindow init(Object... params) {

        WindowManager.buttonsAddImage(getButtons(),
                new String[]{
                        GuildUtils.getWindowConfigInfo("guild-upload-product-img"),
                        GuildUtils.getWindowConfigInfo("guild-unload-product-img"),
                        GuildUtils.getWindowConfigInfo("guild-product-list-img"),
                },
                new String[]{
                        GuildUtils.getWindowConfigInfo("guild-upload-product"),
                        GuildUtils.getWindowConfigInfo("guild-unload-product"),
                        GuildUtils.getWindowConfigInfo("guild-product-list"),

                });
        return this;
    }

    @Override
    public void onClicked(int id, Player player) {
        switch (id) {
            case 0:
                if (player.getInventory().getContents().size() == 0) {
                    player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("upload-product-empty"), WindowManager.getFormWindow(WindowType.MANAGE_PRODUCT_WINDOW)));
                    return;
                }

                player.showFormWindow(WindowManager.getFormWindow(WindowType.UPLOAD_PRODUCT_WINDOW, player));
                break;
            case 1:
                player.showFormWindow(WindowManager.getFormWindow(WindowType.UNLOAD_PRODUCT_WINDOW, player));
                break;
            case 2:
                player.showFormWindow(WindowManager.getFormWindow(WindowType.GUILD_PRODUCT_LIST_WINDOW, player));
                break;
        }
    }

    @Override
    public void onClosed(Player player) {
        player.showFormWindow(WindowManager.getFormWindow(WindowType.MEMBER_WINDOW, player));
    }
}
