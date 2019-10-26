package com.example.weatherz;

class MyView {
    private String address_time;
    private String tmp; // temperature
    private String cond_txt; // weather condition

    MyView(){}
    MyView( String address_time, String tmp, String cond_txt){
        this.address_time = address_time;
        this.tmp = tmp;
        this.cond_txt = cond_txt;
    }

    public String getAddress_time() {
        return address_time;
    }

    public void setAddress_time(String address_time) {
        this.address_time = address_time;
    }

    public String getTmp() {
        return tmp;
    }

    public void setTmp(String tmp) {
        this.tmp = tmp;
    }

    public String getCond_txt() {
        return cond_txt;
    }

    public void setCond_txt(String cond_txt) {
        this.cond_txt = cond_txt;
    }
}
