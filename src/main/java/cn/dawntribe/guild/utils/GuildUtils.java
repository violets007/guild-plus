package cn.dawntribe.guild.utils;

import cn.dawntribe.guild.GuildPlugin;
import cn.dawntribe.guild.gui.RankingType;
import cn.dawntribe.guild.gui.WindowLoader;
import cn.dawntribe.guild.gui.WindowManager;
import cn.dawntribe.guild.gui.WindowType;
import cn.dawntribe.guild.inventorys.FakeInventory;
import cn.dawntribe.guild.inventorys.repository.GuildBigChestInventory;
import cn.dawntribe.guild.inventorys.repository.GuildChestInventory;
import cn.dawntribe.guild.inventorys.repository.GuildHopperInventory;
import cn.dawntribe.guild.pojo.Guild;
import cn.dawntribe.guild.pojo.Member;
import cn.dawntribe.guild.pojo.Product;
import cn.dawntribe.guild.pojo.Warehouse;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author violets007
 * @version 1.0
 * @description: 一些关于公会操作
 * @date 2022/2/27 2:32 AM
 */
public class GuildUtils {
    public static String GUILD_FOLDER = GuildPlugin.getInstance().getDataFolder() + File.separator + "guild" + File.separator;
    public static ArrayList<Guild> GUILDS = new ArrayList<>();
    public static HashMap<String, HashMap<String, FakeInventory>> cacheWarehouse = new HashMap();

    public static String getWindowConfigInfo(String key) {
        return GuildPlugin.getInstance().getWindowConfig().get(key, key);
    }

    public static String getWindowConfigInfo(String key, String[] paramNames, Object... params) {
        String windowConfigInfo = getWindowConfigInfo(key);
        if (paramNames == null || params == null) return windowConfigInfo;
        for (int i = 0; i < paramNames.length; i++) {
            windowConfigInfo = windowConfigInfo.replace(paramNames[i], params[i].toString());
        }
        return windowConfigInfo;
    }


    public static void addWindowClass(WindowType windowType, Class clazz) {
        HashMap<WindowType, Class<? extends WindowLoader>> registerWindow = WindowManager.getRegisterWindow();
        if (!registerWindow.containsKey(windowType)) {
            registerWindow.put(windowType, clazz);
            GuildPlugin.getInstance().getLogger().debug("成功注册窗口 " + windowType.toString() + " 绑定的Class " + clazz.getPackage().getName() + "." + clazz.getSimpleName());
        }
    }

    public static long getNextGuildId() {
        int size = GUILDS.size();
        if (size == 0) return 1L;
        long max = 0L;
        for (Guild guild : GUILDS) {
            if (guild.getId() > max) {
                max = guild.getId();
            }
        }
        return ++max;
    }

    public static Guild createGuild(String name, String president) {
        long guildId = getNextGuildId();
        Config config = GuildPlugin.getInstance().getConfig();
        ArrayList<HashMap<String, Object>> post = (ArrayList<HashMap<String, Object>>) config.get("post");
        String postName = (String) post.get(0).get("name");
        int level = (int) post.get(0).get("level");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String time = dateFormat.format(new Date());
        name = name.replaceAll(" ", "");
        Guild guild = new Guild(guildId, name, president, time, new ArrayList<>(), 1, 0D, null, new ArrayList<>(), "");
        guild.getMembers().add(new Member(president, postName, level, time, 0D));
        GUILDS.add(guild);
        saveGuildConfig(guild);
        return guild;
    }

    public static void terminationGuild(Guild guild) {
        GUILDS.remove(guild);
        String path = GuildPlugin.getInstance().getDataFolder() + File.separator + "guild" + File.separator + guild.getName() + ".yml";
        Language lang = GuildPlugin.getInstance().getLang();
        File file = new File(path);

        if (file.delete()) {
            GuildPlugin.getInstance().getLogger().info(lang.translateString("delete-guild-success", new String[]{VariableTemplate.GUILD_FILE_PATH}, file.getAbsolutePath()));
        } else {
            GuildPlugin.getInstance().getLogger().info(lang.translateString("delete-guild-error", new String[]{VariableTemplate.GUILD_FILE_PATH}, file.getAbsolutePath()));
        }

        for (Member member : guild.getMembers()) {
            Player player = GuildPlugin.getInstance().getServer().getPlayer(member.getName());
            if (player != null) {
                player.sendMessage(GuildPlugin.getInstance().getLang().translateString("dissolve-guild-msg"));
            }
        }

    }

