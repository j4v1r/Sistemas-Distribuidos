import java.util.Arrays;

public class PoligonoIrreg{
	private Coordenada[] vertices;

	public PoligonoIrreg(int numCoor){
		vertices = new Coordenada[numCoor];
		for(int i=0;i<numCoor;i++){
			double x = (Math.random()*100)-50;
			double y = (Math.random()*100)-50;

			x = Math.round(x*100.0)/100.0;
			y = Math.round(y*100.0)/100.0;

			vertices[i]=new Coordenada(x,y);
		}
	}

	public void modificarVertice(int indice, Coordenada nuevaCoord){
		if(indice>=0 && indice<vertices.length){
			vertices[indice]=nuevaCoord;
		}else{
			System.out.println("Error");
		}
	}

	//Sobreescritura del método de la superclase objeto para imprimir con System.out.println( )
	@Override
	public String toString( ) {
		StringBuilder sb = new StringBuilder("\n");
		for(int i=0;i<vertices.length;i++){
			System.out.println(vertices[i]);
		}
		return sb.toString();
	}
}
