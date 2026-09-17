public class PruebaRectangulo {
 	public static void main (String[] args) {
	 	try{
			Rectangulo rect1 = new Rectangulo(2,3,5,1);

			Coordenada supIzq = new Coordenada(2,3);
	 		Coordenada infDer = new Coordenada(5,1);
	 		Rectangulo rect2 = new Rectangulo(supIzq,infDer);

	 		double ancho, alto;
			System.out.println("Calculando el área de un rectángulo dadas sus coordenadas en un plano cartesiano:");
		 	
		 	System.out.println(rect1);
		 	alto = rect1.superiorIzquierda().ordenada() - rect1.inferiorDerecha().ordenada();
		 	ancho = rect1.inferiorDerecha().abcisa() - rect1.superiorIzquierda().abcisa();
		 	System.out.println("El área del rectángulo1 es = " + ancho*alto);

		 	double ancho2, alto2;
		 	alto2 = rect2.superiorIzquierda().ordenada() - rect2.inferiorDerecha().ordenada();
		 	ancho2 = rect2.inferiorDerecha().abcisa() - rect2.superiorIzquierda().abcisa();
		 	System.out.println("El área del rectángulo2 es = "+ancho2*alto2);

	 	}catch(IllegalArgumentException e){
	 		System.out.println("Error al crear el objeto Rectangulo: " + e.getMessage());
	 	}
	}
}
