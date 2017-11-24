package com.example.nicolas.star1dr;

/**
 * Created by nicolas on 23/11/2017.
 */

public class StarJsonFile {

    private String debut_validite;
    private String fin_validite;
    private String data_url;

    public String getDebutvalidite() {
        return debut_validite;
    }

    public String getFinvalidite() {
        return fin_validite;
    }

    public String getUrl() {
        return data_url;
    }

    public void setDebutvalidite(String debutvalidite) {
        this.debut_validite = debutvalidite;
    }

    public void setFinvalidite(String finvalidite) {
        this.fin_validite = finvalidite;
    }

    public void setUrl(String url) {
        this.data_url = url;
    }



}
