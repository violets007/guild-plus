package cn.dawntribe.guild.gui.response;

import cn.nukkit.Player;

/**
 * @author violets007
 * @description: simple响应处理
 * @date: 2022/2/27 1:02 AM
 */
public interface ResponseListenerSimple extends ResponseListener {
    /**
     * 当表单提交数据并关闭窗口时调用. <br>
     * Called when player clicked a button.
     *
     * @param id     button id, starts from 0. | 按钮 ID, 从 0 开始
     * @param player player
     */
    default void onClicked(int id, Player player) {

    }

    /**
     * 当表单关闭而没有提交数据时调用
     *
     * @param player player
     */
    default void onClosed(Player player) {

    }
}
