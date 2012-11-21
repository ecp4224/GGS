package net.mcforge.system.updater;

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
}

