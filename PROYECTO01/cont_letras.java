import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class cont_letras{
    public static void main(String[] args){

        int[] contador = new int[26];

        try{
            File archivo = new File("archivo.txt");
            FileReader lector = new FileReader(archivo);

            int caracter;

            while((caracter=lector.read())!=-1){

                char letra = Character.toLowerCase((char)caracter);

                if(letra>='a'&&letra<='z'){
                    contador[letra-'a']++;
                }
            }

            lector.close();

            for(int i=0;i<26;i++){
                System.out.println((char)('a'+i)+": "+contador[i]);
            }
        }catch(IOException e){
            System.out.println("Erro: "+e);
        }
    }
}