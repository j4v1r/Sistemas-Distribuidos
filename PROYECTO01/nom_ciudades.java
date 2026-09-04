import java.util.Scanner; 

public class nom_ciudades{
	public static void main(String[] args){
		int mayor=0, num=0;
		String[] ciudades = {"CDMX","Monterrey","Guadalajara","New York","Houston","Madrid","Tokio","París","Roma","Bogotá"};
		Scanner teclado = new Scanner(System.in);

		for(int i=0;i<=9;i++){
			int aux = ciudades[i].length();
			if(aux>num){
				mayor = i;
				num = aux;
			}
		}

		System.out.println("La ciudad con el nombre más largo es: "+ciudades[mayor]);
	}
}