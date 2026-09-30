//PROYECTO02
//Colunga Aguilar Javier Alejandro
//Grupo: 7CM2
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

public class PanelPersecucion extends JPanel{
	private Objeto presa;
    private ArrayList<Objeto> perseguidores;
    private int contadorFrames = 0;

    private long tiempoInicio;

    public PanelPersecucion(float mult_velocidad, int num_perseguidores) {
        setBackground(Color.WHITE);
        perseguidores = new ArrayList<>();

        presa = new Objeto(640, 360, 2.5, 0.15, Color.BLUE);
        double vel_perseguidor = 2.25 * mult_velocidad;

        for(int i = 0;i<num_perseguidores;i++) {
            double px = Math.random() < 0.5 ? 50 + Math.random() * 200 : 1030 + Math.random() * 200;
            double py = Math.random() < 0.5 ? 50 + Math.random() * 150 : 520 + Math.random() * 150;
            perseguidores.add(new Objeto(px, py, vel_perseguidor, 0.04, Color.RED));
        }

        tiempoInicio = System.currentTimeMillis();

        Timer timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarLogica(e);
                repaint();
            }
        });
        timer.start();
    }

    private void actualizarLogica(ActionEvent e) {
        contadorFrames++;
        evadirPresa();
        presa.mover(contadorFrames);

        for (int i = 0; i < perseguidores.size(); i++) {
            Objeto p = perseguidores.get(i);
            double anguloHaciaPresa = Math.atan2(presa.y - p.y, presa.x - p.x);
            p.girarHacia(anguloHaciaPresa);
            p.mover(contadorFrames);

            double distanciaColision = Math.hypot(p.x - presa.x, p.y - presa.y);
            if (distanciaColision < 15) {
                ((Timer) e.getSource()).stop();

                long tiempoFin = System.currentTimeMillis();
                double segundosDePersecucion = (tiempoFin - tiempoInicio) / 1000.0;
                
                System.out.println("===============================================");
                System.out.println("             COLISION DETECTADA                ");
                System.out.println("===============================================");
                System.out.printf("Coordenada de impacto: X: %.2f, Y: %.2f\n", presa.x, presa.y);
                System.out.printf("Distancia total recorrida por la presa: %.2f px\n", presa.distanciaRecorrida);
                System.out.printf("Tiempo de persecucion: %.2f segundos\n", segundosDePersecucion);
                
                for (int j = 0; j < perseguidores.size(); j++) {
                    System.out.printf("Distancia recorrida por perseguidor %d: %.2f px\n", (j + 1), perseguidores.get(j).distanciaRecorrida);
                }
                System.exit(0);
            }
        }
    }

    private void evadirPresa() {
        double margen = 150.0;
        if (presa.x < margen || presa.x > 1280 - margen || presa.y < margen || presa.y > 720 - margen) {
            double angulo_centro = Math.atan2(360 - presa.y, 640 - presa.x);
            presa.girarHacia(angulo_centro);
        } else {
            Objeto mas_cercano = null;
            double min_dist = Double.MAX_VALUE;
            for (Objeto p : perseguidores) {
                double d = Math.hypot(p.x - presa.x, p.y - presa.y);
                if (d < min_dist) {
                    min_dist = d;
                    mas_cercano = p;
                }
            }
            if (mas_cercano != null && min_dist<1500) {
                double angulo_evasion = Math.atan2(presa.y - mas_cercano.y, presa.x - mas_cercano.x);
                presa.girarHacia(angulo_evasion);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        presa.dibujar(g2d);
        for (Objeto p : perseguidores) {
            p.dibujar(g2d);
        }
    }
}