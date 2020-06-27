package it.polito.tdp.newufosightings.model;

import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.TreeMap;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;
import it.polito.tdp.newufosightings.model.Event.EventType;

public class Simulator {

	private int anno;
	private String shape;
	private NewUfoSightingsDAO dao;

	private Queue<Event> coda;
	private Map<String, Double> DEFCON;

	public Simulator(int anno, String shape) {
		super();
		this.anno = anno;
		this.shape = shape;
		dao = new NewUfoSightingsDAO();
	}

	public void simula(int T, int alpha) {
		// Lista di evento - anno, shape
		List<Sighting> ls = dao.loadAllSightings(anno, shape);

		// Inizializzo la coda
		coda = new PriorityQueue<Event>();
		for (Sighting s : ls) {
			coda.add(new Event(EventType.NUOVO_AVVISTAMENTO, s.getDatetime(), s));
		}

		// DEFCON iniziale
		DEFCON = new TreeMap<String, Double>();
		for (String s : dao.tuttiGliStati()) {
			DEFCON.put(s, 5.0);
		}

		// SIMULAZIONE VERA E PROPRIA
		while (!coda.isEmpty()) {
			Event e = coda.poll();

			switch (e.getTipo()) {
			case NUOVO_AVVISTAMENTO:

				String stato = e.getStato();

				// Decremento livello DEFCON nello stato in cui si verifica
				Double valoreAttuale = DEFCON.get(stato);
				Double valoreFinale = valoreAttuale - 1.0;
				if (valoreFinale < 1) {
					valoreFinale = 1.0;
				}
				DEFCON.put(stato, valoreFinale);

				// Aggiorno DEFCON degli stati vicini
				List<String> vicini = dao.getStatiVicini(stato);
				for (String vicino : vicini) {
					if (probabilita(alpha)) {
						Double valAttuale = DEFCON.get(vicino);
						Double valFinale = valAttuale - 0.5;
						if (valFinale < 1)
							valFinale = 1.0;
						DEFCON.put(vicino, valFinale);
					}
				}

				// Creo evento fine allerta
				coda.add(new Event(EventType.FINE_ALLERTA, e.getData().plusDays(T), e.getAvvistamento()));

				break;

			case FINE_ALLERTA:

				String stato1 = e.getStato();

				// Decremento livello DEFCON nello stato in cui si verifica
				Double valoreAttuale1 = DEFCON.get(stato1);
				Double valoreFinale1 = valoreAttuale1 + 1.0;
				if (valoreFinale1 > 5) {
					valoreFinale1 = 5.0;
				}
				DEFCON.put(stato1, valoreFinale1);

				// Aggiorno DEFCON degli stati vicini
				List<String> vicini1 = dao.getStatiVicini(stato1);
				for (String vicino : vicini1) {
					if (probabilita(alpha)) {
						Double valAttuale = DEFCON.get(vicino);
						Double valFinale = valAttuale + 0.5;
						if (valFinale > 5)
							valFinale = 5.0;
						DEFCON.put(vicino, valFinale);
					}
				}

				break;
			}
		}

	}

	private boolean probabilita(int alpha) {
		int n = (int) (Math.random() * 100);
		if (n < alpha)
			return true;
		return false;
	}
	

	public Map<String, Double> getDEFCON() {
		return DEFCON;
	}

}
