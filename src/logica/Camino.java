package logica;

public class Camino {
    private int fila;
    private int columna;
    private int indice;

    // Constructor de la clase Camino que toma un índice y un laberinto como argumentos
    public Camino(int indice, Laberinto laberinto) {
        // Inicializa la variable de instancia "indice" con el valor proporcionado
        this.indice = indice;
        // Utiliza la clase Coordenadas para obtener las coordenadas correspondientes al índice y al número de columnas del laberinto
        // Asigna estas coordenadas a las variables de instancia "fila" y "columna" de la clase Camino
        this.fila = Coordenadas.getCordenadas(indice, laberinto.getColumnas()).getFila();
        this.columna = Coordenadas.getCordenadas(indice, laberinto.getColumnas()).getColumna();
    }

    // Método de acceso público para obtener la fila del camino
    public int getFila() {
        return fila;
    }

    // Método de acceso público para obtener la columna del camino
    public int getColumna() {
        return columna;
    }

    // Método de acceso público para obtener el índice del camino
    public int getIndice() {
        return indice;
    }
}
