package it.polito.tdp.newufosightings.db;

import it.polito.tdp.newufosightings.model.State;

public class Arco {
	
	private State s1;
	private State s2;
	int peso;
	
	public Arco(State s1, State s2, int peso) {
		super();
		this.s1 = s1;
		this.s2 = s2;
		this.peso = peso;
	}

	public State getS1() {
		return s1;
	}

	public State getS2() {
		return s2;
	}

	public int getPeso() {
		return peso;
	}
	
	

	
}
