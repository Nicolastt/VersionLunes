package presentacion;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Menu extends JPanel {

    private Image imagenFondo;
    public Menu(){
        try{
            imagenFondo = ImageIO.read(new File("src/recursos/Fondo2.png"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void paintComponent(Graphics g) {

        g.drawImage(imagenFondo,0,0,getWidth(),getHeight(),null);
        g.setColor(Color.YELLOW);
        Font font = new Font("Goudy Stout",Font.BOLD, 28);
        g.setFont(font);
        g.drawString("PACMAN", 90, 100);

        font = new Font("Showcard Gothic", Font.ROMAN_BASELINE, 16);

        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("* Presiona N para comenzar", 70, 200);
        g.drawString("* Presiona A para ver las instrucciones", 20, 250);
        g.drawString("* Presiona H para ver todos los puntajes", 13, 300);
        g.drawString("* Presiona X para salir", 100, 350);
    }




}
