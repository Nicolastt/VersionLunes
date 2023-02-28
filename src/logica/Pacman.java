package logica;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import presentacion.PlayerListener;
import ucd.comp2011j.engine.Game;

public class Pacman implements Game {

    public static final int ALTURA_PANTALLA = 530 + 50;
    public static final int ANCHO_PANTALLA = 380;
    private static final int NO_NIVELES = 3;

    private final PlayerListener listener;
    private int vidasJugador;
    private int puntajeJugador;
    private int puntajeTemp = 0;
    private boolean pausado = true;
    private int nivelActual = 1;
    private final String[] nivel;
    private Laberinto laberintoActual;

    private Jugador jugador;
    private ArrayList<Fantasma> matrizFantasma;
    private ArrayList<Fantasma> listaFantasmasEnBase;
    private final ArrayList<Fantasma> listaFantasmasPorSalir = new ArrayList<>(); // start timer
    private ArrayList<Comida> matrizComida;
    private ArrayList<Fruta> matrizFruta;
    private ArrayList<Poder> matrizPoder;
    private ArrayList<Muro> matrizPared;
    private ArrayList<Camino> matrizCamino;
    private Fruta frutaActual;
    private int curIndiceFrutas;
    private int contadorComerFantasma;

    private final Timer timer = new Timer();
    private boolean actualizarFruta = true;

    /**
     * Clase que representa el juego Pacman.
     * @param listener El objeto que escucha los eventos del jugador.
     */
    public Pacman(PlayerListener listener) {
        this.listener = listener;
        this.nivel = getNiveltxt();
        startNewGame();
    }

    /**
     * Método que devuelve el puntaje actual del jugador.
     * @return El puntaje actual del jugador.
     */
    @Override
    public int getPlayerScore() {
        return puntajeJugador;
    }

    /**
     * Método que devuelve el número de vidas restantes del jugador.
     * @return El número de vidas restantes del jugador.
     */
    @Override
    public int getLives() {
        return vidasJugador;
    }

    /**
     * Método que actualiza el estado del juego.
     * Si el juego no está en pausa, se mueve el jugador, se ordenan los fantasmas,
     * se mueven los fantasmas, se come la fruta, se come el fantasma y se genera
     * una nueva fruta aleatoria.
     */
    @Override
    public void updateGame() {
        if (!isPaused()) {
            moverJugador();
            ordenarFantasmas();
            moverFantasmas();
            comer();
            comerFantasma();
            generarFrutaAleatoria();
        }
    }

    /**
     * Método que verifica si algún fantasma ha sido comido por el jugador.
     * Si un fantasma ha sido comido, se reinicia la posición del jugador y se
     * disminuye en uno el número de vidas restantes del jugador.
     */
    public void comerFantasma() {
        for (Fantasma g : matrizFantasma) {
            if (g.comer(jugador)) {
                jugador.reiniciar();
                vidasJugador--;
            }
        }
    }

    /**
     * Ordena los fantasmas y los saca de la base para moverlos.
     */
    public void ordenarFantasmas() {
        // Se crea un objeto Random para generar valores aleatorios.
        Random rand = new Random();
        int tiempoEspera = 0;
        // Si la cantidad de fantasmas en la base es igual a la cantidad de fantasmas totales,
        // se saca el primer fantasma de la base y se activa su movimiento.
        if (listaFantasmasEnBase.size() == matrizFantasma.size()) {
            listaFantasmasEnBase.get(0).setMover(true);
            listaFantasmasEnBase.remove(0);
        }
        // Se itera sobre la lista de fantasmas en la base.
        for (Fantasma fantasma : listaFantasmasEnBase) {
            // Se suma un valor aleatorio entre 500 y 2500 milisegundos al tiempo de espera.
            tiempoEspera += rand.nextInt(5 * 100, 50 * 100);
            // Se añade el fantasma a la lista de fantasmas por salir.
            listaFantasmasPorSalir.add(fantasma);
            // Se crea una tarea programada que se ejecutará después del tiempo de espera.
            TimerTask pend = new TimerTask() {
                public void run() {
                    // Se activa el movimiento del fantasma y se elimina de la lista de fantasmas por salir.
                    fantasma.setMover(true);
                    listaFantasmasPorSalir.remove(fantasma);
                }
            };
            // Se programa la tarea pendiente para que se ejecute después del tiempo de espera.
            timer.schedule(pend, tiempoEspera);
        }
        // Se eliminan todos los fantasmas de la base.
        listaFantasmasEnBase.clear();
    }

