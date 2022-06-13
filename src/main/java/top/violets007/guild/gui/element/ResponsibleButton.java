package top.violets007.guild.gui.element;

import top.violets007.guild.gui.action.ClickListener;
import top.violets007.guild.gui.action.ClickListenerSimple;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;


import java.util.Objects;

/**
 * @author violets007
 * @description: 处理点击按钮
 * @date: 2022/2/27 12:55 AM
 */
public class ResponsibleButton extends ElementButton {

    private transient ClickListener listener = null;

    public ResponsibleButton(String text) {
        super(Objects.requireNonNull(text));
    }

    public ResponsibleButton(String text, ClickListener onClicked) {
        this(text);
        setClickListener(onClicked);
    }

    public ResponsibleButton(String text, ClickListenerSimple onClicked) {
        this(text, (ClickListener) onClicked);
    }

    public ResponsibleButton(String text, ElementButtonImageData image) {
        super(Objects.requireNonNull(text), image);
    }

    public ResponsibleButton(String text, ElementButtonImageData image, ClickListener onClicked) {
        this(text, image);
        setClickListener(onClicked);
    }

    public ResponsibleButton(String text, ElementButtonImageData image, ClickListenerSimple onClicked) {
        this(text, image, (ClickListener) onClicked);
    }

    /***
     * 绑定点击的按钮执行的操作
     * @param listener (player)->{执行的代码}
     * @return
     */
    public ResponsibleButton setClickListener(ClickListener listener) {
        Objects.requireNonNull(listener);
        this.listener = listener;
        return this;
    }

    public ResponsibleButton setClickListener(ClickListenerSimple listener) {
        return this.setClickListener((ClickListener) listener);
    }

    /**
     * 当玩家点击按钮时此方法执行调用操作
     *
     * @param player 玩家
     */
    public void callClicked(Player player) {
        if (this.listener != null) {
            //执行绑定的函数的操作
            this.listener.accept(player);
        }
    }

    /**
     * 获取到当前按钮绑定的函数
     *
     * @return
     */
    public ClickListener getListener() {
        return listener;
    }
}
