public class SimulacionPila{

	static char[] pila = new char[10];
	static int tope;

	static boolean modificada = true;

	public synchronized void modifica(int tipoHilo){
		try {
            if (tipoHilo == 1) { 
                // ----------------- HILO PRODUCTOR -----------------
                // Espera si la pila está llena (10) o si falta que el impresor actualice
                while (tope == 10 || modificada) {
                    wait();
                }
                pila[tope] = 'X';
                tope++;
                modificada = true; // Avisa que hay un cambio por mostrar
                notifyAll();       // Despierta a los demás hilos
                
            } else if (tipoHilo == 2) { 
                // ----------------- HILO CONSUMIDOR -----------------
                // Espera si la pila está vacía (0) o si falta que el impresor actualice
                while (tope == 0 || modificada) {
                    wait();
                }
                tope--;
                pila[tope] = '\0'; // Limpia el valor simulando extracción
                modificada = true;
                notifyAll();
                
            } else if (tipoHilo == 3) { 
                // ----------------- HILO IMPRESOR -----------------
                // Se pausa hasta que haya una modificación real que mostrar
                while (!modificada) {
                    wait();
                }
                
                // Borrar pantalla multiplataforma (Secuencias de escape ANSI)
                System.out.print("\033[H\033[2J");
                System.out.flush();
                
                // Imprime la pila de arriba hacia abajo (como se ve en el video)
                for (int i = tope - 1; i >= 0; i--) {
                    System.out.println(pila[i]);
                }
                
                // Imprime el valor actual del tope
                System.out.println("Tope = " + tope);
                
                // Marca que ya se mostró en pantalla y libera a los demás hilos
                modificada = false;
                notifyAll();
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Un hilo fue interrumpido.");
        }		

	}

}