package com.carcontroller.Retrofit.Model;

/**
 * Created by Hudeya on 7/11/2017.
 */

public class CarroPost {
    private String direccion;


    public CarroPost(String direccion){
        this.direccion=direccion;

    }
    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
