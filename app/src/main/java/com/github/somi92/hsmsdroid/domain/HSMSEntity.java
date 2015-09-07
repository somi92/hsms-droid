package com.github.somi92.hsmsdroid.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by milos on 9/1/15.
 */
public class HSMSEntity {

    @SerializedName("id")
    private String id;
    @SerializedName("desc")
    private String desc;
    @SerializedName("number")
    private String number;
    @SerializedName("price")
    private String price;
    @SerializedName("status")
    private String status;
    @SerializedName("organisation")
    private String organisation;
    @SerializedName("web")
    private String web;
    @SerializedName("priority")
    private String priority;
    @SerializedName("remark")
    private String remark;

    public HSMSEntity() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public HSMSEntity cloneEntity() {
        HSMSEntity clone = new HSMSEntity();
        clone.setId(this.getId());
        clone.setDesc(this.getDesc());
        clone.setNumber(this.getNumber());
        clone.setOrganisation(this.getOrganisation());
        clone.setPrice(this.getPrice());
        clone.setWeb(this.getWeb());
        clone.setStatus(this.getStatus());
        clone.setPriority(this.getPriority());
        clone.setRemark(this.getRemark());
        return clone;
    }
}
