package gestion.stock.entity;

public class Client {
    private long id;
    private String fullname;
    private String email;
    private String telephone;
    private String address;
    private String profession;
    private String ice;

    public Client() {
        
    }

    public Client(long id, String fullname, String email, String telephone, String address, String profession, String ice) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.telephone = telephone;
        this.address = address;
        this.profession = profession;
        this.ice = ice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getIce() {
        return ice;
    }

    public void setIce(String ice) {
        this.ice = ice;
    }
}