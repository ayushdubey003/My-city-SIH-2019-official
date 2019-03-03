package in.gov.sih.mycity;

public class Collegemodel {


    String district,name,state,uni_name,uni_type;
    float avgrat,num;

    public Collegemodel()
    {

    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setUni_name(String uni_name) {
        this.uni_name = uni_name;
    }

    public void setUni_type(String uni_type) {
        this.uni_type = uni_type;
    }

    public void setAvgrat(float avgrat) {
        this.avgrat = avgrat;
    }

    public void setNum(float num) {
        this.num = num;
    }

    public String getDistrict() {
        return district;
    }

    public String getName() {
        return name;
    }

    public String getState() {
        return state;
    }

    public String getUni_name() {
        return uni_name;
    }

    public String getUni_type() {
        return uni_type;
    }

    public float getAvgrat() {
        return avgrat;
    }

    public float getNum() {
        return num;
    }
}
