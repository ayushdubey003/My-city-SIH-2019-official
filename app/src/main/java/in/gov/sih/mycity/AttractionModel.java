package in.gov.sih.mycity;

public class AttractionModel {

    String name,address, url;
    float avgrat,num;

    public AttractionModel()
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
