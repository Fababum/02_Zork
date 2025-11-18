package ch.bbw.zork;

/**
 * Class Searchable - represents an object that can be searched
 */
public class Searchable {
    
    // Name shown to the player when referencing this object (e.g. "drawer")
    private String name;
    // Short description used in room text
    private String description;
    // Optional item hidden in this searchable object
    private Item item;
    // Whether this object was already searched
    private boolean searched;
    // Special flags used to handle some objects differently
    private boolean isVent;
    private boolean isDoor;
    private String requiredItem;
    private boolean isPasswordProtected;
    private String password;
    private boolean isGate;
    
    public Searchable(String name, String description, Item item) {
        this.name = name;
        this.description = description;
        this.item = item;
        this.searched = false;
        this.isVent = false;
        this.isDoor = false;
        this.requiredItem = null;
        this.isPasswordProtected = false;
        this.password = null;
        this.isGate = false;
    }
    
    public Searchable(String name, String description, Item item, boolean isVent) {
        this.name = name;
        this.description = description;
        this.item = item;
        this.searched = false;
        this.isVent = isVent;
        this.isDoor = false;
        this.requiredItem = null;
        this.isPasswordProtected = false;
        this.password = null;
        this.isGate = false;
    }
    
    public String getName() {
        // Return the identifier players use when searching (e.g. "drawer")
        return name;
    }
    
    public String getDescription() {
        // Short, human-readable description
        return description;
    }
    
    public Item getItem() {
        // Return an item contained in this object (may be null)
        return item;
    }
    
    public boolean isSearched() {
        // True when the player already looked here
        return searched;
    }
    
    public void setSearched(boolean searched) {
        this.searched = searched;
    }
    
    public boolean isVent() {
        // True when this searchable is a vent (special traversal logic)
        return isVent;
    }
    
    public boolean isDoor() {
        // True when this searchable represents a locked/interactive door
        return isDoor;
    }
    
    public void setDoor(boolean isDoor) {
        this.isDoor = isDoor;
    }
    
    public String getRequiredItem() {
        // Name of the item required to open this door/gate (may be null)
        return requiredItem;
    }
    
    public void setRequiredItem(String requiredItem) {
        this.requiredItem = requiredItem;
    }
    
    public boolean isPasswordProtected() {
        // True when this object requires a password to open
        return isPasswordProtected;
    }
    
    public void setPasswordProtected(boolean passwordProtected) {
        this.isPasswordProtected = passwordProtected;
    }
    
    public String getPassword() {
        // The password needed to open (when password-protected)
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isGate() {
        // True when this searchable is an escape gate/hatch
        return isGate;
    }
    
    public void setGate(boolean gate) {
        this.isGate = gate;
    }
}
