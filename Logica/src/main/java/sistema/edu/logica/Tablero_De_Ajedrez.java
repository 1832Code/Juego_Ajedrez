package sistema.edu.logica;

import sistema.edu.logica.PIEZAS.*;

public class Tablero_De_Ajedrez {
    private Piezas[][] board;

    public Tablero_De_Ajedrez() {
        board = new Piezas[8][8];
        initializeBoard();
    }

    private void initializeBoard() {
        // Inicializa las piezas blancas
        board[0][0] = new torre(Piezas.Color.WHITE);
        board[0][1] = new Caballo(Piezas.Color.WHITE);
        board[0][2] = new Alfil(Piezas.Color.WHITE);
        board[0][3] = new reina(Piezas.Color.WHITE);
        board[0][4] = new Rey(Piezas.Color.WHITE);
        board[0][5] = new Alfil(Piezas.Color.WHITE);
        board[0][6] = new Caballo(Piezas.Color.WHITE);
        board[0][7] = new torre(Piezas.Color.WHITE);
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Peon(Piezas.Color.WHITE);
        }

        // Inicializa las piezas negras
        board[7][0] = new torre(Piezas.Color.BLACK);
        board[7][1] = new Caballo(Piezas.Color.BLACK);
        board[7][2] = new Alfil(Piezas.Color.BLACK);
        board[7][3] = new reina(Piezas.Color.BLACK);
        board[7][4] = new Rey(Piezas.Color.BLACK);
        board[7][5] = new Alfil(Piezas.Color.BLACK);
        board[7][6] = new Caballo(Piezas.Color.BLACK);
        board[7][7] = new torre(Piezas.Color.BLACK);
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Peon(Piezas.Color.BLACK);
        }
    }

    public Piezas getPiece(int row, int col) {
        if (isValidPosition(row, col)) {
            return board[row][col];
        }
        return null;
    }

    public void setPiece(int row, int col, Piezas piece) {
        if (isValidPosition(row, col)) {
            board[row][col] = piece;
        }
    }

    public void movePiece(int startRow, int startCol, int endRow, int endCol) {
        if (isValidPosition(startRow, startCol) && isValidPosition(endRow, endCol)) {
            board[endRow][endCol] = board[startRow][startCol];
            board[startRow][startCol] = null;
        }
    }

    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
}