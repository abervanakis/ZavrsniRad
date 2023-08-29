package zavrsnirad.posudbaopremeizlabosa;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import zavrsnirad.posudbaopremeizlabosa.dao.Database;
import zavrsnirad.posudbaopremeizlabosa.model.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Excel {

    public void unesiPodatkeIzExcela() throws IOException, SQLException {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        FileInputStream fileInputStream = new FileInputStream(file);

        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row = null;

        int i = 1;
        while ((row = sheet.getRow(i)) != null) {
            String jmbag = row.getCell(2).getStringCellValue();
            String prezime = row.getCell(3).getStringCellValue();
            String ime = row.getCell(4).getStringCellValue();
            String studij = row.getCell(5).getStringCellValue();
            String email = row.getCell(6).getStringCellValue();

            String telefon;
            if(row.getCell(7).getStringCellValue().isEmpty()) {
                telefon = "";
            }
            else {
                telefon = row.getCell(7).getStringCellValue();
            }
            Student student = new Student(Integer.parseInt(jmbag), prezime, ime, studij, email, telefon);

            String query = "INSERT INTO studenti VALUES (" + student.getJMBAG() + ",'" + student.getPrezime() + "','"
                    + student.getIme() + "','" + student.getStudij() + "','" + student.getEmail() + "','" + student.getTelefon() + "')";
            izvrsiQuery(query);
            i++;
        }
    }
    private void izvrsiQuery(String query) throws SQLException {
        Connection connection = Database.getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
