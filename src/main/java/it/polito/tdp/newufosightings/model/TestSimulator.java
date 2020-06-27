package it.polito.tdp.newufosightings.model;

import java.util.Map;

public class TestSimulator {

	public static void main(String[] args) {
		Simulator sim = new Simulator(2000, "triangle");
		sim.simula(30, 50);

		Map<String,Double> defcon = sim.getDEFCON();
		for(String s: defcon.keySet()) {
			System.out.println(String.format("%s- %f \n", s, defcon.get(s)));
		}
		
		
	}

}
