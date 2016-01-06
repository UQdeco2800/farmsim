package farmsim.util;

import farmsim.GameRenderer;
import farmsim.entities.tileentities.objects.Rock;
import farmsim.entities.tileentities.objects.Water;
import farmsim.entities.tileentities.pathfinding_test.Dir;
import farmsim.tiles.Tile;
import farmsim.world.World;
import farmsim.world.WorldManager;

import java.util.*;

public class PathFinder {

    public enum Strategy {
        NONE, BREADTH_FIRST, WEIGHTED_BREADTH_FIRST
    }

    /**
     * Find a path from a source to a destination using a given strategy
     * @param source
     *              the point to start the path at
     * @param destination
     *              the point at which the path ends
     * @param strategy
     *              the pathfinding strategy. Valid options:
     *                  - NONE: Don't even try, just get shortest path
     *                  - BREADTH_FIRST: performs a breadth-first search
     * @return
     *              the path if the strategy is valid and a path can be found,
     *              else an empty list
     */
    public LinkedList<Point> getPath(Point source, Point destination, Strategy strategy)
            throws NoPathException, IllegalArgumentException {
        LinkedList<Point> path = new LinkedList<>();
        Point current;
        Map<Point, Point> cameFrom;
        switch (strategy) {
            case NONE:
                current = destination;
                path.add(current);

                cameFrom = none(source, destination);

                while (current != source) {
                    current = cameFrom.get(current);
                    path.add(current);
                }
                if (!path.isEmpty()) {
                    return reverse(path);
                } else {
                    System.err.println("no path");
                    throw new NoPathException();
                }
            case BREADTH_FIRST:
                current = destination;
                path.add(current);

                cameFrom = breadthFirst(source, destination);

                while (current != source) {
                    current = cameFrom.get(current);
                    path.add(current);
                }
                if (!path.isEmpty()) {
                    return reverse(path);
                } else {
                    throw new NoPathException();
                }
            case WEIGHTED_BREADTH_FIRST:
                current = destination;
                path.add(current);

                cameFrom = breadthFirst(source, destination);

                while (current != source) {
                    current = cameFrom.get(current);
                    path.add(current);
                }
                if (!path.isEmpty()) {
                    return reverse(path);
                } else {
                    throw new NoPathException();
                }
            default:
                throw new IllegalArgumentException(String.format("Invalid strategy: %s", strategy));
        }
    }

    private Map<Point, Point> none(Point start, Point end) {
        Queue<Point> frontier = new LinkedList<>();
        Map<Point, Point> cameFrom = new HashMap<>();
        frontier.add(start);
        cameFrom.put(start, null);

        while (!frontier.isEmpty()) {
            Point current = frontier.remove();

            if (current.equals(end)) {
                break;
            }

            for (Point neighbour : getNeighbours(current, start)) {
                if (!cameFrom.containsKey(neighbour)) {
                    frontier.add(neighbour);
                    cameFrom.put(neighbour, current);
                }
            }
        }
        return cameFrom;
    }

    private Map<Point, Point> breadthFirst(Point start, Point end) {
        Queue<Point> frontier = new LinkedList<>();
        Map<Point, Point> cameFrom = new HashMap<>();
        frontier.add(start);
        cameFrom.put(start, null);

        while (!frontier.isEmpty()) {
            Point current = frontier.remove();

            if (current.equals(end)) {
                break;
            }

            for (Point neighbour : getNeighbours(current, start)) {
                if (!cameFrom.containsKey(neighbour) && Passable.passable(neighbour)) {
                    frontier.add(neighbour);
                    cameFrom.put(neighbour, current);
                }
            }
        }
        return cameFrom;
    }

    private Map<Point, Point> weightedBreadthFirst(Point start, Point end) {
        Queue<Point> frontier = new PriorityQueue<>();
        Map<Point, Point> cameFrom = new HashMap<>();
        frontier.add(start);
        cameFrom.put(start, null);

        while (!frontier.isEmpty()) {
            Point current = frontier.remove();

            if (current.equals(end)) {
                break;
            }

            for (Point neighbour : getNeighbours(current, start)) {
                if (!cameFrom.containsKey(neighbour) && Passable.passable(neighbour)) {
                    frontier.add(neighbour);
                    cameFrom.put(neighbour, current);
                }
            }
        }
        return cameFrom;
    }

    private ArrayList<Point> getNeighbours(Point centre, Point start) {
        ArrayList<Point> result = new ArrayList<>();
        double x = centre.getX();
        double y = centre.getY();
        World world = WorldManager.getInstance().getWorld();
        Point[] points = {new Point(x - 1, y - 1), new Point(x, y - 1),
                new Point(x + 1, y - 1), new Point(x + 1, y),
                new Point(x + 1, y + 1), new Point(x, y + 1),
                new Point(x - 1, y + 1), new Point(x - 1, y)};
        for (Point point : points) {
            if (point.getX() >= 0 && point.getX() < world.getWidth() &&
                    point.getY() >= 0 && point.getY() < world.getHeight()) {
                result.add(point);
            }
        }
        ArrayList<Point> sorted = new ArrayList<>();
        while (!result.isEmpty()) {
            Point closer = closest(result, (int) start.getX(), (int) start.getY());
            sorted.add(closer);
            result.remove(closer);
        }
        return sorted;
    }

    private LinkedList<Point> reverse(LinkedList list) {
        Object[] data = list.toArray();
        LinkedList<Point> result = new LinkedList<>();
        for (int i = data.length - 1; i >= 0; i--) {
            result.add((Point) data[i]);
        }
        return result;
    }

    private ArrayList<Point> reverse(ArrayList list) {
        Object[] data = list.toArray();
        ArrayList<Point> result = new ArrayList<>();
        for (int i = data.length - 1; i >= 0; i--) {
            result.add((Point) data[i]);
        }
        return result;
    }

    /**
     * Return the nearest point to point (x,y)
     * @param points
     *              a list of points
     * @param x
     *              x-coord of point to check distance from
     * @param y
     *              y-coord of point to check distance from
     * @return
     *              the nearest point to point (x,y) - if multiple point have
     *              the same distance, one is returned
     */
    private Point closest(ArrayList<Point> points, int x, int y) {
        double minDistance = getDistance(x, y, (int) points.get(0).getX(),
                (int) points.get(0).getY());
        Point minPoint = points.get(0);
        for (Point point : points) {
            if (getDistance(x, y, (int) point.getX(),
                    (int) point.getY()) < minDistance) {
                minDistance = getDistance(x, y, (int) point.getX(),
                        (int) point.getY());
                minPoint = point;
            }
        }

        return minPoint;
    }

    /**
     * Get the distance from point (x1,y1) to point (x2,y2)
     * @param x1
     *          x-coord of first point
     * @param y1
     *          y-coord of first point
     * @param x2
     *          x-coord of second point
     * @param y2
     *          y-coord of second point
     * @return
     *          distance between points, as calculated using Pythagoras
     */
    private double getDistance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2));
    }
}
