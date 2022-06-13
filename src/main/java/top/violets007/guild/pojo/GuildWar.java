package top.violets007.guild.pojo;

import top.violets007.guild.GuildPlugin;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * @author violets007
 * @description:
 */
public class GuildWar {

    private Guild initiationGuild;
    private Guild receivingGuild;
    private Date startTime;
    private WarStatus warStatus;
    // 发起挑战公会击杀数
    private int killerCredits = 0;
    // 发起挑战公会死亡数
    private int deathCredits = 0;
    private int joinGameTime = GuildPlugin.getInstance().getConfig().getInt("start-guild-war-time");
    private int battleAgainstTime = GuildPlugin.getInstance().getConfig().getInt("battle-against-time");
    private LinkedHashMap<String, Long> bossBarIds = new LinkedHashMap<String, Long>();

    public GuildWar(Guild initiationGuild, Guild receivingGuild, Date startTime, WarStatus warStatus) {
        this.initiationGuild = initiationGuild;
        this.receivingGuild = receivingGuild;
        this.startTime = startTime;
        this.warStatus = warStatus;
    }

    public Guild getInitiationGuild() {
        return initiationGuild;
    }

    public void setInitiationGuild(Guild initiationGuild) {
        this.initiationGuild = initiationGuild;
    }

    public Guild getReceivingGuild() {
        return receivingGuild;
    }

    public void setReceivingGuild(Guild receivingGuild) {
        this.receivingGuild = receivingGuild;
    }

    public Date getStartTime() {
        return startTime;
    }

    public int getBattleAgainstTime() {
        return battleAgainstTime;
    }

    public void setBattleAgainstTime(int battleAgainstTime) {
        this.battleAgainstTime = battleAgainstTime;
    }

    public LinkedHashMap<String, Long> getBossBarIds() {
        return bossBarIds;
    }

    public void setBossBarIds(LinkedHashMap<String, Long> bossBarIds) {
        this.bossBarIds = bossBarIds;
    }

    public int getKillerCredits() {
        return killerCredits;
    }

    public void setKillerCredits(int killerCredits) {
        this.killerCredits = killerCredits;
    }

    public int getDeathCredits() {
        return deathCredits;
    }

    public void setDeathCredits(int deathCredits) {
        this.deathCredits = deathCredits;
    }


    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public int getJoinGameTime() {
        return joinGameTime;
    }

    public void setJoinGameTime(int joinGameTime) {
        this.joinGameTime = joinGameTime;
    }

    public WarStatus getWarStatus() {
        return warStatus;
    }

    public void setWarStatus(WarStatus warStatus) {
        this.warStatus = warStatus;
    }
}
