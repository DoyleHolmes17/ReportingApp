package com.simap.dishub.far.entity;

/**
 * Created by Aviroez on 20/08/2015.
 */
public class Category {
    private String status;
    private String message;
    private String id_category;
    private String category_infra;
    private String sub_category_infra;

    public String getSub_category_infra() {
        return sub_category_infra;
    }

    public void setSub_category_infra(String sub_category_infra) {
        this.sub_category_infra = sub_category_infra;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId_category() {
        return id_category;
    }

    public void setId_category(String id_category) {
        this.id_category = id_category;
    }

    public String getCategory_infra() {
        return category_infra;
    }

    public void setCategory_infra(String category_infra) {
        this.category_infra = category_infra;
    }

    @Override
    public String toString() {
        return sub_category_infra;
    }

    public Category() {

    }
}