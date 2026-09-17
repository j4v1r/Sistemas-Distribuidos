public class PruebaPoligonoIrreg{
 	public static void main (String[] args) {

	 	PoligonoIrreg pol1 = new PoligonoIrreg(7);
	 	System.out.println("Poligono original: ");
	 	System.out.println(pol1);

	 	pol1.modificarVertice(3, new Coordenada(10.5,20.5));

	 	System.out.println("Poligono modificado: ");
	 	System.out.println(pol1);
	}
}
