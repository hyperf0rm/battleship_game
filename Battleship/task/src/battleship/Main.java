package battleship;

import java.util.*;

public class Main {

    public static final Scanner scanner = new Scanner(System.in);
    public static String[][] fieldP1;
    public static String[][] fogFieldP1;

    public static String[][] fieldP2;

    public static String[][] fogFieldP2;

    public static int playerOne = 1;
    public static int playerTwo = 2;

    public static Turn turn;

    public static void changeTurn() {
        System.out.println("Press Enter and pass the move to another player");
        System.out.print(">");
        while (true) {
            String next = scanner.nextLine().trim();
            if (next.equals("")) {
                break;
            }
        }
        if (turn == Turn.ONE) {
            turn = Turn.TWO;
        } else if (turn == Turn.TWO) {
            turn = Turn.ONE;
        }
    }

    public static void main(String[] args) {
        turn = Turn.ONE;

        fieldP1 = createField();
        fogFieldP1 = createField();
        fieldP2 = createField();
        fogFieldP2 = createField();

        placeShips(fieldP1, playerOne);

        System.out.println();

        changeTurn();

        placeShips(fieldP2, playerTwo);

        System.out.println();

        changeTurn();

        while (true) {

            printField(fogFieldP2);
            System.out.println("---------------------");
            printField(fieldP1);

            shooting(fieldP2, fogFieldP2);

            changeTurn();

            printField(fogFieldP1);
            System.out.println("---------------------");
            printField(fieldP2);

            shooting(fieldP1, fogFieldP1);

            changeTurn();

            if (checkForShips(fieldP1) || checkForShips(fieldP2)) {
                break;
            }
        }

    }

    public static void placeShips(String[][] field, int player) {
        System.out.printf("Player %d, place your ships to the game field", player);
        System.out.println();
        System.out.println();

        printField(field);

        placeShip(field, "Aircraft Carrier", 4);

        printField(field);

        placeShip(field, "Battleship", 3);

        printField(field);

        placeShip(field, "Submarine", 2);

        printField(field);

        placeShip(field, "Cruiser", 2);

        printField(field);

        placeShip(field, "Destroyer", 1);

        printField(field);
    }

    public static void shooting(String[][] field, String[][] fogField) {

        if (turn == Turn.ONE) {
            System.out.println("Player 1, it's your turn:");
        } else {
            System.out.println("Player 2, it's your turn:");
        }

        while (true) {
            int[] coords = getCoordsForShot();
            int row = coords[0];
            int col = coords[1];

            if (shoot(field, fogField, row, col)) {
                if (checkForShips(fieldP1) || checkForShips(fieldP2)) {
                    System.out.println("You sank the last ship. You won. Congratulations!");
                    break;
                } else if (checkForSunk(field, row, col)) {
                    System.out.println();
                    System.out.println("You sank a ship!");
                    break;
                } else {
                    System.out.println();
                    System.out.println("You hit a ship!");
                    break;
                }
            } else {
                System.out.println();
                System.out.println("You missed!");
                break;
            }

        }
    }

    public static boolean checkForSunk(String[][] fieldP1, int row, int col) {
        boolean sunken = true;

        if (row != 10) {
            if (fieldP1[row + 1][col].equals("O")) {
                sunken = false;
            }
        }

        if (row != 0) {
            if (fieldP1[row - 1][col].equals("O")) {
                sunken = false;
            }
        }

        if (col != 10) {
            if (fieldP1[row][col + 1].equals("O")) {
                sunken = false;
            }
        }
        if (col != 0) {
            if (fieldP1[row][col - 1].equals("O")) {
                sunken = false;
            }
        }

        return sunken;

    }

    public static boolean checkForShips(String[][] fieldP1) {

        for (int i = 0; i < fieldP1.length; i++) {
            for (int j = 0; j < fieldP1[i].length; j++) {
                if (fieldP1[i][j].equals("O")) {
                    return false;
                }
            }
        }

        return true;
    }

    public static String[][] createField() {
        String[][] fieldP1 = new String[11][11];

        // general fog of war
        for (int i = 0; i < fieldP1.length; i++) {
            Arrays.fill(fieldP1[i], "~");
        }

        // upper left cell - empty
        fieldP1[0][0] = " ";

        // numbers up top
        for (int i = 1; i < fieldP1.length; i++) {
            fieldP1[0][i] = "" + i;
        }

        // letters left
        int i = 1;
        for (char letter = 'A'; letter <= 'J'; letter++) {
            fieldP1[i][0] = letter + "";
            i++;

        }

        return fieldP1;
    }

    public static void printField(String[][] fieldP1) {

        for (String[] row : fieldP1) {
            for (String col : row) {
                System.out.print(col + " ");
            }
            System.out.println();
        }
    }

