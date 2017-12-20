package com.desiremc.hcf.session.faction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.IdGetter;
import org.mongodb.morphia.annotations.Transient;

import com.desiremc.core.utils.BoundedArea;
import com.desiremc.core.utils.BukkitUtils;
import com.desiremc.hcf.DesireHCF;
import com.desiremc.hcf.session.FSession;
import com.desiremc.hcf.util.FactionsUtils;

/**
 * An implementation of a faction for HCF servers.
 * 
 * @author Michael Ziluck
 */
@Entity(noClassnameStored = true, value = "factions")
public class Faction
{

    @Id
    private int id;

    private String name;

    private String description;

    @Transient
    private String home;
    private Location parsedHome;

    private long founded;

    private long lastLogOff;

    private double balance;

    private double dtr;

    private int trophies;

    private int kothWins;

    private List<FSession> members;

    private Map<UUID, List<String>> announcements;

    private List<BoundedArea> claims;

    private FactionState factionState;

    private FactionType factionType;

    private List<Integer> enemies;
    private List<Faction> parsedEnemies;

    private List<Integer> allies;
    private List<Faction> parsedAllies;

    public Faction()
    {
        members = new LinkedList<>();
        announcements = new HashMap<>();
        claims = new LinkedList<>();
        enemies = new LinkedList<>();
        allies = new LinkedList<>();
    }

    /**
     * Returns the id of this faction. This value can never change. If a faction is deleted it still remains in the
     * database under the same id value, but it just has it's {@link FactionState} changed to
     * {@link FactionState#DELETED}.
     * 
     * @return the unique id for the faction.
     */
    @IdGetter
    public int getId()
    {
        return id;
    }

    /**
     * Sets the id for the faction. This should only ever be done by the {@link FactionHandler} when the faction is
     * created for the first time.
     * 
     * @param id the new id for the faction.
     */
    protected void setId(int id)
    {
        this.id = id;
    }

    /**
     * @return the name of the faction.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the lower case name of the faction.
     */
    public String getStub()
    {
        return name.toLowerCase();
    }

    /**
     * @param name the new name of the faction. This can be set and changed by the faction leader at any time.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the description of the faction. This can be set and changed by the faction leader at any time.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description the new description for the faction.
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Returns the home location of the faction. This can be null if the home location has not been set.
     * 
     * @return the home location of the faction.
     */
    public Location getHomeLocation()
    {
        if (home == null)
        {
            return null;
        }
        else if (parsedHome == null)
        {
            parsedHome = BukkitUtils.toLocation(home);
        }

        return parsedHome;
    }

    /**
     * Sets the new home location.
     * 
     * @param home the new home.
     */
    public void setHomeLocation(Location home)
    {
        this.parsedHome = home;
        this.home = BukkitUtils.toString(home);
    }

    /**
     * @return the unix timestamp of when the faction was created.
     */
    public long getFounded()
    {
        return founded;
    }

    /**
     * Sets the founded date. This should only be run once when the faction is first created.
     * 
     * @param founded the founded date.
     */
    protected void setFounded(long founded)
    {
        this.founded = founded;
    }

    /**
     * @return the Unix timestamp of the last time a player logged off while part of this faction.
     */
    public long getLastLogOff()
    {
        return lastLogOff;
    }

    /**
     * @param lastLogOff the new lastLogOffTime
     * @see Faction#getLastLogOff()
     */
    public void setLastLogOff(long lastLogOff)
    {
        this.lastLogOff = lastLogOff;
    }

    /**
     * @return a player's financial balance.
     */
    public double getBalance()
    {
        return balance;
    }

    /**
     * @param balance a player's new financial balance.
     */
    public void setBalance(double balance)
    {
        this.balance = balance;
    }

    /**
     * @param amount the amount to add to the player's balance.
     */
    public void depositBalance(double amount)
    {
        this.balance += amount;
    }

    /**
     * @param amount the amount to remove from a player's balance.
     */
    public void withdrawBalance(double amount)
    {
        this.balance -= amount;
    }

    /**
     * @return the amount of "deaths til raidable"
     */
    public double getDTR()
    {
        return dtr;
    }

