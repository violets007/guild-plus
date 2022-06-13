package top.violets007.guild.gui.temp;

import top.violets007.guild.gui.WindowLoader;
import top.violets007.guild.gui.response.ResponseListenerModal;
import top.violets007.guild.utils.GuildUtils;
import cn.nukkit.Player;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseModal;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowModal;

import com.google.gson.Gson;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 消息确认窗口
 *
 * @author violets007
 */
public class ConfirmWindow extends FormWindowModal implements ResponseListenerModal, WindowLoader {
    private transient BiConsumer<Boolean, Player> buttonClickedListener = null;
    private transient Consumer<Player> windowClosedListener = null;
    private transient FormWindow parent;


    public ConfirmWindow() {
        super(GuildUtils.getWindowConfigInfo("confirm-window-title"), "", "", "");

    }

    public ConfirmWindow(String content, String trueButtonText, String falseButtonText) {
        super("", content, trueButtonText, falseButtonText);
    }

    /**
     * 构造方法进行确认窗口的创建
     *
     * @param title           窗口名称
     * @param content         内容
     * @param trueButtonText  确认按钮标题
     * @param falseButtonText 取消按钮标题
     */
    public ConfirmWindow(String title, String content, String trueButtonText, String falseButtonText) {
        super(title, content, trueButtonText, falseButtonText);
    }

    /**
     * 在玩家提交表单, 或关闭表单窗口后调用. <br>
     * Called on submitted
     *
     * @param listener 调用的方法
     */
    public final ConfirmWindow onResponded(BiConsumer<Boolean, Player> listener) {
        Objects.requireNonNull(listener);
        this.buttonClickedListener = listener;
        return this;
    }

    /**
     * 在玩家提交表单后调用. <br>
     * Called on submitted
     *
     * @param listener 调用的方法(无 Player)
     */
    public final ConfirmWindow onResponded(Consumer<Boolean> listener) {
        Objects.requireNonNull(listener);
        this.buttonClickedListener = (response, player) -> listener.accept(response);
        return this;
    }

    /**
     * 在玩家提交表单后调用 <br>
     * Called on submitted <br>
     * 参数任意一项可以为 null. <br>
     * Args can be null.
     *
     * @param listenerOnTrue  当玩家点击 <code>true</code> 按钮时调用的函数
     * @param listenerOnFalse 当玩家点击 <code>false</code> 按钮时调用的函数
     */
    public final ConfirmWindow onResponded(Consumer<Player> listenerOnTrue, Consumer<Player> listenerOnFalse) {
        this.buttonClickedListener = (confirmation, player) -> {
            if (confirmation) {
                if (listenerOnTrue != null) {
                    listenerOnTrue.accept(player);
                }
            } else {
                if (listenerOnFalse != null) {
                    listenerOnFalse.accept(player);
                }
            }
        };
        return this;
    }


    /**
     * 在玩家关闭窗口而没有点击按钮提交表单后调用. <br>
     * Called on closed without submitting.
     *
     * @param listener 调用的方法
     */
    public final ConfirmWindow onClosed(Consumer<Player> listener) {
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
    public ConfirmWindow onClosed(Runnable listener) {
        Objects.requireNonNull(listener);
        this.windowClosedListener = (player) -> listener.run();
        return this;
    }


    public static boolean onEvent(FormWindow formWindow, FormResponse response, Player player) {
        if (formWindow instanceof ConfirmWindow) {
            ConfirmWindow window = (ConfirmWindow) formWindow;

            if (window.wasClosed() || response == null) {
                window.callClosed(player);
                window.closed = false;
            } else {
                window.callClicked(((FormResponseModal) response).getClickedButtonId() == 0, player);
            }
            return true;
        }
        return false;
    }


    /**
     * 调用玩家点击按钮操作
     *
     * @param b      确认按钮true 取消按钮false
     * @param player 玩家类
     */
    private final ConfirmWindow callClicked(boolean b, Player player) {
        Objects.requireNonNull(player);
        this.onClicked(b, player);
        if (buttonClickedListener != null) {
            //注入Lambda操作
            buttonClickedListener.accept(b, player);
        }
        return this;
    }

    /**
     * 在玩家关闭窗口而没有点击按钮提交表单后调用. <br>
     * Called on closed without submitting.
     *
     * @param player 调用的方法
     */
    private final ConfirmWindow callClosed(Player player) {
        Objects.requireNonNull(player);
        onClosed(windowClosedListener);
        return this;
    }

    public BiConsumer<Boolean, Player> getButtonClickedListener() {
        return buttonClickedListener;
    }

    public void setButtonClickedListener(BiConsumer<Boolean, Player> buttonClickedListener) {
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
        return new Gson().toJson(this, FormWindowModal.class);
    }

    @Override
    public FormWindow init(Object... params) {
        String content = (String) params[0];
        String trueButton = (String) params[1];
        String falseButton = (String) params[2];
        setContent(content);
        setButton1(trueButton);
        setButton2(falseButton);
        return this;
    }
}
