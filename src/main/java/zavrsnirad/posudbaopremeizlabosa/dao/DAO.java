package zavrsnirad.posudbaopremeizlabosa.dao;

import java.sql.SQLException;

public interface DAO<T> {

    void unesiPodatke(T t) throws SQLException;

    void azurirajPodatke(T t) throws SQLException;

    void izbrisiPodatke(T t) throws SQLException;

    void izvrsiQuery(String query) throws SQLException;

}
