package zavrsnirad.posudbaopremeizlabosa;

import javafx.application.Platform;
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
import zavrsnirad.posudbaopremeizlabosa.model.Oprema;
import zavrsnirad.posudbaopremeizlabosa.model.Student;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class ProfilStudentaController implements Initializable {

    @FXML
    private Label lbJMBAG;
    @FXML
    private Label lbPrezime;
    @FXML
    private Label lbIme;
    @FXML
    private Label lbStudij;
    @FXML
    private Label lbEmail;
    @FXML
    private Label lbTelefon;
    @FXML
    private TextField tfSearchDostupnaOprema;
    @FXML
    private TextField tfID;
    @FXML
    private TextField tfNaziv;
    @FXML
    private TextField tfDetalji;
    @FXML
    private TextField tfKolicina;
    @FXML
    private TableView<Oprema> tvPosudenaOprema;
    @FXML
    private TableColumn<Student, Integer> colIDPosudeno;
    @FXML
    private TableColumn<Student, String> colNazivPosudeno;
    @FXML
    private TableColumn<Student, String> colDetaljiPosudeno;
    @FXML
    private TableColumn<Student, Integer> colKolicinaPosudeno;
    @FXML
    private TableView<Oprema> tvDostupnaOprema;
    @FXML
    private TableColumn<Student, Integer> colIDDostupno;
    @FXML
    private TableColumn<Student, String> colNazivDostupno;
    @FXML
    private TableColumn<Student, String> colDetaljiDostupno;
    @FXML
    private TableColumn<Student, Integer> colKolicinaDostupno;
    @FXML
    private Button btnUnesi;
    @FXML
    private Button btnIzbrisi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO
        try {
            searchFilterDostupnaOprema();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            pokaziListuOpreme();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Platform.runLater(() -> {
            try {
                pokaziListuPosudeneOpremeStudentu();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private Student odabraniStudent;

    public void initData(Student student) {
        odabraniStudent = student;

        lbJMBAG.setText(odabraniStudent.getJMBAG().toString());
        lbPrezime.setText(odabraniStudent.getPrezime());
        lbIme.setText(odabraniStudent.getIme());
        lbStudij.setText(odabraniStudent.getStudij());
        lbEmail.setText(odabraniStudent.getEmail());
        lbTelefon.setText(odabraniStudent.getTelefon());
    }

    public void promijeniNaPopisStudenata(ActionEvent event) throws IOException {
        Parent popisOpreme = FXMLLoader.load(getClass().getResource("PopisStudenataScene.fxml"));
        Scene popisOpremeScene = new Scene(popisOpreme);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(popisOpremeScene);
        window.show();
    }

    @FXML
    protected void pritisakMisaDostupno(MouseEvent event) {
        Oprema oprema = tvDostupnaOprema.getSelectionModel().getSelectedItem();
        tfID.setText("" +oprema.getId());
        tfNaziv.setText(oprema.getNaziv());
        tfDetalji.setText(oprema.getDetalji());
        tfKolicina.setText("" +oprema.getKolicina());
    }

    @FXML
    protected void pritisakMisaPosudeno(MouseEvent event) {
        Oprema oprema = tvPosudenaOprema.getSelectionModel().getSelectedItem();
        tfID.setText("" +oprema.getId());
        tfNaziv.setText(oprema.getNaziv());
        tfDetalji.setText(oprema.getDetalji());
        tfKolicina.setText("" +oprema.getKolicina());
    }

    public void pokaziListuOpreme() throws SQLException {

        OpremaDAO opremaDAO = new PopisOpremeDAOImpl();
        ObservableList<Oprema> listaOpreme = opremaDAO.dohvatiListuOpreme();

        colIDDostupno.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        colNazivDostupno.setCellValueFactory(new PropertyValueFactory<Student, String>("naziv"));
        colDetaljiDostupno.setCellValueFactory(new PropertyValueFactory<Student, String>("detalji"));
        colKolicinaDostupno.setCellValueFactory(new PropertyValueFactory<Student, Integer>("kolicina"));

        tvDostupnaOprema.setItems(listaOpreme);
    }

    public void pokaziListuPosudeneOpremeStudentu() throws SQLException {

        OpremaDAO opremaDAO = new PopisOpremeDAOImpl();
        ObservableList<Oprema> listaOpreme = opremaDAO.dohvatiListuPosudeneOpremeStudentu(lbJMBAG.getText());

        colIDPosudeno.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        colNazivPosudeno.setCellValueFactory(new PropertyValueFactory<Student, String>("naziv"));
        colDetaljiPosudeno.setCellValueFactory(new PropertyValueFactory<Student, String>("detalji"));
        colKolicinaPosudeno.setCellValueFactory(new PropertyValueFactory<Student, Integer>("kolicina"));

        tvPosudenaOprema.setItems(listaOpreme);
    }

    @FXML
    protected void pritisakGumba(ActionEvent event) throws SQLException {
        OpremaDAO opremaDAO = new PopisOpremeDAOImpl();

        if(event.getSource().equals(btnUnesi)){
            opremaDAO.unesiPodatkeKomplicirano(Integer.parseInt(tfKolicina.getText()), Integer.parseInt(tfID.getText()), Integer.parseInt(lbJMBAG.getText()));
            pokaziListuPosudeneOpremeStudentu();
            pokaziListuOpreme();

        }
        else if(event.getSource().equals(btnIzbrisi)){
            opremaDAO.izbrisiPodatkeKomplicirano(Integer.parseInt(tfKolicina.getText()), Integer.parseInt(tfID.getText()), Integer.parseInt(lbJMBAG.getText()));
            pokaziListuPosudeneOpremeStudentu();
            pokaziListuOpreme();
        }
    }
    @FXML
    private void searchFilterDostupnaOprema() throws SQLException {
        OpremaDAO oprematDAO = new PopisOpremeDAOImpl();
        ObservableList<Oprema> listaOpreme = oprematDAO.dohvatiListuOpreme();
        FilteredList<Oprema> filtriranaListaOpreme = new FilteredList<Oprema>(listaOpreme, e -> true);
        tfSearchDostupnaOprema.setOnKeyReleased(e -> {

            tfSearchDostupnaOprema.textProperty().addListener((observable, oldValue, newValue) -> {
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
            opremaSortedList.comparatorProperty().bind(tvDostupnaOprema.comparatorProperty());
            tvDostupnaOprema.setItems(opremaSortedList);
        });
    }
}
