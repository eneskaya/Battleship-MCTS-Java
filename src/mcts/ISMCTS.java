package mcts;

import battleship.*;

import java.util.ArrayList;
import java.util.List;

public class ISMCTS {

    /**
     * Class representing a possible opponent grid based on the information sets (for human and AI)
     *
     */
    private static class Determinization
    {
        Grid possibleHumanGrid; //The grid that the computer assumes for the player
        Grid possibleComputerGrid; //The grid that the player assumes for the computer
    }

    /**
     * Class representing a Field in a matrix
     */
    public static class Field
    {
        public Field(int inRow, int inCol)
        {
            row = inRow;
            col = inCol;
        }

        public int row;
        public int col;
    }

    /**
     * Class representing a matrix of chances
     * In this project it is used to represent the chances of a ship being on each of the matrix fields
     */
    private static class ChanceMatrix
    {
        float[][] grid = new float[Constants.GRID_DIMENSION][Constants.GRID_DIMENSION];

        /***
         * Analyzes this ChanceMatrix and returns the Field with the highest win chance
         * @return The Field with the highest win chance
         */
        public Field bestField()
        {
            Field highestField = new Field(0,0);

            for(int x = 0; x < Constants.GRID_DIMENSION; x++)
            {
                for(int y = 0; y < Constants.GRID_DIMENSION; y++)
                {
                    if(grid[x][y] > grid[highestField.row][highestField.col])
                    {
                        highestField = new Field(x, y);
                    }
                }
            }

            return highestField;
        }

        public void add(ChanceMatrix matrixToAdd)
        {
            for(int x = 0; x < Constants.GRID_DIMENSION; x++)
            {
                for(int y = 0; y < Constants.GRID_DIMENSION; y++)
                {
                    grid[x][y] += matrixToAdd.grid[x][y];
                }
            }
        }

        public void divide(int divisor)
        {
            for(int x = 0; x < Constants.GRID_DIMENSION; x++)
            {
                for(int y = 0; y < Constants.GRID_DIMENSION; y++)
                {
                    grid[x][y] /= divisor;
                }
            }
        }
    }

