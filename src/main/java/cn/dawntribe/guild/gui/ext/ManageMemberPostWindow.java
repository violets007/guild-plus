package cn.dawntribe.guild.gui.ext;

import cn.dawntribe.guild.GuildPlugin;
import cn.dawntribe.guild.gui.CustomWindow;
import cn.dawntribe.guild.gui.WindowLoader;
import cn.dawntribe.guild.gui.WindowManager;
import cn.dawntribe.guild.gui.WindowType;
import cn.dawntribe.guild.pojo.Guild;
import cn.dawntribe.guild.pojo.Member;
import cn.dawntribe.guild.utils.GuildUtils;
import cn.dawntribe.guild.utils.VariableTemplate;
import cn.nukkit.Player;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.window.FormWindow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author violets007
 * @description: 管理职位
 * @date: 2022/5/2 10:14 PM
 */
public class ManageMemberPostWindow extends CustomWindow implements WindowLoader {

    public ManageMemberPostWindow() {
        super(GuildUtils.getWindowConfigInfo("manage-post-title"));
    }

    @Override
    public FormWindow init(Object... params) {
        Player player = (Player) params[0];
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        getElements().clear();
        List<String> members = guild.getMembers().stream().filter(member -> (!member.getName().equals(player.getName()))).map(member -> member.getName()).collect(Collectors.toList());
        ArrayList<HashMap<String, Object>> post = (ArrayList<HashMap<String, Object>>) GuildPlugin.getInstance().getConfig().get("post");
        List<String> postNameList = post.stream().map((map) -> {
            return map.get("name").toString();
        }).collect(Collectors.toList());
        postNameList.remove(0);
        getElements().add(new ElementDropdown(GuildUtils.getWindowConfigInfo("manage-post-member-list"), members));
        getElements().add(new ElementDropdown(GuildUtils.getWindowConfigInfo("manage-post-list"), postNameList));
        return this;
    }

    @Override
    public void onClicked(FormResponseCustom response, Player player) {
        Guild guild = GuildUtils.getGuildByPlayerName(player.getName());
        if (guild == null) return;
        String playerName = response.getDropdownResponse(0).getElementContent();
        String postName = response.getDropdownResponse(1).getElementContent();

        if (playerName.equals("") || player.getName().length() == 0) {
            player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("set-member-post-fail", new String[]{VariableTemplate.PLAYER_NAME}, playerName), WindowManager.getFormWindow(WindowType.MANAGE_MEMBER_POST_WINDOW, player)));
            return;
        }

        for (Member member : guild.getMembers()) {
            if (member.getName().equals(playerName)) {
                member.setPost(postName);
            }
        }

        GuildUtils.saveGuildConfig(guild);
        player.showFormWindow(WindowManager.getFormWindow(WindowType.MESSAGE_WINDOW, GuildPlugin.getInstance().getLang().translateString("set-member-post-success", new String[]{VariableTemplate.PLAYER_NAME, VariableTemplate.POST_NAME}, playerName, postName), WindowManager.getFormWindow(WindowType.MANAGE_MEMBER_POST_WINDOW, player)));

    }

    @Override
    public void onClosed(Player player) {
        player.showFormWindow(WindowManager.getFormWindow(WindowType.PRESIDENT_WINDOW, player));
    }
}
