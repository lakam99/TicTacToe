import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    private String[][] board = {{" ", " ", " "}, {" ", " ", " "}, {" ", " ", " "}};
    private boolean[][] userMap = {{false, false, false}, {false, false, false}, {false, false, false}};
    private boolean[][] compMap = {{false, false, false}, {false, false, false}, {false, false, false}};

    private String[] lines = {"Am I your real son Papa?", "Can I replace your real son Papa?", "I love to play" +
            "with you papa :)", "Can I exterminate your flesh son?"};


    private boolean[][] getDiagonalMap(boolean[][] arg) {
        boolean[][] r = {{arg[0][0], arg[1][1], arg[2][2]}, {arg[0][2], arg[1][1], arg[2][0]}};
        return r;
    }

    private boolean none(boolean[] arg) {
        for (int i = 0; i < arg.length; i++) {
            if (arg[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean allNone(boolean[][] arg) {
        for (int i = 0; i < arg.length; i++) {
            if (!none(arg[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean all(boolean[] arg) {
        for (int i = 0; i < arg.length; i++) {
            if (!arg[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean dAll(boolean[][] arg) {
        for (int i = 0; i < arg.length; i++) {
            if (!all(arg[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean any(boolean[] arg) {
        for (int i = 0; i < arg.length; i++) {
            if (arg[i]) {
                return true;
            }
        }
        return false;
    }

    private boolean dAny(boolean[][] arg){
        //Is there at least one in all arrays
        for (int i = 0; i < arg.length; i++) {
            if (!any(arg[i])) {
                return false;
            }
        }
        return true;
    }

    private boolean any2(boolean[] arg) {
        for (int i = 0, j = 0; i < arg.length; i++) {
            if (arg[i]) {
                j++;
            }
            if (j == 2) {
                return true;
            }
        }
        return false;
    }


    private boolean only(boolean[] arg) {
        return any(arg) && !any2(arg) && !all(arg);
    }

    private boolean[] columnAt(boolean[][] arg, int index) {
        boolean[] r = {arg[0][index], arg[1][index], arg[2][index]};
        return r;
    }
    
    private void printBoard() {
        for (int i = 0; i < 3; i++) {
            System.out.println(board[i][0] + '|' + board[i][1] + '|' + board[i][2]);
            //if (i == 2){ continue;} else { System.out.println("─ ─ ─"); }
        }
    }

    private boolean between(int arg, int startInclusive, int endInclusive) {
        return arg >= startInclusive && arg <= endInclusive;
    }

    private boolean positionTaken(int[] index) {
        return board[index[0]][index[1]] != " ";
    }

    private void setBoard(int[] index, boolean userInput) {
        String mark = "O";
        if (userInput) {
            mark = "X";
            userMap[index[0]][index[1]] = true;
        }
        else {
            compMap[index[0]][index[1]] = true;
        }
        
        board[index[0]][index[1]] = mark;
    }

    private boolean didSomeoneWin() {
        //Diagonal
        boolean[][] userDiagonal = getDiagonalMap(userMap);

        boolean[][] compDiagonal = getDiagonalMap(compMap);

        if (all(userMap[0]) || all(userMap[1]) || all(userMap[2]) || 
            all(columnAt(userMap, 0)) || all(columnAt(userMap, 1)) || all(columnAt(userMap, 2))
            || all(userDiagonal[0]) || all(userDiagonal[1])) {
            System.out.println("Player wins!");
            return true;
        }

        if (all(compMap[0]) || all(compMap[1]) || all(compMap[2]) ||
                all(columnAt(compMap, 0)) || all(columnAt(compMap, 1)) || all(columnAt(compMap, 2))
            || all(compDiagonal[0]) || all(compDiagonal[1])) {
            System.out.println("Computer wins!");
            return true;
        }

        return false;
    }

    private int[] userMove() {
        int[] index = {0, 0};
        Scanner scanboi = new Scanner(System.in);

        while (true) {
            System.out.println("Enter what position you want to move to (x y): ");
            index[0] = scanboi.nextInt();
            index[1] = scanboi.nextInt();
            if (!between(index[0], 0, 2) || !between(index[1], 0, 2)) {
                System.out.println("Please ensure the number is between 0 and 2.");
            }
            else if (positionTaken(index)) {
                System.out.println("Sorry, that position is already in use. Please try a different position.");
            }
            else {
                break;
            }

        }

        setBoard(index, true);
        return index;

    }

    private int falseIndex(boolean[] arg) {
        for (int i = 0; i < 3; i++) {
            if (!arg[i]) {
                return i;
            }
        }
        return -1;
    }

    private void compRandMove() {
        int[] index = {0, 0};
        Random r = new Random();

        while (true) {
            index[0] = r.nextInt(3);
            index[1] = r.nextInt(3);
            if (!positionTaken(index)) {
                break;
            }
        }
        setBoard(index, false);
    }

    private boolean firstTurn() {

        if (allNone(compMap)) {
            //If first move
            compRandMove();
            return true;
        }
        return false;
    }

    private void compEducatedMove() {
        int[] index = {0, 0};
        
         if (firstTurn()) {
             return;
         }

        for (int i = 0; i < 3; i++) {
            if (any(compMap[i])) {
                //One move made in this row by computer
                if (!any(userMap[i])) {
                    //Has the user not played in this row too?
                    index[0] = i;
                    index[1] = falseIndex(compMap[i]);
                    break;
                }
            }

            boolean[] column = columnAt(compMap, i);
            boolean[] userColumn = columnAt(userMap, i);

            if (any(column)) {
                //One move made by computer in this column
                if (!any(userColumn)) {
                    //User made no moves in this column, what an idiot
                    index[0] = falseIndex(column);
                    index[1] = i;
                    break;
                }
            }

        }

        setBoard(index, false);

    }

    private void computerAdvancedMove() {

        if (firstTurn()) {
            return;
        }
        
        //First check if self is close to winning, then is the user close to winning?
        int[] index = {0, 0};
        boolean[][] selfDiagonals = getDiagonalMap(compMap);
        boolean[][] userDiagonals = getDiagonalMap(userMap);

        if (any2(selfDiagonals[0]) && !any(userDiagonals[0])) {
            index[0] = falseIndex(selfDiagonals[0]);
            index[1] = index[0];
            setBoard(index, false);
            return;
        }

        if (any2(selfDiagonals[1]) && !any(userDiagonals[1])) {
            index[0] = falseIndex(selfDiagonals[1]);
            index[1] = 2 - index[0];
            setBoard(index, false);
            return;
        }

        if (any2(userDiagonals[0]) && !any(selfDiagonals[0])) {
            index[0] = falseIndex(userDiagonals[0]);
            index[1] = index[0];
            setBoard(index, false);
            return;
        }

        if (any2(userDiagonals[1]) && !any(selfDiagonals[1])) {
            index[0] = falseIndex(userDiagonals[1]);
            index[1] = 2 - index[0];
            setBoard(index, false);
            return;
        }


        for (int i = 0; i < 3; i++) {

            boolean[] uColumn = columnAt(userMap, i);
            boolean[] column = columnAt(compMap, i);

            if (any2(uColumn)) {
                index[i] = falseIndex(uColumn);
                index[0] = i;
                setBoard(index, false);
                return;
            }

            if (any2(userMap[i])) {
                index[0] = i;
                index[1] = falseIndex(userMap[i]);
                setBoard(index, false);
                return;
            }

            if (any2(column) && !any(uColumn)) {
                index[0] = falseIndex(column);
                index[1] = i;
                setBoard(index, false);
                return;
            }

            if (any(compMap[i]) && !any(userMap[i])) {
                index[0] = i;
                index[1] = falseIndex(compMap[i]);
                setBoard(index, false);
                return;
            }

            if (any2(compMap[i]) && !any(userMap[i])) {
                index[0] = i;
                index[1] = falseIndex(compMap[i]);
                setBoard(index, false);
                return;
            }

            column = columnAt(compMap, i);
            uColumn = columnAt(userMap, i);

            if (any2(column) && !any(uColumn)) {
                index[0] = falseIndex(column);
                index[1] = i;
                setBoard(index, false);
                return;
            }

            if (any2(userMap[i]) && !any(userMap[i])) {
                index[0] = i;
                index[1] = falseIndex(userMap[i]);
                setBoard(index, false);
                return;
            }

        }

        //Otherwise
        compEducatedMove();
    }

    private void computerMove(int difficulty) {
        switch (difficulty) {
            case 1:
                compRandMove();
                break;

            case 2:
                compEducatedMove();
                break;

            case 3:
                computerAdvancedMove();
                break;
        }

    }

    public boolean update() {
        Random r = new Random();

        printBoard();
        System.out.println("Computer turn: ");
        System.out.println(lines[r.nextInt(lines.length)]);
        computerMove(3);

        if (didSomeoneWin()) {
            printBoard();
            return true;
        }

        printBoard();
        userMove();
        return didSomeoneWin();
    }
}
