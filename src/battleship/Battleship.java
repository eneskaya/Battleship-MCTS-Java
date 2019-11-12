package battleship;

import mcts.Field;
import mcts.ISMCTS;
import utils.Logger;

import java.util.Scanner;

public class Battleship
{	
    public static Scanner reader = new Scanner(System.in);
      
    public static void main(String[] args)
    {
        System.out.println("JAVA BATTLESHIP - ** Yuval Marcus **");

        System.out.println("\nPlayer SETUP:");
        Player userPlayer = new Player("Mensch");
        setupComputer(userPlayer);

        Player computer = new Player("Computer");
        setupComputer(computer);
        System.out.println("\nCOMPUTER GRID (FOR DEBUG)...");
        computer.playerGrid.printShips();

        System.out.println("Computer SETUP...DONE...PRESS ENTER TO CONTINUE...");
        reader.nextLine();

        String result = "";

        while(true)
        {
            System.out.println("\nUSER MAKE GUESS:");
            result = askForGuess(userPlayer, computer);

            System.out.println();
            System.out.println("-------" + result + "-------");
            System.out.println();

            if (userPlayer.playerGrid.hasLost())
            {
                System.out.println("COMP HIT!...USER LOSES");

                System.out.println("Computer Grid:");
                computer.playerGrid.printShips();
                System.out.println("User Grid:");
                userPlayer.playerGrid.printShips();

                break;
            }
            else if (computer.playerGrid.hasLost())
            {
                System.out.println("HIT!...COMPUTER LOSES");

                System.out.println("Computer Grid:");
                computer.playerGrid.printShips();
                System.out.println("User Grid:");
                userPlayer.playerGrid.printShips();

                break;
            }

            System.out.println("\nCOMPUTER IS MAKING GUESS...");


            compMakeGuess(computer, userPlayer);
        }
    }
    
    private static void compMakeGuess(Player comp, Player user)
    {
        Field results = ISMCTS.selectFieldToShoot(comp, user);

    	int row = results.row;
    	int col = results.col;
    	
        if (user.playerGrid.hasShip(row, col))
        {
            comp.oppGrid.markHit(row, col);
            user.playerGrid.markHit(row, col);
            System.out.println("COMP HIT AT " + convertIntToLetter(row) + convertCompColToRegular(col));
        }
        else
        {
            comp.oppGrid.markMiss(row, col);
            user.playerGrid.markMiss(row, col);
            System.out.println("COMP MISS AT " + convertIntToLetter(row) + convertCompColToRegular(col));
        }
        
        
        System.out.println("\nYOUR BOARD...PRESS ENTER TO CONTINUE...");
        reader.nextLine();
        user.playerGrid.printCombined();
        System.out.println("PRESS ENTER TO CONTINUE...");
        reader.nextLine();
    }

    private static String askForGuess(Player p, Player opp)
    {
        System.out.println("Viewing My Guesses:");
        p.oppGrid.printStatus();
        
        int row = -1;
        int col = -1;
        
        String oldRow = "Z";
        int oldCol = -1;
        
        while(true)
        {
            System.out.print("Type in row (A-J): ");
            String userInputRow = reader.next();
            userInputRow = userInputRow.toUpperCase();
            oldRow = userInputRow;
            row = convertLetterToInt(userInputRow);
                    
            System.out.print("Type in column (1-10): ");
            col = reader.nextInt();
            oldCol = col;
            col = convertUserColToProCol(col);
                    
            Logger.debug("" + row + col);
                    
            if (col >= 0 && col <= 9 && row != -1)
                break;
                    
            System.out.println("Invalid location!");
        }
        
        if (opp.playerGrid.hasShip(row, col))
        {
            p.oppGrid.markHit(row, col);
            opp.playerGrid.markHit(row, col);
            return "** USER HIT AT " + oldRow + oldCol + " **";
        }
        else
        {
            p.oppGrid.markMiss(row, col);
            opp.playerGrid.markMiss(row, col);
            return "** USER MISS AT " + oldRow + oldCol + " **";
        }
    }
    
