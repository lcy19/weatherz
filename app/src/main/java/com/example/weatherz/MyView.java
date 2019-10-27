package com.example.weatherz;

class MyView {
    private String address;
    private String time;
    private String tmp; // temperature
    private String cond_txt; // weather condition

    MyView(){}
    MyView( String address, String time, String tmp, String cond_txt){
        this.address = address;
        this.time = time;
        this.tmp = tmp;
        this.cond_txt = cond_txt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public String getAddress_time(){
        return this.address + " " + this.time;
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
