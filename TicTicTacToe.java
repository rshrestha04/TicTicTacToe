/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tictictactoe;
import java.util.*;
/**
 *
 * @author jrsullins
 */
public class TicTicTacToe {

    /********************** YOUR CODE GOES HERE **********************/
 
    /**
    * Computes the heuristic value of the board for a given player.
    * @param board A 4x4 array of characters, either 'X', 'O', or ' '.
    * @param ai Which character the computer is, either 'X' or 'O'.
    * @return A positive integer giving the value of the board for the character 
    * the AI is playing, between 0 and 1000.
    */
    private static int lastScore_ai = 0;  //remembers last score of AI
    private static int lastScore_pl= 0;  //remembers last score of player
    
    public static float evaluate(char[][] board, char ai) {
        
    
       
        int value=500 ; //the value of each step initially placed in middle ground
        
        char who; //player Character 
        
        //Findinf out the player character
        if (ai=='X') who ='O';
        else 
            who='X';
        
        int score_ai = getScore(board, ai);
           
   
        int score_pl = getScore(board, who);
          
        //To correct when evaluation is returned and lastsocre is updated but the steps actually do not take place 
        if(score_pl < lastScore_pl)lastScore_pl = score_pl;
        if(score_ai < lastScore_ai)lastScore_ai = score_ai;
  
          
        if(winner(board, ai)) return 1000; // If a move is about to win the game for ai always win 
        
        
        //block if player is about to win the game. This is given the least value 0 and is proritized only below winning the game by 4.
        if(winner(board,who))return 0;
        
        //if player tries to get 3 in a row try to block that by making the evaluation value less
        if(score_pl > lastScore_pl){
            lastScore_pl = score_pl;
          value-=200;
          
        }
        
        
       
        //if current moves increases the score of AI this Step is encouraged with higher evaluation value
        if(score_ai > lastScore_ai){
            lastScore_ai = score_ai;
            value+=200;
        }
        
        
        //prioritizing the moves in the middle of the board
        
        if(count(board,ai) == 1) value +=20;
        if(count(board,ai) == 2) value +=40;
        if(count(board,ai) == 3)value +=60;
        if(count(board,ai) == 4)value +=80;
        
        
        
        //returning the final value after the evaluation
        return value;
        
    }
    
    
    //helper function to count the numbers of AI character in the middle of the tictictack to graph 
    public static int count (char[][] board, char ai){
    int insideBoxes = 0;
        for(int x=1;x<3;x++){
            for(int y=1;y<3;y++){
            if(board[x][y]==ai) insideBoxes++;
            }
        }
    return insideBoxes;
    }
    
    
    
    






/***************** END OF YOUR CODE ********************************/

     
    /**
     * Determines the player in question (X or 0) a winner by having 4 in a row.
     * @param board The game board
     * @param who Which character we are checking (ether 'X' or 'O')
     * @return true if who has 4 in a row, false otherwise
     */
    public static boolean winner(char[][] board, char who) {
        for (int i = 0; i < 4; i++) {
            if (board[i][0] == who && 
                board[i][1] == who &&
                board[i][2] == who &&
                board[i][3] == who) {
                //cout << "Win in row " << i << "\n";
                return true;
            }
        }
        for (int i = 0; i < 4; i++) {
            if (board[0][i] == who && 
                board[1][i] == who &&
                board[2][i] == who &&
                board[3][i] == who) {            
                //cout << "Win in row " << i << "\n";
                return true;
            }   
        }
        if (board[0][0] == who && 
            board[1][1] == who &&
            board[2][2] == who &&
            board[3][3] == who) {
            // cout << "Win along main diagonal\n";
            return true;
            }
        
        if (board[0][3] == who && 
            board[1][2] == who &&
            board[2][1] == who &&
            board[3][0] == who) {
            // cout << "Win along other diagonal\n";
            return true;
            }
        return false;
    }


