import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Se define la tarea implementando la interfaz Runnable
class TareaAsincrona implements Runnable {
    private final int idTarea;

    public TareaAsincrona(int idTarea) {
        this.idTarea = idTarea;
    }

    @Override
    public void run() {
        // Imprime el nombre del hilo que está ejecutando esta tarea
        System.out.println("Iniciando Tarea " + idTarea + " en el hilo: " + Thread.currentThread().getName());
        
        try {
            // Simula un trabajo que toma 1 segundo
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("La tarea " + idTarea + " fue interrumpida.");
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Tarea " + idTarea + " finalizada.");
    }
}

public class EjemploThreadPool {
    public static void main(String[] args) {
        System.out.println("Iniciando el Thread Pool con 3 hilos...");
        
        // Se crea un Thread Pool con un límite fijo de 3 hilos
        ExecutorService pool = Executors.newFixedThreadPool(3);

        // Se envían 10 tareas al pool
        for (int i = 1; i <= 10; i++) {
            TareaAsincrona tarea = new TareaAsincrona(i);
            pool.execute(tarea);
        }

        // Se indica al pool que no acepte más tareas nuevas y termine las pendientes
        pool.shutdown();
        
        System.out.println("Todas las tareas han sido enviadas al pool.");
    }
}