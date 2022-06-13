package cn.dawntribe.guild.gui.temp;

import cn.dawntribe.guild.gui.SimpleWindow;
import cn.dawntribe.guild.gui.WindowLoader;
import cn.dawntribe.guild.gui.WindowManager;
import cn.dawntribe.guild.utils.GuildUtils;
import cn.nukkit.Player;
import cn.nukkit.form.window.FormWindow;


/**
 * @author violets007
 * @description: 消息窗口
 * @date: 2022/2/27 1:18 AM
 */
public class MessageWindow extends SimpleWindow implements WindowLoader {
    private String message;

    public MessageWindow() {
        super(GuildUtils.getWindowConfigInfo("message-title"), "");
    }

    @Override
    public FormWindow init(Object... params) {
        getButtons().clear();

        this.message = (String) params[0];
        this.setParent((FormWindow) params[1]);
        setContent(message);

        if (this.getParent() != null) {
            WindowManager.buttonsAddImage(getButtons(),
                    new String[]{
                            GuildUtils.getWindowConfigInfo("message-window-back-img")
                    },
                    new String[]{
                            GuildUtils.getWindowConfigInfo("message-window-back")
                    });
        } else {
            WindowManager.buttonsAddImage(getButtons(),
                    new String[]{
                            GuildUtils.getWindowConfigInfo("message-window-back-img")
                    },
                    new String[]{
                            GuildUtils.getWindowConfigInfo("message-window-close")
                    });
        }
        return this;
    }

    @Override
    public void onClicked(int id, Player player) {
        if (this.getParent() != null)
            player.showFormWindow(this.getParent());
    }
}
