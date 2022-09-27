/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.util.Date;

/**
 *
 * @author pc
 */
public class InvoiceLine {
  
   private String name;
   private int count;
   private double price;
   private InvoiceHeader INV;

    public InvoiceLine(String name, int count, double price, InvoiceHeader INV) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.INV = INV;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
    
    public double getTotal(){
        return price*count;
    }

    public InvoiceHeader getINV() {
        return INV;
    }

    public void setINV(InvoiceHeader INV) {
        this.INV = INV;
    }
    public String getCSV() {
        return INV.getNum() + "," + name + "," + price + "," + count;
    }
    @Override
    public String toString() {
        return "InvoiceLine{" + "name=" + name + ", count=" + count + ", price=" + price + '}';
    }
   
   
   
    
    
}
