package se.askware.aoc2025.dec09;

import se.askware.aoc2025.common.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Assignment extends AocBase {

    public static void main(String[] args) throws IOException {
        new Assignment().run();
    }

    @Override
    public long solvePartOne(List<String> input) {
        List<XYPair> list = input.stream().map(s -> XYPair.parse(s, ',')).toList();
        long max = 0;
        for (XYPair p1 : list) {
            for (XYPair p2 : list) {
                if (p1 != p2) {
                    long m = ((long) p1.getX() - p2.getX() + 1) * ((long) p1.getY() - p2.getY() + 1);
                    max = Math.max(max, m);
                }
            }
        }
        return max;
    }

    record LineX(int x, int y1, int y2) {
        boolean crosses(LineY yline) {
            if ((yline.y == y1 || yline.y == y2) &&
                    (yline.x1 == x || yline.x2 == x)) {
                return false;
            }
            return x >= yline.x1 && x <= yline.x2
                    && y1 <= yline.y && y2 >= yline.y;
        }
        
        List<XYPair> getPoints(){
            return IntStream.range(y1, y2 + 1).mapToObj(y -> new XYPair(x, y)).toList();
        }
    }

    record LineY(int y, int x1, int x2) {
        boolean crosses(LineX xline) {
            return xline.crosses(this);
        }
        List<XYPair> getPoints(){
            return IntStream.range(x1, x2 + 1).mapToObj(x -> new XYPair(x, y)).toList();
        }


    }

    public static boolean insidePoly(float[] vertx, float[] verty, float testx, float testy) {
        boolean inside = false;

        for (int i = 0, j = vertx.length - 1; i < vertx.length; j = i++) {
            if (((verty[i] > testy) != (verty[j] > testy)) &&
                    (testx < (vertx[j] - vertx[i]) * (testy - verty[i]) / (verty[j] - verty[i]) + vertx[i])) {
                inside = !inside;
            }
        }

        return inside;
    }

    public static boolean isPointInPolygon(XYPair p, XYPair[] polygon) {
        double minX = polygon[0].getX();
        double maxX = polygon[0].getX();
        double minY = polygon[0].getY();
        double maxY = polygon[0].getY();

        for (int i = 1; i < polygon.length; i++) {
            XYPair q = polygon[i];
            minX = Math.min(q.getX(), minX);
            maxX = Math.max(q.getX(), maxX);
            minY = Math.min(q.getY(), minY);
            maxY = Math.max(q.getY(), maxY);
        }

        if (p.getX() < minX || p.getX() > maxX || p.getY() < minY || p.getY() > maxY) {
            return false;
        }

        // https://wrf.ecse.rpi.edu/Research/Short_Notes/pnpoly.html
        boolean inside = false;
        for (int i = 0, j = polygon.length - 1; i < polygon.length; j = i++) {
            if ((polygon[i].getY() > p.getY()) != (polygon[j].getY() > p.getY()) &&
                    p.getX() < (polygon[j].getX() - polygon[i].getX()) *
                            (p.getY() - polygon[i].getY()) /
                            (polygon[j].getY() - polygon[i].getY()) +
                            polygon[i].getX()) {
                inside = !inside;
            }
        }

        return inside;
    }


    @Override
    public long solvePartTwo(List<String> input) {


        List<XYPair> list = new ArrayList<>(input.stream().map(s -> XYPair.parse(s, ',')).toList());
        float[] vertx = new float[list.size() + 1];
        float[] verty = new float[list.size() + 1];
        int idx = 0;
        for (XYPair xyPair : list) {
            vertx[idx] = xyPair.getX();
            verty[idx] = xyPair.getY();
            idx++;
        }
        vertx[list.size()] = vertx[0];
        verty[list.size()] = verty[0];


        List<XYPair> modified = new ArrayList<>(list);
        modified.add(list.getFirst());
        List<LineX> xlines = new ArrayList<>();
        List<LineY> ylines = new ArrayList<>();
        for (int i = 0; i < modified.size() - 1; i++) {
            XYPair xyPair = modified.get(i);
            XYPair xyPair1 = modified.get(i + 1);
            if (xyPair1.getY().equals(xyPair.getY())) {
                LineY lineY = new LineY(xyPair1.getY(), Math.min(xyPair.getX(), xyPair1.getX()), Math.max(xyPair.getX(), xyPair1.getX()));
                ylines.add(lineY);
            } else {
                LineX lineX = new LineX(xyPair1.getX(), Math.min(xyPair.getY(), xyPair1.getY()), Math.max(xyPair.getY(), xyPair1.getY()));
                xlines.add(lineX);
            }
        }
        System.out.println(ylines);


        char[][] grid1 = getGrid(list);

        //fill(ylines, grid1, xlines);
        print(grid1);

        long max = 0;
        list.add(list.getFirst());
        XYPair[] array = list.toArray(new XYPair[list.size()]);

        XYPair[] array1 = list.stream().map(p -> new XYPair(p.getX() + 1, p.getY())).toArray(XYPair[]::new);
        XYPair[] array2 = list.stream().map(p -> new XYPair(p.getX(), p.getY() + 1)).toArray(XYPair[]::new);
        XYPair[] array3 = list.stream().map(p -> new XYPair(p.getX() + 1, p.getY() + 1)).toArray(XYPair[]::new);

        for (int i = 0; i < grid1.length; i++) {
            for (int j = 0; j < grid1[0].length; j++) {
                grid1[i][j] =
                        isPointInPolygon(new XYPair(j, i), array) ||
                                isPointInPolygon(new XYPair(j, i), array1) ||
                                isPointInPolygon(new XYPair(j, i), array2) ||
                                isPointInPolygon(new XYPair(j, i), array3) ? 'o' : '.';
            }
        }
        print(grid1);

        for (int i = 0; i < list.size(); i++) {
            XYPair p1 = list.get(i);
            System.out.println(p1 + " " + max);
            for (int j = i; j < list.size(); j++) {
                XYPair p2 = list.get(j);
                if (p1 != p2 && p1.getX().intValue() != p2.getX().intValue() && p1.getY().intValue() != p2.getY().intValue()) {
                    LineX x1 = new LineX(p1.getX(), Math.min(p1.getY(), p2.getY()), Math.max(p1.getY(), p2.getY()));
                    LineX x2 = new LineX(p2.getX(), Math.min(p1.getY(), p2.getY()), Math.max(p1.getY(), p2.getY()));

                    LineY y1 = new LineY(p1.getY(), Math.min(p1.getX(), p2.getX()), Math.max(p1.getX(), p2.getX()));
                    LineY y2 = new LineY(p2.getY(), Math.min(p1.getX(), p2.getX()), Math.max(p1.getX(), p2.getX()));

                    XYPair pp1 = new XYPair(p1.getX(), Math.min(p1.getY(), p2.getY()));
                    XYPair pp2 = new XYPair(p1.getX(), Math.max(p1.getY(), p2.getY()));
                    XYPair pp3 = new XYPair(p2.getX(), Math.min(p1.getY(), p2.getY()));
                    XYPair pp4 = new XYPair(p2.getX(), Math.max(p1.getY(), p2.getY()));

                    long area = ((long) y1.x2 - y2.x1 + 1) * ((long) x1.y2 - x2.y1 + 1);
                    if (area < max){
                        continue;
                    }
//                    if (x1.y1 <50137 && x1.y2 > 50137){
//                        continue;
//                    }
                    
                    boolean outside = 
                            Stream.concat(
                            Stream.concat(x1.getPoints().stream(), x2.getPoints().stream()),
                            Stream.concat(y1.getPoints().stream(), y2.getPoints().stream())
                    ).parallel().anyMatch(xyPair -> {
                        
                        if (!isPointInPolygon(xyPair, array) &&
                                xlines.stream().noneMatch(
                                        x -> x.x == xyPair.getX() &&
                                                x.y1 <= xyPair.getY() && x.y2 >= xyPair.getY()) &&
                                ylines.stream().noneMatch(
                                        y -> y.y == xyPair.getY() &&
                                                y.x1 <= xyPair.getX() && y.x2 >= xyPair.getX())) {
                            return true;
                        }
                        return false;
                    });

                    if (!outside) {
                        /*
                        char[][] grid = getGrid(list);
                        fill(ylines, grid, xlines);
                        Stream.concat(
                                Stream.concat(x1.getPoints().stream(), x2.getPoints().stream()),
                                Stream.concat(y1.getPoints().stream(), y2.getPoints().stream())).forEach(p -> 
                                       grid[p.getY()][p.getX()] = isPointInPolygon(p, array) 
                                               || isPointInPolygon(p, array1)
                                               || isPointInPolygon(p, array2)
                                               || isPointInPolygon(p, array3)
                                               ? '@':'O'  );

                        print(grid);
                        
                         */
                        //fill(ylines, grid, xlines);

//                        grid[pp1.getY()][pp1.getX()] ='@';
//                        grid[pp2.getY()][pp2.getX()] = '@';
//                        grid[pp3.getY()][pp3.getX()] = '@';
//                        grid[pp4.getY()][pp4.getX()] = '@';
//                        mark(x1, grid, 'w');
//                        mark(x2, grid, 'w');
//                        mark(y1, grid, 'w');
//                        mark(y2, grid, 'w');
//
//                        xlines.stream().filter(x -> x.crosses(y1)).forEach(x -> mark(x, grid, '!'));
//                        xlines.stream().filter(x -> x.crosses(y2)).forEach(x -> mark(x, grid, '!'));
//                        ylines.stream().filter(y -> y.crosses(x1)).forEach(y -> mark(y, grid, '!'));
//                        ylines.stream().filter(y -> y.crosses(x2)).forEach(y -> mark(y, grid, '!'));
//
//                        List<LineX> crossY1 = xlines.stream().filter(x -> x.crosses(y1)).toList();
//                        List<LineX> crossY2 = xlines.stream().filter(x -> x.crosses(y2)).toList();
//                        List<LineY> crossX1 = ylines.stream().filter(y -> y.crosses(x1)).toList();
//                        List<LineY> crossX2 = ylines.stream().filter(y -> y.crosses(x2)).toList();


                        //if (crossX1.isEmpty() && crossX2.isEmpty() && crossY1.isEmpty() && crossY2.isEmpty()) {
                        if (area > max) {
                            max = Math.max(max, area);
                            //System.out.println(List.of(y1, y2, x1, x2));
//                            System.out.println(crossY1 + " " + crossY2);
//                            System.out.println(crossX1 + " " + crossX2);
                            //print(grid);
                            //System.out.println("no cross " + p1 + " " + p2 + " max=" + max);
                        }
                        //}

                    }

                    //                    grid1[p1.getY()][p1.getX()] = 'O';
//                    grid1[p1.getY()][p2.getX()] = 'O';
//                    grid1[p2.getY()][p1.getX()] = 'O';
//                    grid1[p2.getY()][p2.getX()] = 'O';
                    // print(grid1);

//                    if (xlines.stream().anyMatch(x -> x.crosses(y1) || x.crosses(y2))
//                        || ylines.stream().anyMatch(y -> y.crosses(x1) || y.crosses(x2))){
//
//                       
//                        System.out.println("crioss!"  + p1 + " " + p2);
//                    } else {
//                        long m = ((long) p1.getX() - p2.getX() + 1) * ((long) p1.getY() - p2.getY() + 1);
//                        if (m > max) {
//                            max = Math.max(max, m);
//                        System.out.println("no cross " + p1 + " " + p2);
//                        }
//                    }
                }
            }
        }

        return max;
    }

    private static void mark(LineY y1, char[][] grid, char c) {
        for (int x = y1.x1; x <= y1.x2; x++) {
            grid[y1.y][x] = c;
        }
    }

    private static void mark(LineX x1, char[][] grid, char c) {
        for (int y = x1.y1; y <= x1.y2; y++) {
            grid[y][x1.x] = c;
        }
    }

    private static void fill(List<LineY> ylines, char[][] grid, List<LineX> xlines) {
        for (LineY yline : ylines) {
            for (int x = yline.x1 + 1; x < yline.x2; x++) {
                grid[yline.y][x] = 'x';
            }
        }
        for (LineX xline : xlines) {
            for (int y = xline.y1 + 1; y < xline.y2; y++) {
                grid[y][xline.x] = 'x';
            }
        }
    }

    private static char[][] getGrid(List<XYPair> list) {
        char[][] grid = new char[20][20];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = '.';
            }
        }
        for (XYPair p1 : list) {
            try {
                grid[p1.getY()][p1.getX()] = 'X';
            } catch (Exception e) {

            }
        }
        return grid;
    }

    private static void print(char[][] matrix) {
        if (matrix.length > 1000) {
            return;
        }
        for (int y = 0; y < matrix.length; y++) {

            for (int x = 0; x < matrix[y].length; x++) {
                System.out.print(matrix[y][x]);
            }
            System.out.println();
        }
    }

    public void fill(boolean[][] grid) {

        Queue<Pos> q = new ArrayDeque<>();
        //print(grid);

        Pos p = new Pos(grid.length / 2, grid[0].length / 2);
        q.add(p);

        while (!q.isEmpty()) {
            Pos poll = q.poll();
            grid[poll.y][poll.x] = true;
            if (poll.y > 0 && !grid[poll.y - 1][poll.x]) {
                q.add(new Pos(poll.y - 1, poll.x));
            }
            if (poll.y + 1 < grid.length && !grid[poll.y + 1][poll.x]) {
                q.add(new Pos(poll.y + 1, poll.x));
            }
            if (poll.x > 0 && !grid[poll.y][poll.x - 1]) {
                q.add(new Pos(poll.y, poll.x - 1));
            }
            if (poll.x + 1 < grid[0].length && !grid[poll.y][poll.x + 1]) {
                q.add(new Pos(poll.y, poll.x + 1));
            }
        }

    }

    record Pos(int y, int x) {
    }
}