    public static void addMember(Guild guild, String playerName) {
        Config config = GuildPlugin.getInstance().getConfig();
        ArrayList<HashMap<String, Object>> post = (ArrayList<HashMap<String, Object>>) config.get("post");
        HashMap<String, Object> jobInfo = post.get(post.size() - 1);
        String name = jobInfo.get("name").toString();
        int level = (int) jobInfo.get("level");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:ss:mm");
        String time = dateFormat.format(new Date());
        guild.getMembers().add(new Member(playerName, name, level, time, 0D));
    }

    public static Member getMember(String playerName) {
        for (Guild guild : GUILDS) {
            for (Member member : guild.getMembers()) {
                if (member.getName().equals(playerName)) {
                    return member;
                }
            }
        }
        return null;
    }

    public static void removeMember(String playerName, Guild guild) {
        List<Product> products = guild.getProducts().stream().filter(product -> product.getOwner().equals(playerName)).collect(Collectors.toList());
        for (Product product : products) {
            guild.getProducts().remove(product);
        }

        Member quitMember = null;
        for (Member member : guild.getMembers()) {
            if (member.getName().equals(playerName)) {
                quitMember = member;
                guild.setContribute(guild.getContribute() - quitMember.getContribute());
            }
        }

        guild.getMembers().remove(quitMember);
        GuildUtils.saveGuildConfig(guild);
    }

    public static void addMember(Guild guild, Player player) {
        addMember(guild, player.getName());
    }

    public static void removeAllTempApply(String playerName) {
        for (Guild guild : GUILDS) {
            List<String> tempApply = guild.getTempApply();
            if (tempApply.contains(playerName)) {
                tempApply.remove(playerName);
                GuildUtils.saveGuildConfig(guild);
            }
        }
    }

    public static FakeInventory getWarehouse(String type, Player player, Map<Integer, Item> content) {
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        Language lang = GuildPlugin.getInstance().getLang();
        if (guild == null) return null;
        FakeInventory fakeInventory = null;
        HashMap<String, FakeInventory> fakeInventoryHashMap = cacheWarehouse.get(guild.getName());
        if (fakeInventoryHashMap == null) fakeInventoryHashMap = new HashMap<String, FakeInventory>();

        switch (type) {
            case "Hopper":

                if (fakeInventoryHashMap.get(type) == null) {
                    fakeInventory = new GuildHopperInventory(player, lang.translateString("guild-small-warehouse"), content);
                    fakeInventoryHashMap.put(type, fakeInventory);
                    cacheWarehouse.put(guild.getName(), fakeInventoryHashMap);
                }

                return fakeInventoryHashMap.get(type);

            case "Chest":

                if (fakeInventoryHashMap.get(type) == null) {
                    fakeInventory = new GuildChestInventory(player, lang.translateString("guild-warehouse"), content);
                    fakeInventoryHashMap.put(type, fakeInventory);
                    cacheWarehouse.put(guild.getName(), fakeInventoryHashMap);
                }

                return fakeInventoryHashMap.get(type);

            case "Double Chest":

                if (fakeInventoryHashMap.get(type) == null) {
                    fakeInventory = new GuildBigChestInventory(player, lang.translateString("guild-big-warehouse"), content);
                    fakeInventoryHashMap.put(type, fakeInventory);
                    cacheWarehouse.put(guild.getName(), fakeInventoryHashMap);
                }

                return fakeInventoryHashMap.get(type);

        }

        return null;
    }

