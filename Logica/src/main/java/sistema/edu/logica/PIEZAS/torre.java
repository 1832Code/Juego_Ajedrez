package sistema.edu.logica.PIEZAS;

import sistema.edu.logica.Tablero_De_Ajedrez;

import java.util.ArrayList;
import java.util.List;

public class torre extends Piezas {
    public torre(Color color) {
        super(color);
        this.symbol = color == Color.WHITE ? '♖' : '♜';
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
        if (!board.isValidPosition(startRow, startCol) || !board.isValidPosition(endRow, endCol)) {
            return false;
        }

        if (startRow == endRow && startCol == endCol) {
            return false;
        }

        if (startRow != endRow && startCol != endCol) {
            return false;
        }

        int rowDirection = Integer.compare(endRow, startRow);
        int colDirection = Integer.compare(endCol, startCol);

        int currentRow = startRow + rowDirection;
        int currentCol = startCol + colDirection;

        while (currentRow != endRow || currentCol != endCol) {
            if (board.getPiece(currentRow, currentCol) != null) {
                return false;
            }
            currentRow += rowDirection;
            currentCol += colDirection;
        }

        Piezas endPiece = board.getPiece(endRow, endCol);
        return endPiece == null || endPiece.getColor() != this.color;
    }
}