package io.github.sevjet.essencedefence.util;

import com.jme3.cinematic.MotionPath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import io.github.sevjet.essencedefence.entity.Entity;
import io.github.sevjet.essencedefence.entity.building.Building;
import io.github.sevjet.essencedefence.entity.building.Fortress;
import io.github.sevjet.essencedefence.entity.building.Portal;
import io.github.sevjet.essencedefence.field.MapCell;
import io.github.sevjet.essencedefence.field.MapField;

import java.awt.*;
import java.util.*;
import java.util.Queue;

public class PathBuilder {

    private MapField field = null;
    private Point start = null;
    private Vector3f base = Vector3f.ZERO;
    private Set<Class<? extends Entity>> startClasses = new LinkedHashSet<>();
    private Class<? extends Building> finishClass = Fortress.class;
    private Class<? extends Building> buildingClass = null;
    private Point buildingAt = null;
    private BoxSize buildingSize = null;
    private char[][] previous;

    private PathBuilder(final MapField field) {
        if (field == null) {
            throw new IllegalArgumentException("Field can not be null");
        }

        this.field = field;
    }

    public static PathBuilder atField(final MapField field) {
        return new PathBuilder(field);
    }

    public PathBuilder from(final Set<Class<? extends Entity>> startClasses) {
        if (startClasses.size() == 0) {
            throw new IllegalArgumentException("Start Entities Classes list can not be empty");
        }

        this.startClasses.clear();
        this.startClasses.addAll(startClasses);
        return this;
    }

    public PathBuilder from(Class<? extends Entity> startClass) {
        startClasses.clear();
        return andFrom(startClass);
    }

    public PathBuilder andFrom(Class<? extends Entity> startClass) {
        if (startClass == null) {
            throw new IllegalArgumentException("Finish Building Class can not be null");
        }
        startClasses.add(startClass);
        return this;
    }

    public PathBuilder from(final int x, final int y) {
        final Point point = new Point(x, y);
        if (!isInMap(point)) {
            throw new IllegalArgumentException("'From' coordinates is out of the field");
        }

        this.start = point;
        return this;
    }

    public PathBuilder floatingAt(final Vector3f base) {
        this.base = base;

        return this;
    }

    public PathBuilder to(final Class<? extends Building> finishClass) {
        if (finishClass == null) {
            throw new IllegalArgumentException("Finish Building Class can not be null");
        }

        this.finishClass = finishClass;
        return this;
    }

    public PathBuilder withBuilding(final Class<? extends Building> buildingClass) {
        if (buildingClass == null) {
            throw new IllegalArgumentException("Building Class can not be null");
        }

        this.buildingClass = buildingClass;
        return this;
    }

    public PathBuilder atPoint(final int x, final int y) {
        final Point point = new Point(x, y);
        if (!isInMap(point)) {
            throw new IllegalArgumentException("Building 'at' Point coordinates is out of the field");
        }

        this.buildingAt = point;
        return this;
    }

    public PathBuilder withSize(BoxSize size) {
        if (size == null) {
            throw new IllegalArgumentException("Building Size can not be null");
        }

        this.buildingSize = size;
        return this;
    }