    private static boolean validDeterminization(Grid informationSet, Grid possibleGrid)
    {
        for(int x = 0; x < Constants.GRID_DIMENSION; x++)
        {
            for(int y = 0; y < Constants.GRID_DIMENSION; y++)
            {
                if(informationSet.getStatus(x, y) == Location.HIT)
                {
                    if(!possibleGrid.hasShip(x, y))
                    {
                        return false;
                    }
                }
                else if(informationSet.getStatus(x, y) == Location.MISSED)
                {
                    if(possibleGrid.hasShip(x, y))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static void reproduceShots(Grid informationSet, Grid possibleGrid)
    {
        for(int x = 0; x < Constants.GRID_DIMENSION; x++)
        {
            for(int y = 0; y < Constants.GRID_DIMENSION; y++)
            {
                if(informationSet.getStatus(x, y) == Location.HIT)
                {
                    possibleGrid.markHit(x, y);
                }
                else if(informationSet.getStatus(x, y) == Location.MISSED)
                {
                    possibleGrid.markMiss(x, y);
                }
            }
        }
    }

    private static Grid makeRandomDeterminization(Grid informationSet)
    {
        Grid result = new Grid();

        reproduceShots(informationSet, result);

        List<Integer> remainingShipLengths = new ArrayList<>();

        for(int i = 0; i < Player.getNumOfShips(); i++)
        {
            remainingShipLengths.add(Player.SHIP_LENGTHS[i]);
        }

        for(int x = 0; x < Constants.GRID_DIMENSION; x++)
        {
            for(int y = 0; y < Constants.GRID_DIMENSION; y++)
            {
                if(informationSet.getStatus(x, y) == Location.HIT && !result.hasShip(x, y))
                {
                    if(remainingShipLengths.size() == 0)
                    {
                        return null;
                    }
                    int shipLength = remainingShipLengths.remove(Randomizer.nextInt(0, remainingShipLengths.size()-1));

                    Ship newShip = new Ship(shipLength);

                    int dir = Randomizer.nextInt(0, 1);
                    if (Battleship.hasErrorsComp(x, y, dir, result, shipLength))
                    {
                        dir = Randomizer.nextInt(0, 1); //0 = HORIZONTAL
                    }
                    if (Battleship.hasErrorsComp(x, y, dir, result, shipLength))
                    {
                        return null;
                    }

                    newShip.setLocation(x, y);
                    newShip.setDirection(dir);
                    result.addShip(newShip);
                }
            }
        }

        for(Integer remainingShipLength : remainingShipLengths)
        {
            Ship newShip = new Ship(remainingShipLength);

            int row = Randomizer.nextInt(0, 9);
            int col = Randomizer.nextInt(0, 9);
            int dir = Randomizer.nextInt(0, 1);
            while (Battleship.hasErrorsComp(row, col, dir, result, remainingShipLength)) // while the random numbers make errors, start again
            {
                row = Randomizer.nextInt(0, 9);
                col = Randomizer.nextInt(0, 9);
                dir = Randomizer.nextInt(0, 1);
            }

            newShip.setLocation(row, col);
            newShip.setDirection(dir);
            result.addShip(newShip);
        }

        return result;
    }

    private static Grid constructPossibleGrid(Grid informationSet)
    {
        Grid possibleGrid = null;

        int count = 0;

        while (possibleGrid==null || !validDeterminization(informationSet, possibleGrid)) {
            possibleGrid = makeRandomDeterminization(informationSet);
            count++;
        }

        System.out.println(count);

        return possibleGrid;
    }

    private static Determinization createDeterminization(Player comp, Player user) {
        Determinization d = new Determinization();

        Grid compFiredShots = user.oppGrid;
        Grid userFiredShots = comp.oppGrid;

        d.possibleComputerGrid = constructPossibleGrid(compFiredShots);
        d.possibleHumanGrid = constructPossibleGrid(userFiredShots);

        return d;
    }

    /**
     * Creates count Determinizations.
     * A determinazation is Board/Grid with concrete information about placed ships.
     *
     * @param comp The computer
     * @param user The human player
     * @param count The number of Determinizations to create
     * @return A list containing the Determinizations
     */
    private static List<Determinization> createDeterminizations(Player comp, Player user, int count)
    {
        int maxTime = 8000;

        long currentTime = System.currentTimeMillis();
        long endTime = currentTime+maxTime;

        List<Determinization> result = new ArrayList<>();

        for(int i = 0; i < count; i++) {
            // Create a det. for player (opponent)
            result.add(createDeterminization(comp, user));
            if(System.currentTimeMillis() < endTime)
            {
                break;
            }
        }

        return result;
    }

    /**
     * Performs the MCTS-Algorythm and performs it iterations times
     * @param d The determinization to apply MCTS to
     * @param iterations The number of times to perform MCTS
     * @return
     */
    private static ChanceMatrix MCTS(Determinization d, int iterations)
    {
        ChanceMatrix result = new ChanceMatrix();

        //TODO

        return result;
    }

    /**
     * Given multiple ChanceMatrixes returns the average
     * @param chanceMatrixes The ChanceMatrixes to calculate the average for
     * @return The average ChanceMatrix
     */
    private static ChanceMatrix averageChanceMatrix(List<ChanceMatrix> chanceMatrixes)
    {
        ChanceMatrix result = new ChanceMatrix();

        int length = chanceMatrixes.size();

        for(ChanceMatrix matrix : chanceMatrixes)
        {
            result.add(matrix);
        }

        result.divide(length);

        return result;
    }


    /**
     * Function for the computer to select a Field to shoot
     * @param comp The computer
     * @param user The human player
     * @return The field to shoot
     */
    public static Field selectFieldToShoot(Player comp, Player user)
    {
        //Creates 500 determinizations
        List<Determinization> determinizations = createDeterminizations(comp, user, 200);

        List<ChanceMatrix> simulations = new ArrayList<ChanceMatrix>();

        for(Determinization d : determinizations)
        {
            ChanceMatrix winChanceMatrix;
            winChanceMatrix = MCTS(d, 1000);
            simulations.add(winChanceMatrix);
        }

        ChanceMatrix averageWinChanceMatrix = averageChanceMatrix(simulations);

        return averageWinChanceMatrix.bestField();
    }
}
