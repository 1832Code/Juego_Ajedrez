package sistema.edu.logica.PGN;

import sistema.edu.logica.Logic_Juego;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PGNManager {
    private List<String> moveHistory;
    private Logic_Juego chessGame; // Asume que tienes tu clase ChessGame principal

    public PGNManager(Logic_Juego game) {
        this.chessGame = game;
        this.moveHistory = new ArrayList<>();
    }

    // Método para registrar cada movimiento
    public void recordMove(String move) {
        moveHistory.add(move);
    }

    // Guardar partida en formato PGN
    public void savePGNFile(String fileName, String whitePlayerName, String blackPlayerName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Escribir encabezados del PGN
            writer.write("[Event \"Casual Game\"]\n");
            writer.write("[Site \"Local\"]\n");
            writer.write("[Date \"" + new java.util.Date() + "\"]\n");
            writer.write("[White \"" + whitePlayerName + "\"]\n");
            writer.write("[Black \"" + blackPlayerName + "\"]\n");
            writer.write("[Result \"*\"]\n\n");

            // Escribir movimientos
            StringBuilder pgnMoves = new StringBuilder();
            for (int i = 0; i < moveHistory.size(); i++) {
                // Alternar entre movimientos blancos y negros
                if (i % 2 == 0) {
                    pgnMoves.append((i/2 + 1)).append(". ");
                }
                pgnMoves.append(moveHistory.get(i)).append(" ");
            }
            writer.write(pgnMoves.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Modo de reproducción de partida
    public class GameReplay {
        private List<String> moves;
        private int currentMoveIndex;

        public GameReplay(String pgnFileName) {
            this.moves = parsePGNFile(pgnFileName);
            this.currentMoveIndex = 0;
        }

        // Parsear archivo PGN
        private List<String> parsePGNFile(String fileName) {
            List<String> parsedMoves = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                String line;
                boolean startReadingMoves = false;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("1.")) {
                        startReadingMoves = true;
                    }
                    if (startReadingMoves) {
                        String[] moveParts = line.split("\\.");
                        for (String part : moveParts) {
                            String[] moves = part.trim().split("\\s+");
                            for (String move : moves) {
                                if (!move.isEmpty() && !move.matches("\\d+")) {
                                    parsedMoves.add(move);
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return parsedMoves;
        }

        // Avanzar un movimiento
        public String nextMove() {
            if (currentMoveIndex < moves.size()) {
                return moves.get(currentMoveIndex++);
            }
            return null;
        }

        // Retroceder un movimiento
        public String previousMove() {
            if (currentMoveIndex > 0) {
                return moves.get(--currentMoveIndex);
            }
            return null;
        }

        // Reiniciar reproducción
        public void resetReplay() {
            currentMoveIndex = 0;
        }

        // Obtener movimiento actual
        public String getCurrentMove() {
            return currentMoveIndex < moves.size() ? moves.get(currentMoveIndex) : null;
        }
    }
}
