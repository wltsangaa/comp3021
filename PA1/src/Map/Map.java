package Map;

import Exceptions.InvalidMapException;
import Exceptions.InvalidNumberOfPlayersException;
import Exceptions.UnknownElementException;
import Map.Occupant.Crate;
import Map.Occupant.Player;
import Map.Occupiable.DestTile;
import Map.Occupiable.Occupiable;
import Map.Occupiable.Tile;

import java.util.ArrayList;

/**
 * A class holding a the 2D array of cells, representing the world map
 */
public class Map {
    private Cell[][] cells;
    private ArrayList<DestTile> destTiles = new ArrayList<>();
    private ArrayList<Crate> crates = new ArrayList<>();

    private Player player;

    /**
     * This function instantiates and initializes cells, destTiles, crates to the correct map elements (the # char
     * means a wall, @ the player, . is unoccupied Tile, lowercase letter is crate on a Tile,
     * uppercase letter is an unoccupied DestTile).
     *
     * @param rows The number of rows in the map
     * @param cols The number of columns in the map
     * @param rep  The 2d char array read from the map text file
     * @throws InvalidMapException Throw the correct exception when necessary. There should only be 1 player.
     */
    public void initialize(int rows, int cols, char[][] rep) throws InvalidMapException {
        //TODO
        System.out.println(rows);
        System.out.println(cols);
        cells = new Cell[rows][cols];
        destTiles = new ArrayList<>();
       crates = new ArrayList<>();
        for (var r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (rep[r][c] == '.') {
                    cells[r][c] = new Tile();
                } else if (rep[r][c] == '#') {
                    cells[r][c] = new Wall();
                } else if (rep[r][c] == '@') {
                    if (player == null) {
                        cells[r][c] = new Tile();
                        player = new Player(r, c);
                        ((Tile) cells[r][c]).setOccupant(player);
                    } else {
                        throw new InvalidNumberOfPlayersException("player is greater than 1");
                    }
                } else {
                    if (Character.isLetter(rep[r][c])) {
                        if (Character.isLowerCase(rep[r][c])) {
                            cells[r][c] = new Tile();
                            var newCrate = new Crate(r, c, rep[r][c]);
                            crates.add(newCrate);
                            ((Tile) cells[r][c]).setOccupant(newCrate);
                        } else if (Character.isUpperCase(rep[r][c])) {
                            var newDestTile = new DestTile(rep[r][c]);
                            destTiles.add(newDestTile);
                            cells[r][c] = newDestTile;
                        }
                    } else {
                        throw new UnknownElementException("It is a unknown character " + rep[r][c]);
                    }
                }



    }}}

    public ArrayList<DestTile> getDestTiles() {
        return destTiles;
    }

    public ArrayList<Crate> getCrates() {
        return crates;
    }

    public Cell[][] getCells() {
        return cells;
    }

    /**
     * Attempts to move the player in the specified direction. Note that the player only has the strength to push
     * one crate. It cannot push 2 or more crates simultaneously. The player cannot walk through walls or walk beyond
     * map coordinates.
     *
     * @param d The direction the player wants to move
     * @return Whether the move was successful
     */
    public boolean movePlayer(Direction d) {
        //TODO
        int curR = player.getR();
        int curC = player.getC();

        int newR = curR;
        int newC = curC;

        switch (d) {
            case UP:
                newR--;
                break;
            case DOWN:
                newR++;
                break;
            case LEFT:
                newC--;
                break;
            case RIGHT:
                newC++;
                break;
        }

        if (isValid(newR, newC)) {
            if (isOccupiableAndNotOccupiedWithCrate(newR, newC)) {
                ((Occupiable) cells[curR][curC]).removeOccupant();
                ((Occupiable) cells[newR][newC]).setOccupant(player);
                player.setPos(newR, newC);
                return true;
            } else if (cells[newR][newC] instanceof Occupiable) {
                if (moveCrate((Crate) ((Occupiable) cells[newR][newC]).getOccupant().get(), d)) {
                    ((Occupiable) cells[curR][curC]).removeOccupant();
                    ((Occupiable) cells[newR][newC]).setOccupant(player);
                    player.setPos(newR, newC);
                    return true;
                }
            }
        }
        System.out.println("false movePlayer");
        return false;


    }

    /**
     * Attempts to move the crate into the specified direction by 1 cell. Will only succeed if the destination
     * implements the occupiable interface and is not currently occupied.
     *
     * @param c The crate to be moved
     * @param d The desired direction to move the crate in
     * @return Whether or not the move was successful
     */
    private boolean moveCrate(Crate c, Direction d) {
        //TODO
        int curR = c.getR();
        int curC = c.getC();

        int newR = curR;
        int newC = curC;

        switch (d) {
            case UP:
                newR--;
                break;
            case DOWN:
                newR++;
                break;
            case LEFT:
                newC--;
                break;
            case RIGHT:
                newC++;
                break;
        }

        if (isOccupiableAndNotOccupiedWithCrate(newR, newC)) {
            ((Occupiable) cells[curR][curC]).removeOccupant();
            ((Occupiable) cells[newR][newC]).setOccupant(c);
            c.setPos(newR, newC);
            return true;
        } else {
            System.out.println("false moveCrate");
            return false;
        }
    }

    private boolean isValid(int r, int c) {
        return (r >= 0 && r < cells.length && c >= 0 && c < cells[0].length);
    }

    /**
     * @param r The row coordinate
     * @param c The column coordinate
     * @return Whether or not the specified location on the grid is a location which implements Occupiable,
     * yet does not currently have a crate in it. Will return false if out of bounds.
     */
    public boolean isOccupiableAndNotOccupiedWithCrate(int r, int c) {
        //TODO
        if (!isValid(r, c)) {
            return false;
        }

        return cells[r][c] instanceof Occupiable &&
                (!((Occupiable) cells[r][c]).getOccupant().isPresent()
                        || !(((Occupiable) cells[r][c]).getOccupant().get() instanceof Crate));
    }

    public enum Direction {
        UP, DOWN, LEFT, RIGHT
    }
}
