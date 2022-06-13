package cn.dawntribe.guild.listener;

import cn.dawntribe.guild.gui.CustomWindow;
import cn.dawntribe.guild.gui.SimpleWindow;
import cn.dawntribe.guild.gui.temp.ConfirmWindow;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerSettingsRespondedEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.window.FormWindow;

/**
 * @author him188, violets007
 * @description:
 * @date: 2022/2/28 2:16 AM
 */
public class ResponseLister implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onResponded(PlayerFormRespondedEvent event) {
        this.handleResponse(event.getWindow(), event.getResponse(), event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onSettingsResponded(PlayerSettingsRespondedEvent event) {
        this.handleResponse(event.getWindow(), event.getResponse(), event.getPlayer());
    }

    public void handleResponse(FormWindow formWindow, FormResponse response, Player player) {
        if (ConfirmWindow.onEvent(formWindow, response, player))
            return;
        if (CustomWindow.onEvent(formWindow, response, player))
            return;
        if (SimpleWindow.onEvent(formWindow, response, player))
            return;
    }

}
