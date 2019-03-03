package in.gov.sih.mycity;

public class HotelModel {

    String name,desc,imgurl;
    float avgrat,num;
    int amnt;

    public HotelModel()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
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


    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public void setAmnt(int amnt) {
        this.amnt = amnt;
    }

    public int getAmnt() {
        return amnt;
    }
}
