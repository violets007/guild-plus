package cn.dawntribe.guild.gui;

import cn.dawntribe.guild.gui.element.ResponsibleButton;
import cn.dawntribe.guild.gui.response.ResponseListenerSimple;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowSimple;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * @author violets007 & Him188
 * @description: 按钮窗口
 * @date: 2022/2/27 1:08 AM
 */
public class SimpleWindow extends FormWindowSimple implements ResponseListenerSimple {
    protected transient BiConsumer<Integer, Player> buttonClickedListener = null;
    protected transient Consumer<Player> windowClosedListener = null;
    private transient FormWindow parent;
    protected ArrayList<String> buttonNames = new ArrayList<String>();
    protected ArrayList<String> imgPaths = new ArrayList<String>();

    public SimpleWindow(String title, String content) {
        super(title, content);
    }

    /***
     * 小型按钮界面
     * @param title 标题
     * @param content 内容
     * @param buttons 按钮集合
     */
    public SimpleWindow(String title, String content, List<ElementButton> buttons) {
        super(title, content, buttons);
    }

    /**
     * 在玩家提交表单后调用 <br>
     * Called on submitted
     *
     * @param listener 调用的方法
     */
    public final SimpleWindow onClicked(BiConsumer<Integer, Player> listener) {
        Objects.requireNonNull(listener);
        this.buttonClickedListener = listener;
        return this;
    }

    /**
     * 在玩家提交表单后调用 <br>
     * Called on submitted
     *
     * @param listener 调用的方法(无 Player)
     */
    public final SimpleWindow onClicked(Consumer<Integer> listener) {
        Objects.requireNonNull(listener);
        this.buttonClickedListener = (id, player) -> listener.accept(id);
        return this;
    }

    /***
     * 玩家对窗口操作会调用此方法
     * @param formWindow 窗口类型
     * @param response 响应数据类型
     * @param player 玩家
     * @return
     */
    public static boolean onEvent(FormWindow formWindow, FormResponse response, Player player) {
        if (formWindow instanceof SimpleWindow) {
            SimpleWindow window = (SimpleWindow) formWindow;

            if (window.wasClosed() || response == null) {
                window.callClosed(player);
                window.closed = false;
            } else {
                window.callClicked(((FormResponseSimple) response).getClickedButtonId(), player);
            }
            return true;
        }
        return false;
    }

    /**
     * 在玩家关闭窗口而没有点击按钮提交表单后调用. <br>
     * Called on submitted
     *
     * @param listener 调用的方法
     */
    public final SimpleWindow onClosed(Consumer<Player> listener) {
        Objects.requireNonNull(listener);
        this.windowClosedListener = listener;
        return this;
    }

    /**
     * 在玩家关闭窗口而没有点击按钮提交表单后调用. <br>
     * Called on closed without submitting.
     *
     * @param listener 调用的方法
     */
    public final SimpleWindow onClosed(Runnable listener) {
        Objects.requireNonNull(listener);
        this.windowClosedListener = (player) -> listener.run();
        return this;
    }

    /**
     * 调用点击按钮
     *
     * @param id
     * @param player
     */
    public void callClicked(int id, Player player) {
        Objects.requireNonNull(player);

        ElementButton button = getButtons().get(id);
        if (button instanceof ResponsibleButton) {
            ((ResponsibleButton) button).callClicked(player);
        }

        this.onClicked(id, player);

        if (this.buttonClickedListener != null) {
            this.buttonClickedListener.accept(id, player);
        }
    }

    /**
     * 窗口关闭调用指定方法
     *
     * @param player
     */
    public void callClosed(Player player) {
        Objects.requireNonNull(player);

        this.onClosed(player);

        if (this.windowClosedListener != null) {
            this.windowClosedListener.accept(player);
        }
    }

    public BiConsumer<Integer, Player> getButtonClickedListener() {
        return buttonClickedListener;
    }

    public void setButtonClickedListener(BiConsumer<Integer, Player> buttonClickedListener) {
        this.buttonClickedListener = buttonClickedListener;
    }

    public Consumer<Player> getWindowClosedListener() {
        return windowClosedListener;
    }

    public void setWindowClosedListener(Consumer<Player> windowClosedListener) {
        this.windowClosedListener = windowClosedListener;
    }

    public FormWindow getParent() {
        return parent;
    }

    public void setParent(FormWindow parent) {
        this.parent = parent;
    }

    @Override
    public String getJSONData() {
        return new Gson().toJson(this, FormWindowSimple.class);
    }
}
