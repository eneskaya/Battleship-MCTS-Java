package battleship;

public class Player
{
    // These are the lengths of all of the ships.
    public static final int[] SHIP_LENGTHS = {2, 2, 2, 2, 3, 3, 3, 4, 4, 5};

    public static int getNumOfShips()
    {
        return SHIP_LENGTHS.length;
    }
    
    public Ship[] ships;

    // The own grid
    // Contains HITs MISSes and location of SHIPS
    public Grid playerGrid;
    // The grid, that's shown to the opponent
    // Only contains HITs and MISSes
    public Grid oppGrid;

    public Player deepClone()
    {
        Player result = new Player();
        for(int i = 0; i < getNumOfShips(); i++)
        {
            result.ships[i] = this.ships[i].deepClone();
        }

        result.playerGrid = this.playerGrid.deepClone();
        result.oppGrid = this.oppGrid.deepClone();
        return result;
    }

    public Player()
    {        
        ships = new Ship[getNumOfShips()];
        for (int i = 0; i < getNumOfShips(); i++)
        {
            Ship tempShip = new Ship(SHIP_LENGTHS[i]);
            ships[i] = tempShip;
        }
        
        playerGrid = new Grid();
        oppGrid = new Grid();
    }
    
    public void addShips()
    {
        for (Ship s: ships)
        {
            playerGrid.addShip(s);
        }
    }
    
    public int numOfShipsLeft()
    {
        int counter = getNumOfShips();
        for (Ship s: ships)
        {
            if (s.isLocationSet() && s.isDirectionSet())
                counter--;
        }
        
        return counter;
        
    }
    
    public void chooseShipLocation(Ship s, int row, int col, int direction)
    {
        s.setLocation(row, col);
        s.setDirection(direction);
        playerGrid.addShip(s);
    }
}