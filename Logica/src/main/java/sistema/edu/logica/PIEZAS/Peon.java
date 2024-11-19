package sistema.edu.logica.PIEZAS;

import sistema.edu.logica.Tablero_De_Ajedrez;

import java.util.ArrayList;
import java.util.List;

public class Peon extends Piezas {
    public Peon(Color color) {
        super(color);
        this.symbol = color == Color.WHITE ? '♙' : '♟';
    }
    @Override
    public List<int[]> getLegalMoves(int startRow, int startCol, Tablero_De_Ajedrez board) {
        List<int[]> legalMoves = new ArrayList<>();
        int direction = (color == Color.WHITE) ? -1 : 1; // Blancas avanzan hacia arriba (-1), negras hacia abajo (+1)

        // Movimiento simple hacia adelante
        if (board.getPiece(startRow + direction, startCol) == null) {
            legalMoves.add(new int[]{startRow + direction, startCol});
        }

        // Captura en diagonal
        if (board.isValidPosition(startRow + direction, startCol - 1) &&
                board.getPiece(startRow + direction, startCol - 1) != null &&
                board.getPiece(startRow + direction, startCol - 1).getColor() != this.color) {
            legalMoves.add(new int[]{startRow + direction, startCol - 1});
        }

        if (board.isValidPosition(startRow + direction, startCol + 1) &&
                board.getPiece(startRow + direction, startCol + 1) != null &&
                board.getPiece(startRow + direction, startCol + 1).getColor() != this.color) {
            legalMoves.add(new int[]{startRow + direction, startCol + 1});
        }

        return legalMoves;
    }
    @Override
    public boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Tablero_De_Ajedrez board) {
        int direction = (color == Color.WHITE) ? 1 : -1;

        // Movimiento normal de un paso
        if (startCol == endCol && endRow == startRow + direction && board.getPiece(endRow, endCol) == null) {
            return true;
        }

        // Movimiento inicial de dos pasos
        if (startCol == endCol && endRow == startRow + 2 * direction &&
                board.getPiece(endRow, endCol) == null && board.getPiece(startRow + direction, startCol) == null &&
                ((color == Color.WHITE && startRow == 1) || (color == Color.BLACK && startRow == 6))) {
            return true;
        }

        // Captura diagonal
        if (Math.abs(startCol - endCol) == 1 && endRow == startRow + direction) {
            Piezas targetPiece = board.getPiece(endRow, endCol);
            return targetPiece != null && targetPiece.getColor() != this.color;
        }

        return false;
    }
}