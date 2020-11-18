package com.example.knowyourgovernment;


import java.io.Serializable;

public class Official implements Serializable {

    private String city;
    private String state;
    private String zip;
    private String officeName;
    private String officialIndex;
    private String officialName;
    private String address;
    private String party;
    private String phones;
    private String urls;
    private String emails;
    private String photoUrl;
    private String[][] channels;

    public Official(String city, String state, String zip, String officeName, String officialName, String address, String party, String phones, String urls, String emails, String photoUrl, String[][] channels) {
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.officeName = officeName;
        this.officialName = officialName;
        this.address = address;
        this.party = party;
        this.phones = phones;
        this.urls = urls;
        this.emails = emails;
        this.photoUrl = photoUrl;
        this.channels = channels;
    }

    public Official() {

    }

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String name) {
        this.officialName = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String[][] getChannels() {
        return channels;
    }

    public void setChannels(String[][] channels) {
        this.channels = channels;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public String getOfficialIndex() {
        return officialIndex;
    }

    public void setOfficialIndex(String officialIndex) {
        this.officialIndex = officialIndex;
    }
}
