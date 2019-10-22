package mcts;

import battleship.Constants;

/**
 * Class representing a matrix of chances
 * In this project it is used to represent the chances of a ship being on each of the matrix fields
 */
class ChanceMatrix
{
    private int[][] grid = new int[Constants.GRID_DIMENSION][Constants.GRID_DIMENSION];

    /***
     * Analyzes this ChanceMatrix and returns the Field with the highest win chance
     * @return The Field with the highest win chance
     */
    Field bestField()
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

    void incrementRowCol(int row, int col) {
        grid[row][col] = grid[row][col]++;
    }
}