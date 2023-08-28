package zavrsnirad.posudbaopremeizlabosa.dao;

import java.sql.SQLException;

public interface DAO<T> {

    public void unesiPodatke(T t) throws SQLException;

    public void azurirajPodatke(T t) throws SQLException;

    public void izbrisiPodatke(T t) throws SQLException;

    public void izvrsiQuery(String query) throws SQLException;

}
