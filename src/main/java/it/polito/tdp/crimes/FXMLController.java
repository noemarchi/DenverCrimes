/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;
    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    @FXML // fx:id="boxCategoria"
    private ComboBox<String> boxCategoria; // Value injected by FXMLLoader
    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader
    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader
    @FXML // fx:id="boxArco"
    private ComboBox<Adiacenza> boxArco; // Value injected by FXMLLoader
    @FXML // fx:id="btnPercorso"
    private Button btnPercorso; // Value injected by FXMLLoader
    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPercorso(ActionEvent event) 
    {
    	Adiacenza arco = this.boxArco.getValue();
    	if(arco == null)
    	{
    		this.txtResult.setText("Seleziona un arco dal menù a tendina!");
    		return;
    	}
    	
    	String sorgente = arco.getV1();
    	String destinazione = arco.getV2();
    	
    	List<String> percorso = model.calcolaPercorso(sorgente, destinazione);
    	
    	this.txtResult.setText(String.format("Percorso massimo da %s a %s:\n", sorgente, destinazione));
    
    	for(String s: percorso)
    	{
    		this.txtResult.appendText(s + "\n");
    	}
    
    
    }

    @FXML
    void doCreaGrafo(ActionEvent event) 
    {
    	String categoria = this.boxCategoria.getValue();
    	if(categoria == null)
    	{
    		this.txtResult.setText("Seleziona una categoria di reato dal menù a tendina!");
    		return;
    	}
    	Integer mese = this.boxMese.getValue();
    	if(mese == null)
    	{
    		this.txtResult.setText("Seleziona una mese dal menù a tendina!");
    		return;
    	}
    	
    	this.model.creaGrafo(categoria, mese);
    	
    	this.txtResult.clear();
    	this.txtResult.appendText("Grafo creato!\n");
    	this.txtResult.appendText("# vertici: " + model.nVertici() + "\n");
    	this.txtResult.appendText("# archi: " + model.nArchi() + "\n");
    	
    	this.txtResult.appendText("Elenco di archi il cui peso è superiore al peso medio presente nel grafo:\n");
    	
    	for(Adiacenza a: model.getArchiMaggioriPesoMedio())
    	{
    		this.txtResult.appendText(a.toString() + ": " + a.getPeso() + "\n");
    	}
    	
    	this.boxArco.getItems().clear();
    	this.boxArco.getItems().addAll(model.getArchi());
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() 
    {
        assert boxCategoria != null : "fx:id=\"boxCategoria\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxArco != null : "fx:id=\"boxArco\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPercorso != null : "fx:id=\"btnPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) 
    {
    	this.model = model;
    	this.boxCategoria.getItems().setAll(model.getCategorieReato());
    	
    	for(int i=1; i<=12; i++)
    	{
    		this.boxMese.getItems().add(i);
    	}
    }
}
