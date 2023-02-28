package logica;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * La clase Jugador representa al personaje controlado por el usuario en el juego
 */
public class Jugador implements Comestible {

    private int x;
    private int y;
    private int initX;
    private int initY;
    private Direcciones direccion;
    private Boolean tienePoder;
    private Boolean golpeaPared;
    private Rectangle hitBox;

    /**
     * Constructor de la clase Jugador
     * @param x coordenada x inicial del jugador
     * @param y coordenada y inicial del jugador
     */
    public Jugador(int x, int y) {
        this.x = x;
        this.y = y;
        this.initX = x;
        this.initY = y;
        hitBox = new Rectangle(x, y, 20, 20);
        direccion = Direcciones.IZQUIERDA;
        tienePoder = false;
        golpeaPared = false;
    }

    /**
     * Método que reinicia la posición del jugador a la posición inicial
     */
    public void reiniciar() {
        x = initX;
        y = initY;
        hitBox = new Rectangle(x, y, 20, 20);
    }

    /**
     * Método que mueve al jugador en la dirección actual
     */
    public void mover() {
        int velocidad = 2;
        switch (direccion) {
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

    /**
     * Método que mueve al jugador a una posición específica
     * @param distX distancia en el eje x a moverse
     * @param distY distancia en el eje y a moverse
     */
    public void mover(int distX, int distY) {
        x += distX;
        y += distY;
        hitBox = new Rectangle(x, y, 20, 20);
    }

    /**
     * Método que verifica si el jugador ha colisionado con un fantasma
     * @param fantasma objeto de la clase Fantasma
     * @return verdadero si ha colisionado, falso en caso contrario
     */
    public boolean seLoComió(Fantasma fantasma) {
        Rectangle s = fantasma.getHitbox();
        return s.intersects(hitBox.getBounds());
    }

    /**
     * Método que cambia la dirección del jugador
     * @param direcciones objeto de la clase Direcciones
     */
    public void cambiarDireccion(Direcciones direcciones) {
        golpeaPared = false;
        direccion = direcciones;
    }

    /**
     * Método que devuelve la hitbox del jugador
     * @return objeto de la clase Rectangle que representa la hitbox del jugador
     */
    public Rectangle getHitbox() {
        return hitBox;
    }

    /**
     * Método que verifica si el jugador ha comido un objeto comestible
     * @param comestible objeto que implementa la interfaz Comestible
     * @return verdadero si ha comido el objeto, falso en caso contrario
     */
    public boolean come(Comestible comestible) {
        return hitBox.intersects(comestible.getHitbox());
    }

    /**
     * Método que devuelve la coordenada x del jugador
     * @return coordenada x del jugador
     */
    public int getX() {
        return x;
    }

    /**
     * Método que devuelve la coordenada y del jugador
     * @return coordenada y del jugador
     */
    public int getY() {
        return y;
    }

    /**
     * Método que devuelve la dirección actual del jugador
     * @return objeto de la clase Direcciones que representa la dirección actual del jugador
     */
    public Direcciones getDireccion() {
        return this.direccion;
    }

    /**
     * Método que devuelve si el jugador está golpeando una pared
     * @return verdadero si está golpeando una pared, falso en caso contrario
     */
    public Boolean estaGolpeandoPared() {
        return golpeaPared;
    }

    /**
     * Método que establece si el jugador está golpeando una pared
     * @param golpeaPared verdadero si está golpeando una pared, falso en caso contrario
     */
    public void setGolpeaPared(Boolean golpeaPared) {
        this.golpeaPared = golpeaPared;
    }

    /**
     * Método que devuelve si el jugador está en movimiento
     * @return verdadero si está en movimiento, falso en caso contrario
     */
    public boolean estaMoviendose() {
        return tienePoder;
    }

    /**
     * Método que establece que el jugador tiene poderes y establece un temporizador para desactivarlos
     */
    public void setPoder() {
        tienePoder = true;
        Timer timer = new Timer();
        TimerTask poder = new TimerTask() {
            public void run() {
                tienePoder = false;
            }
        };
        timer.schedule(poder, 5000);
    }
}
