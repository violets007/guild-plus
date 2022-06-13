package top.violets007.guild.utils;

import cn.nukkit.utils.Config;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

/**
 * @author violets007
 * @description: 语言
 * @date: 2022/2/27 2:18 AM
 */
public class Language {

    private final Config config;

    public Language(File file) {
        this(new Config(file, Config.PROPERTIES));
    }

    public Language(File file, int type) {
        this(new Config(file, type));
    }

    public Language(Config config) {
        this.config = config;
    }

    public String translateString(String key) {
        return this.translateString(key, new String[0], new Object[0]);
    }

    public String translateString(String key, String[] paramName, Object... params) {
        String string = this.config.getString(key, "§c Unknown key:" + key);
        if (params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                string = string.replace(paramName[i], Objects.toString(params[i]));
            }
        }
        return string;
    }

    public void update(File newFile) {
        this.update(newFile, Config.PROPERTIES);
    }

    public void update(File newFile, int type) {
        this.update(new Config(newFile, type));
    }

    public void update(Config newConfig) {
        boolean needSave = false;
        HashMap<String, String> cache = new HashMap<>();
        for (String key : this.config.getKeys()) {
            if (newConfig.getKeys().contains(key)) {
                cache.put(key, this.config.getString(key, "§c Unknown key:" + key));
            } else {
                this.config.remove(key);
                needSave = true;
            }
        }
        for (String key : newConfig.getKeys()) {
            if (!cache.containsKey(key)) {
                String string = newConfig.getString(key, "§c Unknown key:" + key);
                this.config.set(key, string);
                needSave = true;
            }
        }
        if (needSave) {
            this.config.save();
        }
    }


}
