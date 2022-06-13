package top.violets007.guild.gui.action;

import cn.nukkit.Player;

import java.util.function.Consumer;

/**
 * @author Him188moe @ GUI Project
 * @author zixuan007后期优化
 */
@FunctionalInterface
public interface ClickListener extends Consumer<Player> {
    /**
     * 玩家点击监听
     *
     * @param player
     */
    @Override
    void accept(Player player);

}
