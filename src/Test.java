public class Test {
    public static void main(String[] args) {
        TicTacToe t = new TicTacToe();
        t.menu();

        while (!t.update()) {
        }
    }
}
