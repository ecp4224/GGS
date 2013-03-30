/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.entity;

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

import net.mcforge.system.ticker.Tick;

public abstract class Entity implements org.bukkit.entity.Entity, Tick  {

    @Override
    public List<MetadataValue> getMetadata(String arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasMetadata(String arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeMetadata(String arg0, Plugin arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setMetadata(String arg0, MetadataValue arg1) {
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
    public Location getLocation(Location arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getMaxFireTicks() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<org.bukkit.entity.Entity> getNearbyEntities(double arg0,
            double arg1, double arg2) {
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
    public void playEffect(EntityEffect arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void remove() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setFallDistance(float arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setFireTicks(int arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setLastDamageCause(EntityDamageEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean setPassenger(org.bukkit.entity.Entity arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void setTicksLived(int arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void setVelocity(Vector arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean teleport(Location arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean teleport(org.bukkit.entity.Entity arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean teleport(Location arg0, TeleportCause arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean teleport(org.bukkit.entity.Entity arg0, TeleportCause arg1) {
        // TODO Auto-generated method stub
        return false;
    }

}
