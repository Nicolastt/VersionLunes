package logica;

import java.awt.*;

public class Poder implements Comestible {
    private int fila;
    private int columna;
    private int indice;
    private Rectangle hitBox;

    /**
     * Crea un nuevo objeto Poder en el índice indicado del laberinto especificado.
     * @param indice el índice en el que se encuentra el objeto en la lista de celdas del laberinto.
     * @param maze el laberinto en el que se encuentra el objeto.
     */
    public Poder(int indice, Laberinto maze) {
        this.indice = indice;
        this.fila = Coordenadas.getCordenadas(indice, maze.getColumnas()).getFila();
        this.columna = Coordenadas.getCordenadas(indice, maze.getColumnas()).getColumna();
        hitBox = new Rectangle(columna * 20 + 3, fila * 20 + 50 + 3, 14, 14);
    }

    /**
     * Devuelve la fila del objeto en el laberinto.
     * @return un entero que indica la fila en la que se encuentra el objeto.
     */
    public int getFila() {
        return fila;
    }

    /**
     * Devuelve la columna del objeto en el laberinto.
     * @return un entero que indica la columna en la que se encuentra el objeto.
     */
    public int getColumna() {
        return columna;
    }

    /**
     * Devuelve el índice del objeto en la lista de celdas del laberinto.
     * @return un entero que indica el índice del objeto en la lista de celdas del laberinto.
     */
    public int getIndice() {
        return indice;
    }

    /**
     * Devuelve el objeto Rectangle que representa el área de colisión del objeto.
     * @return el objeto Rectangle que representa el área de colisión del objeto.
     */
    public Rectangle getHitbox() {
        return hitBox;
    }
}