    /**
     * Mueve todos los fantasmas de la lista de fantasmas.
     */
    public void moverFantasmas() {
        // Se itera sobre la lista de fantasmas y se mueve cada uno.
        for (Fantasma fantasma : matrizFantasma) {
            fantasma.mover();
        }
    }

    /**
     * Genera una fruta aleatoria y actualiza su estado.
     */
    public void generarFrutaAleatoria() {
        // Si es necesario actualizar la fruta y hay frutas disponibles en la lista.
        if (actualizarFruta && matrizFruta.size() != 0) {
            // Se crea un objeto Random para generar valores aleatorios.
            Random rand = new Random();
            // Se elige un índice aleatorio de la lista de frutas.
            curIndiceFrutas = rand.nextInt(matrizFruta.size());
            // Se obtiene la fruta actual.
            frutaActual = matrizFruta.get(curIndiceFrutas);
            // Se cambia el estado de actualización de la fruta.
            actualizarFruta = !actualizarFruta;
            // Se crea una tarea programada que se ejecutará después de un tiempo aleatorio.
            TimerTask changeRefreshState = new TimerTask() {
                public void run() {
                    // Se cambia el estado de actualización de la fruta nuevamente.
                    actualizarFruta = !actualizarFruta;
                }
            };
            // Se elige un tiempo aleatorio entre 5 y 10 segundos para ejecutar la tarea.
            int delay = rand.nextInt(5000, 10001);
            timer.schedule(changeRefreshState, delay);
        }
    }

    /**
     * Comprueba si el jugador ha comido puntos, frutas, poderes o fantasmas y aumenta el puntaje correspondiente.
     */
    public void comer() {
        // Comer puntos
        for (int i = 0; i < matrizComida.size(); i++) {
            if (jugador.come(matrizComida.get(i))) {
                matrizComida.remove(i);
                addPuntaje(10);
            }
        }
        // Comer frutas
        if (frutaActual != null) {
            if (jugador.come((frutaActual))) {
                matrizFruta.remove(curIndiceFrutas);
                frutaActual = null;
                addPuntaje(500);
            }
        }
        // Comer poderes
        for (int i = 0; i < matrizPoder.size(); i++) {
            if (jugador.come(matrizPoder.get(i))) {
                matrizPoder.remove(i);
                jugador.setPoder();
                addPuntaje(50);
                contadorComerFantasma = 0;
            }
        }
        // Comer fantasmas
        if (jugador.estaMoviendose()) {
            for (Fantasma fantasma : matrizFantasma) {
                if (jugador.come(fantasma)) {
                    fantasma.reiniciar();
                    listaFantasmasEnBase.add(fantasma);
                    addPuntaje(200 * (int) Math.pow(2, contadorComerFantasma));
                    contadorComerFantasma++;
                }
            }
        }
    }

    /**
     * Mueve al jugador en la dirección indicada por el usuario,
     * siempre y cuando no choque con una pared.
     */
    public void moverJugador() {
        verificarSiJugadorChocaConLaPared();
        if (!jugador.estaGolpeandoPared()) {
            jugador.mover();
        }

        if ((jugador.getX() / 20.0) % 1 == 0 && ((jugador.getY() - 50) / 20.0) % 1 == 0) {
            if (listener.estaPresionandoIzquierda()) {
                if (puedeCambiarDeDireccion(Direcciones.IZQUIERDA)) {
                    jugador.cambiarDireccion(Direcciones.IZQUIERDA);
                }
            } else if (listener.estaPresionandoDerecha()) {
                if (puedeCambiarDeDireccion(Direcciones.DERECHA)) {
                    jugador.cambiarDireccion(Direcciones.DERECHA);
                }
            } else if (listener.estaPresionandoArriba()) {
                if (puedeCambiarDeDireccion(Direcciones.ARRIBA)) {
                    jugador.cambiarDireccion(Direcciones.ARRIBA);
                }
            } else if (listener.estaPresionandoAbajo()) {
                if (puedeCambiarDeDireccion(Direcciones.ABAJO)) {
                    jugador.cambiarDireccion(Direcciones.ABAJO);
                }
            }
        }
    }

