package cn.dawntribe.guild.gui.response;

import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponseCustom;

/**
 * @author violets007
 * @description: Custom窗口响应处理
 * @date: 2022/2/27 12:59 AM
 */
public interface ResponseListenerCustom extends ResponseListener {
    /**
     * 当表单提交数据时调用
     *
     * @param response 数据
     * @param player   player
     */
    default void onClicked(FormResponseCustom response, Player player) {

    }

    /**
     * 当表单关闭而没有提交数据时调用
     *
     * @param player player
     */
    default void onClosed(Player player) {

    }
}
