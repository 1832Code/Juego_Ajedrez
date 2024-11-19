package sistema.edu.logica;

import sistema.edu.logica.PIEZAS.Peon;
import sistema.edu.logica.PIEZAS.Piezas;
import sistema.edu.logica.PIEZAS.Rey;
import sistema.edu.logica.PIEZAS.reina;

import java.util.List;

public class Logic_Juego {
    // Atributos de la clase
    private Tablero_De_Ajedrez board; // Tablero de ajedrez donde se jugará la partida
    private Piezas.Color currentTurn; // Indica de quién es el turno actual (blanco o negro)

    // Constructor de la clase
    public Logic_Juego() {
        board = new Tablero_De_Ajedrez(); // Inicializa el tablero de ajedrez
        currentTurn = Piezas.Color.WHITE; // Empieza el juego con el turno de las piezas blancas
    }

    // Método que verifica si el rey del jugador está en jaque
    public boolean isInCheck(Piezas.Color playerColor) {
        int kingRow = -1; // Fila del rey
        int kingCol = -1; // Columna del rey

        // Busca la posición del rey del jugador
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piezas piece = board.getPiece(row, col); // Obtiene la pieza en la posición (row, col)
                // Si la pieza es un rey y es del color del jugador
                if (piece instanceof Rey && piece.getColor() == playerColor) {
                    kingRow = row; // Guarda la fila del rey
                    kingCol = col; // Guarda la columna del rey
                    break;
                }
            }
            if (kingRow != -1) break; // Sale del bucle exterior si encontró el rey
        }

        // Si el rey no fue encontrado, retorna false
        if (kingRow == -1 || kingCol == -1) {
            return false;
        }

        // Verifica si alguna pieza del oponente puede atacar al rey
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piezas piece = board.getPiece(row, col);
                // Si la pieza no es nula y es del color del oponente
                if (piece != null && piece.getColor() != playerColor) {
                    // Si el movimiento de esta pieza es válido hacia la posición del rey
                    if (piece.isValidMove(row, col, kingRow, kingCol, board)) {
                        return true; // El rey está en jaque
                    }
                }
            }
        }

        return false; // No hay piezas que amenacen al rey
    }

    // Método que verifica si el jugador está en jaque mate
    public boolean isInCheckmate(Piezas.Color playerColor) {
        // Si el jugador no está en jaque, no puede estar en jaque mate
        if (!isInCheck(playerColor)) {
            return false;
        }

        // Revisa si hay algún movimiento legal que pueda evitar el jaque
        for (int startRow = 0; startRow < 8; startRow++) {
            for (int startCol = 0; startCol < 8; startCol++) {
                Piezas piece = board.getPiece(startRow, startCol);
                // Si la pieza no es nula y es del color del jugador
                if (piece != null && piece.getColor() == playerColor) {
                    for (int endRow = 0; endRow < 8; endRow++) {
                        for (int endCol = 0; endCol < 8; endCol++) {
                            // Si el movimiento de la pieza es válido
                            if (piece.isValidMove(startRow, startCol, endRow, endCol, board)) {
                                // Guarda la pieza capturada (si la hay)
                                Piezas capturedPiece = board.getPiece(endRow, endCol);
                                // Realiza el movimiento
                                board.movePiece(startRow, startCol, endRow, endCol);

                                // Verifica si aún está en jaque
                                boolean stillInCheck = isInCheck(playerColor);

                                // Deshace el movimiento
                                board.movePiece(endRow, endCol, startRow, startCol);
                                board.setPiece(endRow, endCol, capturedPiece);

                                // Si no está en jaque, entonces hay un movimiento legal
                                if (!stillInCheck) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }

        return true; // No hay movimientos legales, por lo tanto es jaque mate
    }

    // Método para mover una pieza en el tablero
    public boolean movePiece(int startRow, int startCol, int endRow, int endCol) {
        // Verifica si las posiciones son válidas
        if (!board.isValidPosition(startRow, startCol) || !board.isValidPosition(endRow, endCol)) {
            return false;
        }

        Piezas piece = board.getPiece(startRow, startCol); // Obtiene la pieza que se quiere mover

        // Verifica si la pieza existe y si es del color correspondiente
        if (piece == null || piece.getColor() != currentTurn) {
            return false;
        }

        // Verifica si el movimiento de la pieza es válido
        if (piece.isValidMove(startRow, startCol, endRow, endCol, board)) {
            Piezas capturedPiece = board.getPiece(endRow, endCol); // Guarda la pieza capturada (si la hay)
            board.movePiece(startRow, startCol, endRow, endCol); // Realiza el movimiento

            // Verifica si el movimiento pone al rey en jaque
            if (isInCheck(currentTurn)) {
                // Deshacer el movimiento si deja al jugador en jaque
                board.movePiece(endRow, endCol, startRow, startCol);
                board.setPiece(endRow, endCol, capturedPiece);
                return false; // Movimiento no válido
            }

            // Verifica si el peón ha llegado a la última fila para promoverlo
            if (piece instanceof Peon && (endRow == 0 || endRow == 7)) {
                promotePawn(endRow, endCol); // Promociona al peón
            }

            // Cambia el turno después de un movimiento válido
            currentTurn = (currentTurn == Piezas.Color.WHITE) ? Piezas.Color.BLACK : Piezas.Color.WHITE;
            return true; // Movimiento realizado correctamente
        }

        return false; // Movimiento no válido
    }

    // Método para promover un peón
    private void promotePawn(int row, int col) {
        // Aquí se puede mostrar una interfaz o diálogo para seleccionar una pieza
        // Por ahora, vamos a promover automáticamente a una reina
        board.setPiece(row, col, new reina(currentTurn)); // Cambia el peón por una reina
    }

    // Método que verifica si el juego ha terminado
    public boolean isGameOver() {
        return isInCheckmate(currentTurn); // Verifica si el jugador actual está en jaque mate
    }

    // Método para obtener el turno actual
    public Piezas.Color getCurrentTurn() {
        return currentTurn; // Retorna el color del turno actual
    }

    // Método para obtener el tablero de ajedrez
    public Tablero_De_Ajedrez getBoard() {
        return board; // Retorna el tablero de ajedrez
    }
}
