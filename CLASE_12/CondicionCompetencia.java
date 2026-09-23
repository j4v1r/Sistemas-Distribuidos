public class CondicionCompetencia implements Runnable{

	public static int variable_compartida = 0;
	private int n;

	public CondicionCompetencia(int n){
		this.n = n;
	}

	public static void modifica(){
		String id = Thread.currentThread().getName();
		if(id=="hilo_1"){
			variable_compartida++;
		}

		if(id=="hilo_2"){
			variable_compartida--;
		}

	}

	@Override
	public void run(){
		for(int i=0;i<n;i++){
			modifica();
		}
	}
}