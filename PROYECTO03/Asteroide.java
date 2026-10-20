//PROYECTO03
//Colunga Aguilar Javier Alejandro
//Grupo: 7CM2
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Random;

public class Asteroide {
    private double x, y;
    private double velocidad;
    private double dx, dy;
    private double velocidadAngular;
    private double rotacionActual;
    
    private Polygon forma;
    private int radioMaximo = 40;

    public Asteroide(double x, double y, double velocidad, double dx, double dy, double velocidadAngular) {
        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
        this.velocidadAngular = velocidadAngular;
        this.rotacionActual = 0;

        double magnitud = Math.hypot(dx, dy);
        if (magnitud != 0) {
            this.dx = dx / magnitud;
            this.dy = dy / magnitud;
        } else {
            this.dx = 1;
            this.dy = 0;
        }
        
        generarFormaAleatoria();
    }

    // Crea un polígono irregular único conectando puntos en un círculo
    private void generarFormaAleatoria() {
        forma = new Polygon();
        Random r = new Random();
        int numVertices = 8 + r.nextInt(6); // Cada asteroide tendrá entre 8 y 13 lados
        
        for (int i = 0; i < numVertices; i++) {
            double angulo = 2 * Math.PI * i / numVertices;
            double radio = 15 + r.nextDouble() * 25; // Radio variable para crear cráteres y picos
            
            int px = (int) (Math.cos(angulo) * radio);
            int py = (int) (Math.sin(angulo) * radio);
            forma.addPoint(px, py);
        }
    }

    public void mover(int anchoPantalla, int altoPantalla) {
        // Movimiento lineal y rotación
        x += velocidad * dx;
        y += velocidad * dy;

        rotacionActual += velocidadAngular;

        // Reaparición en el lado opuesto
        if (x < -radioMaximo) x = anchoPantalla + radioMaximo;
        else if (x > anchoPantalla + radioMaximo) x = -radioMaximo;

        if (y < -radioMaximo) y = altoPantalla + radioMaximo;
        else if (y > altoPantalla + radioMaximo) y = -radioMaximo;
    }

    public void dibujar(Graphics2D g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(Color.WHITE); // Estilo arcade clásico
        
        g2d.translate(x, y);
        g2d.rotate(rotacionActual);
        
        // drawPolygon dibuja solo las líneas (wireframe), como en la imagen de referencia
        g2d.drawPolygon(forma); 
        
        g2d.dispose();
    }
}