package zavrsnirad.posudbaopremeizlabosa.dao;

import javafx.collections.ObservableList;
import zavrsnirad.posudbaopremeizlabosa.model.Oprema;

import java.sql.SQLException;

public interface OpremaDAO extends DAO<Oprema>{
    public ObservableList<Oprema> dohvatiListuOpreme() throws SQLException;
}
