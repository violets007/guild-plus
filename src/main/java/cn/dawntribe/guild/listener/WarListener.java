package cn.dawntribe.guild.listener;


import cn.dawntribe.guild.GuildPlugin;
import cn.dawntribe.guild.event.GuildWarOverEvent;
import cn.dawntribe.guild.pojo.Guild;
import cn.dawntribe.guild.pojo.GuildWar;
import cn.dawntribe.guild.utils.*;
import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.item.Item;
import cn.nukkit.utils.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author violets007
 * @description:
 */
public class WarListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        EntityDamageEvent.DamageCause cause = event.getCause();
        Entity damage = event.getDamager();
        Entity entity = event.getEntity();
        Language lang = GuildPlugin.getInstance().getLang();

        if (!(damage instanceof Player && entity instanceof Player)) return;
        EntityDamageEvent lastDamageCause = entity.getLastDamageCause();
        Entity killer = event.getDamager();
        if (event.getDamage() >= entity.getHealth() && lastDamageCause.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
            event.setCancelled();
            entity.setHealth(entity.getMaxHealth());

            GuildWar guildWar = WarHandler.guildWarCache.get(0);

            if (guildWar.getInitiationGuild().getMembers().stream().filter(member -> member.getName().equals(damage.getName())).collect(Collectors.toList()).size() > 0) {
                guildWar.setKillerCredits(guildWar.getKillerCredits() + 1);
            }

            if (guildWar.getReceivingGuild().getMembers().stream().filter(member -> member.getName().equals(damage.getName())).collect(Collectors.toList()).size() > 0) {
                guildWar.setDeathCredits(guildWar.getDeathCredits() + 1);
            }


            WarHandler.tpGamePos(entity.getName(), lang.translateString("guild-war-death", new String[]{VariableTemplate.KILLER}, killer.getName()));

        }
    }


    @EventHandler
    public void onWarOver(GuildWarOverEvent event) {
        GuildWar guildWar = event.getGuildWar();

        int killerCredits = guildWar.getKillerCredits();
        int deathCredits = guildWar.getDeathCredits();
        Guild initiationGuild = guildWar.getInitiationGuild();
        Guild receivingGuild = guildWar.getReceivingGuild();
        Config warConfig = GuildPlugin.getInstance().getWarConfig();

        HashMap<String, List<String>> rewardMap = (HashMap<String, List<String>>) warConfig.get("rewards");
        List<String> itemList = rewardMap.get("item");
        ArrayList<Item> items = new ArrayList<>();
        for (String stringData : itemList) {
            items.add(Utils.parseItem(stringData));
        }


        if (killerCredits > deathCredits) {
            initiationGuild.setVictory(initiationGuild.getVictory() + 1);
            WarHandler.addItems(initiationGuild, items);
        } else {
            receivingGuild.setVictory(receivingGuild.getVictory() + 1);
            WarHandler.addItems(receivingGuild, items);
        }

        GuildUtils.saveGuildConfig(initiationGuild);
        GuildUtils.saveGuildConfig(receivingGuild);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (WarHandler.guildWarCache.size() < 1) return;
        GuildWar guildWar = WarHandler.guildWarCache.get(0);
        Language lang = GuildPlugin.getInstance().getLang();
        Guild initiationGuild = guildWar.getInitiationGuild();
        Guild receivingGuild = guildWar.getReceivingGuild();

        if (initiationGuild.getMembers().stream().filter(member -> member.getName().equals(player.getName())).collect(Collectors.toList()).size() > 0) {
            WarHandler.tpPosByGuild(player, initiationGuild, lang.translateString("guild-war-start"));
        }

        if (receivingGuild.getMembers().stream().filter(member -> member.getName().equals(player.getName())).collect(Collectors.toList()).size() > 0) {
            WarHandler.tpPosByGuild(player, receivingGuild, lang.translateString("guild-war-start"));
        }
    }


}
