import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main18 {

    private static final Random random = new Random();

    public static String generarCurpAleatoria() {
        String letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numeros = "0123456789";

        StringBuilder curp = new StringBuilder();

        for (int i = 0; i < 4; i++) {
            curp.append(letras.charAt(random.nextInt(letras.length())));
        }

        for (int i = 0; i < 6; i++) {
            curp.append(numeros.charAt(random.nextInt(numeros.length())));
        }

        curp.append(random.nextBoolean() ? 'H' : 'M');

        for (int i = 0; i < 5; i++) {
            curp.append(letras.charAt(random.nextInt(letras.length())));
        }

        for (int i = 0; i < 2; i++) {
            curp.append(numeros.charAt(random.nextInt(numeros.length())));
        }

        return curp.toString();
    }

    // Método de ordenamiento de la Clase 11
    public static void insertarOrdenadamente(ArrayList<String> listaCurps, String nuevaCurp) {
    // Usamos ListIterator, que permite navegar hacia adelante, hacia atrás y modificar la lista
    ListIterator<String> iterador = listaCurps.listIterator();

    while (iterador.hasNext()) {
        String curpActual = iterador.next();

        // Comparamos la cadena completa directamente sin extraer prefijos
        if (nuevaCurp.compareTo(curpActual) < 0) {
            // Retrocedemos el cursor un paso porque next() ya lo había avanzado
            iterador.previous();
            
            // Insertamos directamente en la posición del iterador
            iterador.add(nuevaCurp);
            return; // Salimos del método una vez insertado
        }
    }

    // Si termina el bucle, la nueva CURP es mayor a todas, se añade al final
    iterador.add(nuevaCurp);
}

    public static void ordenarCurps(ArrayList<String> lista) {

        ArrayList<String> temporal = new ArrayList<>();

        for (String curp : lista) {
            insertarOrdenadamente(temporal, curp);
        }
    }

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Uso: java Main18 <numero_hilos>");
            return;
        }

        int numeroHilos = Integer.parseInt(args[0]);

        if (numeroHilos <= 0) {
            System.out.println("El numero de hilos debe ser mayor que 0.");
            return;
        }

        final int M = 500;
        final int N = 50000;

        System.out.println("Hilos del pool: " + numeroHilos);
        System.out.println("Generando las listas...");

        ArrayList<ArrayList<String>> listas = new ArrayList<>();

        for (int i = 0; i < M; i++) {

            ArrayList<String> lista = new ArrayList<>(N);

            for (int j = 0; j < N; j++) {
                lista.add(generarCurpAleatoria());
            }

            listas.add(lista);
        }

        System.out.println("Listas generadas.");
        System.out.println("Comenzando ordenamiento...");

        ExecutorService pool =
                Executors.newFixedThreadPool(numeroHilos);

        for (ArrayList<String> lista : listas) {

            pool.execute(() -> {
                ordenarCurps(lista);
            });
        }

        pool.shutdown();

        try {

            pool.awaitTermination(
                    Long.MAX_VALUE,
                    TimeUnit.NANOSECONDS
            );

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Ordenamiento terminado.");
    }
}