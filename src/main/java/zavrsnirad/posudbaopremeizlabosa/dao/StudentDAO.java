package zavrsnirad.posudbaopremeizlabosa.dao;

import javafx.collections.ObservableList;
import zavrsnirad.posudbaopremeizlabosa.model.Student;

import java.sql.SQLException;

public interface StudentDAO extends DAO<Student>{
    public ObservableList<Student> dohvatiListuStudenata() throws SQLException;
}
