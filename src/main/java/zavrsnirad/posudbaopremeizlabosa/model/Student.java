package zavrsnirad.posudbaopremeizlabosa.model;

public class Student {
    private Integer JMBAG;
    private String prezime;
    private String ime;
    private String studij;
    private String email;
    private String telefon;

    public Student(Integer JMBAG, String prezime, String ime, String studij, String email, String telefon) {
        this.JMBAG = JMBAG;
        this.prezime = prezime;
        this.ime = ime;
        this.studij = studij;
        this.email = email;
        this.telefon = telefon;
    }

    public Integer getJMBAG() {
        return JMBAG;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getIme() {
        return ime;
    }

    public String getStudij() {
        return studij;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefon() {
        return telefon;
    }
}