    /**
     * Exhaustively searches the current board to count the sets of 3 this character has in a row.
     * @param board The game board
     * @param who Which character we are checking (ether 'X' or 'O')
     * @return The number of 3 in a row that character has.
     */
    public static int getScore(char[][] board, char who) {
      int row, col;
      int X, O;
      int score = 0;

      /* check all rows */
      for (row = 0; row < 4; row++)
        for (col = 0; col < 2; col++)
          if (board[row][col] == who &&
              board[row][col+1] == who &&
              board[row][col+2] == who) score++;

      /* check all columns */
      for (row = 0; row < 2; row++)
        for (col = 0; col < 4; col++)
          if (board[row][col] == who &&
              board[row+1][col] == who &&
              board[row+2][col] == who) score++;

      /* check all diagonals */
      for (row = 0; row < 2; row++)
        for (col = 0; col < 2; col++)
          if (board[row][col] == who &&
              board[row+1][col+1] == who &&
              board[row+2][col+2] == who) score++;
      for (row = 0; row < 2; row++)
        for (col = 2; col < 4; col++)
          if (board[row][col] == who &&
              board[row+1][col-1] == who &&
              board[row+2][col-2] == who) score++;
      return score;
      }
   


    
    static final int MAXLEVEL = 2;
    
 
/* This is the main function for playing the game. It alternatively
   prompts the user for a move, and uses the minmax algorithm in 
   conjunction with the given evaluation function to determine the
   opposing move. This continues until the board is full. It returns
   the number scored by X minus the number scored by O. */

    public static boolean run(int[] scores, char who) { 
        int i, j;
        char current, other;
        int playerrow, playercol;
        int[] location = new int[2];  // Allows us to pass row, col by reference
        int move = 1;

        /* Initialize the board */
        char[][] board = new char[4][4];
        for (i = 0; i < 4; i++) { 
            for (j = 0; j < 4; j++) {
                board[i][j] = ' ';
            }
        }

        if (who == 'O') display(board);

        while (move <= 16) {
            if (move % 2 == 1) {
                current = 'X';
                other = 'O';
            }
            else {                
                current = 'O';
                other = 'X';
            }

            if (current == who) {        /* The computer's move */
                choose(location, board, who);  /* Call function to compute move */
                System.out.println("Computer chooses " + (location[0]+1) + ", " + (location[1]+1));
                if (board[location[0]][location[1]] == ' ') 
                    board[location[0]][location[1]] = current;
                else {
                    System.out.println("BUG! " + (location[0]+1) + ", " + (location[1]+1) + " OCCUPIED!!!");
                    System.exit(0);
                }
                if (winner(board, who)) {
                    System.out.println("Computer has 4 in a row! Computer wins!");
                    display(board);
                    return true;
                }
            }

            else {                       /* Ask for player's move */
                Scanner in = new Scanner(System.in);
                System.out.print("Player " + current + ", enter row: ");
                playerrow = in.nextInt();
                System.out.print("Player " + current + ", enter column: ");
                playercol = in.nextInt();
                while (board[playerrow-1][playercol-1] != ' ' ||
                    playerrow < 1 || playerrow > 4 ||
                    playercol < 1 || playercol > 4) {
                        System.out.println("Illegal move! You cannot use that square!");
                
                        System.out.print("Player " + current + ", enter row: ");
                        playerrow = in.nextInt();
                        System.out.print("Player " + current + ", enter column: ");
                        playercol = in.nextInt();
                }
                playercol--; playerrow--;
                board[playerrow][playercol] = current;
                if (winner(board, current)) {
                    System.out.println("Player has 4 in a row! Player wins!");
                    display(board);
                    return true;            
                }
            }
 
            display(board);    /* Redisplay board to show the move */

            move++; /* Increment the move number and do next move. */
        }
        scores[0] = getScore(board, 'X');
        scores[1] = getScore(board, 'O');
        return false;
    }


    
/* This displays the current configuration of the board. */

    public static void display(char[][] board) {
        int row, col;  
        int scores[] = new int[2];
        System.out.print("\n");
        for (row = 3; row >= 0; row--) {
            System.out.print("  +-+-+-+-+\n");
            System.out.print((row+1) + " ");
            for (col = 0; col < 4; col++) {
            if (board[row][col] == 'X')  /* if contents are 0, print space */
                System.out.print("|X");
            else if (board[row][col] == 'O')
                System.out.print("|0");
            else System.out.print("| ");
            }
            System.out.print("|\n");
        }
        System.out.print("  +-+-+-+-+\n");  /* print base, and indices */
        System.out.print("   1 2 3 4\n");
        scores[0] = getScore(board, 'X');
        scores[1] = getScore(board, 'O');
        System.out.println("X: " + scores[0]);
        System.out.println("O: " + scores[1]);
    }
   
/* Basic function for choosing the computer's move. It essentially
   initiates the first level of the MINMAX algorithm, and returns
   the column number it chooses. */

    public static void choose(int[] location, char[][] board, char who) {
        int move; 
        float value;
        getmax(location, board, 1, who);
    }


/* This handles any MAX level of a MINMAX tree. This essentially handles moves for the computer. */

