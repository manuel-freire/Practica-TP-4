package es.ucm.fdi.model;

import java.util.ArrayList;
import java.util.Random;

public class Car extends Vehicle{
	protected int resistance;
	protected double fault_probability;
	protected int max_fault_duration;
	protected long seed = System.currentTimeMillis();
	protected int kmSinceLastFailure; // Cuenta cuanto ha recorrido desde la última vez que se averió, no el tiempo, no?
	private Random numAleatorio;

	
	public Car(String id, int velMaxima, ArrayList<Junction> itinerario, int resistance, double fault_probability, int max_fault_duration) {
		super(id, velMaxima, itinerario);
		this.resistance = resistance;
		this.fault_probability = fault_probability;
		this.max_fault_duration = max_fault_duration;
		kmSinceLastFailure = 0;
		numAleatorio = new Random(seed);
	}
	
	public Car(String id, int velMaxima, ArrayList<Junction> itinerario, int resistance, double fault_probability, int max_fault_duration, long seed) {
		super(id, velMaxima, itinerario);
		this.resistance = resistance;
		this.fault_probability = fault_probability;
		this.max_fault_duration = max_fault_duration;
		this.seed = seed;
		kmSinceLastFailure = 0;
		numAleatorio = new Random(seed);
	}
	
	public void avanza() {
		if(tiempoAveria > 0) {
			--tiempoAveria;
		} else {
			//Vemos si se va a averiar en este paso
			boolean isCarOk = true;
			if(resistance >= kmSinceLastFailure) {
				if(numAleatorio.nextDouble() < fault_probability) {
					//Hemos averiado el vehículo
					this.setTiempoAveria(numAleatorio.nextInt(max_fault_duration) + 1);
					isCarOk = false;
					kmSinceLastFailure = 0;
				}
			}
			
			if(isCarOk) {		
				//Eliminamos el coche de la carretera
				carreteraActual.situacionCarretera.removeValue(localizacionCarretera, this);
				if(localizacionCarretera + velActual < carreteraActual.longitud) {
					localizacionCarretera += velActual;
					kilometrage += velActual;
					kmSinceLastFailure += velActual;
				} else {
					kilometrage += (carreteraActual.longitud - localizacionCarretera);
					kmSinceLastFailure += (carreteraActual.longitud - localizacionCarretera);
					localizacionCarretera = carreteraActual.longitud;
				}
				//Y lo volvemos a meter donde debería ir
				carreteraActual.situacionCarretera.putValue(localizacionCarretera, this);
			}
		}	
	}

}