package sistema.edu.pe;


import sistema.edu.logica.Panel_Ajedrez;
import sistema.edu.logica.Tablero_De_Ajedrez;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameMenu{
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 245);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180);
    private static final Color BUTTON_HOVER_COLOR = new Color(100, 160, 210);
    private static final Color TEXT_COLOR = new Color(50, 50, 50);
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 24);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 16);

    public static void showMenu() {
        JFrame frame = new JFrame("Chess Game Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
             frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Bienvenidos al Juego de Ajedrez", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        buttonPanel.setOpaque(false);

        JButton vsComputerButton = createStyledButton("Jugar con el Ordenador");
        JButton vsPlayerButton = createStyledButton("Jugar contra otro jugador");

        buttonPanel.add(vsComputerButton);
        buttonPanel.add(vsPlayerButton);

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        frame.add(mainPanel);

        vsComputerButton.addActionListener(e -> {
            frame.dispose();
            // Iniciar juego contra la computadora
            JOptionPane.showMessageDialog(null, "Comenzando el juego contra la computadora...");
            // Aquí llamas a tu lógica para iniciar el juego contra la computadora
        });

        vsPlayerButton.addActionListener(e -> {
            frame.dispose();  // Cierra el menú
            // Inicia el juego contra otro jugador
            SwingUtilities.invokeLater(() -> new Panel_Ajedrez());  // Llama a la clase ChessGUI
        });

        frame.setVisible(true);
    }

    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
          button.setBackground(BUTTON_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
           button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_HOVER_COLOR);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameMenu::showMenu);
    }
}