    public static float getmax(int[] location, char[][] board, int level, char who) {
        char[][] tempboard = new char[4][4];
        int r,c = 0;
        float max = -1;
        float val;
        int[] tempLocation = new int[2];
        for (r = 0; r < 4; r++)
            for (c = 0; c < 4; c++) {  /* Try each row and column in board */
                if (board[r][c] == ' ') {     /* Make sure square not full */

                /* To avoid changing original board  during tests, make a copy */
                copy(tempboard, board); 

                /* Find out what would happen if we chose this column */
                tempboard[r][c] = who;

                /* If this is the bottom of the search tree (that is, a leaf) we need
                    to use the evaluation function to decide how good the move is */
                if (level == MAXLEVEL) 
                    val = evaluate(tempboard, who);

                /* Otherwise, this move is only as good as the worst thing our
                    opponent can do to us. */
                else
                    val = getmin(tempLocation, tempboard, level+1, who);

                /* Return the highest evaluation, and set call by ref. parameter
                    "move" to the corresponding column */
                if (val > max) {
                    max = val;
                    if (level==1) {location[0] = r; location[1] = c;}
                 }

            }
        }
        return max;
    }

/* This handles any MIN level of a MINMAX tree. This essentially handles moves for the player. */

    public static float getmin(int[] location, char[][] board, int level, char who) {
        char[][] tempboard = new char[4][4];
        int r,c = 0;   
        int[] tempLocation = new int[2];
        float min = 10001;
        float val;

        /* Since this is opponent's move, we need to figure out which they are */
        char other;
        if (who == 'X') other = 'O'; else other = 'X'; 

        for (r = 0; r < 4; r++)
            for (c = 0; c < 4; c++) {  /* Try each row and column in board */
                if (board[r][c] == ' ') {     /* Make sure square not full */

                    /* To avoid changing original board  during tests, make a copy */
                    copy(tempboard, board);

                    /* Find out what would happen if opponent chose this column */
                    tempboard[r][c] = other;

                    /* If this is the bottom of the search tree (that is, a leaf) we need
                    to use the evaluation function to decide how good the move is */
                    if (level == MAXLEVEL)  
                        val = evaluate(tempboard, who);

                    /* Otherwise, find the best thing that we can do if opponent
                        chooses this move. */
                    else
                        val = getmax(tempLocation, tempboard, level+1, who);

                    /* Return the lowest evaluation (which we will assume will be 
                        chosen by opponent, and set call by ref. parameter
                        "move" to the corresponding column */
                    if (val < min) {
                        min = val;
                        // *move = col;
                    }
                }
            }
        return min;
   }


/* This function makes a copy of a given board. This is necessary to be
   able to "try out" the effects of different moves without messing up
   the actual current board. */

    public static void copy(char[][] a, char[][] b) {
        int i, j;
        for (i = 0; i < 4; i++) { 
            for (j = 0; j < 4; j++) {
                a[i][j] = b[i][j];  
            }
        }
    }
   


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        char player, computer;
        int[] scores = new int[2];
        /* Decide who goes first */
        System.out.print("Do you want to play X or O: ");
        Scanner in = new Scanner(System.in);
        player = in.nextLine().charAt(0);
        if (player == 'X') computer = 'O';
        else computer = 'X';
        boolean win = false;
        win = run(scores, computer);
        if (!win)
            System.out.println("\nFinal score: \nX: " + scores[0] + "\nO: " + scores[1] + "\n");
        }
}


