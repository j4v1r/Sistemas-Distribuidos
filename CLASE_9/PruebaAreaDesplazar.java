public class PruebaAreaDesplazar{
 	public static void main (String[] args) {

 		Coordenada[] verticesTrian = new Coordenada[3];
 		verticesTrian[0]=new Coordenada(0,2.31);
 		verticesTrian[1]=new Coordenada(-2,-1.15);
 		verticesTrian[2]=new Coordenada(2,-1.15);

 		Coordenada[] verticesRect = new Coordenada[4];    
 		verticesRect[0]=new Coordenada(2,7);
 		verticesRect[1]=new Coordenada(8,7);
 		verticesRect[2]=new Coordenada(2,3);
 		verticesRect[3]=new Coordenada(8,3);

 		Figura trieq = new TrianguloEq(verticesTrian);
 		Figura rect = new Rectangulo(verticesRect);


 		System.out.println("El área del triángulo es: "+trieq.area());
 		trieq.mostrarVertices();
 		System.out.println("\nEl área del rectángulo es: "+rect.area());
 		rect.mostrarVertices();

 		System.out.println("\nDesplazando(dx=3,dy=-2)...");
 		trieq.desplazar(3,-2);
 		rect.desplazar(3,-2);

 		System.out.println("\nEl area del triangulo desplazado es: "+trieq.area());
 		trieq.mostrarVertices();
 		System.out.println("\nEl area del rectangulo desplazado es: "+rect.area());
 		rect.mostrarVertices();
	}
}
