package me.borawski.hcf.barrier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.borawski.hcf.session.Region;
import me.borawski.hcf.session.RegionHandler;

public class BarrierTask implements Runnable {

    private static List<UUID> toClear = new ArrayList<>();

    private HashMap<UUID, Set<Block>> cache = new HashMap<>();

    @SuppressWarnings("deprecation")
    @Override
    public void run() {
        Player p;
        for (UUID uuid : TagHandler.getTaggedPlayers()) {
            p = Bukkit.getPlayer(uuid);
            if (p == null) {
                continue;
            }
            Set<Block> localCache = cache.get(uuid);
            if (localCache == null) {
                localCache = new HashSet<>();
            }

            for (Region r : RegionHandler.getInstance().getRegions()) {
                for (Block b : r.getRegion().getWallBlocks(Bukkit.getWorld(r.getWorld()))) {
                    if (b.getType() == Material.AIR) {
                        if (b.getLocation().distanceSquared(p.getLocation()) <= r.getViewDistance() * r.getViewDistance()) {
                            p.sendBlockChange(b.getLocation(), r.getBarrierMaterial(), (byte) r.getBarrierMaterialData());
                            localCache.add(b);
                        } else if (localCache.contains(b)) {
                            p.sendBlockChange(b.getLocation(), 0, (byte) 0);
                            localCache.remove(b);
                        }
                    }
                }
            }
            cache.put(uuid, localCache);
        }
        for (UUID uuid : toClear) {
            Player pl = Bukkit.getPlayer(uuid);
            if (pl != null) {
                Set<Block> localCache = cache.get(uuid);
                if (localCache == null) {
                    continue;
                }
                for (Block block : localCache) {
                    if (block.getType() == Material.AIR) {
                        pl.sendBlockChange(block.getLocation(), 0, (byte) 0);
                    }
                }
            }
        }
        toClear.clear();

    }

    public static void addToClear(UUID uuid) {
        toClear.add(uuid);
    }

}