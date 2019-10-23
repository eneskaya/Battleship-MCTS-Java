package mcts;

import battleship.Constants;

/**
 * Class representing a matrix of chances
 * In this project it is used to represent the chances of a ship being on each of the matrix fields
 */
class ChanceMatrix
{
    private float[][] grid = new float[Constants.GRID_DIMENSION][Constants.GRID_DIMENSION];

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

    void incrementRowCol(Field field, float value) {
        grid[field.row][field.col] += value;
    }
}