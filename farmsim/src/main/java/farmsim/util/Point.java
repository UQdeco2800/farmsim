package farmsim.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Point implements Comparable<Point> {

    private double x;
    private double y;
    private LinkedList<Point> path = new LinkedList<>();

    public Point(double x, double y) {
        this.x = x;
        this.y = y;

    }

    public Point(Point point) {
        this.x = point.x;
        this.y = point.y;
    }

    public Point(String point) {
        Pattern pattern = Pattern.compile("\\[\\d+\\.\\d*\\,\\s\\d+\\.\\d*\\]");
        Matcher matcher = pattern.matcher(point);
        if(matcher.find()) {
            String commaSeparated = point.substring(1, point.length() -1);
            String[] values = commaSeparated.split(",");
            this.x = Double.parseDouble(values[0]);
            this.y = Double.parseDouble(values[1]);
        } else {
            this.x = 0;
            this.y = 0;
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distance(Point point) {
        return Math.sqrt(
                Math.pow(point.getX() - x, 2) + Math.pow(point.getY() - y, 2));
    }

    /**
     * Move the point towards a location
     * @param point
     *              the location to move towards
     * @param distance
     *              the distance to move each step (the speed)
     */
    public void moveToward(Point point, double distance) {
        if (distance(point) < distance) {
            this.x = point.x;
            this.y = point.y;
            if (path.peek() == point) {
                path.remove();
            }
            return;
        }
        double deltaX = this.x - point.x;
        double deltaY = this.y - point.y;
        double angle;

        angle = Math.atan2(deltaY, deltaX) + Math.PI;

        double xShift = Math.cos(angle) * distance;
        double yShift = Math.sin(angle) * distance;
        
        this.x += xShift;
        this.y += yShift;
    }
    
    public void pathToward(Point destination, double distance) throws NoPathException {
        if (path.isEmpty() || !path.getLast().equals(destination)) {
            PathFinder pathFinder = new PathFinder();
            path = pathFinder.getPath(new Point((int) this.x, (int) this.y), destination,
                    PathFinder.Strategy.BREADTH_FIRST);
        }
        moveToward(path.peek(), distance);
    }

    /**
     * Check whether next step is passable
     */
    public boolean nextStep(Point point, double distance) {
        if (distance(point) < distance) {
            return Passable.passable(new Point(this.x, this.y));
        }
        double deltaX = this.x - point.x;
        double deltaY = this.y - point.y;
        double angle;

        angle = Math.atan2(deltaY, deltaX) + Math.PI;

        double xShift = Math.cos(angle) * distance;
        double yShift = Math.sin(angle) * distance;

        double px = this.x + xShift;
        double py = this.y + yShift;
        return Passable.passable(new Point(px, py));
    }
    
    @Override
    public String toString() {
        return String.format("[%1$,.2f, %2$,.2f]", this.x, this.y);
    }
    
    /**
     * Takes a Point from a Task and converts it into a human friendly string.
     *
     * @return A String version of the point.
     */
    public String toNeatString() {
        return String.format("(%d,%d)", (int) x, (int) y);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Point)) {
            return false;
        }
        Point point = (Point) object;
        return (int) point.x == (int) this.x && (int) point.y == (int) this.y;
    }

    @Override
    public int hashCode() {
        return (int) (((13 * this.x) + this.y) * 17);
    }

    @Override
    public int compareTo(Point point) {
        if (this.y < point.y) {
            return -1;
        } else if ((int) this.y == (int) point.y) {
            if (this.x < point.x) {
                return -1;
            } else if ((int) this.x == (int) point.x) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }

    }

}
