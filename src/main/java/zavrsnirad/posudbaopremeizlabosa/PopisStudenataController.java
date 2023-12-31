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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.Predicate;

import zavrsnirad.posudbaopremeizlabosa.dao.PopisStudenataDAOImpl;
import zavrsnirad.posudbaopremeizlabosa.dao.StudentDAO;
import zavrsnirad.posudbaopremeizlabosa.model.Student;

public class PopisStudenataController extends Excel implements Initializable {
    @FXML
    private TextField tfSearch;
    @FXML
    private TextField tfJMBAG;
    @FXML
    private TextField tfPrezime;
    @FXML
    private TextField tfIme;
    @FXML
    private TextField tfStudij;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfTelefon;
    @FXML
    private TableView<Student> tvStudenti;
    @FXML
    private TableColumn<Student, Integer> colJMBAG;
    @FXML
    private TableColumn<Student, String> colPrezime;
    @FXML
    private TableColumn<Student, String> colIme;
    @FXML
    private TableColumn<Student, String> colStudij;
    @FXML
    private TableColumn<Student, String> colEmail;
    @FXML
    private TableColumn<Student, String> colTelefon;
    @FXML
    private Button btnUnesiPodatke;
    @FXML
    private Button btnAzuriraj;
    @FXML
    private Button btnIzbrisi;
    @FXML
    private Button btnUnesiPodatkeIzExcela;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO
        try {
            searchFilter();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            pokaziListuStudenata();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void promijeniNaPopisOpreme(ActionEvent event) throws IOException {
        Parent popisOpreme = FXMLLoader.load(getClass().getResource("PopisOpremeScene.fxml"));
        Scene popisOpremeScene = new Scene(popisOpreme);

        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(popisOpremeScene);
        window.show();
    }

    public void promijeniNaProfilStudenta(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("ProfilStudentaScene.fxml"));
        Parent profilStudenta = loader.load();

        Scene profilStudentaScene = new Scene(profilStudenta);

        ProfilStudentaController controller = loader.getController();
        controller.initData(tvStudenti.getSelectionModel().getSelectedItem());


        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();

        window.setScene(profilStudentaScene);
        window.show();
    }

    @FXML
    protected void pritisakMisa(MouseEvent event) {
        Student student = tvStudenti.getSelectionModel().getSelectedItem();
        tfJMBAG.setText("" +student.getJMBAG());
        tfPrezime.setText(student.getPrezime());
        tfIme.setText(student.getIme());
        tfStudij.setText(student.getStudij());
        tfEmail.setText(student.getEmail());
        tfTelefon.setText(student.getTelefon());
    }
    @FXML
    protected void pritisakGumba(ActionEvent event) throws IOException, SQLException {

        StudentDAO studentDAO = new PopisStudenataDAOImpl();
        Student student = new Student(Integer.parseInt(tfJMBAG.getText()), tfPrezime.getText(), tfIme.getText(), tfStudij.getText(), tfEmail.getText(), tfTelefon.getText());

        if(event.getSource().equals(btnUnesiPodatke)){
            studentDAO.unesiPodatke(student);
            pokaziListuStudenata();
        }
        else if(event.getSource().equals(btnAzuriraj)){
            studentDAO.azurirajPodatke(student);
            pokaziListuStudenata();
        }
        else if(event.getSource().equals(btnIzbrisi)){
            studentDAO.izbrisiPodatke(student);
            pokaziListuStudenata();
        }
        else if(event.getSource().equals(btnUnesiPodatkeIzExcela)) {
            unesiPodatkeIzExcela();
            pokaziListuStudenata();
        }
    }



    @FXML
    private void searchFilter() throws SQLException {
        StudentDAO studentDAO = new PopisStudenataDAOImpl();
        ObservableList<Student> listaStudenata = studentDAO.dohvatiListuStudenata();
        FilteredList<Student> filtriranaListaStudenata = new FilteredList<Student>(listaStudenata, e -> true);
        tfSearch.setOnKeyReleased(e -> {

            tfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filtriranaListaStudenata.setPredicate((Predicate<? super Student >) student -> {
                    if(newValue == null) {
                        return true;
                    }
                    String toLowerCaseFilter = newValue.toLowerCase();
                    if(student.getJMBAG().toString().toLowerCase().contains(newValue)){
                        return true;
                    }
                    else if(student.getPrezime().toLowerCase().contains(toLowerCaseFilter)) {
                        return true;
                    }
                    else if(student.getIme().toLowerCase().contains(toLowerCaseFilter)) {
                        return true;
                    }
                    else if(student.getStudij().toLowerCase().contains(toLowerCaseFilter)) {
                        return true;
                    }
                    else if(student.getEmail().toLowerCase().contains(toLowerCaseFilter)) {
                        return true;
                    }
                    else if(student.getTelefon().toLowerCase().contains(toLowerCaseFilter)) {
                        return true;
                    }

                return false;
                });
            });
            final SortedList<Student> studenti = new SortedList<>(filtriranaListaStudenata);
            studenti.comparatorProperty().bind(tvStudenti.comparatorProperty());
            tvStudenti.setItems(studenti);
        });

    }

    public void pokaziListuStudenata() throws SQLException {
        StudentDAO studentDAO = new PopisStudenataDAOImpl();
        ObservableList<Student> listaStudenata = studentDAO.dohvatiListuStudenata();

        colJMBAG.setCellValueFactory(new PropertyValueFactory<Student, Integer>("JMBAG"));
        colPrezime.setCellValueFactory(new PropertyValueFactory<Student, String>("prezime"));
        colIme.setCellValueFactory(new PropertyValueFactory<Student, String>("ime"));
        colStudij.setCellValueFactory(new PropertyValueFactory<Student, String>("studij"));
        colEmail.setCellValueFactory(new PropertyValueFactory<Student, String>("email"));
        colTelefon.setCellValueFactory(new PropertyValueFactory<Student, String>("telefon"));

        tvStudenti.setItems(listaStudenata);
    }
}