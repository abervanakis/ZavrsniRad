module posudbaopremeizlabosa.posudbaopremeizlabosa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.poi.ooxml;


    opens zavrsnirad.posudbaopremeizlabosa to javafx.fxml;
    exports zavrsnirad.posudbaopremeizlabosa;
    exports zavrsnirad.posudbaopremeizlabosa.model;
    opens zavrsnirad.posudbaopremeizlabosa.model to javafx.fxml;
}