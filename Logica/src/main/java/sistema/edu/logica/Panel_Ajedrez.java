package sistema.edu.logica;
import sistema.edu.logica.PIEZAS.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Panel_Ajedrez extends JFrame {
    public Logic_Juego game;
    private JButton[][] boardButtons;
       private JLabel statusLabel;
    private int selectedRow = -1;
      private int selectedCol = -1;

    public Panel_Ajedrez() {
        game = new Logic_Juego();
        boardButtons = new JButton[8][8];
        statusLabel = new JLabel("Turno de las Blancas");

        initializeUI();
    }

    private void initializeUI() {
        setTitle("Ajedrez");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boardButtons[row][col] = new JButton();
                boardButtons[row][col].setFont(new Font("DejaVu Sans", Font.PLAIN, 30));
                boardButtons[row][col].addActionListener(new MoveActionListener(row, col));
                boardPanel.add(boardButtons[row][col]);
            }
        }

        add(boardPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        updateBoard();
        setVisible(true);
    }

    public void updateBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piezas piece = game.getBoard().getPiece(row, col);
                if (piece != null) {
                    boardButtons[row][col].setText(String.valueOf(piece.getSymbol()));
                } else {
                    boardButtons[row][col].setText("");
                }
            }
        }
    }

    class MoveActionListener implements ActionListener {
        private int row;
        private int col;

        public MoveActionListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Verifica si no hay ninguna pieza seleccionada aún
            if (selectedRow == -1 && selectedCol == -1) {
                // Obtiene la pieza en la posición actual del tablero (fila y columna)
                Piezas piece = game.getBoard().getPiece(row, col);

                // Verifica si hay una pieza en esa posición y si pertenece al jugador del turno actual
                if (piece != null && piece.getColor() == game.getCurrentTurn()) {
                    // Almacena la posición de la pieza seleccionada (fila y columna)
                    selectedRow = row;
                    selectedCol = col;

                    // Cambia el color de la casilla seleccionada a gris
                    boardButtons[selectedRow][selectedCol].setBackground(Color.GRAY);
                    System.out.println("Pieza seleccionada en (" + selectedRow + ", " + selectedCol + ")");
                }
            } else {
                // Intenta mover la pieza seleccionada a la nueva posición (fila y columna actuales)
                if (game.movePiece(selectedRow, selectedCol, row, col)) {

                    // Si el movimiento es válido, actualiza la visualización del tablero
                    updateBoard();
                    // Resetea la selección de la pieza (ya no hay pieza seleccionada)
                    selectedRow = -1;
                    selectedCol = -1;
                    // Actualiza el estado del juego, como el turno y posibles mensajes de jaque
                    updateStatus();
                } else {
                    // Si el movimiento es inválido, muestra un mensaje de advertencia en una ventana
                    JOptionPane.showMessageDialog(null,
                            "Movimiento inválido.",
                            "Advertencia",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        }
    }
    //vovler al color original
        public Color getOriginalColor(int row, int col) {
        return (row + col) % 2 == 0 ? Color.WHITE : Color.BLACK;
    }


    public void restartGame() {
        game = new Logic_Juego();
        updateBoard();
        statusLabel.setText("Turno de las Blancas");
    }

    public void updateStatus() {
        // Verifica si el juego ha terminado.
        if (game.isGameOver()) {
            // Muestra un mensaje indicando el jaque mate y quién ganó la partida.
            JOptionPane.showMessageDialog(this,
                    "Jaque Mate. " + (game.getCurrentTurn() == Piezas.Color.WHITE ? "Negras" : "Blancas") + " ganan. El juego ha terminado.",
                    "Fin del Juego",
                    JOptionPane.INFORMATION_MESSAGE);

            // Pregunta al usuario si desea jugar otra partida.
            int option = JOptionPane.showConfirmDialog(this,
                    "¿Deseas jugar otra partida?",
                    "Nueva partida",
                    JOptionPane.YES_NO_OPTION);

            // Si el usuario elige jugar otra partida, reinicia el juego.
            if (option == JOptionPane.YES_OPTION) {
                restartGame();
            } else {
                // Si el usuario elige no jugar otra partida, cierra el programa.
                System.exit(0);
            }
        }
        // Si no ha terminado el juego, verifica si el turno actual está en jaque mate.
        else if (game.isInCheckmate(game.getCurrentTurn())) {
            // Muestra un mensaje indicando que el jugador actual está en jaque mate y cierra el juego.
            JOptionPane.showMessageDialog(this, "¡Jaque Mate! " + (game.getCurrentTurn() == Piezas.Color.WHITE ? "Blancas" : "Negras") + " ganan.");
            System.exit(0); // Cierra el programa al detectar jaque mate.
        }
        // Si el turno actual no está en jaque mate, verifica si está en jaque.
        else if (game.isInCheck(game.getCurrentTurn())) {
            // Muestra un cuadro de diálogo de advertencia para indicar que el jugador actual está en jaque.
            JOptionPane.showMessageDialog(this,
                    "¡Jaque! Turno de las " + (game.getCurrentTurn() == Piezas.Color.WHITE ? "Blancas" : "Negras"),
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            // Si no hay jaque ni jaque mate, simplemente muestra el turno del jugador actual.
            statusLabel.setText("Turno de las " + (game.getCurrentTurn() == Piezas.Color.WHITE ? "Blancas" : "Negras"));
        }
    }


    private void promotePawn(int row, int col) {
        // Diálogo para seleccionar promoción
        String[] options = {"Reina", "Torre", "Alfil", "Caballo"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Selecciona una pieza para promover:",
                "Promoción de Peón",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        Piezas promotedPiece;
        switch (choice) {
            case 1:
                promotedPiece = new torre(game.getCurrentTurn());
                break;
            case 2:
                promotedPiece = new Alfil(game.getCurrentTurn());
                break;
            case 3:
                promotedPiece = new Caballo(game.getCurrentTurn());
                break;
            default:
                promotedPiece = new reina(game.getCurrentTurn());
                break;
        }

        game.getBoard().setPiece(row, col, promotedPiece);
        restartGame(); // Actualizar la UI para mostrar la nueva pieza
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Panel_Ajedrez());
    }
}