public class PruebaHilos{
	
	public static void main(String[] args){

		SimulacionPila monitor = new SimulacionPila();

        // 1. Creación del Hilo Productor
        Thread productor = new Thread(() -> {
            while (true) {
                try {
                    // Tiempo aleatorio tp (ej: entre 500ms y 1500ms)
                    int tp = (int) (Math.random() * 1000) + 500;
                    Thread.sleep(tp);
                } catch (InterruptedException e) {}
                
                monitor.modifica(1); // Llama al método synchronized
            }
        });

        // 2. Creación del Hilo Consumidor
        Thread consumidor = new Thread(() -> {
            while (true) {
                try {
                    // Tiempo aleatorio tc (ej: entre 800ms y 2000ms)
                    int tc = (int) (Math.random() * 1200) + 800;
                    Thread.sleep(tc);
                } catch (InterruptedException e) {}
                
                monitor.modifica(2); // Llama al método synchronized
            }
        });

        // 3. Creación del Hilo Impresor
        Thread impresor = new Thread(() -> {
            while (true) {
                monitor.modifica(3); // Siempre intenta imprimir, pero el wait() lo controla
            }
        });

        // Iniciamos los hilos (el impresor primero para dibujar el estado inicial)
        impresor.start();
        productor.start();
        consumidor.start();
	}
}