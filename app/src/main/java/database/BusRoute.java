package database;

/**
 * Created by nicolas on 26/11/2017.
 */

public class BusRoute {

    private String id;
    private String routeShortName;
    private String routeLongName;
    private String routeDescritpion;
    private String routetype;
    private String routeColor;
    private String routeTextColor;

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getRouteShortName() {
        return routeShortName;
    }

    public void setRouteShortName(String routeShortName) {
        this.routeShortName = routeShortName;
    }

    public String getRouteLongName() {
        return routeLongName;
    }

    public void setRouteLongName(String routeLongName) {
        this.routeLongName = routeLongName;
    }

    public String getRouteDescritpion() {
        return routeDescritpion;
    }

    public void setRouteDescritpion(String routeDescritpion) { this.routeDescritpion = routeDescritpion; }

    public String getRoutetype() {
        return routetype;
    }

    public void setRoutetype(String routetype) {
        this.routetype = routetype;
    }

    public String getRouteColor() {
        return routeColor;
    }

    public void setRouteColor(String routeColor) {
        this.routeColor = routeColor;
    }

    public String getRouteTextColor() {
        return routeTextColor;
    }

    public void setRouteTextColor(String routeTextColor) {
        this.routeTextColor = routeTextColor;
    }

}
