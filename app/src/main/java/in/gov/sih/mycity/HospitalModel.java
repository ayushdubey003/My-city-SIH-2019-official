package in.gov.sih.mycity;

public class HospitalModel {

    String name,address;
    float avgrat,num;

    public HospitalModel()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
