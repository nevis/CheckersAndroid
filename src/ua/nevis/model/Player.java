package ua.nevis.model;

public class Player {
	private String name;
    private int hashCode;
    public Player(String _name, int _hashCode) {
        name = _name;
        hashCode = _hashCode;
    }
    public String getName() {
        return this.name;
    }
    public int getHashCode() {
        return this.hashCode;
    }
}
