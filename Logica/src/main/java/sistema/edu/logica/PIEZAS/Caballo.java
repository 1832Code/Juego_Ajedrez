package sistema.edu.logica.PIEZAS;
import sistema.edu.logica.Tablero_De_Ajedrez;

import java.util.ArrayList;
import java.util.List;

public class Caballo extends Piezas {

    public Caballo(Color color) {
        super(color);
        this.symbol = color == Color.WHITE ? '♘' : '♞';
    }

    @Override
    public List<int[]> getLegalMoves(int startRow, int startCol, Tablero_De_Ajedrez board) {
        List<int[]> legalMoves = new ArrayList<>();

        // El rey puede moverse en una casilla en cualquier dirección
        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1},        {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        for (int[] direction : directions) {
            int newRow = startRow + direction[0];
            int newCol = startCol + direction[1];

            if (board.isValidPosition(newRow, newCol)) {
                Piezas targetPiece = board.getPiece(newRow, newCol);
                if (targetPiece == null || targetPiece.getColor() != this.color) {
                    legalMoves.add(new int[]{newRow, newCol});
                }
            }
        }

        return legalMoves;
    }
    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Tablero_De_Ajedrez board) {
        int rowDiff = Math.abs(endRow - startRow);
        int colDiff = Math.abs(endCol - startCol);

        // El caballo se mueve en forma de "L": 2 casillas en una dirección, 1 en otra
        if ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)) {
            Piezas targetPiece = board.getPiece(endRow, endCol);
            return targetPiece == null || targetPiece.getColor() != getColor();
        }
        return false;
    }
}