package cn.dawntribe.guild.utils;


import cn.nukkit.item.Item;
import cn.nukkit.utils.Binary;

import java.io.*;
import java.nio.charset.StandardCharsets;


/**
 * @author violets007
 * @version 1.0
 * @description:
 * @date 2022/2/24 9:10 PM
 */
public class Utils {

/*    private static HashMap<String, Integer> guildRepOpenTime = new HashMap<>();
    private static HashMap<Long, Inventory> existGuildRep = new HashMap<>();*/

    public static Item parseItem(String item) {
        String[] itemDataStr = item.split(":");
        int id = Integer.parseInt(itemDataStr[0]);
        int meta = Integer.parseInt(itemDataStr[1]);
        int count = Integer.parseInt(itemDataStr[2]);
        byte[] nbt = new byte[0];
        if (itemDataStr.length > 3) {
            nbt = cn.nukkit.utils.Utils.parseHexBinary(itemDataStr[3]);
        }
        return Item.get(id, meta, count, nbt);
    }

    public static String itemToString(Item item) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(item.getId() + ":" + item.getDamage() + ":" + item.getCount());
        if (item.hasCompoundTag()) stringBuilder.append(":" + Binary.bytesToHexString(item.getCompoundTag()));
        return stringBuilder.toString();
    }

    public static boolean isNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    // save file
    public static boolean createJsonFile(String content, String filePath) {
        BufferedWriter writer = null;
        File file = new File(filePath);

        deleteFileOrDirectory(new File(filePath));

        //如果文件不存在，则新建一个
        if (!file.exists()) {

            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();

            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //写入
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;

    }

    private static void deleteFileOrDirectory(File file) {
        if (null != file) {

            if (!file.exists()) {
                return;
            }

            int i;
            // file 是文件
            if (file.isFile()) {
                boolean result = file.delete();
                // 限制循环次数，避免死循环
                for (i = 0; !result && i++ < 10; result = file.delete()) {
                    // 垃圾回收
                    System.gc();
                }

                return;
            }

            // file 是目录
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteFileOrDirectory(f);
                }
            }
            file.delete();
        }
    }

    public static void writeFile(File file, String content) throws IOException {
        writeFile(file, new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
    }

    public static void writeFile(File file, InputStream content) throws IOException {
        if (content == null) {
            throw new IllegalArgumentException("content must not be null");
        }
        if (!file.exists()) {
            file.createNewFile();
        }
        try (FileOutputStream stream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = content.read(buffer)) != -1) {
                stream.write(buffer, 0, length);
            }
        }
        content.close();
    }
}
