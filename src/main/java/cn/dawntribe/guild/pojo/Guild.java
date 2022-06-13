package cn.dawntribe.guild.pojo;

import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author violets007
 * @version 1.0
 * @description: 公会插件实体类
 * @date 2022/2/24 12:20 AM
 */
public class Guild {

    private long id;
    private String name;
    private String president;
    private String createTime;
    private double contribute;
    private List<Member> members;
    private int level;
    private List<Warehouse> warehouse = new ArrayList() {
        {
            add(new Warehouse(InventoryType.HOPPER.getDefaultTitle(), new ArrayList<String>(), new HashMap<Integer, Item>()));
        }
    };
    private List<Product> products = new ArrayList<>();
    private Position position;
    private List<String> tempApply = new ArrayList<>();
    private int victory;
    private String description;

    public Guild(long id, String name, String president, String createTime, List<Member> members, int level, double contribute, Position position, List<String> tempApply, String description) {
        this.id = id;
        this.name = name;
        this.president = president;
        this.createTime = createTime;
        this.members = members;
        this.level = level;
        this.contribute = contribute;
        this.position = position;
        this.tempApply = tempApply;
        this.description = description;
    }

    public double getContribute() {
        return contribute;
    }

    public void setContribute(double contribute) {
        this.contribute = contribute;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPresident() {
        return president;
    }

    public void setPresident(String president) {
        this.president = president;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getVictory() {
        return victory;
    }

    public void setVictory(int victory) {
        this.victory = victory;
    }

    public List<Warehouse> getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(List<Warehouse> warehouse) {
        this.warehouse = warehouse;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public List<String> getTempApply() {
        return tempApply;
    }

    public void setTempApply(ArrayList<String> tempApply) {
        this.tempApply = tempApply;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Guild guild = (Guild) o;

        return id == guild.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return "Guild{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", president='" + president + '\'' +
                ", createTime='" + createTime + '\'' +
                ", contribute=" + contribute +
                ", members=" + members +
                ", level=" + level +
                ", warehouse=" + warehouse +
                ", products=" + products +
                ", position=" + position +
                ", tempApply=" + tempApply +
                ", description='" + description + '\'' +
                '}';
    }
}
