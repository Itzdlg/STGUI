# STGUI

STGUI is a Java library for creating & managing Spigot GUI's effortlessly

## Installation

1. Clone STGUI to a local computer
2. Build the project
3. Install STGUI into your local repository

## Maven

To install STGUI into your local repository, use the following batch command in the project folder
```
mvn install:install-file -Dfile=target/STGUI.jar -DgroupId=me.schooltests -DartifactId=stgui -Dversion=1.0 -Dpackaging=jar
```

Then, import and shade the project

```
<dependencies>
    <dependency>
        <groupId>me.schooltests</groupId>
        <artifactId>stgui</artifactId>
        <version>1.0</version>
        <type>jar</type>
        <scope>compile</scope>
    </dependency>
</dependencies>

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.0.0</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <artifactSet>
                            <includes>
                                <include>me.schooltests</include>
                            </includes>
                        </artifactSet>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

## Usage

```
ChestGUI gui = new ChestGUI(plugin, ChatColor.BLUE + "GUI Test", 6);

FillerPane background = new FillerPane(6, 9, new GUIPosition(0, 0));
background.addRawItems(new ItemStack(Material.SAND), new ItemStack(Material.SANDSTONE));
gui.addPane(background);

PaginatedPane pageView = new PaginatedPane(3, 7, new GUIPosition(1, 1));
List<GUIItem> items = new ArrayList<>();
for (int i = 1; i <= 128; i++) {
    final int j = i;
    ItemStack item = new ItemBuilder(Material.WOOL)
            .data(i <= 64 ? DyeColor.WHITE : DyeColor.RED)
            .amount(i <= 64 ? i : i - 64)
            .name("&7Item: &6" + i)
            .get();
    GUIItem guiItem = new GUIItem(item, (click) ->
            click.getPlayer().sendMessage(ChatColor.GRAY + "You have clicked item number " + ChatColor.GOLD + j));
    items.add(guiItem);
}

pageView.setItems(items);
gui.addPane(pageView);

gui.setItem(4, 6, new GUIItem(DefaultItems.BACKWARD, (click) -> {
  pageView.backward();
  gui.draw();
}));

gui.setItem(4, 7, new GUIItem(DefaultItems.FORWARD, (click) -> {
  pageView.forward();
  gui.draw();
}));

gui.open(player);
```

In Game: https://imgur.com/b2NHbGd