    /**
     * @param dtr the new dtr value.
     */
    public void setDTR(double dtr)
    {
        this.dtr = dtr;
    }

    public double getMaxDTR()
    {
        switch (getMemberSize())
        {
            case 1:
                return 1.01;
            case 2:
                return 1.80;
            case 3:
                return 2.90;
            case 4:
                return 3.60;
            case 5:
            default:
                return 4.30;

        }
    }

    public double getMinDTR()
    {
        return -getMaxDTR();
    }

    /**
     * @return {@code true} if the faction's "deaths til raidable" is less than 0.
     * @see #getDTR()
     */
    public boolean isRaidable()
    {
        return dtr < 0;
    }

    /**
     * @return the amount of trophies this faction has.
     */
    public int getTrophies()
    {
        return trophies;
    }

    /**
     * @param trophies the new amount of trophies.
     */
    public void setTrophies(int trophies)
    {
        this.trophies = trophies;
    }

    /**
     * @return the amount of koth wins this faction has in total.
     */
    public int getKothWins()
    {
        return kothWins;
    }

    /**
     * @param kothWins the new amount of koth wins.
     */
    public void setKothWins(int kothWins)
    {
        this.kothWins = kothWins;
    }

    // TODO add comments for removeMember and addMember once those are made.
    /**
     * Returns all the members of the faction. The list that is returned is a view of the {@link List} created by
     * {@link Collections#unmodifiableList(List)} so it can't be edited at all. If you need to remove a member, use the
     * 
     * @return
     */
    public List<FSession> getMembers()
    {
        return Collections.unmodifiableList(members);
    }

    // TODO instead store the members that are currently online
    /**
     * Returns all online members of the faction. The returned list is not related to the actual members list and should
     * be stored for any time period other than the lifetime of a single method call.
     * 
     * @return the online members.
     */
    public List<FSession> getOnlineMembers()
    {
        List<FSession> online = new LinkedList<>(members);
        online.removeIf(member -> !member.isOnline());
        return online;
    }

    /**
     * Returns all offline members of the faction. The returned list is not related to the actual members list and
     * should be stored for any time period other than the lifetime of a single method call.
     * 
     * @return the offline members.
     */
    public List<FSession> getOfflineMembers()
    {
        List<FSession> offline = new LinkedList<>(members);
        offline.removeIf(member -> member.isOnline());
        return offline;
    }

    /**
     * @return the amount of members that this faction has.
     */
    public int getMemberSize()
    {
        return members.size();
    }

    /**
     * Gets a player's cached announcements. These are a list of all messages send out with /faction announce since the
     * player has last been on the server.
     * 
     * @param session the player's announcements to retrieve
     * @return a player's cached announcements.
     */
    public List<String> getAnnouncements(FSession session)
    {
        return announcements.get(session.getUniqueId());
    }

    /**
     * Adds an announcement for this player to get the next time they get on the server. If they have pending
     * announcements already it will take the new one on to the end of the list.
     * 
     * @param session the player to receive the announcement.
     * @param announcement the announcement.
     */
    public void addAnnouncement(FSession session, String announcement)
    {
        // get the pending announcements the player already has.
        List<String> pending = announcements.get(session.getUniqueId());

        // if they previously had no pending announcements, make a new list.
        if (pending == null)
        {
            pending = new ArrayList<>();
        }

        // add the new announcement
        pending.add(announcement);

        // add the pending announcements back to the HashMap
        // HashMaps don't allow duplicate keys so it removes the old one.
        announcements.put(session.getUniqueId(), pending);
    }

    /**
     * Clear the announcements that were sent to this player.
     * 
     * @param session the player's to clear.
     */
    public void clearAnnouncements(FSession session)
    {
        announcements.remove(session.getUniqueId());
    }

    /**
     * @return all claims owned by this faction.
     */
    public List<BoundedArea> getClaims()
    {
        return claims;
    }

    /**
     * Gets the current {@link FactionState} of the faction. A faction that is marked as {@link FactionState#DELETED}
     * should not exist in the {@link FactionHandler}'s loaded factions.
     * 
     * @return the current state of the faction.
     */
    public FactionState getState()
    {
        return factionState;
    }

