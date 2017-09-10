package com.desiremc.hcf.session;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;

@Entity(value = "regions", noClassnameStored = true)
@Indexes(@Index(fields = @Field(value = "name") , options = @IndexOptions(unique = true) ))
public class Region
{

    @Id
    private int id;

    private String name;

    private String world;

    @Embedded
    private RegionBlocks region;

    private Material barrierMaterial;
    private short barrierMaterialData;

    private int viewDistance;

    @SuppressWarnings("deprecation")
    public Region(String name, String world, RegionBlocks region, MaterialData barrierMaterial, int viewDistance)
    {
        this.name = name;
        this.world = world;
        this.region = region;
        this.setBarrierMaterial(barrierMaterial.getItemType());
        this.barrierMaterialData = barrierMaterial.getData();
        this.viewDistance = viewDistance;
    }

    public Region()
    {
    }
    
    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getName()
    {
        return name;
    }

    public String getWorld()
    {
        return world;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setViewDistance(int viewDistance)
    {
        this.viewDistance = viewDistance;
    }

    public RegionBlocks getRegion()
    {
        return region;
    }

    public int getViewDistance()
    {
        return viewDistance;
    }

    public void setRegion(String world, RegionBlocks region)
    {
        this.world = world;
        this.region = region;
    }

    public short getBarrierMaterialData()
    {
        return barrierMaterialData;
    }

    public void setBarrierMaterialData(short barrierMaterialData)
    {
        this.barrierMaterialData = barrierMaterialData;
    }

    public Material getBarrierMaterial()
    {
        return barrierMaterial;
    }

    public void setBarrierMaterial(Material barrierMaterial)
    {
        this.barrierMaterial = barrierMaterial;
    }

}