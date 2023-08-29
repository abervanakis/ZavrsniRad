package zavrsnirad.posudbaopremeizlabosa.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import zavrsnirad.posudbaopremeizlabosa.model.Oprema;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PopisOpremeDAOImpl implements OpremaDAO{
    @Override
    public void unesiPodatke(Oprema oprema) throws SQLException {
        String query = "INSERT INTO oprema VALUES (" +oprema.getId() + ",'" + oprema.getNaziv() + "','" + oprema.getDetalji() + "',"
                + oprema.getKolicina() + ")";
        izvrsiQuery(query);
    }

    @Override
    public void azurirajPodatke(Oprema oprema) throws SQLException {
        String query = "UPDATE oprema SET naziv = '" + oprema.getNaziv() + "', detalji = '" +
                oprema.getDetalji() + "', kolicina = " + oprema.getKolicina() + " WHERE id = " + oprema.getId() + "";
        izvrsiQuery(query);
    }

    @Override
    public void izbrisiPodatke(Oprema oprema) throws SQLException {
        String query = "DELETE FROM oprema WHERE id =" + oprema.getId() + "";
        izvrsiQuery(query);
    }

    @Override
    public void izvrsiQuery(String query) throws SQLException {
        Connection connection = Database.getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public ObservableList<Oprema> dohvatiListuOpreme() throws SQLException {
        ObservableList<Oprema> listaOpreme = FXCollections.observableArrayList();
        Connection connection = Database.getConnection();
        String query = "SELECT * FROM oprema";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Oprema oprema;
            while (resultSet.next()){
                oprema = new Oprema(resultSet.getInt("id"), resultSet.getString("naziv"), resultSet.getString("detalji"), resultSet.getInt("kolicina"), null);
                listaOpreme.add(oprema);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaOpreme;
    }
    @Override
    public ObservableList<Oprema> dohvatiListuPosudeneOpremeStudentu(String jmbag) throws SQLException {
        ObservableList<Oprema> listaOpreme = FXCollections.observableArrayList();
        Connection connection = Database.getConnection();
        String query = "SELECT * FROM posudenaoprema WHERE JMBAGstudenta = '" + jmbag + "'";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Oprema oprema;
            while (resultSet.next()){
                oprema = new Oprema(resultSet.getInt("id"), resultSet.getString("naziv"), resultSet.getString("detalji"), resultSet.getInt("kolicina"), resultSet.getInt("JMBAGstudenta"));
                listaOpreme.add(oprema);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaOpreme;
    }

    @Override
    public Boolean imamLiVecTajId(Integer id) throws SQLException {
        boolean imamTajId = false;
        Connection connection = Database.getConnection();
        String query = "SELECT * FROM posudenaoprema";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Oprema oprema;
            while (resultSet.next()){
                oprema = new Oprema(resultSet.getInt("id"), resultSet.getString("naziv"), resultSet.getString("detalji"), resultSet.getInt("kolicina"), resultSet.getInt("JMBAGstudenta"));
                if(oprema.getId().equals(id)) {
                    imamTajId = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imamTajId;
    }

    @Override
    public Boolean imamLiVecTajJMBAGUzId(Integer id, Integer jmbag) throws SQLException {
        boolean imamTajJmbag = false;
        Connection connection = Database.getConnection();
        String query = "SELECT * FROM posudenaoprema";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Oprema oprema;
            while (resultSet.next()){
                oprema = new Oprema(resultSet.getInt("id"), resultSet.getString("naziv"), resultSet.getString("detalji"), resultSet.getInt("kolicina"), resultSet.getInt("JMBAGstudenta"));
                if(oprema.getId().equals(id) && oprema.getJmbagStudenta().equals(jmbag)) {
                    imamTajJmbag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imamTajJmbag;
    }

    @Override
    public Integer pronadiKolicinuPosudeneOpreme(Integer id, Integer jmbagStudenta) throws SQLException {
        int kolicina;
        Connection connection = Database.getConnection();
        String query = "SELECT kolicina FROM posudenaoprema WHERE id = " + id + " and JMBAGstudenta = " + jmbagStudenta + "";
        Statement statement;
        ResultSet resultSet;

        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        resultSet.next();
        kolicina = resultSet.getInt("kolicina");

        return kolicina;
    }

    @Override
    public Integer pronadiKolicinuDostupneOpreme(Integer id) throws SQLException {
        int kolicina;
        Connection connection = Database.getConnection();
        String query = "SELECT kolicina FROM oprema where id = " + id + "";
        Statement statement;
        ResultSet resultSet;

        statement = connection.createStatement();
        resultSet = statement.executeQuery(query);
        resultSet.next();
        kolicina = resultSet.getInt("kolicina");

        return kolicina;
    }

    @Override
    public void unesiPodatkeKomplicirano(Integer kolicina, Integer id, Integer jmbag) throws SQLException {
        ObservableList<Oprema> listaDostupneOpreme = dohvatiListuOpreme();
        int kolicinaKojaSePosuduje = kolicina;

        for(Oprema oprema : listaDostupneOpreme) {
            //postoji li oprema sa tim id-om
            if(oprema.getId().equals(id)) {
                //jeli unesena ispravna kolicina
                if(kolicinaKojaSePosuduje > oprema.getKolicina()) {
                    System.out.println("Pokusaj posudbe vece kolicine od dostupne");
                }
                else {
                    int kolicinaKojuSaljemUPosudenaOpremaTablica = kolicina;
                    int kolicinaKojuSaljemUDostupnaOpremaTablica = oprema.getKolicina() - kolicinaKojuSaljemUPosudenaOpremaTablica;

                    Oprema opremaKojaIdeStudentu = new Oprema(oprema.getId(), oprema.getNaziv(), oprema.getDetalji(), oprema.getKolicina(), oprema.getJmbagStudenta());
                    Oprema opremaKojuVracamUDostupnuOpremu = new Oprema(oprema.getId(), oprema.getNaziv(), oprema.getDetalji(), oprema.getKolicina(), oprema.getJmbagStudenta());

                    opremaKojaIdeStudentu.setKolicina(kolicinaKojuSaljemUPosudenaOpremaTablica);
                    opremaKojaIdeStudentu.setJmbagStudenta(jmbag);
                    opremaKojuVracamUDostupnuOpremu.setKolicina(kolicinaKojuSaljemUDostupnaOpremaTablica);

                    //imam vec taj id
                    if(imamLiVecTajId(oprema.getId())) {
                        //imam vec taj jmbag uz id
                        if(imamLiVecTajJMBAGUzId(oprema.getId(), opremaKojaIdeStudentu.getJmbagStudenta())) {
                            int zbrojenaOpremaPrethodnoPosudenogINovoPosudenog = pronadiKolicinuPosudeneOpreme(oprema.getId(), opremaKojaIdeStudentu.getJmbagStudenta()) + kolicinaKojuSaljemUPosudenaOpremaTablica;
                            String queryPosudeno = "UPDATE posudenaoprema SET kolicina = " + zbrojenaOpremaPrethodnoPosudenogINovoPosudenog + " WHERE id = " + opremaKojaIdeStudentu.getId() + " and JMBAGstudenta = " + opremaKojaIdeStudentu.getJmbagStudenta() + "";
                            String queryPreostalo = "UPDATE oprema SET kolicina = " + opremaKojuVracamUDostupnuOpremu.getKolicina() + " WHERE id = " + opremaKojuVracamUDostupnuOpremu.getId() + "";

                            izvrsiQuery(queryPosudeno);
                            izvrsiQuery(queryPreostalo);
                            System.out.println("1");
                        }
                        //nemam taj jmbag uz id
                        else {
                            String queryPosudeno = "INSERT INTO posudenaoprema VALUES (" + opremaKojaIdeStudentu.getId() + ",'" + opremaKojaIdeStudentu.getNaziv() + "','"
                                    + opremaKojaIdeStudentu.getDetalji() + "'," + opremaKojaIdeStudentu.getKolicina() + "," + opremaKojaIdeStudentu.getJmbagStudenta() + ")";
                            String queryPreostalo = "UPDATE oprema SET kolicina = " + opremaKojuVracamUDostupnuOpremu.getKolicina() + " WHERE id = " + opremaKojuVracamUDostupnuOpremu.getId() + "";

                            izvrsiQuery(queryPosudeno);
                            izvrsiQuery(queryPreostalo);
                            System.out.println("2");
                        }
                    }
                    //nemam id sto zanci da nemam studenta s tim jmbagom
                    else {
                        String queryPosudeno = "INSERT INTO posudenaoprema VALUES (" + opremaKojaIdeStudentu.getId() + ",'" + opremaKojaIdeStudentu.getNaziv() + "','"
                                + opremaKojaIdeStudentu.getDetalji() + "'," + opremaKojaIdeStudentu.getKolicina() + "," + opremaKojaIdeStudentu.getJmbagStudenta() + ")";
                        String queryPreostalo = "UPDATE oprema SET kolicina = " + opremaKojuVracamUDostupnuOpremu.getKolicina() + " WHERE id = " + opremaKojuVracamUDostupnuOpremu.getId() + "";

                        izvrsiQuery(queryPosudeno);
                        izvrsiQuery(queryPreostalo);
                        System.out.println("3");
                    }

                }
            }
            else {
                System.out.println("Ne postoji id");
            }
        }
    }

    @Override
    public void izbrisiPodatkeKomplicirano(Integer kolicina, Integer id, Integer jmbag) throws SQLException {
        ObservableList<Oprema> listaPosudeneOpremeStudentu = dohvatiListuPosudeneOpremeStudentu(jmbag.toString()); //vec dohvaca listu samo s odabranim jmbagom
        int kolicinaKojaSeVraca = kolicina;
        int kolicinaDostupneOpreme = pronadiKolicinuDostupneOpreme(id);
        int kolicinaPosudeneOpremeKodStudenta = pronadiKolicinuPosudeneOpreme(id, jmbag);

        int ukupnaKolicinaDostupneOpremeNakonVracanja = kolicinaKojaSeVraca + kolicinaDostupneOpreme;
        int ukupnaKolicinaOpremeKodStudentaNakonVracanja = kolicinaPosudeneOpremeKodStudenta - kolicinaKojaSeVraca;

        for(Oprema oprema : listaPosudeneOpremeStudentu) {
            //postoji li oprema sa tim id-om
            if(oprema.getId().equals(id)) {
                //jeli unesena ispravna kolicina
                if(kolicinaKojaSeVraca > oprema.getKolicina()) {
                    System.out.println("Pokusaj vracanja vece kolicine od posudene");
                }
                //vraca se sva oprema
                else if(kolicinaKojaSeVraca == oprema.getKolicina()){
                    String queryVraceno = "UPDATE oprema SET kolicina = " + ukupnaKolicinaDostupneOpremeNakonVracanja + " WHERE id = " + oprema.getId() + "";
                    String queryPreostalo = "DELETE from posudenaoprema WHERE id = " + oprema.getId() + " and JMBAGstudenta = " + oprema.getJmbagStudenta() + "";

                    izvrsiQuery(queryVraceno);
                    izvrsiQuery(queryPreostalo);
                    System.out.println("4");
                }
                else if(kolicinaKojaSeVraca < oprema.getKolicina()) {
                    String queryVraceno = "UPDATE oprema SET kolicina = " + ukupnaKolicinaDostupneOpremeNakonVracanja + " WHERE id = " + oprema.getId() + "";
                    String queryPreostalo = "UPDATE posudenaoprema SET kolicina = " + ukupnaKolicinaOpremeKodStudentaNakonVracanja + " WHERE id = " + oprema.getId() + " and JMBAGstudenta = " + oprema.getJmbagStudenta() + "";

                    izvrsiQuery(queryVraceno);
                    izvrsiQuery(queryPreostalo);
                    System.out.println("5");
                }
            }
            else {
                System.out.println("Ne postoji id");
            }
        }
    }
}