    public boolean isValid() {
        if (field == null ||
                startClasses.size() == 0 ||
                finishClass == null ||
                (buildingClass != null &&
                        (buildingAt == null || buildingSize == null))) {
            throw new IllegalStateException("PathBuilder is not initialized for isValid() yet");
        }

        MapCell cell;
        Queue<Point> queue = new LinkedList<>();
        previous = new char[field.getRows()][];
        for (int i = 0; i < field.getRows(); i++) {
            previous[i] = new char[field.getCols()];
            for (int j = 0; j < field.getCols(); j++) {
                previous[i][j] = ' ';
                cell = field.getCell(i, j);
                if (cell.hasContent() && finishClass.isInstance(cell.getContent())) {
                    queue.add(new Point(i, j));
                    previous[i][j] = '•';
                }
            }
        }

        Point current;
        while (queue.size() > 0) {
            current = queue.poll();

            queueNearest(queue, new Point(current.x + 1, current.y), '↑');
            queueNearest(queue, new Point(current.x - 1, current.y), '↓');
            queueNearest(queue, new Point(current.x, current.y + 1), '←');
            queueNearest(queue, new Point(current.x, current.y - 1), '→');
        }

        Node currentNode;
        Entity entity;
        for (Class<? extends Entity> clazz : startClasses) {
            currentNode = field.getObjects(clazz);
            if (currentNode != null) {
                for (Spatial spatial : currentNode.getChildren()) {
                    entity = spatial.getUserData("entity");
                    if (entity != null) {
                        char prev = previous[entity.getX()][entity.getY()];
                        previous[entity.getX()][entity.getY()] = '+';
                        if (prev == ' ') {
                            previous[entity.getX()][entity.getY()] = '-';
                            return false;
                        }
                    } else {
                        throw new IllegalStateException("Spatial in Node is not belongs to Entity " + clazz.getName());
                    }
                }
            }
        }

        return true;
    }

    public MotionPath build() {
        if (field == null ||
                start == null ||
                finishClass == null) {
            throw new IllegalStateException("PathBuilder is not initialized for build() yet");
        }

        previous = new char[field.getRows()][];
        for (int i = 0; i < field.getRows(); i++) {
            previous[i] = new char[field.getCols()];
            for (int j = 0; j < field.getCols(); j++) {
                previous[i][j] = ' ';
            }
        }
        ArrayList<Point> list = new ArrayList<>();
        Queue<Point> queue = new LinkedList<>();
        queue.add(start);
        previous[start.x][start.y] = '•';

        Point current;
        Point finish = null;
        while (queue.size() > 0) {
            current = queue.poll();

            if (isFinish(current)) {
                finish = current;
                queue.clear();
            } else {
                queueNearest(queue, new Point(current.x + 1, current.y), '↑');
                queueNearest(queue, new Point(current.x - 1, current.y), '↓');
                queueNearest(queue, new Point(current.x, current.y + 1), '←');
                queueNearest(queue, new Point(current.x, current.y - 1), '→');
            }
        }

        if (finish != null) {
            current = new Point(finish);
            while (previous[current.x][current.y] != '•') {
                list.add(new Point(current));
                switch (previous[current.x][current.y]) {
                    case '↑':
                        current.translate(-1, 0);
                        break;
                    case '↓':
                        current.translate(1, 0);
                        break;
                    case '←':
                        current.translate(0, -1);
                        break;
                    case '→':
                        current.translate(0, 1);
                        break;
                }
            }
            Collections.reverse(list);

            MotionPath path = new MotionPath();
            path.addWayPoint(base);
            for (Point point : list) {
                path.addWayPoint(new Vector3f(point.x, point.y, base.getZ()));
            }
            if(path.getNbWayPoints() == 2 && path.getWayPoint(0).equals(path.getWayPoint(1))) {
                return null;
            }
            return path;
        }

        return null;
    }

    private void queueNearest(final Queue<Point> queue, final Point next, final char dir) {
        if (isInMap(next) && isPassable(next) && previous[next.x][next.y] == ' ') {
            queue.add(next);
            previous[next.x][next.y] = dir;
        }
    }

    private boolean isInMap(final Point point) {
        return 0 <= point.x && point.x < field.getRows() &&
                0 <= point.y && point.y < field.getCols();
    }

    private boolean isPassable(final Point point) {
        if (buildingClass != null &&
                (!buildingClass.isAssignableFrom(Portal.class) &&
                        !buildingClass.isAssignableFrom(Fortress.class))) {
            Point a = new Point(buildingAt);
            Point b = new Point(a.x + buildingSize.getHeight(), a.y + buildingSize.getWidth());
            if (a.x <= point.x && point.x < b.x && a.y <= point.y && point.y < b.y) {
                return false;
            }
        }
        return field.getCell(point.x, point.y).isPassable();
    }

    private boolean isFinish(final Point point) {
        return field.getCell(point.x, point.y).contains(finishClass);
    }

}
