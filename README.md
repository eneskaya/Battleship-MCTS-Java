# Battleships with Monte Carlo Tree Search

Intelligente Systeme, Praktikum 1, Enes Kaya, Jasper Wolny

#### Battleships

Rules of game:

1. Each player first sets up their ships in the game before the actual game begins
1. Available ships are:
    - Carrier, which has 5 holes
    - Battleship, which has 4 holes
    - Cruiser, which has 3 holes
    - Destroyer, which has 2 holes
1.  For the sake of simplicity, in the actual implementation the "war field" dimensions are 10x10 and only one instance of each ship can be placed on the grid, without overlapping each other

### MCTS for games of imperfect information

MCTS algorithms like UCB1/UCT are mostly designed for games like Chess and Go, which are games of *perfect information*. Assuming two players, each player at any given time in the game can see the complete state of the game and, potentially, all legal moves and combinations which lead to a win. The game we have chosen, Battleships, is a game of *imperfect information*. One player can only see their current state of the board (location of ships and the hits and misses of opponent) and to win the player can only make (educated) guesses about the opponents ships locations. So, the game has random elements and hidden information, that need to be determinized at some point for MCTS algorithms to work.

### Information Set MCTS

Information Set MCTS is a family of algorithms that are suitable for games of imperfect information:

Information sets [1]:

> Information sets are collections of states, which appear in a game when one player has knowledge about the state that another player does not. For example, in a card game each player hides his own cards from his opponents. In this example, the information set contains all states which correspond to all possible permutations of opponent cards. A player knows which information set they are in, but not which state within that information set.

There are many possibilites to approach the uncertainty. We will use the _Determinization UTC_ technique first, and maybe explore the _Information Set UTC_ (both described in the referenced papers). 

The general idea is as follows (_opponent_ is always the human player, by _We_ we mean the AI):

1. The grid size and number of ships and length is known to both players
1. After shooting at the grid in each round the opponent has to reveal wether we HIT or MISS
1. We can use the opponent grid information to sample a set of possible grids (game state)
1. We run the MCTS algorithm for each of the sampled grids
1. The next moves suggested by each MCTS run are averaged, and the highest ranked is chosen
1. Let opponent do his turn
1. Repeat at 3.

### References

[0] Daniel Whitehouse, Edward J. Powley and Peter I. Cowling.
[Determinization and Information Set Monte Carlo Tree Search for the card game Dou Di Zhu.](http://orangehelicopter.com/academic/papers/cig11.pdf)
Proceedings of IEEE Conference on Computational Intelligence in Games (CIG), 87–94, 2011.

[1] Daniel Whitehouse, Edward J. Powley and Peter I. Cowling.
[Information Set Monte Carlo Tree Search](https://ieeexplore.ieee.org/abstract/document/6203567/authors#authors)
IEEE Transactions on Computational Intelligence and AI in Games

[2] http://www.aifactory.co.uk/newsletter/2013_01_reduce_burden.htm

### Lessons Learned

1. If you have a program in which some state will change very often (e.g. simulating from a given state), maybe don't use Java. It's harder to clone Objects, especially if they're complex. Functional programming would've made it a lot easier.