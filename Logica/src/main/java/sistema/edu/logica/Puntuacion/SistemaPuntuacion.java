package sistema.edu.logica.Puntuacion;

import sistema.edu.logica.PIEZAS.Peon;
import sistema.edu.logica.PIEZAS.Piezas;
import sistema.edu.logica.PIEZAS.Rey;
import sistema.edu.logica.PIEZAS.reina;

// Valores de las piezas
public class SistemaPuntuacion {
    private int puntosBlanco;
    private int puntosNegro;

    public SistemaPuntuacion() {
        puntosBlanco = 0;
        puntosNegro = 0;
    }

    // Método para actualizar puntuación
    public void actualizarPuntuacion(Piezas piezaCapturada, Piezas.Color jugador) {
        if (piezaCapturada == null) return;

        int puntos = obtenerValorPieza(piezaCapturada);

        if (jugador == Piezas.Color.WHITE) {
            puntosBlanco += puntos;
        } else {
            puntosNegro += puntos;
        }
    }

    // Asignar valores a cada pieza
    private int obtenerValorPieza(Piezas pieza) {
        if (pieza instanceof reina) return 9;
        if (pieza instanceof Rey) return 0; // El rey no tiene valor porque no puede capturarse
        if (pieza instanceof Peon) return 1;
        // Puedes agregar más piezas aquí (torres, caballos, alfiles)
        return 3; // Valor por defecto
    }

    // Getters para los puntos
    public int getPuntosBlanco() {
        return puntosBlanco;
    }

    public int getPuntosNegro() {
        return puntosNegro;
    }
}
