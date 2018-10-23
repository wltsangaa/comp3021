
import Exceptions.InvalidMapException;
import Map.*;
import Map.Occupant.Crate;
import Map.Occupiable.DestTile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Holds the necessary components for running the game.
 */
public class Game {

    private Map m;
    private int numRows;
    private int numCols;
    private char[][] rep;

    /**
     * Loads and reads the map line by line, instantiates and initializes Map m.
     * Print out the number of rows, then number of cols (on two separate lines).
     *
     * @param filename the map text filename
     * @throws InvalidMapException
     */
    public void loadMap(String filename) throws InvalidMapException {
        //TODO
        File f = new File(filename);
        try (Scanner reader = new Scanner(f)) {
            numRows = reader.nextInt();
            numCols = reader.nextInt();
            reader.nextLine();

            rep = new char[numRows][numCols];
            for (int r = 0; r < numRows; r++) {
                String row = reader.nextLine();
                for (int c = 0; c < numCols; c++) {
                    rep[r][c] = row.charAt(c);
                }
            }
            m = new Map();
            m.initialize(numRows, numCols, rep);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Can be done using functional concepts.
     * @return Whether or not the win condition has been satisfied
     */
    public boolean isWin() {
        //TODO
        return m.getDestTiles().stream().allMatch(DestTile::isCompleted);
    }

    /**
     * When no crates can be moved but the game is not won, then deadlock has occurred.
     *
     * @return Whether deadlock has occurred
     */
    public boolean isDeadlocked() {
        //TODO
        for (Crate c : m.getCrates()) {
            boolean canMoveLR = m.isOccupiableAndNotOccupiedWithCrate(c.getR(), c.getC() - 1)
                    && m.isOccupiableAndNotOccupiedWithCrate(c.getR(), c.getC() + 1);
            boolean canMoveUD = m.isOccupiableAndNotOccupiedWithCrate(c.getR() - 1, c.getC()) &&
                    m.isOccupiableAndNotOccupiedWithCrate(c.getR() + 1, c.getC());
            if (canMoveLR || canMoveUD)
                return false;
        }
        return true;
    }

    /**
     * Print the map to console
     */
    public void display() {
        //TODO
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                System.out.print(m.getCells()[r][c].getRepresentation());
            }
            System.out.println();
        }
    }

    /**
     * @param c The char corresponding to a move from the user
     *          w: up
     *          a: left
     *          s: down
     *          d: right
     *          r: reload map (resets any progress made so far)
     * @return Whether or not the move was successful
     */
    public boolean makeMove(char c) {
        //TODO
        boolean madeMove = false;
        switch (c) {
            case 'w':
                madeMove = m.movePlayer(Map.Direction.UP);
                break;
            case 'a':
                madeMove = m.movePlayer(Map.Direction.LEFT);
                break;
            case 's':
                madeMove = m.movePlayer(Map.Direction.DOWN);
                break;
            case 'd':
                madeMove = m.movePlayer(Map.Direction.RIGHT);
                break;
        }

        return madeMove;
    }


}