/***************** END OF YOUR CODE ********************************/

     
    /**
     * Determines the player in question (X or 0) a winner by having 4 in a row.
     * @param board The game board
     * @param who Which character we are checking (ether 'X' or 'O')
     * @return true if who has 4 in a row, false otherwise
     */
    public static boolean winner(char[][] board, char who) {
        for (int i = 0; i < 4; i++) {
            if (board[i][0] == who && 
                board[i][1] == who &&
                board[i][2] == who &&
                board[i][3] == who) {
                //cout << "Win in row " << i << "\n";
                return true;
            }
        }
        for (int i = 0; i < 4; i++) {
            if (board[0][i] == who && 
                board[1][i] == who &&
                board[2][i] == who &&
                board[3][i] == who) {            
                //cout << "Win in row " << i << "\n";
                return true;
            }   
        }
        if (board[0][0] == who && 
            board[1][1] == who &&
            board[2][2] == who &&
            board[3][3] == who) {
            // cout << "Win along main diagonal\n";
            return true;
            }
        
        if (board[0][3] == who && 
            board[1][2] == who &&
            board[2][1] == who &&
            board[3][0] == who) {
            // cout << "Win along other diagonal\n";
            return true;
            }
        return false;
    }


    /**
     * Exhaustively searches the current board to count the sets of 3 this character has in a row.
     * @param board The game board
     * @param who Which character we are checking (ether 'X' or 'O')
     * @return The number of 3 in a row that character has.
     */
    public static int getScore(char[][] board, char who) {
      int row, col;
      int X, O;
      int score = 0;

      /* check all rows */
      for (row = 0; row < 4; row++)
        for (col = 0; col < 2; col++)
          if (board[row][col] == who &&
              board[row][col+1] == who &&
              board[row][col+2] == who) score++;

      /* check all columns */
      for (row = 0; row < 2; row++)
        for (col = 0; col < 4; col++)
          if (board[row][col] == who &&
              board[row+1][col] == who &&
              board[row+2][col] == who) score++;

      /* check all diagonals */
      for (row = 0; row < 2; row++)
        for (col = 0; col < 2; col++)
          if (board[row][col] == who &&
              board[row+1][col+1] == who &&
              board[row+2][col+2] == who) score++;
      for (row = 0; row < 2; row++)
        for (col = 2; col < 4; col++)
          if (board[row][col] == who &&
              board[row+1][col-1] == who &&
              board[row+2][col-2] == who) score++;
      return score;
      }
   


    
    static final int MAXLEVEL = 2;
    
 
/* This is the main function for playing the game. It alternatively
   prompts the user for a move, and uses the minmax algorithm in 
   conjunction with the given evaluation function to determine the
   opposing move. This continues until the board is full. It returns
   the number scored by X minus the number scored by O. */

    public static boolean run(int[] scores, char who) { 
        int i, j;
        char current, other;
        int playerrow, playercol;
        int[] location = new int[2];  // Allows us to pass row, col by reference
        int move = 1;

        /* Initialize the board */
        char[][] board = new char[4][4];
        for (i = 0; i < 4; i++) { 
            for (j = 0; j < 4; j++) {
                board[i][j] = ' ';
            }
        }

        if (who == 'O') display(board);

        while (move <= 16) {
            if (move % 2 == 1) {
                current = 'X';
                other = 'O';
            }
            else {                
                current = 'O';
                other = 'X';
            }

            if (current == who) {        /* The computer's move */
                choose(location, board, who);  /* Call function to compute move */
                System.out.println("Computer chooses " + (location[0]+1) + ", " + (location[1]+1));
                if (board[location[0]][location[1]] == ' ') 
                    board[location[0]][location[1]] = current;
                else {
                    System.out.println("BUG! " + (location[0]+1) + ", " + (location[1]+1) + " OCCUPIED!!!");
                    System.exit(0);
                }
                if (winner(board, who)) {
                    System.out.println("Computer has 4 in a row! Computer wins!");
                    display(board);
                    return true;
                }
            }

            else {                       /* Ask for player's move */
                Scanner in = new Scanner(System.in);
                System.out.print("Player " + current + ", enter row: ");
                playerrow = in.nextInt();
                System.out.print("Player " + current + ", enter column: ");
                playercol = in.nextInt();
                while (board[playerrow-1][playercol-1] != ' ' ||
                    playerrow < 1 || playerrow > 4 ||
                    playercol < 1 || playercol > 4) {
                        System.out.println("Illegal move! You cannot use that square!");
                
                        System.out.print("Player " + current + ", enter row: ");
                        playerrow = in.nextInt();
                        System.out.print("Player " + current + ", enter column: ");
                        playercol = in.nextInt();
                }
                playercol--; playerrow--;
                board[playerrow][playercol] = current;
                if (winner(board, current)) {
                    System.out.println("Player has 4 in a row! Player wins!");
                    display(board);
                    return true;            
                }
            }
 
            display(board);    /* Redisplay board to show the move */

            move++; /* Increment the move number and do next move. */
        }
        scores[0] = getScore(board, 'X');
        scores[1] = getScore(board, 'O');
        return false;
    }


    
