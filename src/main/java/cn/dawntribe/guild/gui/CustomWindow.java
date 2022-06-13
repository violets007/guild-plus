package cn.dawntribe.guild.gui;

import cn.dawntribe.guild.gui.response.ResponseListenerCustom;
import cn.nukkit.Player;
import cn.nukkit.form.element.Element;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author violets007
 * @description: 自定义组件窗口
 * @date: 2022/2/27 1:08 AM
 */
public class CustomWindow extends FormWindowCustom implements ResponseListenerCustom {
    private transient BiConsumer<FormResponseCustom, Player> buttonClickedListener = null;
    private transient Consumer<Player> windowClosedListener = null;
    private transient FormWindow parent = null;
    private boolean isBack = false;


    public CustomWindow(String title) {
        super(title);
    }

    public CustomWindow(String title, List<Element> contents) {
        super(title, contents);
    }

    public CustomWindow(String title, List<Element> contents, String icon) {
        super(title, contents, icon);
    }

    public CustomWindow(String title, List<Element> contents, ElementButtonImageData icon) {
        super(title, contents, icon);
    }

    public static boolean onEvent(FormWindow formWindow, FormResponse response, Player player) {
        if (formWindow instanceof CustomWindow) {
            CustomWindow window = (CustomWindow) formWindow;
            if (window.wasClosed() || response == null) {
                if (window.isBack) {
                    window.callBack(player);
                } else {
                    window.callClosed(player);
                }
                window.closed = false;
            } else {
                window.callClicked((FormResponseCustom) response, player);
            }
            return true;
        }
        return false;
    }

    /**
     * 当点击时调用点击按钮
     *
     * @param response
     * @param player
     */
    public void callClicked(FormResponseCustom response, Player player) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(response);

        this.onClicked(response, player);

        //检测点击按钮是否为空
        if (this.buttonClickedListener != null) {
            //调用赋值好的函数
            this.buttonClickedListener.accept(response, player);
        }
    }

    /**
     * 调用关闭窗口
     *
     * @param player
     */
    private void callClosed(Player player) {
        Objects.requireNonNull(player);
        onClosed(player);

        if (windowClosedListener != null) {
            //后期植入代码
            windowClosedListener.accept(player);
        }
    }

    public BiConsumer<FormResponseCustom, Player> getButtonClickedListener() {
        return buttonClickedListener;
    }

    /**
     * 点击指定按钮后期需要植入的代码
     *
     * @param buttonClickedListener (FormResponseCustom,plyaer)->{代码块}
     */
    public void setButtonClickedListener(BiConsumer<FormResponseCustom, Player> buttonClickedListener) {
        this.buttonClickedListener = buttonClickedListener;
    }

    public Consumer<Player> getWindowClosedListener() {
        return windowClosedListener;
    }

    /**
     * 关闭窗口后期需要植的代码
     *
     * @param windowClosedListener (plyaer)->{代码块}
     */
    public void setWindowClosedListener(Consumer<Player> windowClosedListener) {
        this.windowClosedListener = windowClosedListener;
    }

    public FormWindow getParent() {
        return parent;
    }

    public void setParent(FormWindow parent) {
        this.parent = parent;
    }

    public void callBack(Player player) {
        Objects.requireNonNull(player);
        player.showFormWindow(this.parent);
    }

    public Boolean getBack() {
        return this.isBack;
    }

    public void setBack(Boolean back, FormWindow formWindow) {
        this.isBack = back;
        setParent(formWindow);
    }


    @Override
    public String getJSONData() {
        String jsonStr = new Gson().toJson(this, FormWindowCustom.class);
        return jsonStr.replace("defaultOptionIndex", "default")
                .replace("defaultText", "default")
                .replace("defaultValue", "default")
                .replace("defaultStepIndex", "default");
    }

    @Override
    public String toString() {
        return "CustomWindow{" +
                "buttonClickedListener=" + buttonClickedListener +
                ", windowClosedListener=" + windowClosedListener +
                ", parent=" + parent +
                '}';
    }
}