    public static void saveGuildConfig(Guild guild) {
        String guildPath = GUILD_FOLDER + guild.getName() + ".yml";
        File file = new File(guildPath);
        File guildFolder = new File(GUILD_FOLDER);
        Language lang = GuildPlugin.getInstance().getLang();
        if (!file.exists()) {
            if (!guildFolder.exists()) guildFolder.mkdirs();
            try {
                boolean newFile = file.createNewFile();
                if (!newFile) {
                    GuildPlugin.getInstance().getLogger().info(lang.translateString("save-guild-error", new String[]{VariableTemplate.GUILD_FILE_PATH}, file.getAbsolutePath()));
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Config guildConfig = new Config(guildPath, Config.YAML);
        guildConfig.set("id", guild.getId());
        guildConfig.set("name", guild.getName());
        guildConfig.set("president", guild.getPresident());
        guildConfig.set("createTime", guild.getCreateTime());

        ArrayList<HashMap<String, Object>> memberList = new ArrayList<>();
        for (Member member : guild.getMembers()) {
            memberList.add(new HashMap<String, Object>() {
                {
                    put("name", member.getName());
                    put("post", member.getPost());
                    put("jobLevel", member.getJobLevel());
                    put("joinTime", member.getJoinTime());
                    put("contribute", member.getContribute());
                }
            });
        }

        List<Warehouse> warehouses = guild.getWarehouse();
        List<HashMap> warehouseData = guild.getWarehouse().stream().map(map -> {

            Map<Integer, Item> content = map.getContent();
            HashMap<String, Object> contentData = new HashMap<>();
            content.entrySet().stream().forEach((entry) -> {
                contentData.put(entry.getKey().toString(), Utils.itemToString(entry.getValue()));
            });

            return new HashMap<String, Object>() {
                {
                    put("type", map.getType());
                    put("allowOperation", map.getAllowOperation());
                    put("content", contentData);
                }
            };
        }).collect(Collectors.toList());
        guildConfig.set("warehouse", warehouseData);

        List<HashMap<String, String>> products = guild.getProducts().stream().map(product -> {
            return new HashMap<String, String>() {
                {
                    put("owner", product.getOwner());
                    put("price", product.getPrice() + "");
                    put("item", Utils.itemToString(product.getItem()));
                    put("createTime", product.getCreateTime());
                }
            };
        }).collect(Collectors.toList());
        guildConfig.set("products", products);
        guildConfig.set("members", memberList);
        guildConfig.set("level", guild.getLevel());
        guildConfig.set("contribute", guild.getContribute());
        guildConfig.set("victory", guild.getVictory());

        if (guild.getPosition() != null) {
            Position position = guild.getPosition();
            guildConfig.set("position", position.getFloorX() + "," + position.getFloorY() + "," + position.getFloorZ() + "," + position.getLevel().getName());
        }
        guildConfig.set("tempApplication", guild.getTempApply());
        guildConfig.set("description", guild.getDescription());
        guildConfig.save();

        if (GUILDS.contains(guild)) {
            GUILDS.set(GUILDS.indexOf(guild), guild);
        }
    }


    public static void loadGuild() {
        File folder = new File(GUILD_FOLDER);
        for (File file : folder.listFiles()) {
            if (file.getName().endsWith("yml")) {
                Config config = new Config(GUILD_FOLDER + file.getName(), Config.YAML);
                long id = config.getLong("id");
                String name = config.getString("name");
                String president = config.getString("president");
                String createTime = config.getString("createTime");
                List<Member> members = getConfigMembers(config);
                List<Warehouse> warehouse = getConfigWarehouse(config);
                List<Product> products = getConfigProducts(config);
                int level = config.getInt("level");
                String pos = config.getString("position");
                double contribute = config.getDouble("contribute");
                Integer victory = config.get("victory", 0);
                Position position = pos == null || pos.equals("null") || pos.length() < 1 ? null : new Position(Double.parseDouble(pos.split(",")[0]), Double.parseDouble(pos.split(",")[1]), Double.parseDouble(pos.split(",")[2]), Server.getInstance().getLevelByName(pos.split(",")[3]));
                List<String> tempApplication = config.getList("tempApplication");
                String description = config.getString("description");

                Guild guild = new Guild(id, name, president, createTime, members, level, contribute, position, tempApplication, description);
                guild.setWarehouse(warehouse);
                guild.setProducts(products);
                guild.setVictory(victory);
                GUILDS.add(guild);
                GuildPlugin.getInstance().getLogger().debug(guild.toString());
            }
        }
    }

    private static List<Product> getConfigProducts(Config config) {
        List<HashMap<String, String>> products = (List<HashMap<String, String>>) config.get("products");
        return products.stream().map(map -> {
            return new Product(map.get("owner"), Utils.parseItem(map.get("item")), Double.parseDouble(map.get("price")), map.get("createTime"));
        }).collect(Collectors.toList());
    }

    private static List<Warehouse> getConfigWarehouse(Config config) {
        ArrayList<Warehouse> warehouses = new ArrayList<>();
        ArrayList<HashMap<String, Object>> warehouse = (ArrayList<HashMap<String, Object>>) config.get("warehouse");
        for (HashMap<String, Object> map : warehouse) {
            String type = (String) map.get("type");
            List<String> allowOperation = (List<String>) map.get("allowOperation");
            HashMap<Integer, Item> content = new HashMap<>();
            HashMap<String, String> contentData = (HashMap<String, String>) map.get("content");
            contentData.forEach((k, v) -> {
                content.put(Integer.parseInt(k), Utils.parseItem(v));
            });
            warehouses.add(new Warehouse(type, allowOperation, content));
        }
        return warehouses;
    }

    private static List<Member> getConfigMembers(Config config) {
        List<Map<String, Object>> memberList = (List<Map<String, Object>>) config.get("members");
        List<Member> members = memberList.stream().map(entry -> {
            String memberName = (String) entry.get("name");
            String post = (String) entry.get("post");
            int level = (int) entry.get("jobLevel");
            String joinTime = (String) entry.get("joinTime");
            double contribute = (double) entry.get("contribute");

            return new Member(memberName, post, level, joinTime, contribute);
        }).collect(Collectors.toList());
        return members;
    }


    public static Guild getGuildByPlayerName(String playerName) {
        for (Guild guild : GUILDS) {
            List<Member> members = guild.getMembers();
            for (Member member : members) {
                if (member.getName().equals(playerName)) {
                    return guild;
                }
            }
        }
        return null;
    }

    public static String getMemberListByGuild(Guild guild) {
        if (guild == null) return null;

        StringBuilder stringBuilder = new StringBuilder();
        guild.getMembers().sort(new Comparator<Member>() {
            @Override
            public int compare(Member o1, Member o2) {
                return o1.getJobLevel() < o2.getJobLevel() ? 1 : o1.getJobLevel() > o2.getJobLevel() ? -1 : 0;
            }
        });

        for (Member member : guild.getMembers()) {
            stringBuilder.append(GuildUtils.getWindowConfigInfo("member-list-format", new String[]{
                            VariableTemplate.PLAYER_NAME, VariableTemplate.POST_NAME, VariableTemplate.CONTRIBUTE
                    },
                    member.getName(), member.getPost(), member.getContribute()));
        }
        return stringBuilder.toString();
    }

    public static List<Guild> getGuildList(RankingType rankingType) {
        switch (rankingType) {
            case CONTRIBUTION:
                return GUILDS.stream().sorted(new Comparator<Guild>() {
                    @Override
                    public int compare(Guild o1, Guild o2) {
                        double guildContribute = totalContribution(o1);
                        double guildContribute1 = totalContribution(o2);
                        return guildContribute < guildContribute1 ? 1 : guildContribute > guildContribute1 ? -1 : 0;
                    }
                }).collect(Collectors.toList());

            case LEVEL:
                return GUILDS.stream().sorted(Comparator.comparingInt(Guild::getLevel).reversed()).collect(Collectors.toList());

            case VICTORY:
                return GUILDS.stream().sorted(Comparator.comparingInt(Guild::getVictory).reversed()).collect(Collectors.toList());
        }
        return null;
    }

    public static Double totalContribution(Guild guild) {
        if (guild == null) return 0D;
        return guild.getMembers().stream().mapToDouble(member -> member.getContribute()).sum();
    }

    public static void uploadProduct(Guild guild, Item item, Player player, int price, Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(date);
        Product product = new Product(player.getName(), item, price, dateStr);
        guild.getProducts().add(product);
        player.getInventory().removeItem(item);
        GuildUtils.saveGuildConfig(guild);
    }

    public static Product getProductByItemName(Guild guild, String itemName) {
        List<Product> products = guild.getProducts().stream().filter(product -> ItemIDSunName.getIDByName(product.getItem()).equals(itemName)).collect(Collectors.toList());
        return products.size() > 0 ? products.get(0) : null;
    }

    public static double getTotalContribute(Guild guild) {
        return guild.getMembers().stream().mapToDouble(Member::getContribute).sum();
    }

    public static void guildContributeAdd(Player player, Guild guild, double contributeMoney) {
        for (Member member : guild.getMembers()) {
            if (member.getName().equals(player.getName())) {
                member.setContribute(member.getContribute() + contributeMoney);
            }
        }
        guild.setContribute(Double.valueOf(guild.getContribute() + contributeMoney).longValue());
        GuildUtils.saveGuildConfig(guild);
    }

    public static void upgradeGuild(Guild guild, Long needContribute) {
        guild.setContribute(guild.getContribute() - needContribute);
        guild.setLevel(guild.getLevel() + 1);
        GuildUtils.saveGuildConfig(guild);
    }

    public static void saveWarehouses(Player who, String type, Map<Integer, Item> contents) {
        Guild guild = GuildUtils.getGuildByPlayerName(who.getName());
        if (guild != null) {
            List<Warehouse> warehouses = guild.getWarehouse().stream().filter(warehouse -> warehouse.getType().equals(type)).collect(Collectors.toList());
            if (warehouses.size() >= 1) {
                warehouses.get(0).setContent(contents);
                GuildUtils.saveGuildConfig(guild);
            }
        }
    }

    public static Warehouse createWarehouseByType(Player player, String type) {
        Language lang = GuildPlugin.getInstance().getLang();

        switch (type) {
            case "Hopper":
                return new Warehouse(InventoryType.HOPPER.getDefaultTitle(), new ArrayList<String>(), new HashMap<Integer, Item>());
            case "Chest":
                return new Warehouse(InventoryType.CHEST.getDefaultTitle(), new ArrayList<String>(), new HashMap<Integer, Item>());
            case "Double Chest":
                return new Warehouse(InventoryType.DOUBLE_CHEST.getDefaultTitle(), new ArrayList<String>(), new HashMap<Integer, Item>());
        }
        return null;
    }

    public static String getWarehouseNameByType(String type) {
        switch (type) {
            case "Hopper":
                return GuildPlugin.getInstance().getLang().translateString("guild-small-warehouse");
            case "Chest":
                return GuildPlugin.getInstance().getLang().translateString("guild-warehouse");
            case "Double Chest":
                return GuildPlugin.getInstance().getLang().translateString("guild-big-warehouse");
        }
        return null;
    }

    public static boolean canGuildWar() {
        if (GUILDS.size() < 2) return false;
        Config warConfig = GuildPlugin.getInstance().getWarConfig();
        return warConfig.get("preparing-birthplace") != null && warConfig.get("sparring-points") != null && warConfig.get("sparring-points1") != null;
    }

    public static Position getPosByStr(String posStr) {
        Integer x = Integer.parseInt(posStr.split(",")[0]);
        Integer y = Integer.parseInt(posStr.split(",")[1]);
        Integer z = Integer.parseInt(posStr.split(",")[2]);
        String levelName = posStr.split(",")[3];
        return new Position(x, y, z, Server.getInstance().getLevelByName(levelName));
    }
}
