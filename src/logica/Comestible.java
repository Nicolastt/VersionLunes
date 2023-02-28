package logica;

import java.awt.*;

// Interfaz Comestible para objetos que pueden ser consumidos por el jugador
public interface Comestible {

    // Método para obtener el rectángulo de colisión del objeto comestible
    // Devuelve un objeto de tipo Rectangle que representa la hitbox del objeto
    Rectangle getHitbox();
}
