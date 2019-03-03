package in.gov.sih.mycity;

public class PeopleModel {
    String name,desc,url;

    public PeopleModel()
    {

    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
