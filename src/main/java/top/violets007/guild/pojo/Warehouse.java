package top.violets007.guild.pojo;

import cn.nukkit.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author violets007
 * @version 1.0
 * @description: 仓库实体类
 * @date 2022/4/7 10:44 PM
 */
public class Warehouse {
    private String type;
    private List<String> allowOperation = new ArrayList<>();
    private Map<Integer, Item> content = new HashMap<>();

    public Warehouse(String type, List<String> allowOperation, Map<Integer, Item> content) {
        this.type = type;
        this.allowOperation = allowOperation;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getAllowOperation() {
        return allowOperation;
    }

    public void setAllowOperation(List<String> allowOperation) {
        this.allowOperation = allowOperation;
    }

    public Map<Integer, Item> getContent() {
        return content;
    }

    public void setContent(Map<Integer, Item> content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Warehouse warehouse = (Warehouse) o;

        return type.equals(warehouse.type);
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "type='" + type + '\'' +
                ", allowOperation=" + allowOperation +
                ", content=" + content +
                '}';
    }
}
