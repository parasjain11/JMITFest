package com.jmit.festmanagement.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by arpitkh96 on 29/9/16.
 */

public class Fest implements Parcelable {

    @SerializedName("fest_id")
    @Expose
    private String festId;
    @SerializedName("fest_name")
    @Expose
    private String festName;
    @SerializedName("fest_desc")
    @Expose
    private String festDesc;

    transient boolean expanded=false;

    protected Fest(Parcel in) {
        festId = in.readString();
        festName = in.readString();
        festDesc = in.readString();
    }

    public static final Creator<Fest> CREATOR = new Creator<Fest>() {
        @Override
        public Fest createFromParcel(Parcel in) {
            return new Fest(in);
        }

        @Override
        public Fest[] newArray(int size) {
            return new Fest[size];
        }
    };

    /**
     *
     * @return
     * The festId
     */
    public String getFestId() {
        return festId;
    }

    /**
     *
     * @param festId
     * The fest_id
     */
    public void setFestId(String festId) {
        this.festId = festId;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
    /**
     *
     * @return
     * The festName
     */
    public String getFestName() {
        return festName;
    }

    /**
     *
     * @param festName
     * The fest_name
     */
    public void setFestName(String festName) {
        this.festName = festName;
    }

    /**
     *
     * @return
     * The festDesc
     */
    public String getFestDesc() {
        return festDesc;
    }

    /**
     *
     * @param festDesc
     * The fest_desc
     */
    public void setFestDesc(String festDesc) {
        this.festDesc = festDesc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(festId);
        dest.writeString(festName);
        dest.writeString(festDesc);
    }
}
