//PROYECTO02
//Colunga Aguilar Javier Alejandro
//Grupo: 7CM2
import javax.swing.*;

public class Main extends JFrame {

    public static void main(String[] args) {
        if (args.length != 2) {
            println("Ingresa todos los datos necesarios.");
            return;
        }

        try {
            float mult_velocidad = Float.parseFloat(args[0]);
            int num_perseguidores = Integer.parseInt(args[1]);

            if (!Float.isFinite(mult_velocidad) || mult_velocidad <= 1.0f) {
                System.out.println("El multiplicador debe ser un numero mayor que 1.");
                return;
            }
            if (num_perseguidores < 1 || num_perseguidores > 5) {
                System.out.println("El numero de perseguidores debe estar entre 1 y 5.");
                return;
            }

            SwingUtilities.invokeLater(() -> {
                Main frame = new Main(mult_velocidad, num_perseguidores);
                frame.setVisible(true);
            });
        } catch (NumberFormatException ex) {
            System.out.println("Los parametros deben ser numericos: <multiplicador> <perseguidores>.");
        }
    }

    public Main(float mult_velocidad, int num_perseguidores) {
        setTitle("PROYECTO02 - Persecucion");
        setSize(1280, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        PanelPersecucion panel = new PanelPersecucion(mult_velocidad, num_perseguidores);
        add(panel);
    }
}
