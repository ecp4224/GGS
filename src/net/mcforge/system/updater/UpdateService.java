package net.mcforge.system.updater;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

import net.mcforge.server.Server;
import net.mcforge.server.Tick;
import net.mcforge.util.FileUtils;

public class UpdateService implements Tick {
	private final UpdateManager um = new UpdateManager();
	private ArrayList<Updatable> queue = new ArrayList<Updatable>();
	private ArrayList<String> restart = new ArrayList<String>();
	private int wait = 0;
	private Server server;
	private boolean update;
	private UpdateType defaulttype;

	public UpdateService(Server server) {
		this.server = server;
		this.server.Add(this);
		defaulttype = UpdateType.parse(this.server.getSystemProperties().getValue("default_update_type"));
		try {
			load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		applyUpdates();
	}

	public Server getServer() {
		return server;
	}

	/**
	 * Get the {@link UpdateManager}, this object handles all the
	 * objects that will be updated.
	 * @return
	 *        The {@link UpdateManager} object
	 */
	public final UpdateManager getUpdateManager() {
		return um;
	}

	public void checkAll() {
		if (update)
			return;
		queue.clear();
		for (int i = 0; i < um.getUpdateObjects().size(); i++) {
			Updatable u = um.getUpdateObjects().get(i);
			if (restart.contains(u))
				continue;
			URL url;
			try {
				url = new URL(u.getCheckURL());
				BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
				String str;
				while ((str = in.readLine()) != null) {
					if (!str.equals(u.getCurrentVersion()))
						queue.add(u);
				}
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (queue.size() > 0)
			update = true;
	}

	public void update(Updatable u) {
		UpdateType type = u.getUpdateType();
		if (type.getType() < defaulttype.getType())
			type = defaulttype;
		if (type == UpdateType.Auto_Silent || type == UpdateType.Auto_Notify) {
			u.unload();
			try {
				downloadFile(u.getDownloadURL(), u.getDownloadPath());
				server.getPluginHandler().loadFile(server, new File(u.getDownloadPath()));
				if (type == UpdateType.Auto_Notify)
					server.Log(u.getDownloadPath() + " has been updated!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if (type == UpdateType.Auto_Silent_Restart || type == UpdateType.Auto_Notify_Restart) {
			restart.add(u.getDownloadURL() + "@@" + u.getDownloadPath() + "@@" + type.getType());
			if (type == UpdateType.Auto_Notify_Restart)
				server.Log(u.getDownloadPath() + " will be updated after a restart!");
			save();
		}
		else if (type == UpdateType.Ask) {
			//TODO Ask the user
		}
	}

	@Override
	public void tick() {
		if (wait < 3000 && !update) {
			wait++;
			return;
		}
		wait = 0;
		if (!update)
			checkAll();
		else {
			for (int i = 0; i < queue.size(); i++) {
				update(queue.get(i));
			}
			update = false;
		}
	}


	private void save() {
		try {
			if (!new File("system").exists())
				new File("system").mkdir();
			FileUtils.createIfNotExist("system/restart.cache");
			PrintWriter out = new PrintWriter("system/restart.cache");
			for (String s : restart) {
				out.println(s);
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void load() throws IOException {
		if (!new File("system/restart.cache").exists())
			return;
		FileInputStream fstream = new FileInputStream("system/restart.cache");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String strLine;
		while ((strLine = br.readLine()) != null)   {
			if (strLine.startsWith("#"))
				continue;
			restart.add(strLine);
		}
		in.close();
		new File("system/restart.cache").delete();
	}
	
	private void applyUpdates() {
		for (String s : restart) {
			if (s.split("\\@@").length != 3)
				continue;
			String url = s.split("\\@@")[0];
			String path = s.split("\\@@")[1];
			UpdateType ut = UpdateType.parse(Integer.parseInt(s.split("\\@@")[2]));
			try {
				downloadFile(url, path);
			} catch (IOException e) {
				e.printStackTrace();
			}
			//We dont need to load the plugin, the server will do that.
			if (ut == UpdateType.Auto_Notify_Restart)
				server.Log("Updates for " + path + " have been applied!");
		}
	}
	
	private void downloadFile(String url, String path) throws IOException {
		URL website = new URL(url);
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream(path);
		fos.getChannel().transferFrom(rbc, 0, 1 << 24);
		fos.close();
	}
}
