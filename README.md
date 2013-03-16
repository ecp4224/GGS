#MCForge Vanilla

![MCForge Logo](http://www.mcforge.net/community/public/style_images/4_logo.png)

MCForge minecraft server software

##Important stuff for Modders / Forkers
- [MCForge Wiki](https://github.com/MCForge/MCForge-Vanilla/wiki)

##Issues and Pull requests
- Found a bug? [Report it](https://github.com/MCForge/MCForge-Core/issues)
- [Make a pull request](https://github.com/MCForge/MCForge-Core/pulls)

##About MCForge Vanilla

MCForge is a Minecraft Server that can run a Minecraft Classic Custom Server and a Minecraft Premium Custom Server. 

It has been released under an open-source license to allow the community to help us develop it further.  We welcome all pull requests and will merge them in at our own discretion.

The official website for MCForge is [mcforge.net][1]

##Coding Convention

Please follow these rules when making pull requests

1. All public methods/variables must hava javadocs. Javadocs must document what the method does, what each parameter stands for, and document why the method might throw an exception (if one is thrown). It also helps to document what the method will call using @link. If the method is overriding another method, and the method being overrided already has documentation, that method does not need javadocs.

2. Try to follow the [Oracle Naming Convention](http://www.oracle.com/technetwork/java/javase/documentation/codeconventions-135099.html#367)

3. Put //TODO or //FIXME if something needs to be done or fixed in the source code at a later date

4. All public facing methods must raise exceptions if something is wrong. Its better to catch a bug that may happen than let it grow.

5. Follow Oracle Code Convention [10.5.1][4], [10.5.2][5], and [10.5.3][6]

6. Use 4 spaces

##Required Dependencies
- [MySQL Driver][7]
- [SQLite Driver][8]
- [Kryo][10]
- [GSon][11]
- [Bukkit][12]

##Building with Maven
The source has a maven script for required dependencies, so just run 'mvn clean install' in the source directory
to build the source.

##Copyright/License

Unless otherwise noted, this software and its source code is
Copyright 2011 MCForge. Dual-licensed under the [GNU General Public License, Version 3][2] and the [Educational Community License, Version 2.0][3]

In an effort to add copyright notices to the source code, we utilized automated methods to insert these.
Some copyright attributions may be incorrect.  If you identify any such cases, please notify us immediately so we may correct it.

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License, version 3
along with this program.  If not, see <http://www.gnu.org/licenses/>.

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
