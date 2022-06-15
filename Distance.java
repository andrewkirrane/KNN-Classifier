/**
 * Class that saves the distance information between points
 */
public class Distance {
    private double distance;
    private String className;

    public Distance(double distance, String className) {
        this.distance = distance;
        this.className = className;
    }

    public double getDistance() {
        return distance;
    }

    public String getClassName() {
        return className;
    }
}