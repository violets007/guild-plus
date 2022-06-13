package cn.dawntribe.guild.gui.action;

import cn.nukkit.Player;

/**
 * @author Him188moe @ GUI Project
 * @violets007 后期优化
 */
@FunctionalInterface
public interface ClickListenerSimple extends ClickListener {
    /**
     * 绑定每个按钮点击以后需要执行的函数
     */
    void run();

    /**
     * simple监听器
     *
     * @param player
     */
    @Override
    default void accept(Player player) {
        run();
    }
}
