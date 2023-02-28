/**
 * Clase que representa una fruta comestible en el laberinto.
 */
package logica;
import java.awt.*;

public class Fruta implements Comestible {
    private int fila; // Fila en la que se encuentra la fruta
    private int columna; // Columna en la que se encuentra la fruta
    private int indice; // Índice de la fruta en el laberinto
    private Rectangle hitBox; // Rectángulo que representa el área que ocupa la fruta en pantalla
    /**
     * Crea una nueva fruta a partir de su índice y el laberinto en el que se encuentra.
     * @param indice Índice de la fruta en el laberinto.
     * @param laberinto Laberinto en el que se encuentra la fruta.
     */
    public Fruta(int indice, Laberinto laberinto) {
        this.indice = indice;
        this.fila = Coordenadas.getCordenadas(indice, laberinto.getColumnas()).getFila();
        this.columna = Coordenadas.getCordenadas(indice, laberinto.getColumnas()).getColumna();
        hitBox = new Rectangle(columna * 20 + 3, fila * 20 + 50 + 3, 14, 14);
    }

    /**
     * Devuelve el rectángulo que representa el área que ocupa la fruta en pantalla.
     * @return Rectángulo que representa el área que ocupa la fruta en pantalla.
     */
    public Rectangle getHitbox() {
        return hitBox;
    }

    /**
     * Devuelve la fila en la que se encuentra la fruta.
     * @return Fila en la que se encuentra la fruta.
     */
    public int getFila() {
        return fila;
    }

    /**
     * Devuelve la columna en la que se encuentra la fruta.
     * @return Columna en la que se encuentra la fruta.
     */
    public int getColumna() {
        return columna;
    }

    /**
     * Devuelve el índice de la fruta en el laberinto.
     * @return Índice de la fruta en el laberinto.
     */
    public int getIndice() {
        return indice;
    }
}