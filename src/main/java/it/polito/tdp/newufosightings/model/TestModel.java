package it.polito.tdp.newufosightings.model;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Model m = new Model();
		m.creaGrafo(2000, "oval");
		System.out.println(m.numVertex());
		System.out.println(m.numEdges());
	}

}
