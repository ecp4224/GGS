/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.ep.ggs.entity;

import java.util.List;
import java.util.UUID;

import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import com.ep.ggs.system.ticker.Tick;


public abstract class Entity implements org.bukkit.entity.Entity, Tick  {

    @Override
    public List<MetadataValue> getMetadata(String metadataKey) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasMetadata(String metadataKey) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeMetadata(String metadatakey, Plugin owningPlugin) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setMetadata(String metadataKey, MetadataValue newMetadataValue) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean inSeperateThread() {
        return false;
    }

    @Override
    public int getTimeout() {
        return 20;
    }

    @Override
    public String tickName() {
        return "Entity" + getEntityId() + "-Tick";
    }

    @Override
    public boolean eject() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getEntityId() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public float getFallDistance() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getFireTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public EntityDamageEvent getLastDamageCause() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Location getLocation() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Location getLocation(Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMaxFireTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<org.bukkit.entity.Entity> getNearbyEntities(double x, double y, double z) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public org.bukkit.entity.Entity getPassenger() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Server getServer() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getTicksLived() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public EntityType getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UUID getUniqueId() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public org.bukkit.entity.Entity getVehicle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Vector getVelocity() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public World getWorld() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isDead() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isInsideVehicle() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isValid() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean leaveVehicle() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void playEffect(EntityEffect type) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void remove() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setFallDistance(float distance) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setFireTicks(int ticks) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent event) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean setPassenger(org.bukkit.entity.Entity passenger) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setTicksLived(int ticks) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setVelocity(Vector velocity) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean teleport(Location location) {  	
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean teleport(org.bukkit.entity.Entity destination) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean teleport(Location location, TeleportCause cause) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean teleport(org.bukkit.entity.Entity destination, TeleportCause cause) {
        // TODO Auto-generated method stub
        return false;
    }

}
