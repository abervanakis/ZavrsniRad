package zavrsnirad.posudbaopremeizlabosa;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import zavrsnirad.posudbaopremeizlabosa.dao.OpremaDAO;
import zavrsnirad.posudbaopremeizlabosa.dao.PopisOpremeDAOImpl;
import zavrsnirad.posudbaopremeizlabosa.dao.PopisStudenataDAOImpl;
import zavrsnirad.posudbaopremeizlabosa.dao.StudentDAO;
import zavrsnirad.posudbaopremeizlabosa.model.Oprema;
import zavrsnirad.posudbaopremeizlabosa.model.Student;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class PopisOpremeController implements Initializable {
    @FXML
    private TextField tfSearch;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO
        try {
            searchFilter();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            pokaziListuOpreme();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
        else if(event.getSource().equals(btnIzbrisi)) {
            if(!opremaJePosudenaStudentu(oprema)) {
                opremaDAO.izbrisiPodatke(oprema);
            }
            pokaziListuOpreme();
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

    public boolean opremaJePosudenaStudentu(Oprema oprema) throws SQLException {
        OpremaDAO opremaDAO = new PopisOpremeDAOImpl();
        ObservableList<Oprema> listaOpreme = opremaDAO.dohvatiListuPosudeneOpreme();
        boolean opremaJePosudena = false;
        for(Oprema posudenaOprema : listaOpreme) {
            if(posudenaOprema.getId().equals(oprema.getId())) {
                opremaJePosudena = true;
                break;
            }
        }
        return opremaJePosudena;
    }

    @FXML
    private void searchFilter() throws SQLException {
        OpremaDAO oprematDAO = new PopisOpremeDAOImpl();
        ObservableList<Oprema> listaOpreme = oprematDAO.dohvatiListuOpreme();
        FilteredList<Oprema> filtriranaListaOpreme = new FilteredList<Oprema>(listaOpreme, e -> true);
        tfSearch.setOnKeyReleased(e -> {

            tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filtriranaListaOpreme.setPredicate((Predicate<? super Oprema>) oprema -> {
                    if (newValue == null) {
                        return true;
                    }
                    String toLowerCaseFilter = newValue.toLowerCase();
                    if (oprema.getId().toString().toLowerCase().contains(newValue)) {
                        return true;
                    } else if (oprema.getNaziv().toLowerCase().contains(toLowerCaseFilter)) {
                        return true;
                    } else if (oprema.getDetalji().toLowerCase().contains(toLowerCaseFilter)) {
                        return true;
                    } else if (oprema.getKolicina().toString().toLowerCase().contains(toLowerCaseFilter)) {
                        return true;
                    }

                    return false;
                });
            });
            final SortedList<Oprema> opremaSortedList = new SortedList<>(filtriranaListaOpreme);
            opremaSortedList.comparatorProperty().bind(tvOprema.comparatorProperty());
            tvOprema.setItems(opremaSortedList);
        });
    }
}
