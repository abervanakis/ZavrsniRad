package zavrsnirad.posudbaopremeizlabosa.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import zavrsnirad.posudbaopremeizlabosa.model.Oprema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PopisOpremeDAOImpl implements OpremaDAO{
    @Override
    public void unesiPodatke(Oprema oprema) throws SQLException {
        String query = "INSERT INTO oprema VALUES (" +oprema.getId() + ",'" + oprema.getNaziv() + "','" + oprema.getDetalji() + "',"
                + oprema.getKolicina() + ")";
        izvrsiQuery(query);
    }

    @Override
    public void azurirajPodatke(Oprema oprema) throws SQLException {
        String query = "UPDATE oprema SET naziv = '" + oprema.getNaziv() + "', detalji = '" +
                oprema.getDetalji() + "', kolicina = " + oprema.getKolicina() + " WHERE id = " + oprema.getId() + "";
        izvrsiQuery(query);
    }

    @Override
    public void izbrisiPodatke(Oprema oprema) throws SQLException {
        String query = "DELETE FROM oprema WHERE id =" + oprema.getId() + "";
        izvrsiQuery(query);
    }

    @Override
    public void izvrsiQuery(String query) throws SQLException {
        Connection connection = Database.getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<Oprema> dohvatiListuOpreme() throws SQLException {
        ObservableList<Oprema> listaOpreme = FXCollections.observableArrayList();
        Connection connection = Database.getConnection();
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
}
