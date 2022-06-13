package top.violets007.guild.gui;

import top.violets007.guild.GuildPlugin;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindow;

import java.util.HashMap;
import java.util.List;

/**
 * @author violets007
 * @description: 管理窗口
 * @date: 2022/2/27 1:26 AM
 */
public class WindowManager {
    private static final HashMap<WindowType, Class<? extends WindowLoader>> registerWindow = new HashMap<>();
    private static final HashMap<WindowType, WindowLoader> alreadyCreateWindow = new HashMap<>();


    /**
     * 获取窗口
     *
     * @param windowType 窗口类型
     * @param params     窗口初始化需要的参数
     * @return
     */
    public static FormWindow getFormWindow(WindowType windowType, Object... params) {
        if (!alreadyCreateWindow.containsKey(windowType)) {
            Class<? extends WindowLoader> windowLoaderClazz = registerWindow.get(windowType);
            try {
                WindowLoader windowLoader = windowLoaderClazz.newInstance();
                return windowLoader.init(params);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return alreadyCreateWindow.get(windowType).init(params);
    }


    public static List<ElementButton> addButtonByNames(List<ElementButton> elementButtons, String[] names) {
        elementButtons.clear();
        for (int i = 0; i < names.length; i++) {
            elementButtons.add(new ElementButton(names[i]));
        }
        return elementButtons;
    }

    public static void buttonsAddImage(String[] imgPath, List<ElementButton> list) {
        if (imgPath.length != list.size()) GuildPlugin.getInstance().getLang().translateString("images-length-error");
        for (int i = 0; i < imgPath.length; i++) {
            list.get(i).addImage(new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, imgPath[i]));
        }
    }

    public static void buttonsAddImage(List<ElementButton> elementButtons, String[] imgPaths, String[] buttonNames) {
        if (imgPaths.length != buttonNames.length) {
            GuildPlugin.getInstance().getLang().translateString("images-length-error");
        }
        elementButtons.clear();

        for (int i = 0; i < imgPaths.length; i++) {
            String imgPath = imgPaths[i];
            String buttonName = buttonNames[i];
            elementButtons.add(new ElementButton(buttonName, new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_PATH, imgPath)));
        }
    }


    public static HashMap<WindowType, Class<? extends WindowLoader>> getRegisterWindow() {
        return registerWindow;
    }

    public static HashMap<WindowType, WindowLoader> getAlreadyCreateWindow() {
        return alreadyCreateWindow;
    }

}
