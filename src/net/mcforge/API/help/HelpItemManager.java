package net.mcforge.API.help;

import java.util.ArrayList;

import net.mcforge.API.EventHandler;
import net.mcforge.API.Listener;
import net.mcforge.API.plugin.Command;
import net.mcforge.API.plugin.CommandLoadEvent;
import net.mcforge.groups.Group;
import net.mcforge.server.Server;

public class HelpItemManager implements Listener {
    private ArrayList<HelpItem> helpitems = new ArrayList<HelpItem>();
    
    Server s;
    
    public void init(Server s) {
        this.s = s;
        s.getEventSystem().registerEvents(this);
    }
    
    public void deinit() {
        CommandLoadEvent.getEventList().unregister(this);
        helpitems.clear();
    }
    
    public boolean helpTopicExists(String type) {
        for (HelpItem h : helpitems) {
            if (h.getType().equals(type))
                return true;
        }
        return false;
    }
    
    public Command[] getCommands(String type, Group g) {
        ArrayList<Command> commands = new ArrayList<Command>();
        for (HelpItem h : helpitems) {
            if (h.getType().equals(type)) {
                if (h instanceof Command) {
                    Command c = (Command)h;
                    if (g.canExecute(c))
                        commands.add(c);
                }
            }
        }
        return commands.toArray(new Command[commands.size()]);
    }
    
    public String[] getHelpTypes() {
        ArrayList<String> types = new ArrayList<String>();
        for (HelpItem h : helpitems) {
            if (types.contains(h.getType()))
                continue;
            types.add(h.getType());
        }
        return types.toArray(new String[types.size()]);
    }
    
    @EventHandler
    public void onCommandLoad(CommandLoadEvent event) {
         Object obj = event.getCommand();
         if (obj instanceof HelpItem) {
             helpitems.add((HelpItem)obj);
             return;
         }
    }
}
