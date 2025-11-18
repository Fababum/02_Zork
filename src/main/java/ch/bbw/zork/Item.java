package ch.bbw.zork;

/**
 * Item - small value object for things the player can pick up.
 *
 * Items have a short name used in commands and a longer description
 * shown in inventory lists.
 */
public class Item {
    
    private String name;
    private String description;
    
    public Item(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        // Return the machine-friendly name used in commands
        return name;
    }
    
    public String getDescription() {
        // Return a user-facing description for inventory output
        return description;
    }
}
