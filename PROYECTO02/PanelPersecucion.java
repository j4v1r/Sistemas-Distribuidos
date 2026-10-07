//PROYECTO02
//Colunga Aguilar Javier Alejandro
//Grupo: 7CM2
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class PanelPersecucion extends JPanel {
    private static final int MIN_X = 40;
    private static final int MAX_X = 1220;
    private static final int MIN_Y = 40;
    private static final int MAX_Y = 640;
    private static final double DISTANCIA_MINIMA_INICIAL = 180.0;

    private final Objeto presa;
    private final ArrayList<Objeto> perseguidores;
    private int contadorFrames = 0;
    private final long tiempoInicio;
    private final Random aleatorio = new Random();

    public PanelPersecucion(float mult_velocidad, int num_perseguidores) {
        setBackground(Color.WHITE);
        perseguidores = new ArrayList<>();

        presa = new Objeto(640, 360, 2.5, 0.15, Color.BLUE);
        double vel_perseguidor = 2.5 * mult_velocidad;

        //Distribuir perseguidores
        for (int i = 0; i < num_perseguidores; i++) {
            Point posicion = generarPosicionInicial();

            double maxGiro = 0.025 + aleatorio.nextDouble() * 0.035;
            perseguidores.add(new Objeto(posicion.x, posicion.y,
                    vel_perseguidor, maxGiro, Color.RED));
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

    private Point generarPosicionInicial() {
        for (int intento = 0; intento < 500; intento++) {
            int x = MIN_X + aleatorio.nextInt(MAX_X - MIN_X + 1);
            int y = MIN_Y + aleatorio.nextInt(MAX_Y - MIN_Y + 1);

            if (Math.hypot(x - presa.x, y - presa.y) < DISTANCIA_MINIMA_INICIAL) {
                continue;
            }

            boolean separada = true;
            for (Objeto otro : perseguidores) {
                if (Math.hypot(x - otro.x, y - otro.y) < DISTANCIA_MINIMA_INICIAL) {
                    separada = false;
                    break;
                }
            }
            if (separada) return new Point(x, y);
        }

        return new Point(MIN_X + aleatorio.nextInt(MAX_X - MIN_X + 1),
                MIN_Y + aleatorio.nextInt(MAX_Y - MIN_Y + 1));
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
                System.out.printf("Coordenada de impacto: X: %.2f, Y: %.2f%n", presa.x, presa.y);
                System.out.printf("Distancia total recorrida por la presa: %.2f px%n", presa.distanciaRecorrida);
                System.out.printf("Tiempo de persecucion: %.2f segundos%n", segundosDePersecucion);

                for (int j = 0; j < perseguidores.size(); j++) {
                    System.out.printf("Distancia recorrida por perseguidor %d: %.2f px%n",
                            j + 1, perseguidores.get(j).distanciaRecorrida);
                }
                System.exit(0);
            }
        }
    }

    private void evadirPresa() {
        Objeto mas_cercano = null;
        double min_dist = Double.MAX_VALUE;
        for (Objeto p : perseguidores) {
            double d = Math.hypot(p.x - presa.x, p.y - presa.y);
            if (d < min_dist) {
                min_dist = d;
                mas_cercano = p;
            }
        }

        double fuerzaX = 0;
        double fuerzaY = 0;
        boolean enPeligro = false;

        if (mas_cercano != null && min_dist < 1500) {
            fuerzaX = presa.x - mas_cercano.x;
            fuerzaY = presa.y - mas_cercano.y;
            double magnitud = Math.hypot(fuerzaX, fuerzaY);
            if (magnitud > 0) {
                fuerzaX = (fuerzaX / magnitud) * 100;
                fuerzaY = (fuerzaY / magnitud) * 100;
            }
            enPeligro = true;
        }

        double margen = 150.0;
        if (presa.x < margen) {
            fuerzaX += 300;
            fuerzaY += (fuerzaY >= 0) ? 300 : -300;
            enPeligro = true;
        } else if (presa.x > getWidth() - margen) {
            fuerzaX -= 300;
            fuerzaY += (fuerzaY >= 0) ? 300 : -300;
            enPeligro = true;
        }

        if (presa.y < margen) {
            fuerzaY += 300;
            fuerzaX += (fuerzaX >= 0) ? 300 : -300;
            enPeligro = true;
        } else if (presa.y > getHeight() - margen) {
            fuerzaY -= 300;
            fuerzaX += (fuerzaX >= 0) ? 300 : -300;
            enPeligro = true;
        }

        if (enPeligro) {
            presa.girarHacia(Math.atan2(fuerzaY, fuerzaX));
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        presa.dibujar(g2d);
        for (Objeto p : perseguidores) {
            p.dibujar(g2d);
        }
        g2d.dispose();
    }
}
