package zavrsnirad.posudbaopremeizlabosa;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import zavrsnirad.posudbaopremeizlabosa.dao.OpremaDAO;
import zavrsnirad.posudbaopremeizlabosa.dao.PopisOpremeDAOImpl;
import zavrsnirad.posudbaopremeizlabosa.model.Oprema;
import zavrsnirad.posudbaopremeizlabosa.model.Student;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class PopisOpremeController implements Initializable {
    @FXML
    private TextField tfID;
    @FXML
    private TextField tfNaziv;
    @FXML
    private TextField tfDetalji;
    @FXML
    private TextField tfKolicina;
    @FXML
    private TableView<Oprema> tvOprema;
    @FXML
    private TableColumn<Student, Integer> colID;
    @FXML
    private TableColumn<Student, String> colNaziv;
    @FXML
    private TableColumn<Student, String> colDetalji;
    @FXML
    private TableColumn<Student, Integer> colKolicina;
    @FXML
    private Button btnUnesiPodatke;
    @FXML
    private Button btnAzuriraj;
    @FXML
    private Button btnIzbrisi;


    public void promijeniNaPopisStudenata(ActionEvent event) throws IOException {
        Parent popisOpreme = FXMLLoader.load(getClass().getResource("PopisStudenataScene.fxml"));
        Scene popisOpremeScene = new Scene(popisOpreme);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(popisOpremeScene);
        window.show();
    }
    @FXML
    protected void pritisakMisa(MouseEvent event) {
        Oprema oprema = tvOprema.getSelectionModel().getSelectedItem();
        tfID.setText("" +oprema.getId());
        tfNaziv.setText(oprema.getNaziv());
        tfDetalji.setText(oprema.getDetalji());
        tfKolicina.setText("" +oprema.getKolicina());
    }

    @FXML
    protected void pritisakGumba(ActionEvent event) throws SQLException {

        OpremaDAO opremaDAO = new PopisOpremeDAOImpl();
        Oprema oprema = new Oprema(Integer.parseInt(tfID.getText()), tfNaziv.getText(), tfDetalji.getText(), Integer.parseInt(tfKolicina.getText()), null);

        if(event.getSource().equals(btnUnesiPodatke)){
            opremaDAO.unesiPodatke(oprema);
            pokaziListuOpreme();
        }
        else if(event.getSource().equals(btnAzuriraj)){
            opremaDAO.azurirajPodatke(oprema);
            pokaziListuOpreme();
        }
        else if(event.getSource().equals(btnIzbrisi)){
            opremaDAO.izbrisiPodatke(oprema);
            pokaziListuOpreme();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO
        try {
            pokaziListuOpreme();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void pokaziListuOpreme() throws SQLException {
        OpremaDAO opremaDAO = new PopisOpremeDAOImpl();
        ObservableList<Oprema> listaOpreme = opremaDAO.dohvatiListuOpreme();

        colID.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        colNaziv.setCellValueFactory(new PropertyValueFactory<Student, String>("naziv"));
        colDetalji.setCellValueFactory(new PropertyValueFactory<Student, String>("detalji"));
        colKolicina.setCellValueFactory(new PropertyValueFactory<Student, Integer>("kolicina"));

        tvOprema.setItems(listaOpreme);
    }
}