    public static void placeShip(String[][] fieldP1, String ship, int shipSize) {

        System.out.println();
        System.out.printf("Enter the coordinates of the %s (%d cells):", ship, shipSize + 1);
        System.out.println();

        while (true) {

            String[] coords = getCoords();
            String c1 = coords[0];
            String c2 = coords[1];

            char c1Letter = c1.charAt(0);
            char c2Letter = c2.charAt(0);
            int c1Number = Integer.parseInt(c1.substring(1));
            int c2Number = Integer.parseInt(c2.substring(1));
            if (c1.endsWith("10")) {
                c1Number = 10;
            }
            if (c2.endsWith("10")) {
                c2Number = 10;
            }

            if (c1Letter > c2Letter) {
                char tempChar = c1Letter;
                c1Letter = c2Letter;
                c2Letter = tempChar;
            }
            if (c1Number > c2Number) {
                int tempNumber = c1Number;
                c1Number = c2Number;
                c2Number = tempNumber;
            }

            if (c1Letter != c2Letter && c1Number != c2Number) {
                System.out.println();
                System.out.println("Error! Wrong ship location! Try again:");
                continue;
            }
            if (c1Letter == c2Letter) {
                if (c2Number - c1Number != shipSize) {
                    System.out.println();
                    System.out.printf("Error! Wrong length of the %s! Try again:", ship);
                    continue;
                }
                if (isAnotherShipCloseRow(fieldP1, c1Letter, c1Number, c2Number)) {
                    System.out.println();
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    continue;
                }
            }
            if (c1Number == c2Number) {
                if (c2Letter - c1Letter != shipSize) {
                    System.out.println();
                    System.out.printf("Error! Wrong length of the %s! Try again:", ship);
                    continue;
                }
                if (isAnotherShipCloseCol(fieldP1, c1Letter, c2Letter, c1Number)) {
                    System.out.println();
                    System.out.println("Error! You placed it too close to another one. Try again:");
                    continue;
                }
            }

            if (c1Letter == c2Letter) {
                placeRowShip(fieldP1, c1Letter, c1Number, c2Number);
                break;
            } else {
                placeColShip(fieldP1, c1Letter, c2Letter, c1Number);
                break;
            }
        }
    }

    public static void placeRowShip(String[][] fieldP1, char l, int n1, int n2) {
        int row = findRow(l);
        int col1 = findColumn(n1);
        int col2 = findColumn(n2);

        while (col1 <= col2) {
            fieldP1[row][col1] = "O";
            col1++;
        }
    }

    public static void placeColShip(String[][] fieldP1, char l1, char l2, int n) {
        int row1 = findRow(l1);
        int row2 = findRow(l2);
        int col = findColumn(n);

        while (row1 <= row2) {
            fieldP1[row1][col] = "O";
            row1++;
        }
    }

    public static String[] getCoords() {
        System.out.println();
        System.out.print(">");

        String coords = scanner.nextLine().trim();
        String c1 = null;
        String c2 = null;

        try {
            String[] parts = coords.split(" ");
            c1 = parts[0];
            c2 = parts[1];
        } catch (Exception e) {
            throw new InputMismatchException(e.getMessage());
        }
        return new String[]{c1, c2};
    }

    public static int[] getCoordsForShot() {

        while (true) {
            System.out.println();
            System.out.print(">");

            String coords = scanner.nextLine().trim();
            char cLetter = coords.charAt(0);
            int cNumber = Integer.parseInt(coords.substring(1));
            if (coords.endsWith("10")) {
                cNumber = 10;
            }

            if (isWrongCoordsForShot(coords, cLetter, cNumber)) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                continue;
            }

            int row = findRow(cLetter);
            int col = findColumn(cNumber);

            return new int[] {row, col};
        }
    }

    public static boolean shoot(String[][] fieldP1, String[][] fogFieldP1, int row, int col) {



        if (fieldP1[row][col].equals("O")) {
            fogFieldP1[row][col] = "X";
            fieldP1[row][col] = "X";
            return true;
        }

        if (fieldP1[row][col].equals("X")) {
            return true;
        }

        // else - miss
            fogFieldP1[row][col] = "M";
            fieldP1[row][col] = "M";
            return false;
    }

    public static boolean isWrongCoordsForShot(String coords, char cLetter, int cNumber) {
        if (coords.length() < 2 || coords.length() > 3) {
            return true;
        }

        if (cLetter < 'A' || cLetter > 'J') {
            return true;
        }

        return cNumber < 0 || cNumber > 10;
    }

    public static int findRow(char letter) {
        int row = 0;
        for (int i = 0; i < fieldP1.length; i++) {
            for (int j = 0; j < fieldP1[i].length; j++) {
                if (fieldP1[i][j].equals(Character.toString(letter))) {
                    row = i;
                    break;
                }
            }
        }
        return row;
    }

    public static int findColumn(int number) {
        int col = 0;
        for (int i = 0; i < fieldP1.length; i++) {
            for (int j = 0; j < fieldP1[i].length; j++) {
                if (fieldP1[i][j].equals(Integer.toString(number))) {
                    col = j;
                    break;
                }
            }
        }
        return col;
    }

    public static boolean isAnotherShipCloseRow(String[][] fieldP1, char c1Letter, int c1Number, int c2Number) {

        int row = findRow(c1Letter);
        int col1 = findColumn(c1Number);
        int col2 = findColumn(c2Number);
        if (col1 != 1) {
            col1--;
        }
        if (col2 != 10) {
            col2++;
        }

        while (col1 <= col2) {
            if (fieldP1[row][col1].equals("O")) {
                return true;
            }
            col1++;
        }
        return false;
    }

    public static boolean isAnotherShipCloseCol(String[][] fieldP1, char c1Letter, char c2Letter, int c1Number) {

        int row1 = findRow(c2Letter);
        int row2 = findRow(c2Letter);
        if (c1Letter != 'A') {
            row1--;
        }
        if (c2Letter != 'J') {
            row2++;
        }
        int col = findColumn(c1Number);

        while (row1 <= row2) {
            if (fieldP1[row1][col].equals("O")) {
                return true;
            }
            row1++;
        }
        return false;
    }
}


