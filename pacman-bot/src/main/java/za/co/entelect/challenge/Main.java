package za.co.entelect.challenge;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final int WIDTH = 19;
    private static final int HEIGHT = 22;
    private static final int PORTAL1_X = 10;
    private static final int PORTAL1_Y = 18;
    private static final int PORTAL2_X = 0;
    private static final int PORTAL2_Y = 10;
    private static final char WALL = '#';
    private static final char PLAYER_SYMBOL = 'A';
    private static final String OUTPUT_FILE_NAME = "game.state";

    public static void main(String[] args) {
        char[][] maze = ReadMaze(args[0]);
        Point coordinate = getCurrentPosition(maze);
        if (coordinate != null) {
            List<Point> possibleMoveList = determineMoves(coordinate, maze);
            Random random = new Random();
            int randomMoveIndex = random.nextInt(possibleMoveList.size());
            maze = makeMove(coordinate, possibleMoveList.get(randomMoveIndex), maze);
            writeMaze(maze, OUTPUT_FILE_NAME);
        }
    }

    private static Point getCurrentPosition(char[][] maze) {
        Point coordinate = new Point();
        for (int x = 0; x < HEIGHT; x++) {
            for (int y = 0; y < WIDTH; y++) {
                if (maze[x][y] == PLAYER_SYMBOL) {
                    coordinate.setLocation(x, y);
                    return coordinate;
                }
            }
        }
        return null;
    }

    private static List<Point> determineMoves(Point currentPoint, char[][] maze) {
        List<Point> moveList = new ArrayList<Point>();
        if (currentPoint.getY() + 1 < WIDTH)
            if (maze[(int) currentPoint.getX()][(int) currentPoint.getY() + 1] != WALL)
                moveList.add(new Point((int) currentPoint.getX(), (int) currentPoint.getY() + 1));

        if ((int) currentPoint.getY() - 1 >= 0)
            if (maze[(int) currentPoint.getX()][(int) currentPoint.getY() - 1] != WALL)
                moveList.add(new Point((int) currentPoint.getX(), (int) currentPoint.getY() - 1));

        if ((int) currentPoint.getX() + 1 < HEIGHT)
            if (maze[(int) currentPoint.getX() + 1][(int) currentPoint.getY()] != WALL)
                moveList.add(new Point((int) currentPoint.getX() + 1, (int) currentPoint.getY()));

        if ((int) currentPoint.getX() - 1 >= 0)
            if (maze[(int) currentPoint.getX() - 1][(int) currentPoint.getY()] != WALL)
                moveList.add(new Point((int) currentPoint.getX() - 1, (int) currentPoint.getY()));

        if ((int) currentPoint.getX() == PORTAL1_X && currentPoint.getY() == PORTAL1_Y)
            moveList.add(new Point(PORTAL2_Y, PORTAL2_X));

        if ((int) currentPoint.getX() == PORTAL2_X && currentPoint.getY() == PORTAL2_Y)
            moveList.add(new Point(PORTAL1_Y, PORTAL1_X));

        return moveList;
    }

    private static char[][] makeMove(Point currentPoint, Point movePoint, char[][] maze) {
        maze[(int) currentPoint.getX()][(int) currentPoint.getY()] = ' ';
        maze[(int) movePoint.getX()][(int) movePoint.getY()] = PLAYER_SYMBOL;
        return maze;
    }

    private static void writeMaze(char[][] maze, String filePath) {
        try {
            PrintWriter writer = new PrintWriter(filePath);
            String output = "";
            for (int x = 0; x < HEIGHT; x++) {
                for (int y = 0; y < WIDTH; y++) {
                    output += maze[x][y];
                }
                if (x != HEIGHT - 1) output += ("\r\n");
            }
            writer.print(output);
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static char[][] ReadMaze(String filePath) {
        char[][] map = new char[HEIGHT][];
        try {
            Scanner reader = new Scanner(new File(filePath));
            int rowCount = 0;
            while (reader.hasNext()) {
                String row = reader.nextLine();
                map[rowCount] = row.toCharArray();
                rowCount++;
            }
        } catch (IOException e) {
            System.err.println(e);
        }
        return map;
    }

}