    /**
     * Determina si el jugador puede cambiar de dirección sin chocar con una pared.
     * @param direccion la dirección a la que se quiere cambiar.
     * @return true si el jugador puede cambiar de dirección, false en caso contrario.
     */
    public boolean puedeCambiarDeDireccion(Direcciones direccion) {
        int fila, columna, indice;
        switch (direccion) {
            case ARRIBA:
                fila = (jugador.getY() - 1 - 50) / 20;
                columna = jugador.getX() / 20;
                indice = Coordenadas.getIndice(fila, columna, laberintoActual.getColumnas()).getIndice();
                if (laberintoActual.getLaberinto().get(indice).equals("W") ||
                        laberintoActual.getLaberinto().get(indice).equals("-")) {
                    return false;
                }
                break;
            case ABAJO:
                fila = (jugador.getY() - 50) / 20;
                columna = jugador.getX() / 20;
                indice = Coordenadas.getIndice(fila + 1, columna, laberintoActual.getColumnas()).getIndice();
                if (laberintoActual.getLaberinto().get(indice).equals("W") ||
                        laberintoActual.getLaberinto().get(indice).equals("-")) {
                    return false;
                }
                break;
            case IZQUIERDA:
                fila = (jugador.getY() - 50) / 20;
                columna = (jugador.getX() - 1) / 20;
                indice = Coordenadas.getIndice(fila, columna, laberintoActual.getColumnas()).getIndice();
                if (laberintoActual.getLaberinto().get(indice).equals("W") ||
                        laberintoActual.getLaberinto().get(indice).equals("-")) {
                    return false;
                }
                break;
            case DERECHA:
                fila = (jugador.getY() - 50) / 20;
                columna = jugador.getX() / 20;
                indice = Coordenadas.getIndice(fila, columna + 1, laberintoActual.getColumnas()).getIndice();
                if (laberintoActual.getLaberinto().get(indice).equals("W") ||
                        laberintoActual.getLaberinto().get(indice).equals("-")) {
                    return false;
                }
                break;
        }
        return true;
    }

    /**
     * Verifica si el jugador choca con una pared en la dirección actual.
     */
    public void verificarSiJugadorChocaConLaPared() {
        // Se inicializan las variables necesarias para calcular la posición del jugador.
        int fila, columna, indice;
        // Se utiliza un switch para manejar cada una de las cuatro posibles direcciones del jugador.
        switch (jugador.getDireccion()) {
            case ARRIBA:
                fila = (jugador.getY() - 1 - 50) / 20;
                columna = jugador.getX() / 20;
                // Se obtiene el índice correspondiente en la lista del laberinto.
                indice = Coordenadas.getIndice(fila, columna, laberintoActual.getColumnas()).getIndice();
                // Si el elemento en la posición obtenida es una pared o un borde, se establece que el jugador golpea la pared.
                if (laberintoActual.getLaberinto().get(indice).equals("W") ||
                        laberintoActual.getLaberinto().get(indice).equals("-")) {
                    jugador.setGolpeaPared(true);
                }
                break;
            case ABAJO:
                fila = (jugador.getY() - 50) / 20;
                columna = jugador.getX() / 20;
                indice = Coordenadas.getIndice(fila + 1, columna, laberintoActual.getColumnas()).getIndice();
                if (laberintoActual.getLaberinto().get(indice).equals("W") ||
                        laberintoActual.getLaberinto().get(indice).equals("-")) {
                    jugador.setGolpeaPared(true);
                }
                break;
            case IZQUIERDA:
                fila = (jugador.getY() - 50) / 20;
                columna = (jugador.getX() - 1) / 20;
                indice = Coordenadas.getIndice(fila, columna, laberintoActual.getColumnas()).getIndice();
                if (laberintoActual.getLaberinto().get(indice).equals("W") ||
                        laberintoActual.getLaberinto().get(indice).equals("-")) {
                    jugador.setGolpeaPared(true);
                }
                break;
            case DERECHA:
                fila = (jugador.getY() - 50) / 20;
                columna = jugador.getX() / 20;
                indice = Coordenadas.getIndice(fila, columna + 1, laberintoActual.getColumnas()).getIndice();
                if (laberintoActual.getLaberinto().get(indice).equals("W") ||
                        laberintoActual.getLaberinto().get(indice).equals("-")) {
                    jugador.setGolpeaPared(true);
                }
                break;
        }
    }

