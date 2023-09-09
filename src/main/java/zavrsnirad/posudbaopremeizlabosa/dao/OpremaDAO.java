package zavrsnirad.posudbaopremeizlabosa.dao;

import javafx.collections.ObservableList;
import zavrsnirad.posudbaopremeizlabosa.model.Oprema;

import java.sql.SQLException;

public interface OpremaDAO extends DAO<Oprema>{
    ObservableList<Oprema> dohvatiListuOpreme() throws SQLException;
    ObservableList<Oprema> dohvatiListuPosudeneOpreme() throws SQLException;
    ObservableList<Oprema> dohvatiListuPosudeneOpremeStudentu(String jmbag) throws SQLException;
    Boolean imamLiVecTajId(Integer id) throws SQLException;
    Boolean imamLiVecTajJMBAGUzId(Integer id, Integer jmbag) throws SQLException;
    Integer pronadiKolicinuPosudeneOpreme(Integer id, Integer jmbagStudenta) throws SQLException;
    Integer pronadiKolicinuDostupneOpreme(Integer id) throws SQLException;
    void unesiPodatkeKomplicirano(Integer kolicina, Integer id, Integer jmbag) throws SQLException;
    void izbrisiPodatkeKomplicirano(Integer kolicina, Integer id, Integer jmbag) throws SQLException;


}
