![Logo](http://i.imgur.com/JkNbVO5.png)

This is the old GGS code for Minecraft Classic/SMP.

No more development is being done on this software. All pull requests will be ignored

##Required Dependencies
- [MySQL Driver][7]
- [SQLite Driver][8]
- [Kryo][10]
- [GSon][11]
- [Bukkit][12]

##Documentation
Javadocs for GGS can be found in the docs folder in this repo

##Building with Maven
The source has a maven script for required dependencies, so just run 'mvn clean install' in the source directory
to build the source.

##Command-line arguments
- --smponly - This argument will only allow SMP clients to connect (buggy and non-working)
- --classiconly - This argument will only allow classic clients to connect

##GGS Plugin Pack
The GGS Plugin Pack can be found [here][13]


[1]: http://www.mcforge.net
[2]: http://www.gnu.org/licenses/gpl-3.0.html
[3]: http://www.opensource.org/licenses/ecl2.php
[4]: http://www.oracle.com/technetwork/java/javase/documentation/codeconventions-137265.html#331
[5]: http://www.oracle.com/technetwork/java/javase/documentation/codeconventions-137265.html#333
[6]: http://www.oracle.com/technetwork/java/javase/documentation/codeconventions-137265.html#353
[7]: http://www.mysql.com/downloads/connector/j/
[8]: http://mirror.nexua.org/Dependencies/sqlite-jdbc.jar
[9]: http://www.minecraft.net/classic/list
[10]: http://code.google.com/p/kryo/downloads/list
[11]: http://code.google.com/p/google-gson/downloads/list
[12]: http://dl.bukkit.org/latest-dev/bukkit.jar
[13]: https://github.com/hypereddie10/GGS-Plugin-Pack
