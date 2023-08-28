package zavrsnirad.posudbaopremeizlabosa.model;

public class Oprema {
    private Integer id;
    private String naziv;
    private String detalji;
    private Integer kolicina;

    private Integer jmbagStudenta;

    public Oprema(Integer id, String naziv, String detalji, Integer kolicina, Integer jmbagStudenta) {
        this.id = id;
        this.naziv = naziv;
        this.detalji = detalji;
        this.kolicina = kolicina;
        this.jmbagStudenta = jmbagStudenta;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getDetalji() {
        return detalji;
    }

    public Integer getKolicina() {
        return kolicina;
    }

    public void setKolicina(Integer kolicina) {
        this.kolicina = kolicina;
    }
    public Integer getId() {
        return id;
    }

    public Integer getJmbagStudenta() {
        return jmbagStudenta;
    }

    public void setJmbagStudenta(Integer jmbagStudenta) {
        this.jmbagStudenta = jmbagStudenta;
    }
}