    /**
     * Devuelve el jugador
     * @return el jugador
     */
    public Jugador getJugador() {
        return jugador;
    }

    /**
     * Devuelve si el juego está pausado
     * @return si el juego está pausado
     */
    @Override
    public boolean isPaused() {
        return pausado;
    }

    /**
     * Verifica si el usuario ha presionado el botón de pausa y cambia el estado del juego a pausado o no pausado.
     * También se asegura de que el estado del botón de pausa se reinicie.
     */
    @Override
    public void checkForPause() {
        if (listener.hasPressedPause()) {
            pausado = !pausado;
            listener.resetPause();
        }
    }

    /**
     * Inicia un nuevo juego estableciendo los valores iniciales de las variables del juego.
     * Genera los objetos jugador, fantasmas, comidas, frutas, poderes, muros y caminos.
     * Establece la fruta actual como la primera de la lista de frutas y agrega los fantasmas a la lista de fantasmas en la base.
     */
    @Override
    public void startNewGame() {
        vidasJugador = 3;
        puntajeJugador = 0;
        contadorComerFantasma = 0;
        laberintoActual = new Laberinto(nivel[nivelActual]);

        jugador = generarJugador();
        matrizFantasma = generarFantasmaEnMatriz();
        matrizComida = generarComidaEnLaMatriz();
        matrizFruta = generarFrutasEnLaMatriz();
        matrizPoder = generarPoderesEnLaMatriz();
        matrizPared = generarParedesEnLaMatriz();
        matrizCamino = generarCaminosEnLaMatriz();
        frutaActual = matrizFruta.get(0);

        listaFantasmasEnBase = new ArrayList<>();
        for (Fantasma g : matrizFantasma) {
            listaFantasmasEnBase.add(g);
        }
    }


    /**
     * Genera una lista de objetos Comida que representan los puntos de comida en la matriz del laberinto actual.
     * @return ArrayList<Comida> - la lista de objetos Comida generada
     */
    public ArrayList<Comida> generarComidaEnLaMatriz() {
        ArrayList<String> matrizLaberinto = laberintoActual.getLaberinto();
        ArrayList<Comida> matrizComida = new ArrayList<>();
        for (int i = 0; i < matrizLaberinto.size(); i++) {
            if (matrizLaberinto.get(i).equals(".")) {
                matrizComida.add(new Comida(i, laberintoActual));
            }
        }
        return matrizComida;
    }

    /**
     * Devuelve la lista de objetos Comida que representan los puntos de comida en la matriz del laberinto actual.
     * @return ArrayList<Comida> - la lista de objetos Comida
     */
    public ArrayList<Comida> getComidaEnLaMatriz() {
        return matrizComida;
    }


    /**
     * Genera una lista de objetos Fruta que representan las frutas en la matriz del laberinto actual.
     * @return ArrayList<Fruta> - la lista de objetos Fruta generada
     */
    public ArrayList<Fruta> generarFrutasEnLaMatriz() {
        ArrayList<String> matrizLaberinto = laberintoActual.getLaberinto();
        ArrayList<Fruta> matrizFruta = new ArrayList<>();
        for (int i = 0; i < matrizLaberinto.size(); i++) {
            if (matrizLaberinto.get(i).equals("F")) {
                matrizFruta.add(new Fruta(i, laberintoActual));
            }
        }
        return matrizFruta;
    }

    /**
     * Retorna la matriz de frutas del laberinto actual
     * @return ArrayList de objetos Fruta
     */
    public ArrayList<Fruta> getMatrizFruta() {
        return matrizFruta;
    }

