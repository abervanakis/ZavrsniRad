package zavrsnirad.posudbaopremeizlabosa.dao;

import javafx.collections.ObservableList;
import zavrsnirad.posudbaopremeizlabosa.model.Oprema;

import java.sql.SQLException;

public interface ProfilStudentaDAO extends OpremaDAO{
    public ObservableList<Oprema> dohvatiListuPosudeneOpremeStudentu(String jmbag) throws SQLException;
    public Boolean imamLiVecTajId(Integer id) throws SQLException;
    public Boolean imamLiVecTajJMBAGUzId(Integer id, Integer jmbag) throws SQLException;
    public Integer pronadiKolicinuPosudeneOpreme(Integer id, Integer jmbagStudenta) throws SQLException;
    public Integer pronadiKolicinuDostupneOpreme(Integer id) throws SQLException;

    public void unesiPodatkeKomplicirano(Integer kolicina, Integer id, Integer jmbag) throws SQLException;
    public void izbrisiPodatkeKomplicirano(Integer kolicina, Integer id, Integer jmbag) throws SQLException;
}
