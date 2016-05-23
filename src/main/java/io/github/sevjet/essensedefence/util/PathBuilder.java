package io.github.sevjet.essensedefence.util;

import com.jme3.cinematic.MotionPath;
import com.jme3.math.Vector3f;
import io.github.sevjet.essensedefence.entity.building.Building;
import io.github.sevjet.essensedefence.entity.building.Fortress;
import io.github.sevjet.essensedefence.field.MapField;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

public class PathBuilder {

    private MapField field;
    private Point start;
    private float baseZ = 0;
    private Class<? extends Building> buildingClass = Fortress.class;
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

    public PathBuilder from(final int x, final int y) {
        final Point point = new Point(x, y);
        if (!isInMap(point)) {
            throw new IllegalArgumentException("'From' coordinates is out of the field");
        }

        this.start = point;
        return this;
    }

    public PathBuilder floatingAt(final float z) {
        this.baseZ = z;

        return this;
    }

    public PathBuilder to(final Class<? extends Building> buildingClass) {
        if (buildingClass == null) {
            throw new IllegalArgumentException("Building Class can not be null");
        }

        this.buildingClass = buildingClass;
        return this;
    }

    public MotionPath build() {
        if (field == null || start == null || buildingClass == null) {
            throw new IllegalStateException("PathBuilder is not initialized yet");
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
                queueNearest(queue, current, new Point(current.x + 1, current.y), '↑');
                queueNearest(queue, current, new Point(current.x - 1, current.y), '↓');
                queueNearest(queue, current, new Point(current.x, current.y + 1), '←');
                queueNearest(queue, current, new Point(current.x, current.y - 1), '→');
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
            list.add(new Point(current));
            Collections.reverse(list);

            MotionPath path = new MotionPath();
            for (Point point : list) {
                path.addWayPoint(new Vector3f(point.x, point.y, baseZ));
            }
            return path;
        }

        return null;
    }

    private void queueNearest(final Queue<Point> queue, final Point parent, final Point next, final char dir) {
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
        return field.getCell(point.x, point.y).isPassable();
    }

    private boolean isFinish(final Point point) {
        return field.getCell(point.x, point.y).contains(buildingClass);
    }

}
