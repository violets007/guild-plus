package top.violets007.guild.command;

import top.violets007.guild.gui.WindowManager;
import top.violets007.guild.gui.WindowType;
import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.form.window.FormWindow;

/**
 * @author violets007
 * @version 1.0
 * @description: 公会命令
 * @date 2022/2/25 10:22 PM
 */
public class GuildCommand extends Command {

    public GuildCommand(String name) {
        super(name, "公会命令");
        setPermission("guild.command.user");
        getCommandParameters().clear();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            FormWindow formWindow = WindowManager.getFormWindow(WindowType.GUILD_WINDOW, player);
            player.showFormWindow(formWindow);
        }

        return false;
    }
}
