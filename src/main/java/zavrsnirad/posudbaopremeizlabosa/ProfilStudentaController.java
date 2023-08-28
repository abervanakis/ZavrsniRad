package zavrsnirad.posudbaopremeizlabosa;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import zavrsnirad.posudbaopremeizlabosa.dao.PopisStudenataDAOImpl;
import zavrsnirad.posudbaopremeizlabosa.dao.ProfilStudentaDAO;
import zavrsnirad.posudbaopremeizlabosa.dao.ProfilStudentaDAOImpl;
import zavrsnirad.posudbaopremeizlabosa.dao.StudentDAO;
import zavrsnirad.posudbaopremeizlabosa.model.Oprema;
import zavrsnirad.posudbaopremeizlabosa.model.Student;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO
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

/*
    public Connection getConnection() {
        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/posudba-opreme-iz-labosa", "root", "passRootworD");
            return connection;
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }*/
/*
    public ObservableList<Oprema> dohvatiListuOpreme() {
        ObservableList<Oprema> listaOpreme = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "SELECT * FROM oprema";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Oprema oprema;
            while (resultSet.next()){
                oprema = new Oprema(resultSet.getInt("id"), resultSet.getString("naziv"), resultSet.getString("detalji"), resultSet.getInt("kolicina"), null);
                listaOpreme.add(oprema);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaOpreme;
    }
*/
    public void pokaziListuOpreme() throws SQLException {
        ProfilStudentaDAO profilStudentaDAO = new ProfilStudentaDAOImpl();
        ObservableList<Oprema> listaOpreme = profilStudentaDAO.dohvatiListuOpreme();

        colIDDostupno.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        colNazivDostupno.setCellValueFactory(new PropertyValueFactory<Student, String>("naziv"));
        colDetaljiDostupno.setCellValueFactory(new PropertyValueFactory<Student, String>("detalji"));
        colKolicinaDostupno.setCellValueFactory(new PropertyValueFactory<Student, Integer>("kolicina"));

        tvDostupnaOprema.setItems(listaOpreme);
    }
    /*
    public ObservableList<Oprema> dohvatiListuPosudeneOpremeStudentu() {
        ObservableList<Oprema> listaOpreme = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "SELECT * FROM posudenaoprema WHERE JMBAGstudenta = '" + lbJMBAG.getText() + "'";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Oprema oprema;
            while (resultSet.next()){
                oprema = new Oprema(resultSet.getInt("id"), resultSet.getString("naziv"), resultSet.getString("detalji"), resultSet.getInt("kolicina"), resultSet.getInt("JMBAGstudenta"));
                listaOpreme.add(oprema);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaOpreme;
    }*/

    public void pokaziListuPosudeneOpremeStudentu() throws SQLException {
        ProfilStudentaDAO profilStudentaDAO = new ProfilStudentaDAOImpl();
        ObservableList<Oprema> listaOpreme = profilStudentaDAO.dohvatiListuPosudeneOpremeStudentu(lbJMBAG.getText());

        colIDPosudeno.setCellValueFactory(new PropertyValueFactory<Student, Integer>("id"));
        colNazivPosudeno.setCellValueFactory(new PropertyValueFactory<Student, String>("naziv"));
        colDetaljiPosudeno.setCellValueFactory(new PropertyValueFactory<Student, String>("detalji"));
        colKolicinaPosudeno.setCellValueFactory(new PropertyValueFactory<Student, Integer>("kolicina"));

        tvPosudenaOprema.setItems(listaOpreme);
    }

    @FXML
    protected void pritisakGumba(ActionEvent event) throws SQLException {

        ProfilStudentaDAO profilStudentaDAO = new ProfilStudentaDAOImpl();

        if(event.getSource().equals(btnUnesi)){
            Oprema oprema = new Oprema(Integer.parseInt(tfID.getText()), tfNaziv.getText(), tfDetalji.getText(), Integer.parseInt(tfKolicina.getText()), null);

            profilStudentaDAO.unesiPodatkeKomplicirano(Integer.parseInt(tfKolicina.getText()), Integer.parseInt(tfID.getText()), Integer.parseInt(lbJMBAG.getText()));
            pokaziListuPosudeneOpremeStudentu();
            pokaziListuOpreme();
            //unesiPodatke();
        }
        else if(event.getSource().equals(btnIzbrisi)){
            profilStudentaDAO.izbrisiPodatkeKomplicirano(Integer.parseInt(tfKolicina.getText()), Integer.parseInt(tfID.getText()), Integer.parseInt(lbJMBAG.getText()));
            pokaziListuPosudeneOpremeStudentu();
            pokaziListuOpreme();
            //izbrisiPodatke();
        }
    }
