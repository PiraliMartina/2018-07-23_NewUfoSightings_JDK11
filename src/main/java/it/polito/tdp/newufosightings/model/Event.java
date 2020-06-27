package it.polito.tdp.newufosightings.model;

import java.time.LocalDateTime;

public class Event implements Comparable<Event>{

	public enum EventType {
		NUOVO_AVVISTAMENTO, FINE_ALLERTA
	}
	private EventType tipo;
	private LocalDateTime data;
	private Sighting avvistamento;
	private String stato;
	
	public Event(EventType tipo, LocalDateTime data, Sighting avvistamento) {
		super();
		this.tipo = tipo;
		this.data = data;
		this.avvistamento = avvistamento;
		this.stato = avvistamento.getState(); 
	}

	public EventType getTipo() {
		return tipo;
	}

	public LocalDateTime getData() {
		return data;
	}

	public Sighting getAvvistamento() {
		return avvistamento;
	}

	public String getStato() {
		return stato;
	}

	@Override
	public int compareTo(Event o) {
		return this.data.compareTo(o.data);
	}
	
	
	
	
	
}
