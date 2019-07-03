import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class TicTacToe {
    private String[][] board = {{" ", " ", " "}, {" ", " ", " "}, {" ", " ", " "}};
    private boolean[][] userMap = {{false, false, false}, {false, false, false}, {false, false, false}};
    private boolean[][] compMap = {{false, false, false}, {false, false, false}, {false, false, false}};


    private boolean all(boolean[] arg) {
        for (int i = 0; i < arg.length; i++) {
            if (!arg[i]) {
                return false;
            }
        }
        return true;
    }
    
    private boolean[] columnAt(boolean [][] arg, int index) {
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
        if (all(userMap[0]) || all(userMap[1]) || all(userMap[2]) || 
            all(columnAt(userMap, 0)) || all(columnAt(userMap, 1)) || all(columnAt(userMap, 2))) {
            System.out.println("Player wins!");
            return true;
        }

        if (all(compMap[0]) || all(compMap[1]) || all(compMap[2]) ||
                all(columnAt(compMap, 0)) || all(columnAt(compMap, 1)) || all(columnAt(compMap, 2))) {
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


    private boolean any2(boolean[] arg) {
        for (int i = 0, j = 0; i < 3; i++) {
            if (arg[i]) {
                j++;
            }
            if (j == 2) {
                return true;
            }
        }
        return false;
    }

    private int falseIndex(boolean[] arg) {
        for (int i = 0; i < 3; i++) {
            if (!arg[i]) {
                return i;
            }
        }
        return -1;
    }

    private void computerMove() {
        int[] index = {0, 0};
        Random r = new Random();

        for (int i = 0; i < 3; i++) {
            if (any2(compMap[i]) && !all(compMap[i])) {
                index[0] = i;
                index[1] = falseIndex(compMap[i]);
                setBoard(index, false);
                return;
            }

            boolean[] column = columnAt(compMap, i);
            if (any2(column) && !all(column)) {
                index[0] = falseIndex(column);
                index[1] = i;
                setBoard(index, false);
            }
        }

        while (true) {
            index[0] = r.nextInt(3);
            index[1] = r.nextInt(3);
            if (!positionTaken(index)) {
                break;
            }
        }
        setBoard(index, false);

    }

    public boolean update() {
        printBoard();
        userMove();
        computerMove();
        return didSomeoneWin();
    }
}