    /**
     * Genera una matriz de objetos Muro a partir del laberinto actual
     * @return ArrayList de objetos Muro
     */
    public ArrayList<Muro> generarParedesEnLaMatriz() {
        ArrayList<String> matrizLaberinto = laberintoActual.getLaberinto();
        ArrayList<Muro> matrizPared = new ArrayList<>();
        for (int i = 0; i < matrizLaberinto.size(); i++) {
            if (matrizLaberinto.get(i).equals("W")) {
                matrizPared.add(new Muro(i, laberintoActual));
            }
        }
        return matrizPared;
    }

    /**
     * Retorna la matriz de paredes del laberinto actual
     * @return ArrayList de objetos Muro
     */
    public ArrayList<Muro> getMatrizPared() {
        return matrizPared;
    }

    /**
     * Genera una lista de objetos tipo Camino que representan los caminos vacios en el laberinto actual
     * @return ArrayList de objetos tipo Camino
     */
    public ArrayList<Camino> generarCaminosEnLaMatriz() {
        ArrayList<String> matrizLaberinto = laberintoActual.getLaberinto();
        ArrayList<Camino> matrizPared = new ArrayList<>();
        for (int i = 0; i < matrizLaberinto.size(); i++) {
            if (matrizLaberinto.get(i).equals("-")) {
                matrizPared.add(new Camino(i, laberintoActual));
            }
        }
        return matrizPared;
    }

    /**
     * Obtiene la lista de objetos tipo Camino que representan los caminos vacios en el laberinto actual
     * @return ArrayList de objetos tipo Camino
     */
    public ArrayList<Camino> getListaParedesDeAire() {
        return matrizCamino;
    }


    /**
     * Genera una lista de objetos tipo Poder que representan los puntos de poder en el laberinto actual
     * @return ArrayList de objetos tipo Poder
     */
    public ArrayList<Poder> generarPoderesEnLaMatriz() {
        ArrayList<String> matrizLaberinto = laberintoActual.getLaberinto();
        ArrayList<Poder> matrizPoder = new ArrayList<>();
        for (int i = 0; i < matrizLaberinto.size(); i++) {
            if (matrizLaberinto.get(i).equals("*")) {
                matrizPoder.add(new Poder(i, laberintoActual));
            }
        }
        return matrizPoder;
    }

    /**
     * Obtiene la lista de objetos tipo Poder que representan los puntos de poder en el laberinto actual
     * @return ArrayList de objetos tipo Poder
     */
    public ArrayList<Poder> getMatrizPoder() {
        return matrizPoder;
    }

    /**
     * Genera una instancia del objeto Jugador y lo posiciona en la matriz del laberinto.
     * @return Jugador - instancia del objeto jugador.
     * @throws Error si no existe un jugador en el laberinto.
     */
    public Jugador generarJugador() {
        ArrayList<String> matrizLaberinto = laberintoActual.getLaberinto();
        for (int i = 0; i < matrizLaberinto.size(); i++) {
            if (matrizLaberinto.get(i).equals("P")) {
                int posX = Coordenadas.getCordenadas(i, laberintoActual.getColumnas()).getColumna() * 20;
                int posY = Coordenadas.getCordenadas(i, laberintoActual.getColumnas()).getFila() * 20 + 50;
                return new Jugador(posX, posY);
            }
        }
        throw new Error("Jugador no existe");
    }

    /**
     * Genera una lista de instancias del objeto Fantasma y los posiciona en la matriz del laberinto.
     * @return ArrayList<Fantasma> - lista de instancias del objeto Fantasma.
     */
    public ArrayList<Fantasma> generarFantasmaEnMatriz() {
        ArrayList<String> matrizLaberinto = laberintoActual.getLaberinto();
        ArrayList<Fantasma> matrizFantasma = new ArrayList<>();
        for (int i = 0; i < matrizLaberinto.size(); i++) {
            if (matrizLaberinto.get(i).equals("G")) {
                int posX = Coordenadas.getCordenadas(i, laberintoActual.getColumnas()).getColumna() * 20;
                int posY = Coordenadas.getCordenadas(i, laberintoActual.getColumnas()).getFila() * 20 + 50;
                matrizFantasma.add(new Fantasma(posX, posY, laberintoActual, jugador));
            }
        }
        return matrizFantasma;
    }

