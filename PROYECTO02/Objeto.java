//PROYECTO02
//Colunga Aguilar Javier Alejandro
//Grupo: 7CM2
import java.awt.*;
import java.util.ArrayList;

public class Objeto {
    public double x, y;
    public double distanciaRecorrida = 0;

    private final double velocidad;
    private double angulo;
    private final double maxGiro;
    private final Color color;
    private final ArrayList<Point> trayectoria;

    public Objeto(double x, double y, double velocidad, double maxGiro, Color color) {
        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
        this.maxGiro = maxGiro;
        this.color = color;
        this.angulo = Math.random() * Math.PI * 2;
        this.trayectoria = new ArrayList<>();
        trayectoria.add(new Point((int) Math.round(x), (int) Math.round(y)));
    }

    //Genera curvas suaves
    public void girarHacia(double anguloDeseado) {
        double diferencia = anguloDeseado - this.angulo;

        while (diferencia > Math.PI) diferencia -= 2 * Math.PI;
        while (diferencia < -Math.PI) diferencia += 2 * Math.PI;

        if (diferencia > maxGiro) diferencia = maxGiro;
        if (diferencia < -maxGiro) diferencia = -maxGiro;

        this.angulo += diferencia;
    }

    public void mover(int frameCount) {
        double anteriorX = x;
        double anteriorY = y;

        double nuevoX = x + velocidad * Math.cos(angulo);
        double nuevoY = y + velocidad * Math.sin(angulo);

        //Mantener el objeto dentro del frame
        if (nuevoX < 40 || nuevoX > 1220) {
            angulo = Math.PI - angulo;
            nuevoX = x + velocidad * Math.cos(angulo);
        }
        if (nuevoY < 40 || nuevoY > 640) {
            angulo = -angulo;
            nuevoY = y + velocidad * Math.sin(angulo);
        }

        x = Math.max(40, Math.min(1220, nuevoX));
        y = Math.max(40, Math.min(640, nuevoY));

        distanciaRecorrida += Math.hypot(x - anteriorX, y - anteriorY);

        //Se dibuja el rastro
        trayectoria.add(new Point((int) Math.round(x), (int) Math.round(y)));
    }

    public void dibujar(Graphics2D g) {
        g.setColor(color);

        for (int i = 0; i < trayectoria.size() - 1; i++) {
            Point p1 = trayectoria.get(i);
            Point p2 = trayectoria.get(i + 1);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(color);
        g2d.translate(x, y);
        g2d.rotate(angulo - Math.PI / 2);
        g2d.scale(0.15, 0.15);
        g2d.translate(-50, -50);

        Polygon poligono = new Polygon();
        poligono.addPoint(0, 0);
        poligono.addPoint(100, 0);
        poligono.addPoint(50, 100);
        g2d.fillPolygon(poligono);
        g2d.dispose();
    }
}
