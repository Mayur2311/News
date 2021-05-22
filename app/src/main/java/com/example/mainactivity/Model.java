package com.example.mainactivity;

import java.util.ArrayList;
import java.util.List;

public class Model {

    String page;
    String per_page;
    String total;
    String total_page;
    public ArrayList<MData> data;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getPer_page() {
        return per_page;
    }

    public void setPer_page(String per_page) {
        this.per_page = per_page;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTotal_page() {
        return total_page;
    }

    public void setTotal_page(String total_page) {
        this.total_page = total_page;
    }

    public List<Model.MData> getData() {
        return data;
    }

    public void setData(ArrayList<Model.MData> data) {
        this.data = data;
    }

    public static class MData {
        String id;
        String email;
        String first_name;
        String last_name;
        String avtar;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirst_name() {
            return first_name;
        }

        public void setFirst_name(String first_name) {
            this.first_name = first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

        public String getAvtar() {
            return avtar;
        }

        public void setAvtar(String avtar) {
            this.avtar = avtar;
        }
    }

}