    private static void setup(Player p)
    {
        p.playerGrid.printShips();
        System.out.println();
        int counter = 1;
        int normCounter = 0;
        while (p.numOfShipsLeft() > 0)
        {
            for (Ship s: p.ships)
            {
                System.out.println("\nShip #" + counter + ": Length-" + s.getLength());
                int row = -1;
                int col = -1;
                int dir = -1;
                while(true)
                {
                    System.out.print("Type in row (A-J): ");
                    String userInputRow = reader.next();
                    userInputRow = userInputRow.toUpperCase();
                    row = convertLetterToInt(userInputRow);
                    
                    System.out.print("Type in column (1-10): ");
                    col = reader.nextInt();
                    col = convertUserColToProCol(col);
                    
                    System.out.print("Type in direction (0-H, 1-V): ");
                    dir = reader.nextInt();
                    
                    Logger.debug("" + row + col + dir);
                    
                    if (col >= 0 && col <= 9 && row != -1 && dir != -1) // Check valid input
                    {
                        if (!hasErrors(row, col, dir, p, normCounter)) // Check if errors will produce (out of bounds)
                        {
                            break;
                        }
                    }
    
                    System.out.println("Invalid location!");
                }
            
                Logger.debug("row = " + row + "; col = " + col);
                p.ships[normCounter].setLocation(row, col);
                p.ships[normCounter].setDirection(dir);
                p.playerGrid.addShip(p.ships[normCounter]);
                p.playerGrid.printShips();
                System.out.println();
                System.out.println("You have " + p.numOfShipsLeft() + " remaining ships to place.");
                
                normCounter++;
                counter++;
            }
        }
    }

    private static void setupComputer(Player p)
    {
        Logger.debug("Setting up computer player");

        int counter = 1;
        int normCounter = 0;
        
        while (p.numOfShipsLeft() > 0)
        {
            for (Ship s: p.ships)
            {
                int row = Randomizer.nextInt(0, Constants.GRID_DIMENSION-1);
                int col = Randomizer.nextInt(0, Constants.GRID_DIMENSION-1);
                int dir = Randomizer.nextInt(0, 1);
                
                Logger.debug("row-" + row + "; col-" + col + "; dir-" + dir);
                
                while (hasErrorsComp(row, col, dir, p, normCounter)) // while the random nums make error, start again
                {
                    row = Randomizer.nextInt(0, Constants.GRID_DIMENSION-1);
                    col = Randomizer.nextInt(0, Constants.GRID_DIMENSION-1);
                    dir = Randomizer.nextInt(0, 1);

                    Logger.debug("row-" + row + "; col-" + col + "; dir-" + dir);
                }
                
                Logger.debug("row = " + row + "; col = " + col);

                p.ships[normCounter].setLocation(row, col);
                p.ships[normCounter].setDirection(dir);
                p.playerGrid.addShip(p.ships[normCounter]);
                
                normCounter++;
                counter++;
            }
        }
    }
    
    private static boolean hasErrors(int row, int col, int dir, Player p, int count)
    {
        Logger.debug("count arg is " + count);
        
        int length = p.ships[count].getLength();
        
        // Check if off grid - Horizontal
        if (dir == 0)
        {
            int checker = length + col;
            Logger.debug("checker is " + checker);

            if (checker > Constants.GRID_DIMENSION)
            {
                System.out.println("SHIP DOES NOT FIT");
                return true;
            }
        }
        
        // Check if off grid - Vertical
        if (dir == 1) // VERTICAL
        {
            int checker = length + row;
            Logger.debug("checker is " + checker);

            if (checker > Constants.GRID_DIMENSION)
            {
                System.out.println("SHIP DOES NOT FIT");
                return true;
            }
        }
            
        // Check if overlapping with another ship
        if (dir == 0) // Hortizontal
        {
            // For each location a ship occupies, check if ship is already there
            for (int i = col; i < col+length; i++)
            {
                Logger.debug("row = " + row + "; col = " + i);

                if(p.playerGrid.hasShip(row, i))
                {
                    System.out.println("THERE IS ALREADY A SHIP AT THAT LOCATION");
                    return true;
                }
            }
        }
        else if (dir == 1) // Vertical
        {
            // For each location a ship occupies, check if ship is already there
            for (int i = row; i < row+length; i++)
            {
                Logger.debug("row = " + row + "; col = " + i);
                if(p.playerGrid.hasShip(i, col))
                {
                    System.out.println("THERE IS ALREADY A SHIP AT THAT LOCATION");
                    return true;
                }
            }
        }
        
        return false;
    }

