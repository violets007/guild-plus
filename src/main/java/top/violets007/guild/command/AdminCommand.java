package top.violets007.guild.command;

import top.violets007.guild.gui.WindowManager;
import top.violets007.guild.gui.WindowType;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;

/**
 * @author violets007
 * @version 1.0
 * @description:
 * @date 2022/2/25 10:37 PM
 */
public class AdminCommand extends Command {

    public AdminCommand(String name) {
        super(name, "公会管理命令");
        setPermission("guild.command.admin");
        getCommandParameters().clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) {
            sender.sendMessage("你没有权限使用此命令");
            return false;
        }

        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        player.showFormWindow(WindowManager.getFormWindow(WindowType.GUILD_ADMIN_WINDOW));
        return true;
    }
}
