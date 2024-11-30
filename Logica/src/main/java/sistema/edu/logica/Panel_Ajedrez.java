package sistema.edu.logica;
import sistema.edu.logica.PIEZAS.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Panel_Ajedrez extends JFrame {
    public Logic_Juego game;
    private JButton[][] boardButtons;
       private JLabel statusLabel;
    private int selectedRow = -1;
      private int selectedCol = -1;
    private JLabel puntosBlancoLabel;
    private JLabel puntosNegroLabel;
    public Panel_Ajedrez() {
        game = new Logic_Juego();
        boardButtons = new JButton[8][8];
        statusLabel = new JLabel("Turno de las Blancas");

        initializeUI();
    }

    private void initializeUI() {
        puntosBlancoLabel = new JLabel("Puntos Blancos: 0");
        puntosNegroLabel = new JLabel("Puntos Negros: 0");

        JPanel puntuacionPanel = new JPanel();
        puntuacionPanel.add(puntosBlancoLabel);
        puntuacionPanel.add(puntosNegroLabel);


        setTitle("Ajedrez");
        setSize(600, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(8, 8));

        // Colores típicos del ajedrez: madera oscura y clara
        Color darkWood = new Color(139, 69, 19); // Marrón oscuro
        Color lightWood = new Color(222, 184, 135); // Marrón claro

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boardButtons[row][col] = new JButton();
                boardButtons[row][col].setFont(new Font("DejaVu Sans", Font.PLAIN, 30));
                boardButtons[row][col].addActionListener(new MoveActionListener(row, col));

                // Alternar los colores de las casillas
                if ((row + col) % 2 == 0) {
                    boardButtons[row][col].setBackground(lightWood);
                } else {
                    boardButtons[row][col].setBackground(darkWood);
                }

                boardButtons[row][col].setOpaque(true);
                boardButtons[row][col].setBorderPainted(false);

                boardPanel.add(boardButtons[row][col]);
            }
        }

        add(boardPanel, BorderLayout.CENTER);
        add(puntuacionPanel, BorderLayout.NORTH);
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
    private void updatePuntuacion() {
        puntosBlancoLabel.setText("Puntos Blancos: " + game.getSistemaPuntuacion().getPuntosBlanco());
        puntosNegroLabel.setText("Puntos Negros: " + game.getSistemaPuntuacion().getPuntosNegro());

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
            if (selectedRow == -1 && selectedCol == -1) {
                // Selecciona una pieza
                Piezas piece = game.getBoard().getPiece(row, col);

                if (piece != null && piece.getColor() == game.getCurrentTurn()) {
                    selectedRow = row;
                    selectedCol = col;

                    // Resaltar los movimientos posibles
                    highlightPossibleMoves(row, col);
                }
            } else {
                // Intenta mover la pieza seleccionada
                if (game.movePiece(selectedRow, selectedCol, row, col)) {
                    updateBoard();
                    updatePuntuacion(); // Actualizar la puntuación
                    selectedRow = -1;
                    selectedCol = -1;
                    resetBoardColors(); // Limpia los colores resaltados
                    updateStatus(); // Actualiza el estado del turno
                } else {
                    JOptionPane.showMessageDialog(null, "Movimiento inválido.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
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

    private void highlightPossibleMoves(int row, int col) {
        resetBoardColors(); // Limpia los colores previos
        List<int[]> possibleMoves = game.getPossibleMoves(row, col); // Obtén movimientos válidos

        for (int[] move : possibleMoves) {
            int moveRow = move[0];
            int moveCol = move[1];
            boardButtons[moveRow][moveCol].setBackground(Color.DARK_GRAY); // Resalta en amarillo
        }
    }
    private void resetBoardColors() {
        Color darkWood = new Color(139, 69, 19); // Marrón oscuro
        Color lightWood = new Color(222, 184, 135); // Marrón claro

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                boardButtons[row][col].setBackground((row + col) % 2 == 0 ? lightWood : darkWood);
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Panel_Ajedrez());
    }
}