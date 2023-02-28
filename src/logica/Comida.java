package logica;

import java.awt.*;

// Clase Comida que implementa la interfaz Comestible
public class Comida implements Comestible {
    private int fila;
    private int columna;
    private int indice;
    private Rectangle hitBox;

    // Constructor de la clase Comida que toma un índice y un laberinto como argumentos
    public Comida(int indice, Laberinto laberinto) {
        // Inicializa las variables de instancia "indice", "fila" y "columna" con los valores proporcionados
        this.indice = indice;
        this.fila = Coordenadas.getCordenadas(indice, laberinto.getColumnas()).getFila();
        this.columna = Coordenadas.getCordenadas(indice, laberinto.getColumnas()).getColumna();
        // Crea un rectángulo de colisión para la comida
        // La posición del rectángulo se calcula en función de la fila y columna de la comida y la posición del laberinto
        // La anchura y altura del rectángulo son de 10 píxeles
        hitBox = new Rectangle(columna * 20 + 5, fila * 20 + 50 + 5, 10, 10);
    }

    // Método de acceso público para obtener la fila de la comida
    public int getFila() {
        return fila;
    }

    // Método de acceso público para obtener la columna de la comida
    public int getColumna() {
        return columna;
    }

    // Método de acceso público para obtener el índice de la comida
    public int getIndice() {
        return indice;
    }

    // Implementación del método de la interfaz Comestible para obtener el rectángulo de colisión de la comida
    public Rectangle getHitbox() {
        return hitBox;
    }
}
