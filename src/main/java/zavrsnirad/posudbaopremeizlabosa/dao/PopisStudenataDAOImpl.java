package zavrsnirad.posudbaopremeizlabosa.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import zavrsnirad.posudbaopremeizlabosa.model.Student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PopisStudenataDAOImpl implements StudentDAO{
    @Override
    public void unesiPodatke(Student student) throws SQLException {
        String query = "INSERT INTO studenti VALUES (" + student.getJMBAG() + ",'" + student.getPrezime() + "','"
                + student.getIme() + "','" + student.getStudij() + "','" + student.getEmail() + "','" + student.getTelefon() + "')";
        izvrsiQuery(query);
    }

    @Override
    public void azurirajPodatke(Student student) throws SQLException {
        String query = "UPDATE studenti SET prezime = '" + student.getPrezime() + "', ime = '" +
                student.getIme() + "', studij = '" + student.getStudij() + "', email = '" +
                student.getEmail() + "', telefon = '" + student.getTelefon() + "' WHERE JMBAG = " + student.getJMBAG() + "";
        izvrsiQuery(query);
    }

    @Override
    public void izbrisiPodatke(Student student) throws SQLException {
        String query = "DELETE FROM studenti WHERE JMBAG =" + student.getJMBAG() + "";
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
    public ObservableList<Student> dohvatiListuStudenata() throws SQLException {
        ObservableList<Student> listaStudenata = FXCollections.observableArrayList();
        Connection connection = Database.getConnection();
        String query = "SELECT * FROM studenti";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Student student;
            while (resultSet.next()){
                student = new Student(resultSet.getInt("JMBAG"), resultSet.getString("prezime"), resultSet.getString("ime"), resultSet.getString("studij"), resultSet.getString("email"), resultSet.getString("telefon"));
                listaStudenata.add(student);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaStudenata;
    }
}
