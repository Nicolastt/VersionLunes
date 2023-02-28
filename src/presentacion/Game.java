package presentacion;

import javax.swing.*;

import logica.Camino;
import logica.Comida;
import logica.Fruta;
import logica.Fantasma;
import logica.Pacman;
import logica.Jugador;
import logica.Poder;
import logica.Muro;

import java.awt.*;
import java.util.ArrayList;


public class Game extends JPanel {
    private final Pacman game;

    public Game(Pacman game) {
        this.game = game;
    }

    public void paintComponent(Graphics g) {
        if (game != null) {
            g.setColor(new Color(0, 0, 0));
            g.fillRect(0, 0, Pacman.ANCHO_PANTALLA, Pacman.ALTURA_PANTALLA);

            if (!game.isGameOver()) {
                dibujarMuro(g, game.getMatrizPared());
                dibujarMuroAire(g, game.getListaParedesDeAire());
                dibujarComida(g, game.getComidaEnLaMatriz());
                dibujarComida(g, game.getFrutaActual());
                dibujarPoder(g, game.getMatrizPoder());
                dibujarFantasmas(g, game.getMatrizFantasma(), game.getJugador());
                dibujarJugador(g, game.getJugador());

                dibujarInformacion(g);

                if (game.isPaused()) {
                    dibujarPausa(g);
                }
            }
        }
    }

    public void drawGameOver() {

    }

    public void dibujarJugador(Graphics g, Jugador p) {
        g.setColor(new Color(255, 255, 0));
        //g.fillOval(p.getX() + 1, p.getY() + 1, 20, 20);
        g.fillArc(p.getX() + 1, p.getY() +1, 20, 20, 30, 300);
       }

    public void dibujarComida(Graphics g, ArrayList<Comida> listaComida) {
        g.setColor(new Color(255, 184, 151));
        for (Comida dot : listaComida) {
            int posX = dot.getColumna() * 20;
            int posY = dot.getFila() * 20 + 50;
            g.fillOval(posX + 7, posY + 7, 6, 6);
        }
    }

    public void dibujarComida(Graphics g, Fruta fruta) {
        if (fruta != null) {
            g.setColor(new Color(237, 175, 31));
            int posX = fruta.getColumna() * 20;
            int posY = fruta.getFila() * 20 + 50;
            g.fillOval(posX + 3, posY + 3, 14, 14);
        }
    }

    public void dibujarPoder(Graphics g, ArrayList<Poder> listaPoderes) {
        g.setColor(new Color(255, 255, 255));
        for (Poder power : listaPoderes) {
            int posX = power.getColumna() * 20;
            int posY = power.getFila() * 20 + 50;
            g.fillOval(posX + 3, posY + 3, 14, 14);

        }
    }

    public void dibujarFantasmas(Graphics g, ArrayList<Fantasma> listaFantasmas, Jugador jugador) {
        Color[] colors = new Color[4];
        colors[0] = new Color(255, 0, 0);
        colors[1] = new Color(255, 184, 222);
        colors[2] = new Color(255, 184, 71);
        colors[3] = new Color(0, 255, 222);
        for (int i = 0; i < listaFantasmas.size(); i++) {
            if (jugador.estaMoviendose()) {
                g.setColor(new Color(0, 0, 255));
            } else {
                g.setColor(colors[i]);
            }
            Fantasma p = listaFantasmas.get(i);
            g.fillOval(p.getX(), p.getY(), 20, 20);
        }
    }

    public void dibujarMuro(Graphics g, ArrayList<Muro> listaMuros) {
        g.setColor(new Color(90, 48, 20));
        for (Muro wall : listaMuros) {
            int posX = wall.getColumna() * 20;
            int posY = wall.getFila() * 20 + 50;
            g.fillRect(posX, posY, 20, 20);
        }
    }

    public void dibujarMuroAire(Graphics g, ArrayList<Camino> listaCaminos) {
        g.setColor(new Color(115, 115, 115));
        for (Camino wall : listaCaminos) {
            int posX = wall.getColumna() * 20;
            int posY = wall.getFila() * 20 + 50;
            g.fillRect(posX, posY, 20, 20);
        }
    }

    public void dibujarPausa(Graphics g) {
        g.setColor(new Color(255, 255, 0));
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Presiona 'P' para continuar ", 80, 280);
    }

    public void dibujarInformacion(Graphics g) {
        g.setColor(new Color(255, 255, 255));
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Nivel: " + game.getNivel(), 20, 35);
        g.drawString("Intentos: " + game.getLives(), 120, 35);
        g.drawString("Puntaje: " + game.getPlayerScore(), 250, 35);
    }
}