    /**
     * Devuelve la matriz de Fantasmas.
     * @return una ArrayList de objetos Fantasma.
     */
    public ArrayList<Fantasma> getMatrizFantasma() {
        return matrizFantasma;
    }

    /**
     * Devuelve un array con los nombres de los archivos de niveles disponibles.
     * @return un array de Strings con los nombres de los archivos de niveles.
     */
    public String[] getNiveltxt() {
        String[] matriz = new String[NO_NIVELES];
        for (int i = 0; i < NO_NIVELES; i++) {
            matriz[i] = "./maze/" + i + ".txt";
        }
        return matriz;
    }

    /**
     * Agrega el puntaje al jugador, y aumenta una vida si se llegó a 10000 puntos.
     * @param num entero con la cantidad de puntos a agregar.
     */
    public void addPuntaje(int num) {
        puntajeJugador += num;
        puntajeTemp += num;
        if (puntajeTemp >= 10000) {
            vidasJugador += 1;
            puntajeTemp = 0;
        }
    }

    /**
     * Devuelve la fruta actual que está en el juego.
     * @return objeto Fruta actual en el juego.
     */
    public Fruta getFrutaActual() {
        return frutaActual;
    }


    /**
     * Devuelve el número del nivel actual en el juego.
     * @return entero con el número del nivel actual.
     */
    public int getNivel() {
        return nivelActual;
    }


    /**
     * Devuelve el objeto Laberinto actual en el juego.
     * @return objeto Laberinto actual en el juego.
     */
    public Laberinto getLaberinto() {
        return laberintoActual;
    }


    /**
     * Retorna verdadero si el nivel ha sido completado, es decir, si no hay comida ni poderes restantes.
     * Retorna falso en caso contrario.
     * @return verdadero si el nivel ha sido completado, falso en caso contrario.
     */
    @Override
    public boolean isLevelFinished() {
        if (matrizComida.size() == 0 && matrizPoder.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Retorna verdadero siempre, ya que el jugador siempre está vivo.
     * @return verdadero siempre
     */
    @Override
    public boolean isPlayerAlive() {
        return true;
    }


    /**
     * No hace nada ya que el jugador no se destruye en este juego.
     */
    @Override
    public void resetDestroyedPlayer() {
    }


    /**
     * Este método permite avanzar al siguiente nivel del juego, actualizando las variables correspondientes y generando
     * los elementos del nuevo nivel, como el jugador, los fantasmas, la comida, las frutas y los poderes, así como la
     * pared y los caminos que conforman el laberinto del nivel.
     */
    @Override
    public void moveToNextLevel() {
        pausado = true;
        nivelActual++;
        contadorComerFantasma = 0;
        laberintoActual = new Laberinto(nivel[nivelActual]);

        jugador = generarJugador();
        matrizFantasma = generarFantasmaEnMatriz();
        matrizComida = generarComidaEnLaMatriz();
        matrizFruta = generarFrutasEnLaMatriz();
        matrizPoder = generarPoderesEnLaMatriz();
        matrizPared = generarParedesEnLaMatriz();
        matrizCamino = generarCaminosEnLaMatriz();
        frutaActual = matrizFruta.get(0);

        listaFantasmasEnBase = new ArrayList<>();
        for (Fantasma g : matrizFantasma) {
            listaFantasmasEnBase.add(g);
        }
    }

    /**
     * Indica si el juego ha terminado o no.
     * @return true si el juego ha terminado, false en caso contrario.
     */
    @Override
    public boolean isGameOver() {
        if (vidasJugador == 0) {
            return true;
        }
        if (nivelActual > NO_NIVELES) {
            return true;
        }
        return false;
    }

    /**
     * Devuelve el ancho de pantalla del juego.
     * @return el ancho de pantalla.
     */
    @Override
    public int getScreenWidth() {
        return ANCHO_PANTALLA;
    }


    /**
     * Devuelve la altura de pantalla del juego.
     * @return la altura de pantalla.
     */
    @Override
    public int getScreenHeight() {
        return ALTURA_PANTALLA;
    }
}
