package logica;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class Fantasma implements Comestible {
    private int x;
    private int y;
    private int initX;
    private int initY;
    private Rectangle hitBox;
    private Direcciones dirección;
    private Laberinto actLaberinto;
    private Jugador jugador;
    private boolean puedeMoverse;

    public Fantasma(int x, int y, Laberinto actLaberinto, Jugador jugador) {
        this.x = x;
        this.y = y;
        this.initX = x;
        this.initY = y;
        this.actLaberinto = actLaberinto;
        this.jugador = jugador;
        hitBox = new Rectangle(x, y, 20, 20);
        dirección = null;
        puedeMoverse = false;
    }

    public void reiniciar() {
        puedeMoverse = false;
        x = initX;
        y = initY;
        hitBox = new Rectangle(x, y, 20, 20);
    }

    public boolean comer(Jugador jugador) {
        return hitBox.intersects(jugador.getHitbox());
    }

    private class Opcion implements Comparable<Opcion> {
        protected Direcciones direccion;
        protected double distancia;

        public Opcion(Direcciones direccion, double distancia) {
            this.direccion = direccion;
            this.distancia = distancia;
        }

        public int compareTo(Opcion opcion) {
            if (distancia > opcion.distancia) {
                return 1;

            } else if (distancia < opcion.distancia) {
                return -1;
            } else {
                return 0;
            }
        }

        public String toString() {
            return direccion + " " + distancia;
        }
    }

    public void mover() {
        if (!puedeMoverse) {
            return;
        }
        int velocidad = 2;
        if ((x / 20.0) % 1 == 0 && ((y - 50) / 20.0) % 1 == 0) {
            if (jugador.estaMoviendose()) {
                getSiguienteDireccion(false);
            } else {
                getSiguienteDireccion(true);
            }
        }
        switch (dirección) {
            case ARRIBA:
                y -= velocidad;
                break;
            case ABAJO:
                y += velocidad;
                break;
            case IZQUIERDA:
                x -= velocidad;
                break;
            case DERECHA:
                x += velocidad;
                break;
        }
        hitBox = new Rectangle(x, y, 20, 20);
    }

    public void getSiguienteDireccion(boolean perseguir) {
        int velocidad = 2;
        ArrayList<Direcciones> todasDirecciones = new ArrayList<>();
        ArrayList<Direcciones> direccionCorrecta = new ArrayList<>();
        ArrayList<Opcion> todasOpciones = new ArrayList<>();
        todasDirecciones.add(Direcciones.ARRIBA);
        todasDirecciones.add(Direcciones.ABAJO);
        todasDirecciones.add(Direcciones.IZQUIERDA);
        todasDirecciones.add(Direcciones.DERECHA);


        for (int i = 0; i < todasDirecciones.size(); i++) {
            if (!golpeaPared(todasDirecciones.get(i))) {
                direccionCorrecta.add(todasDirecciones.get(i));
            }
        }
        todasDirecciones = direccionCorrecta;


        if (todasDirecciones.size() == 1) {
            dirección = todasDirecciones.get(0);
        }


        for (int i = 0; i < todasDirecciones.size(); i++) {
            int postempoX, postempY;
            double distancia;
            switch (todasDirecciones.get(i)) {
                case ARRIBA:
                    postempoX = x;
                    postempY = y - velocidad;
                    distancia = Math.abs(Math.pow(postempoX - jugador.getX(), 2) + Math.pow(postempY - jugador.getY(), 2));
                    todasOpciones.add(new Opcion(Direcciones.ARRIBA, distancia));
                    break;
                case ABAJO:
                    postempoX = x;
                    postempY = y + velocidad;
                    distancia = Math.abs(Math.pow(postempoX - jugador.getX(), 2) + Math.pow(postempY - jugador.getY(), 2));
                    todasOpciones.add(new Opcion(Direcciones.ABAJO, distancia));
                    break;
                case IZQUIERDA:
                    postempoX = x - velocidad;
                    postempY = y;
                    distancia = Math.abs(Math.pow(postempoX - jugador.getX(), 2) + Math.pow(postempY - jugador.getY(), 2));
                    todasOpciones.add(new Opcion(Direcciones.IZQUIERDA, distancia));
                    break;
                case DERECHA:
                    postempoX = x + velocidad;
                    postempY = y;
                    distancia = Math.abs(Math.pow(postempoX - jugador.getX(), 2) + Math.pow(postempY - jugador.getY(), 2));
                    todasOpciones.add(new Opcion(Direcciones.DERECHA, distancia));
                    break;
            }
        }

        Collections.sort(todasOpciones);
        if (perseguir) {
            dirección = todasOpciones.get(0).direccion;
        } else {
            dirección = todasOpciones.get(todasOpciones.size() - 1).direccion;
        }
    }

    public boolean golpeaPared(Direcciones direccion) {
        int fila, columna, indice;
        switch (direccion) {
            case ARRIBA:
                fila = (y - 1 - 50) / 20;
                columna = x / 20;
                indice = Coordenadas.getIndice(fila, columna, actLaberinto.getColumnas()).getIndice();
                if (actLaberinto.getLaberinto().get(indice).equals("W")) {
                    return true;
                }
                break;
            case ABAJO:
                fila = (y - 50) / 20;
                columna = x / 20;
                indice = Coordenadas.getIndice(fila + 1, columna, actLaberinto.getColumnas()).getIndice();
                if (actLaberinto.getLaberinto().get(indice).equals("W")) {
                    return true;
                }
                break;
            case IZQUIERDA:
                fila = (y - 50) / 20;
                columna = (x - 1) / 20;
                indice = Coordenadas.getIndice(fila, columna, actLaberinto.getColumnas()).getIndice();
                if (actLaberinto.getLaberinto().get(indice).equals("W")) {
                    return true;
                }
                break;
            case DERECHA:
                fila = (y - 50) / 20;
                columna = x / 20;
                indice = Coordenadas.getIndice(fila, columna + 1, actLaberinto.getColumnas()).getIndice();
                if (actLaberinto.getLaberinto().get(indice).equals("W")) {
                    return true;
                }
                break;
        }
        return false;
    }

    public Rectangle getHitbox() {
        return hitBox;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Direcciones getNuevaDireccion() {
        return dirección;
    }

    public void setMover(boolean t) {
        puedeMoverse = t;
    }
}
