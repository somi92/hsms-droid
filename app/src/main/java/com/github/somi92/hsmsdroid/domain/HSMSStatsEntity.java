package com.github.somi92.hsmsdroid.domain;

/**
 * Created by milos on 9/11/15.
 */
public class HSMSStatsEntity {

    private String actionId;
    private String actionDesc;
    private String actionPrice;
    private String actionNumber;
    private int numberOfDonations;

    public HSMSStatsEntity() {}

    public HSMSStatsEntity(String actionDesc, String actionId, String actionPrice, String actionNumber, int numberOfDonations) {
        this.actionDesc = actionDesc;
        this.actionId = actionId;
        this.actionPrice = actionPrice;
        this.actionNumber = actionNumber;
        this.numberOfDonations = numberOfDonations;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getActionDesc() {
        return actionDesc;
    }

    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }

    public String getActionPrice() {
        return actionPrice;
    }

    public void setActionPrice(String actionPrice) {
        this.actionPrice = actionPrice;
    }

    public String getActionNumber() {
        return actionNumber;
    }

    public void setActionNumber(String actionNumber) {
        this.actionNumber = actionNumber;
    }

    public int getNumberOfDonations() {
        return numberOfDonations;
    }

    public void setNumberOfDonations(int numberOfDonations) {
        this.numberOfDonations = numberOfDonations;
    }
}
