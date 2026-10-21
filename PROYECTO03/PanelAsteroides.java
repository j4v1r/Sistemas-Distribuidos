//PROYECTO03
//Colunga Aguilar Javier Alejandro
//Grupo: 7CM2
import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class PanelAsteroides extends JPanel {
    
    // Lista sincronizada para evitar errores si el servidor agrega asteroides mientras se dibuja
    private final List<Asteroide> asteroides;
    private final int ancho = 1000;
    private final int alto = 1000;
    private final Random random = new Random();

    public PanelAsteroides() {
        setBackground(Color.BLACK);
        asteroides = Collections.synchronizedList(new ArrayList<>());

        Timer timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarFisicas();
                repaint();
            }
        });
        timer.start();
    }

    // Método que llamará el Servidor HTTP cuando reciba la petición del cliente Lanterna
    public void agregarAsteroideDesdeServidor(double velocidad, double dx, double dy, double velAngular) {
        // Regla: Generar inicialmente fuera de la pantalla
        double startX = 0, startY = 0;
        int lado = random.nextInt(4); // 0: Arriba, 1: Abajo, 2: Izquierda, 3: Derecha
        int offsetFueraPantalla = 100;

        switch (lado) {
            case 0: // Arriba
                startX = random.nextInt(ancho);
                startY = -offsetFueraPantalla;
                break;
            case 1: // Abajo
                startX = random.nextInt(ancho);
                startY = alto + offsetFueraPantalla;
                break;
            case 2: // Izquierda
                startX = -offsetFueraPantalla;
                startY = random.nextInt(alto);
                break;
            case 3: // Derecha
                startX = ancho + offsetFueraPantalla;
                startY = random.nextInt(alto);
                break;
        }

        // Modificador aleatorio entre -0.5 y 0.5
        double variacionX = random.nextDouble() - 0.5;
        double variacionY = random.nextDouble() - 0.5;

        double escalaAleatoria = 0.5 + (random.nextDouble() * 2.0);

        Asteroide nuevo = new Asteroide(startX, startY, velocidad, dx+variacionX, dy+variacionY, velAngular, escalaAleatoria);
        asteroides.add(nuevo);
    }

    private void actualizarFisicas() {
        synchronized (asteroides) {
            for (Asteroide a : asteroides) {
                a.mover(ancho, alto);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        synchronized (asteroides) {
            for (Asteroide a : asteroides) {
                a.dibujar(g2d);
            }
        }
    }
}