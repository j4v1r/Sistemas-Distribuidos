//PROYECTO02
//Colunga Aguilar Javier Alejandro
//Grupo: 7CM2
import java.awt.*;
import java.util.ArrayList;

public class Objeto{
	public double x, y;
    public double distanciaRecorrida=0;
    
    private double velocidad;
    private double angulo; 
    private double maxGiro; 
    private Color color;
    private ArrayList<Point> trayectoria; 

    public Objeto(double x, double y, double velocidad, double maxGiro, Color color) {
        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
        this.maxGiro = maxGiro;
        this.color = color;
        this.angulo = Math.random() * Math.PI * 2; //Dirección inicial aleatoria
        this.trayectoria = new ArrayList<>();
    }

    //Curva de giro
    public void girarHacia(double anguloDeseado) {
        double diferencia = anguloDeseado - this.angulo;

        while (diferencia > Math.PI) diferencia -= 2 * Math.PI;
        while (diferencia < -Math.PI) diferencia += 2 * Math.PI;

        if (diferencia > maxGiro) diferencia = maxGiro;
        if (diferencia < -maxGiro) diferencia = -maxGiro;

        this.angulo += diferencia;
    }

    public void mover(int frameCount) {
        x+=velocidad*Math.cos(angulo);
        y+=velocidad*Math.sin(angulo);
        distanciaRecorrida+=velocidad;

        //Bordes del frame
        if (x < 20) x = 20;
        if (x > 1250) x = 1250;
        if (y < 20) y = 20;
        if (y > 650) y = 650;

        //Guardar rastro
        if(frameCount%3==0){
            trayectoria.add(new Point((int) x, (int) y));
        }
    }

    public void dibujar(Graphics2D g) {
        g.setColor(color);

        //Dibujar rastro
        for(int i=0;i<trayectoria.size()-1;i++){
            Point p1=trayectoria.get(i);
            Point p2=trayectoria.get(i + 1);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }

        //Dibujar polígono
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