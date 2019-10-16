## Battleship (Schiffe versenken) with Determinized MCTS

**Intelligente Systeme, Praktikum 1 <br/>
Enes Kaya, Jasper Wolny**

Rules of Game:

1. Each player first sets up their ships in the game before the actual game begins
1. Available ships are:
    - Carrier, which has 5 holes
    - Battleship, which has 4 holes
    - Cruiser, which has 3 holes
    - Destroyer, which has 2 holes
1.  For the sake of simplicity, in the actual implementation the "war field" dimensions are 10x10 and only one instance 
of each ship can be placed on the grid, without overlapping each other
