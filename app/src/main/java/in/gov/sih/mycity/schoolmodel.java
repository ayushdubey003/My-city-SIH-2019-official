package in.gov.sih.mycity;

public class schoolmodel {


  String address,name,board,email,website,principal,distate;
  long phone;
  float avgrat,num;

  public schoolmodel()
  {

  }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public float getAvgrat() {
        return avgrat;
    }

    public void setAvgrat(float avgrat) {
        this.avgrat = avgrat;
    }

    public float getNum() {
        return num;
    }

    public void setNum(float num) {
        this.num = num;
    }

    public String getDistate() {
        return distate;
    }

    public void setDistate(String distate) {
        this.distate = distate;
    }
}
