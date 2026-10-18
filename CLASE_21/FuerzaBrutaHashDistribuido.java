import java.security.MessageDigest;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class FuerzaBrutaHashDistribuido {

    // Variable atómica compartida: permite que todos los Workers sepan si alguien más ya encontró el resultado
    private static final AtomicBoolean encontrado = new AtomicBoolean(false);

    public static void main(String[] args) throws Exception {
        String passwordSecreta = "zanja";
        String hashObjetivo = sha256(passwordSecreta);

        System.out.println("Hash objetivo: " + hashObjetivo);
        System.out.println("Buscando contraseña de forma concurrente...\n");

        long inicio = System.currentTimeMillis();

        // El Master detecta cuántos núcleos lógicos tiene la computadora para optimizar el pool
        int nucleos = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(nucleos);

        // El Master divide el trabajo: crea 26 tareas, asignando a cada una letra inicial distinta
        for (char letraInicial = 'a'; letraInicial <= 'z'; letraInicial++) {
            final char primeraLetra = letraInicial;
            
            // Envía la tarea al pool de Workers
            executor.submit(() -> ejecutarWorker(primeraLetra, hashObjetivo, inicio));
        }

        // El Master cierra la admisión de nuevas tareas y espera a que los Workers terminen
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.HOURS);

        if (!encontrado.get()) {
            System.out.println("No encontrada");
        }
    }

    // La rutina que ejecutará cada Worker
    private static void ejecutarWorker(char letraInicial, String hashObjetivo, long tiempoInicio) {
        try {
            // Instancia local de MessageDigest exclusiva para este Worker
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            int intentos = 0;

            // Nota que el ciclo exterior ya no existe; la letra inicial la da el Master
            for (char b = 'a'; b <= 'z'; b++) {
                for (char c = 'a'; c <= 'z'; c++) {
                    for (char d = 'a'; d <= 'z'; d++) {
                        for (char e = 'a'; e <= 'z'; e++) {
                            
                            // Evaluación en cada iteración: si otro Worker ya la encontró, abortar tarea
                            if (encontrado.get()) {
                                return;
                            }

                            String intento = "" + letraInicial + b + c + d + e;
                            intentos++;

                            // Calculamos el hash usando el digest local
                            md.reset();
                            byte[] hashBytes = md.digest(intento.getBytes());
                            StringBuilder hex = new StringBuilder();
                            for (byte by : hashBytes) {
                                hex.append(String.format("%02x", by));
                            }
                            String hash = hex.toString();

                            // Si hay coincidencia, cambiamos la bandera atómica y detenemos todo
                            if (hash.equals(hashObjetivo)) {
                                if (encontrado.compareAndSet(false, true)) {
                                    long fin = System.currentTimeMillis();
                                    System.out.println("¡Contraseña encontrada por el hilo " + Thread.currentThread().getName() + "!");
                                    System.out.println("Contraseña: " + intento);
                                    System.out.println("Intentos del Worker: " + intentos);
                                    System.out.println("Tiempo(ms): " + (fin - tiempoInicio));
                                }
                                return;
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Método secuencial auxiliar solo para generar el hash objetivo en el main
    public static String sha256(String texto) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(texto.getBytes());
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}