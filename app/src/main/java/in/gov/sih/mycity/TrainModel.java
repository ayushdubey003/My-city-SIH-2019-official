package in.gov.sih.mycity;

public class TrainModel {
    private String mTrainNumber, mDeparture, mArrival, mTrainName;

    public TrainModel(String tno, String dep, String arr, String tname) {
        mTrainNumber = tno;
        mDeparture = dep;
        mArrival = arr;
        mTrainName = tname;
    }

    public String getmArrival() {
        return mArrival;
    }

    public String getmDeparture() {
        return mDeparture;
    }

    public String getmTrainName() {
        return mTrainName;
    }

    public String getmTrainNumber() {
        return mTrainNumber;
    }
}

