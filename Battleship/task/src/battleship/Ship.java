package battleship;

public class Ship {

    private String[] coords;
    private boolean sunken;


    public Ship(String[] coords) {
        this.coords = coords;
        this.sunken = false;
    }

    public String[] getCoords() {
        return coords;
    }

    public boolean isSunken() {
        return sunken;
    }

    public void setSunken(boolean sunken) {
        this.sunken = sunken;
    }
}
