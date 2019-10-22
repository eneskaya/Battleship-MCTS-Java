package mcts;

import battleship.*;

import java.util.List;

public class ISMCTS {
    /**
     * Function for the computer to select a Field to shoot
     * @param comp The computer
     * @param user The human player
     * @return The field to shoot
     */
    public static Field selectFieldToShoot(Player comp, Player user)
    {
        List<Determinization> determinizations = Determinization.createDeterminizations(comp, user, 200);

        ChanceMatrix resultsMatrix = new ChanceMatrix();

        for(Determinization d : determinizations)
        {
            MCTSAlgorithm algorithm = new MCTSAlgorithm(d.computerPlayer, d.humanPlayer, 500);
            Field selectedFields = algorithm.run();

            resultsMatrix.incrementRowCol(selectedFields.row, selectedFields.col);
        }

        return resultsMatrix.bestField();
    }
}
