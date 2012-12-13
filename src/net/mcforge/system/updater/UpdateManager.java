/*******************************************************************************
 * Copyright (c) 2012 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package net.mcforge.system.updater;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class UpdateManager {
    private ArrayList<Updatable> updatelist = new ArrayList<Updatable>();

    /**
     * This method can only be called by the {@link Updateable} object being
     * removed
     * @param object
     * @throws IllegalAccessException
     */
    public void remove(Updatable object) throws IllegalAccessException {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        try {
            StackTraceElement e = stacks[2];
            Class<?> class_ = Class.forName(e.getClassName());
            class_.asSubclass(Updatable.class);
            if (object.getClass() == class_) {
                if (updatelist.contains(object))
                    updatelist.remove(object);
                return;
            }
        } catch (ClassNotFoundException e1) { }
        catch (ClassCastException e2) { }
        catch (ArrayIndexOutOfBoundsException e3) { }

        throw new IllegalAccessException("You can only call this method by the Updatable object being removed");
    }

    /**
     * Add an object that can be updated
     * @param object
     *              The updatable object.
     */
    public void add(Updatable object) {
        if (!updatelist.contains(object))
            updatelist.add(object);
    }

    /**
     * Get a list of objects that will be updating
     * @return
     *        An {@link ArrayList> of udpatable objects
     */
    public ArrayList<Updatable> getUpdateObjects() {
        return updatelist;
    }

    public boolean checkUpdateServer(Updatable object) {
        try {
            URL url = new URL(object.getCheckURL());

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.connect();
            if (con.getResponseCode() != HttpURLConnection.HTTP_OK)
                return false;
            url = new URL(object.getCheckURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

