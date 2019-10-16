package battleship;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Battleship
{	
    public static Scanner reader = new Scanner(System.in);
      
    public static void main(String[] args)
    {
        System.out.println("JAVA BATTLESHIP - ** Yuval Marcus **");  
        
        System.out.println("\nPlayer SETUP:");
        Player userPlayer = new Player();
        //setup(userPlayer);
        setupComputer(userPlayer);
        
        System.out.println("Computer SETUP...DONE...PRESS ENTER TO CONTINUE...");
        reader.nextLine();
        reader.nextLine();
        Player computer = new Player();
        setupComputer(computer);
        System.out.println("\nCOMPUTER GRID (FOR DEBUG)...");
        computer.playerGrid.printShips();
        
        String result = "";
        while(true)
        {
            System.out.println(result);
            System.out.println("\nUSER MAKE GUESS:");
            result = askForGuess(userPlayer, computer);
            
            if (userPlayer.playerGrid.hasLost())
            {
                System.out.println("COMP HIT!...USER LOSES");
                break;
            }
            else if (computer.playerGrid.hasLost())
            {
                System.out.println("HIT!...COMPUTER LOSES");
                break;
            }
            
            System.out.println("\nCOMPUTER IS MAKING GUESS...");
              
              
            compMakeGuess(computer, userPlayer);
        }
    }
    
    /**
     * Class representing the Determinization of a GameState
     * 
     */
    private static class Determinization
    {
    	Grid assumedHumanGrid; //The grid that the computer assumes for the player
    	Grid assumedComputerGrid; //The grid that the player assumes for the computer
    }
    
    /**
     * Class representing a Field in a matrix
     */
    private static class Field
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
     */
    private static class ChanceMatrix
    {
    	float[][] grid = new float[10][10];
    	
    	/***
    	 * Analyzes this ChanceMatrix and returns the Field with the highest win chance
    	 * @return The Field with the highest win chance
    	 */
    	public Field bestField()
    	{
    		Field highestField = new Field(0,0);
    		
    		for(int x = 0; x < 10; x++)
    		{
        		for(int y = 0; y < 10; y++)
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
    		for(int x = 0; x < 10; x++)
    		{
        		for(int y = 0; y < 10; y++)
        		{
        			grid[x][y] += matrixToAdd.grid[x][y];
        		}
    		}
    	}
    	
    	public void divide(int divisor)
    	{
    		for(int x = 0; x < 10; x++)
    		{
        		for(int y = 0; y < 10; y++)
        		{
        			grid[x][y] /= divisor;
        		}
    		}
    	}
    }
    
    private static boolean validDeterminization(Grid base, Player player)
    {
		for(int x = 0; x < 10; x++)
		{
    		for(int y = 0; y < 10; y++)
    		{ 
    			if(base.getStatus(x, y) == Location.HIT)
    			{
        	    	if(!player.playerGrid.hasShip(x, y))
        	    	{
        	    		return false;
        	    	}
    			}
    			else if(base.getStatus(x, y) == Location.MISSED)
    			{
        	    	if(player.playerGrid.hasShip(x, y))
        	    	{
        	    		return false;
        	    	}
    			}
    		}
		}
		return true;
    }
    
    private static void reproduceShots(Grid base, Player player)
    {
		for(int x = 0; x < 10; x++)
		{
    		for(int y = 0; y < 10; y++)
    		{
    			if(base.getStatus(x, y) == Location.HIT)
    			{
        	    	player.playerGrid.markHit(x, y);
    			}
    			else if(base.getStatus(x, y) == Location.MISSED)
    			{
        	    	player.playerGrid.markMiss(x, y);
    			}
    		}
		}
    }
    
    private static Grid constructPossibleGrid(Grid base)
    {
    	Player user = new Player();

    	setupComputer(user);
    	reproduceShots(base, user);
    	
    	while (!validDeterminization(base, user)) {
    		user = new Player();
        	setupComputer(user);	
        	reproduceShots(base, user);
    	}
    	
    	return user.playerGrid;
    }
    
    private static Determinization createDeterminization(Player comp, Player user) {
    	Determinization d = new Determinization();
    	
    	Grid compFiredShots = user.oppGrid;
    	Grid userFiredShots = comp.oppGrid;

    	d.assumedComputerGrid = constructPossibleGrid(compFiredShots);
    	d.assumedHumanGrid = constructPossibleGrid(userFiredShots);
    	
    	System.out.println("assumedHumanGrid: ");
    	d.assumedHumanGrid.printCombined();
    	System.out.println("assumedComputerGrid: ");
    	d.assumedComputerGrid.printCombined();
    	
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
    private static Determinization[] createDeterminizations(Player comp, Player user, int count)
    {
    	 Determinization[] result = new Determinization[count];
    	 
    	 for(int i = 0; i < count; i++) {
    		 // Create a det. for player (opponent)
    		 result[i] = createDeterminization(comp, user);
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
    private static Field selectOpponentField(Player comp, Player user)
    {    	
    	//Creates 500 determinizations
    	Determinization[] determinizations = createDeterminizations(comp, user, 100);
    	
    	List<ChanceMatrix> simulations = new ArrayList<ChanceMatrix>();
    	
    	for(Determinization d : determinizations)
    	{
    		ChanceMatrix winChanceMatrix = new ChanceMatrix();
        	winChanceMatrix = MCTS(d, 1000);
        	simulations.add(winChanceMatrix);
    	}
    	
    	ChanceMatrix averageWinChanceMatrix = averageChanceMatrix(simulations);
    	
    	return averageWinChanceMatrix.bestField();
    }
    
    private static void compMakeGuess(Player comp, Player user)
    {
    	Field results = selectOpponentField(comp, user);

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
                    
            //System.out.println("DEBUG: " + row + col);
                    
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
                    
                    //System.out.println("DEBUG: " + row + col + dir);
                    
                    if (col >= 0 && col <= 9 && row != -1 && dir != -1) // Check valid input
                    {
                        if (!hasErrors(row, col, dir, p, normCounter)) // Check if errors will produce (out of bounds)
                        {
                            break;
                        }
                    }
    
                    System.out.println("Invalid location!");
                }
            
                //System.out.println("FURTHER DEBUG: row = " + row + "; col = " + col);
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
        System.out.println();
        int counter = 1;
        int normCounter = 0;
        
        while (p.numOfShipsLeft() > 0)
        {
            for (Ship s: p.ships)
            {
                int row = Randomizer.nextInt(0, 9);
                int col = Randomizer.nextInt(0, 9);
                int dir = Randomizer.nextInt(0, 1);
                
                //System.out.println("DEBUG: row-" + row + "; col-" + col + "; dir-" + dir);
                
                while (hasErrorsComp(row, col, dir, p, normCounter)) // while the random nums make error, start again
                {
                    row = Randomizer.nextInt(0, 9);
                    col = Randomizer.nextInt(0, 9);
                    dir = Randomizer.nextInt(0, 1);
                    //System.out.println("AGAIN-DEBUG: row-" + row + "; col-" + col + "; dir-" + dir);
                }
                
                //System.out.println("FURTHER DEBUG: row = " + row + "; col = " + col);
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
        //System.out.println("DEBUG: count arg is " + count);
        
        int length = p.ships[count].getLength();
        
        // Check if off grid - Horizontal
        if (dir == 0)
        {
            int checker = length + col;
            //System.out.println("DEBUG: checker is " + checker);
            if (checker > 10)
            {
                System.out.println("SHIP DOES NOT FIT");
                return true;
            }
        }
        
        // Check if off grid - Vertical
        if (dir == 1) // VERTICAL
        {
            int checker = length + row;
            //System.out.println("DEBUG: checker is " + checker);
            if (checker > 10)
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
                //System.out.println("DEBUG: row = " + row + "; col = " + i);
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
                //System.out.println("DEBUG: row = " + row + "; col = " + i);
                if(p.playerGrid.hasShip(i, col))
                {
                    System.out.println("THERE IS ALREADY A SHIP AT THAT LOCATION");
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private static boolean hasErrorsComp(int row, int col, int dir, Player p, int count)
    {
        //System.out.println("DEBUG: count arg is " + count);
        
        int length = p.ships[count].getLength();
        
        // Check if off grid - Horizontal
        if (dir == 0)
        {
            int checker = length + col;
            //System.out.println("DEBUG: checker is " + checker);
            if (checker > 10)
            {
                return true;
            }
        }
        
        // Check if off grid - Vertical
        if (dir == 1) // VERTICAL
        {
            int checker = length + row;
            //System.out.println("DEBUG: checker is " + checker);
            if (checker > 10)
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
                //System.out.println("DEBUG: row = " + row + "; col = " + i);
                if(p.playerGrid.hasShip(row, i))
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
                //System.out.println("DEBUG: row = " + row + "; col = " + i);
                if(p.playerGrid.hasShip(i, col))
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