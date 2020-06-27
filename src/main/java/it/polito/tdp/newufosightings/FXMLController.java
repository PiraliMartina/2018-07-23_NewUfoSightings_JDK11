package it.polito.tdp.newufosightings;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.newufosightings.model.Model;
import it.polito.tdp.newufosightings.model.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare al branch master_turnoB per turno B

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField txtAnno;

    @FXML
    private Button btnSelezionaAnno;

    @FXML
    private ComboBox<String> cmbBoxForma;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private TextField txtT1;

    @FXML
    private TextField txtAlfa;

    @FXML
    private Button btnSimula;

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	try {
    		int anno = Integer.parseInt(txtAnno.getText());
    		String shape = cmbBoxForma.getValue();
    		model.creaGrafo(anno, shape);
    		
    		txtResult.setText(String.format("Grafo creato con %d vertici e %d archi \n", model.numVertex(), model.numEdges()));
    		for(State s: model.getAllVertex()) {
    			txtResult.appendText(String.format("%s - %d\n", s, model.pesoAdiacenti(s)));
    		}
    	} catch(Exception e) {
    		txtResult.setText("ERRORE!!!");
    	}
    }

    @FXML
    void doSelezionaAnno(ActionEvent event) {
    	try {
    		int anno = Integer.parseInt(txtAnno.getText());
    		if(anno>2014 || anno<1910) {
    			txtResult.setText("Inserisci anno tra 2014 e 1910");
    			return;
    		}
    		
    		List<String> ls = model.getAllShapes(anno);
    		ls.sort(null);
    		ls.remove(0);
    		cmbBoxForma.getItems().addAll(ls);
    		cmbBoxForma.setValue(ls.get(0));
    		
    		
    	} catch(NumberFormatException e) {
    		txtResult.setText("Non hai inserito un numero!!!");
    	} catch(Exception e) {
    		txtResult.setText("ERRORE!!!");
    	}
    }

    @FXML
    void doSimula(ActionEvent event) {
    	try {
    		int T = Integer.parseInt(txtT1.getText());
    		int alpha = Integer.parseInt(txtAlfa.getText());
    		int anno = Integer.parseInt(txtAnno.getText());
    		String shape = cmbBoxForma.getValue();
    		
    		if(T<0 || T>365) {
    			txtResult.setText("Valore di T errato!");
    			return;
    		}
    		if(alpha<0 || alpha>100) {
    			txtResult.setText("Valore di alpha errato!");
    			return;
    		}
    		if(anno>2014 || anno<1910) {
    			txtResult.setText("Inserisci anno tra 2014 e 1910");
    			return;
    		}
    		
    		Map<String,Double> defcon = model.simula(anno, shape, T, alpha);
    		txtResult.setText("SIMULAZIONE: \n");
    		for(String s: defcon.keySet()) {
    			txtResult.appendText(String.format("%s- %.1f \n", s, defcon.get(s)));
    		}
    		
    	} catch(NumberFormatException e) {
    		txtResult.setText("Non hai inserito un numero!!!");
    	} catch(Exception e) {
    		txtResult.setText("ERRORE!!!");
    	}
    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSelezionaAnno != null : "fx:id=\"btnSelezionaAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert cmbBoxForma != null : "fx:id=\"cmbBoxForma\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtT1 != null : "fx:id=\"txtT1\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAlfa != null : "fx:id=\"txtAlfa\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