    /**
     * Sets the {@link FactionState} of the faction to whatever is passed through the parameter. This should only be
     * done by the {@link FactionHandler}.
     * 
     * @param state the new state of the faction.
     */
    protected void setState(FactionState state)
    {
        this.factionState = state;
    }

    /**
     * Gets the type of faction. System and default factions are not able to be joined by players, however they are the
     * default for each player.
     * 
     * @return the type of the faction.
     */
    public FactionType getType()
    {
        return factionType;
    }

    /**
     * Sets the type of faction to whatever is passed through the parameter. This should only be done by the
     * {@link FactionHandler}.
     * 
     * @param factionType the new stype of the faction.
     */
    protected void setType(FactionType factionType)
    {
        this.factionType = factionType;
    }

    /**
     * @return {@code true} if this is the wilderness faction.
     */
    public boolean isWilderness()
    {
        return FactionHandler.getWilderness() == this;
    }

    /**
     * @return {@code true} if the {@link #getType()} is {@link FactionType#PLAYER}.
     */
    public boolean isNormal()
    {
        return getType() == FactionType.PLAYER;
    }

    /**
     * @return all enemy {@link Faction Factions}.
     */
    public List<Faction> getEnemies()
    {
        if (parsedEnemies == null)
        {
            parsedEnemies = new LinkedList<>();
            for (Integer id : enemies)
            {
                parsedEnemies.add(FactionsUtils.getFaction(id));
            }
        }
        return parsedEnemies;
    }

    /**
     * Add a faction as an enemy.
     * 
     * @param faction the faction to add as an enemy.
     */
    public void addEnemy(Faction faction)
    {
        enemies.add(faction.getId());
        getEnemies().add(faction);
    }

    /**
     * Remove a faction as an enemy.
     * 
     * @param faction the faction to remove as an enemy.
     */
    public void removeEnemy(Faction faction)
    {
        enemies.remove(faction.getId());
        getEnemies().remove(faction);
    }

    /**
     * Check if the given faction is an enemy.
     * 
     * @param faction the faction to check.
     * @return {@code true} if the given faction is an enemy.
     */
    public boolean isEnemy(Faction faction)
    {
        return getEnemies().contains(faction);
    }

    /**
     * @return all allied {@link Faction Factions}.
     */
    public List<Faction> getAllies()
    {
        if (parsedAllies == null)
        {
            parsedAllies = new LinkedList<>();
            for (Integer id : allies)
            {
                parsedAllies.add(FactionsUtils.getFaction(id));
            }
        }
        return parsedAllies;
    }

    /**
     * Add a faction as an ally.
     * 
     * @param faction the faction to add as an ally.
     */
    public void addAlly(Faction faction)
    {
        allies.add(faction.getId());
        getAllies().add(faction);
    }

    /**
     * Remove a faction as an ally.
     * 
     * @param faction the faction to remove as an ally.
     */
    public void removeAlly(Faction faction)
    {
        allies.remove(faction.getId());
        getAllies().remove(faction);
    }

    /**
     * Check if the given faction is an ally.
     * 
     * @param faction the faction to check.
     * @return {@code true} if the given faction is an ally.
     */
    public boolean isAlly(Faction faction)
    {
        return getAllies().contains(faction);
    }

    /**
     * Check the relationship between this faction and the passed faction.
     * 
     * @param faction the faction to check.
     * @return the relationship of the two factions.
     */
    public FactionRelationship getRelationshipTo(Faction faction)
    {
        if (getAllies().contains(faction))
        {
            return FactionRelationship.ALLY;
        }
        else if (getEnemies().contains(faction))
        {
            return FactionRelationship.ENEMY;
        }
        else if (faction == this)
        {
            return FactionRelationship.MEMBER;
        }
        else
        {
            return FactionRelationship.NEUTRAL;
        }
    }

    /**
     * Saves the faction asynchronously.
     */
    public void save()
    {
        Bukkit.getScheduler().runTaskAsynchronously(DesireHCF.getInstance(), new Runnable()
        {
            @Override
            public void run()
            {
                FactionHandler.getInstance().save(Faction.this);
            }
        });
    }

}
