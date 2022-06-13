package cn.dawntribe.guild.gui;

import cn.nukkit.form.window.FormWindow;

/**
 * 窗口配置加载
 *
 * @author violets007
 */
public interface WindowLoader {

    /**
     * 初始化窗体数据并展示给玩家
     *
     * @param params
     * @return
     */
    FormWindow init(Object... params);


}