/*
    private void unesiPodatke() throws SQLException {
        ObservableList<Oprema> listaDostupneOpreme = dohvatiListuOpreme();
        int kolicinaKojaSePosuduje = Integer.parseInt(tfKolicina.getText());

        for(Oprema oprema : listaDostupneOpreme) {
            //postoji li oprema sa tim id-om
            if(oprema.getId().equals(Integer.parseInt(tfID.getText()))) {
                //jeli unesena ispravna kolicina
                if(kolicinaKojaSePosuduje > oprema.getKolicina()) {
                    System.out.println("Pokusaj posudbe vece kolicine od dostupne");
                }
                else {
                    int kolicinaKojuSaljemUPosudenaOpremaTablica = Integer.parseInt(tfKolicina.getText());
                    int kolicinaKojuSaljemUDostupnaOpremaTablica = oprema.getKolicina() - kolicinaKojuSaljemUPosudenaOpremaTablica;

                    Oprema opremaKojaIdeStudentu = new Oprema(oprema.getId(), oprema.getNaziv(), oprema.getDetalji(), oprema.getKolicina(), oprema.getJmbagStudenta());
                    Oprema opremaKojuVracamUDostupnuOpremu = new Oprema(oprema.getId(), oprema.getNaziv(), oprema.getDetalji(), oprema.getKolicina(), oprema.getJmbagStudenta());

                    opremaKojaIdeStudentu.setKolicina(kolicinaKojuSaljemUPosudenaOpremaTablica);
                    opremaKojaIdeStudentu.setJmbagStudenta(Integer.parseInt(lbJMBAG.getText()));
                    opremaKojuVracamUDostupnuOpremu.setKolicina(kolicinaKojuSaljemUDostupnaOpremaTablica);

                    //imam vec taj id
                    if(imamLiVecTajId(oprema.getId())) {
                        //imam vec taj jmbag uz id
                        if(imamLiVecTajJMBAGUzId(oprema.getId(), opremaKojaIdeStudentu.getJmbagStudenta())) {
                            int zbrojenaOpremaPrethodnoPosudenogINovoPosudenog = pronadiKolicinuPosudeneOpreme(oprema.getId(), opremaKojaIdeStudentu.getJmbagStudenta()) + kolicinaKojuSaljemUPosudenaOpremaTablica;
                            String queryPosudeno = "UPDATE posudenaoprema SET kolicina = " + zbrojenaOpremaPrethodnoPosudenogINovoPosudenog + " WHERE id = " + opremaKojaIdeStudentu.getId() + " and JMBAGstudenta = " + opremaKojaIdeStudentu.getJmbagStudenta() + "";
                            String queryPreostalo = "UPDATE oprema SET kolicina = " + opremaKojuVracamUDostupnuOpremu.getKolicina() + " WHERE id = " + opremaKojuVracamUDostupnuOpremu.getId() + "";

                            izvrsiQuery(queryPosudeno);
                            izvrsiQuery(queryPreostalo);
                            pokaziListuPosudeneOpremeStudentu();
                            pokaziListuOpreme();
                            System.out.println("1");
                        }
                        //nemam taj jmbag uz id
                        else {
                            String queryPosudeno = "INSERT INTO posudenaoprema VALUES (" + opremaKojaIdeStudentu.getId() + ",'" + opremaKojaIdeStudentu.getNaziv() + "','"
                                    + opremaKojaIdeStudentu.getDetalji() + "'," + opremaKojaIdeStudentu.getKolicina() + "," + opremaKojaIdeStudentu.getJmbagStudenta() + ")";
                            String queryPreostalo = "UPDATE oprema SET kolicina = " + opremaKojuVracamUDostupnuOpremu.getKolicina() + " WHERE id = " + opremaKojuVracamUDostupnuOpremu.getId() + "";

                            izvrsiQuery(queryPosudeno);
                            izvrsiQuery(queryPreostalo);
                            pokaziListuPosudeneOpremeStudentu();
                            pokaziListuOpreme();
                            System.out.println("2");
                        }
                    }
                    //nemam id sto zanci da nemam studenta s tim jmbagom
                    else {
                        String queryPosudeno = "INSERT INTO posudenaoprema VALUES (" + opremaKojaIdeStudentu.getId() + ",'" + opremaKojaIdeStudentu.getNaziv() + "','"
                                + opremaKojaIdeStudentu.getDetalji() + "'," + opremaKojaIdeStudentu.getKolicina() + "," + opremaKojaIdeStudentu.getJmbagStudenta() + ")";
                        String queryPreostalo = "UPDATE oprema SET kolicina = " + opremaKojuVracamUDostupnuOpremu.getKolicina() + " WHERE id = " + opremaKojuVracamUDostupnuOpremu.getId() + "";

                        izvrsiQuery(queryPosudeno);
                        izvrsiQuery(queryPreostalo);
                        pokaziListuPosudeneOpremeStudentu();
                        pokaziListuOpreme();
                        System.out.println("3");
                    }

                }
            }
            else {
                System.out.println("Ne postoji id");
            }
        }
    }*/
