package com.ueniweb.techsuperficial.nearbyme.model;

import java.util.ArrayList;

public class ServerResponse<T> {
   // private ArrayList html_attributions;
    private String next_page_token;
    private ArrayList<T> results;

  /*  public ArrayList getHtml_attributions() {
        return html_attributions;
    }

    public void setHtml_attributions(ArrayList html_attributions) {
        this.html_attributions = html_attributions;
    }
*/
    public String getNext_page_token() {
        return next_page_token;
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    public ArrayList<T> getResults() {
        return results;
    }

    public void setResults(ArrayList<T> results) {
        this.results = results;
    }
}
