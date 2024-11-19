package sistema.edu.logica.PIEZAS;

import sistema.edu.logica.Tablero_De_Ajedrez;

import java.util.List;

public abstract class Piezas {
    protected Color color;
    protected char symbol;
    public enum Type {
        QUEEN, ROOK, BISHOP, KNIGHT, PAWN, KING
    }

    protected Type type;

    public Piezas(Color color, Type type) {
        this.color = color;
        this.type = type;
    }

    public Type getType() {
        return type;
    }
    public enum Color { WHITE, BLACK }

    public Piezas(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public char getSymbol() {
        return symbol;
    }
    // Método abstracto para obtener movimientos legales
    public abstract List<int[]> getLegalMoves(int startRow, int startCol, Tablero_De_Ajedrez board);

    public abstract boolean isValidMove(int startRow, int startCol, int endRow, int endCol, Tablero_De_Ajedrez board);
}