    public static boolean hasErrorsComp(int row, int col, int dir, Player p, int count)
    {
        return hasErrorsComp(row, col, dir, p.playerGrid, Player.SHIP_LENGTHS[count]);
    }

    public static boolean hasErrorsComp(int row, int col, int dir, Grid playerGrid, int length)
    {
        Logger.debug("Legnth is " + length);

        // Check if off grid - Horizontal
        if (dir == 0)
        {
            int checker = length + col;
            Logger.debug("checker is " + checker);

            if (checker > Constants.GRID_DIMENSION)
            {
                return true;
            }
        }

        // Check if off grid - Vertical
        if (dir == 1) // VERTICAL
        {
            int checker = length + row;
            Logger.debug("checker is " + checker);

            if (checker > Constants.GRID_DIMENSION)
            {
                return true;
            }
        }

        // Check if overlapping with another ship
        if (dir == 0) // Hortizontal
        {
            // For each location a ship occupies, check if ship is already there
            for (int i = col; i < col+length; i++)
            {
                Logger.debug("row = " + row + "; col = " + i);
                if(playerGrid.hasShip(row, i) || playerGrid.getStatus(row, i) == Location.MISSED)
                {
                    return true;
                }
            }
        }
        else if (dir == 1) // Vertical
        {
            // For each location a ship occupies, check if ship is already there
            for (int i = row; i < row+length; i++)
            {
                Logger.debug("row = " + row + "; col = " + i);
                if(playerGrid.hasShip(i, col) || playerGrid.getStatus(i, col) == Location.MISSED)
                {
                    return true;
                }
            }
        }

        return false;
    }


    /*HELPER METHODS*/
    private static int convertLetterToInt(String val)
    {
        int toReturn = -1;
        switch (val)
        {
            case "A":   toReturn = 0;
                        break;
            case "B":   toReturn = 1;
                        break;
            case "C":   toReturn = 2;
                        break;
            case "D":   toReturn = 3;
                        break;
            case "E":   toReturn = 4;
                        break;
            case "F":   toReturn = 5;
                        break;
            case "G":   toReturn = 6;
                        break;
            case "H":   toReturn = 7;
                        break;
            case "I":   toReturn = 8;
                        break;
            case "J":   toReturn = 9;
                        break;
            default:    toReturn = -1;
                        break;
        }
        
        return toReturn;
    }
    
    private static String convertIntToLetter(int val)
    {
        String toReturn = "Z";
        switch (val)
        {
            case 0:   toReturn = "A";
                        break;
            case 1:   toReturn = "B";
                        break;
            case 2:   toReturn = "C";
                        break;
            case 3:   toReturn = "D";
                        break;
            case 4:   toReturn = "E";
                        break;
            case 5:   toReturn = "F";
                        break;
            case 6:   toReturn = "G";
                        break;
            case 7:   toReturn = "H";
                        break;
            case 8:   toReturn = "I";
                        break;
            case 9:   toReturn = "J";
                        break;
            default:    toReturn = "Z";
                        break;
        }
        
        return toReturn;
    }
    
    private static int convertUserColToProCol(int val)
    {
        int toReturn = -1;
        switch (val)
        {
            case 1:   toReturn = 0;
                        break;
            case 2:   toReturn = 1;
                        break;
            case 3:   toReturn = 2;
                        break;
            case 4:   toReturn = 3;
                        break;
            case 5:   toReturn = 4;
                        break;
            case 6:   toReturn = 5;
                        break;
            case 7:   toReturn = 6;
                        break;
            case 8:   toReturn = 7;
                        break;
            case 9:   toReturn = 8;
                        break;
            case 10:   toReturn = 9;
                        break;
            default:    toReturn = -1;
                        break;
        }
        
        return toReturn;
    }
    
    private static int convertCompColToRegular(int val)
    {
        int toReturn = -1;
        switch (val)
        {
            case 0:   toReturn = 1;
                        break;
            case 1:   toReturn = 2;
                        break;
            case 2:   toReturn = 3;
                        break;
            case 3:   toReturn = 4;
                        break;
            case 4:   toReturn = 5;
                        break;
            case 5:   toReturn = 6;
                        break;
            case 6:   toReturn = 7;
                        break;
            case 7:   toReturn = 8;
                        break;
            case 8:   toReturn = 9;
                        break;
            case 9:   toReturn = 10;
                        break;
            default:    toReturn = -1;
                        break;
        }
        
        return toReturn;
    }
}