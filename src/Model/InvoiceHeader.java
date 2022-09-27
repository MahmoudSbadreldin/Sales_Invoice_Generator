/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import View.SalesInvoiceGeneratorFWDFrame;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author pc
 */
public class InvoiceHeader {
    
    private int num;
    private Date date;
    private String Name;
    private ArrayList<InvoiceLine> lines;

    public InvoiceHeader(int num, Date date, String Name) {
        this.num = num;
        this.date = date;
        this.Name = Name;
    }
    public double getTotal(){
        double total=0.0;
        for(int i=0; i<getLines().size(); i++){
            InvoiceLine Line=getLines().get(i);
            total+=Line.getTotal();
          
        }
        return total;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public ArrayList<InvoiceLine> getLines() {
        
        if (lines==null){
          lines= new ArrayList<>();
        }
        return lines;
    }
 public String getCSV() {
        return num + "," + SalesInvoiceGeneratorFWDFrame.sdf.format(date) + "," + Name;
    }
    @Override
    public String toString() {
        return "InvoiceHeader{" + "num=" + num + ", date=" + date + ", Name=" + Name + ", lines=" + lines + '}';
    }
    


        
        
   
}