/* This displays the current configuration of the board. */

    public static void display(char[][] board) {
        int row, col;  
        int scores[] = new int[2];
        System.out.print("\n");
        for (row = 3; row >= 0; row--) {
            System.out.print("  +-+-+-+-+\n");
            System.out.print((row+1) + " ");
            for (col = 0; col < 4; col++) {
            if (board[row][col] == 'X')  /* if contents are 0, print space */
                System.out.print("|X");
            else if (board[row][col] == 'O')
                System.out.print("|0");
            else System.out.print("| ");
            }
            System.out.print("|\n");
        }
        System.out.print("  +-+-+-+-+\n");  /* print base, and indices */
        System.out.print("   1 2 3 4\n");
        scores[0] = getScore(board, 'X');
        scores[1] = getScore(board, 'O');
        System.out.println("X: " + scores[0]);
        System.out.println("O: " + scores[1]);
    }
   
/* Basic function for choosing the computer's move. It essentially
   initiates the first level of the MINMAX algorithm, and returns
   the column number it chooses. */

    public static void choose(int[] location, char[][] board, char who) {
        int move; 
        float value;
        getmax(location, board, 1, who);
    }


/* This handles any MAX level of a MINMAX tree. This essentially handles moves for the computer. */

    public static float getmax(int[] location, char[][] board, int level, char who) {
        char[][] tempboard = new char[4][4];
        int r,c = 0;
        float max = -1;
        float val;
        int[] tempLocation = new int[2];
        for (r = 0; r < 4; r++)
            for (c = 0; c < 4; c++) {  /* Try each row and column in board */
                if (board[r][c] == ' ') {     /* Make sure square not full */

                /* To avoid changing original board  during tests, make a copy */
                copy(tempboard, board); 

                /* Find out what would happen if we chose this column */
                tempboard[r][c] = who;

                /* If this is the bottom of the search tree (that is, a leaf) we need
                    to use the evaluation function to decide how good the move is */
                if (level == MAXLEVEL) 
                    val = evaluate(tempboard, who);

                /* Otherwise, this move is only as good as the worst thing our
                    opponent can do to us. */
                else
                    val = getmin(tempLocation, tempboard, level+1, who);

                /* Return the highest evaluation, and set call by ref. parameter
                    "move" to the corresponding column */
                if (val > max) {
                    max = val;
                    if (level==1) {location[0] = r; location[1] = c;}
                 } 

            }
        }
        return max;
    }

/* This handles any MIN level of a MINMAX tree. This essentially handles moves for the player. */

    public static float getmin(int[] location, char[][] board, int level, char who) {
        char[][] tempboard = new char[4][4];
        int r,c = 0;   
        int[] tempLocation = new int[2];
        float min = 10001;
        float val;

        /* Since this is opponent's move, we need to figure out which they are */
        char other;
        if (who == 'X') other = 'O'; else other = 'X'; 

        for (r = 0; r < 4; r++)
            for (c = 0; c < 4; c++) {  /* Try each row and column in board */
                if (board[r][c] == ' ') {     /* Make sure square not full */

                    /* To avoid changing original board  during tests, make a copy */
                    copy(tempboard, board);

                    /* Find out what would happen if opponent chose this column */
                    tempboard[r][c] = other;

                    /* If this is the bottom of the search tree (that is, a leaf) we need
                    to use the evaluation function to decide how good the move is */
                    if (level == MAXLEVEL)  
                        val = evaluate(tempboard, who);

                    /* Otherwise, find the best thing that we can do if opponent
                        chooses this move. */
                    else
                        val = getmax(tempLocation, tempboard, level+1, who);

                    /* Return the lowest evaluation (which we will assume will be 
                        chosen by opponent, and set call by ref. parameter
                        "move" to the corresponding column */
                    if (val < min) {
                        min = val;
                        // *move = col;
                    }
                }
            }
        return min;
   }


/* This function makes a copy of a given board. This is necessary to be
   able to "try out" the effects of different moves without messing up
   the actual current board. */

    public static void copy(char[][] a, char[][] b) {
        int i, j;
        for (i = 0; i < 4; i++) { 
            for (j = 0; j < 4; j++) {
                a[i][j] = b[i][j];  
            }
        }
    }
   


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        char player, computer;
        int[] scores = new int[2];
        /* Decide who goes first */
        System.out.print("Do you want to play X or O: ");
        Scanner in = new Scanner(System.in);
        player = in.nextLine().charAt(0);
        if (player == 'X') computer = 'O';
        else computer = 'X';
        boolean win = false;
        win = run(scores, computer);
        if (!win)
            System.out.println("\nFinal score: \nX: " + scores[0] + "\nO: " + scores[1] + "\n");
        }
}