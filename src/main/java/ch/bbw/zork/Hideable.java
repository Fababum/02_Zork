package ch.bbw.zork;

/**
 * Class Hideable - represents a place where the player can hide
 */
public class Hideable {
    
    private String name;
    private String description;
    
    public Hideable(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
}
