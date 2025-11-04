package ch.bbw.zork;

/**
 * Class Searchable - represents an object that can be searched
 */
public class Searchable {
    
    private String name;
    private String description;
    private Item item;
    private boolean searched;
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
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Item getItem() {
        return item;
    }
    
    public boolean isSearched() {
        return searched;
    }
    
    public void setSearched(boolean searched) {
        this.searched = searched;
    }
    
    public boolean isVent() {
        return isVent;
    }
    
    public boolean isDoor() {
        return isDoor;
    }
    
    public void setDoor(boolean isDoor) {
        this.isDoor = isDoor;
    }
    
    public String getRequiredItem() {
        return requiredItem;
    }
    
    public void setRequiredItem(String requiredItem) {
        this.requiredItem = requiredItem;
    }
    
    public boolean isPasswordProtected() {
        return isPasswordProtected;
    }
    
    public void setPasswordProtected(boolean passwordProtected) {
        this.isPasswordProtected = passwordProtected;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public boolean isGate() {
        return isGate;
    }
    
    public void setGate(boolean gate) {
        this.isGate = gate;
    }
}
