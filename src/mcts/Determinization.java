package mcts;

import battleship.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a possible opponent grid based on the information sets (for human and AI)
 *
 */
class Determinization
{
    Player humanPlayer;
    Player computerPlayer;

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

        while (possibleGrid == null || !validDeterminization(informationSet, possibleGrid)) {
            possibleGrid = makeRandomDeterminization(informationSet);
        }

        return possibleGrid;
    }

    private static Determinization createDeterminization(Player comp, Player user) {
        Determinization d = new Determinization();

        // TODO not sure this is right, fix later
        // Also make sure to clone the player objects, so we don't mess up the actual game state

        d.computerPlayer = comp;
        user.oppGrid = constructPossibleGrid(user.oppGrid);
        d.humanPlayer = user;

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
    static List<Determinization> createDeterminizations(Player comp, Player user, int count)
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
}