/*
    private void izbrisiPodatke() throws SQLException {
        ObservableList<Oprema> listaPosudeneOpremeStudentu = dohvatiListuPosudeneOpremeStudentu(); //vec dohvaca listu samo s odabranim jmbagom
        int kolicinaKojaSeVraca = Integer.parseInt(tfKolicina.getText());
        int kolicinaDostupneOpreme = pronadiKolicinuDostupneOpreme(Integer.parseInt(tfID.getText()));
        int kolicinaPosudeneOpremeKodStudenta = pronadiKolicinuPosudeneOpreme(Integer.parseInt(tfID.getText()), Integer.parseInt(lbJMBAG.getText()));

        int ukupnaKolicinaDostupneOpremeNakonVracanja = kolicinaKojaSeVraca + kolicinaDostupneOpreme;
        int ukupnaKolicinaOpremeKodStudentaNakonVracanja = kolicinaPosudeneOpremeKodStudenta - kolicinaKojaSeVraca;

        for(Oprema oprema : listaPosudeneOpremeStudentu) {
            //postoji li oprema sa tim id-om
            if(oprema.getId().equals(Integer.parseInt(tfID.getText()))) {
                //jeli unesena ispravna kolicina
                if(kolicinaKojaSeVraca > oprema.getKolicina()) {
                    System.out.println("Pokusaj vracanja vece kolicine od posudene");
                }
                //vraca se sva oprema
                else if(kolicinaKojaSeVraca == oprema.getKolicina()){
                    String queryVraceno = "UPDATE oprema SET kolicina = " + ukupnaKolicinaDostupneOpremeNakonVracanja + " WHERE id = " + oprema.getId() + "";
                    String queryPreostalo = "DELETE from posudenaoprema WHERE id = " + oprema.getId() + " and JMBAGstudenta = " + oprema.getJmbagStudenta() + "";

                    izvrsiQuery(queryVraceno);
                    izvrsiQuery(queryPreostalo);
                    pokaziListuPosudeneOpremeStudentu();
                    pokaziListuOpreme();
                    System.out.println("4");
                }
                else if(kolicinaKojaSeVraca < oprema.getKolicina()) {
                    String queryVraceno = "UPDATE oprema SET kolicina = " + ukupnaKolicinaDostupneOpremeNakonVracanja + " WHERE id = " + oprema.getId() + "";
                    String queryPreostalo = "UPDATE posudenaoprema SET kolicina = " + ukupnaKolicinaOpremeKodStudentaNakonVracanja + " WHERE id = " + oprema.getId() + " and JMBAGstudenta = " + oprema.getJmbagStudenta() + "";

                    izvrsiQuery(queryVraceno);
                    izvrsiQuery(queryPreostalo);
                    pokaziListuPosudeneOpremeStudentu();
                    pokaziListuOpreme();
                    System.out.println("5");
                }
            }
            else {
                System.out.println("Ne postoji id");
            }
        }
    }
/*
    private Boolean imamLiVecTajId(Integer id) {
        boolean imamTajId = false;
        Connection connection = getConnection();
        String query = "SELECT * FROM posudenaoprema";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Oprema oprema;
            while (resultSet.next()){
                oprema = new Oprema(resultSet.getInt("id"), resultSet.getString("naziv"), resultSet.getString("detalji"), resultSet.getInt("kolicina"), resultSet.getInt("JMBAGstudenta"));
                if(oprema.getId().equals(id)) {
                    imamTajId = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imamTajId;
    }*/
/*
    private Boolean imamLiVecTajJMBAGUzId(Integer id, Integer jmbag) throws SQLException {
        boolean imamTajJmbag = false;
        Connection connection = getConnection();
        String query = "SELECT * FROM posudenaoprema";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Oprema oprema;
            while (resultSet.next()){
                oprema = new Oprema(resultSet.getInt("id"), resultSet.getString("naziv"), resultSet.getString("detalji"), resultSet.getInt("kolicina"), resultSet.getInt("JMBAGstudenta"));
                if(oprema.getId().equals(id) && oprema.getJmbagStudenta().equals(jmbag)) {
                    imamTajJmbag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imamTajJmbag;
    }
    private Integer pronadiKolicinuPosudeneOpreme(Integer id, Integer jmbagStudenta) throws SQLException {
        int kolicina;
        Connection connection = getConnection();
        String query = "SELECT kolicina FROM posudenaoprema WHERE id = " + id + " and JMBAGstudenta = " + jmbagStudenta + "";
        Statement statement;
        ResultSet resultSet;

        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        resultSet.next();
        kolicina = resultSet.getInt("kolicina");

        return kolicina;
    }
    private Integer pronadiKolicinuDostupneOpreme(Integer id) throws SQLException {
        int kolicina;
        Connection connection = getConnection();
        String query = "SELECT kolicina FROM oprema where id = " + id + "";
        Statement statement;
        ResultSet resultSet;

        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        resultSet.next();
        kolicina = resultSet.getInt("kolicina");

        return kolicina;
    }
    private void izvrsiQuery(String query) {
        Connection connection = getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (Exception e){
            e.printStackTrace();
        }
    }*/
}
