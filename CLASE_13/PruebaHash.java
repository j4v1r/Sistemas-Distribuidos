import java.util.*;
import java.io.*;

public class PruebaHash{
	
	public static void main(String[] args) throws Exception{

		Map<Character, Integer> hm = new HashMap<Character, Integer>();

        BufferedReader bfro = new BufferedReader(new FileReader("El_viejo_y_el_mar.txt"));
        
        int cr;

        while ((cr = bfro.read()) != -1) {
            Character caracter = (char) cr; // Convertimos el entero a char
               
            if(hm.containsKey(caracter)){
            	hm.put(caracter,hm.get(caracter)+1);
            }else{
            	hm.put(caracter,1);
            }
        }

        System.out.println("El número de caracteres es "+hm.size());

        List<Map.Entry<Character, Integer>> listaEntradas = new ArrayList<>(hm.entrySet());

        Collections.sort(listaEntradas, new Comparator<Map.Entry<Character, Integer>>() {
            @Override
            public int compare(Map.Entry<Character, Integer> entrada1, Map.Entry<Character, Integer> entrada2) {
                // Comparamos los valores de forma ascendente
                return entrada1.getValue().compareTo(entrada2.getValue());
            }
        });

        for(Map.Entry<Character, Integer> entrada:listaEntradas){
        	System.out.println(entrada.getKey()+" : "+entrada.getValue());
        }

	}
}