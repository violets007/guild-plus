package cn.dawntribe.guild.gui.response;

import cn.nukkit.Player;

/**
 * @author violets007
 * @description: 确认窗口响应处理
 * @date: 2022/2/27 1:00 AM
 */
public interface ResponseListenerModal extends ResponseListener {
    /**
     * 当表单提交数据并关闭窗口时调用
     *
     * @param confirmation 点击按钮类型. true就是确认false就是取消
     * @param player       player
     */
    default void onClicked(boolean confirmation, Player player) {

    }

    /**
     * 当表单关闭而没有提交数据时调用
     *
     * @param player player
     */
    default void onClosed(Player player) {

    }
}
