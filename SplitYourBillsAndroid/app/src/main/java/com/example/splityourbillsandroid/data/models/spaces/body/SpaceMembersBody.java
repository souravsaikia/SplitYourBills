package com.example.splityourbillsandroid.data.models.spaces.body;

import com.google.gson.annotations.SerializedName;

public class SpaceMembersBody {


    @SerializedName("phoneNo")
    private String phoneNo;

    @SerializedName("spaceId")
    private Long spaceId;

    @SerializedName("name")
    private String name;

    public SpaceMembersBody(String phoneNo, Long spaceId, String name) {
        this.phoneNo = phoneNo;
        this.spaceId = spaceId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SpaceMembersBody(String phoneNo, Long spaceId) {
        this.phoneNo = phoneNo;
        this.spaceId = spaceId;
    }


    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Long getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(Long spaceId) {
        this.spaceId = spaceId;
    }

    public SpaceMembersBody() {
    }

}
