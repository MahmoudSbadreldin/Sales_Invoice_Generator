/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author pc
 */

import View.SalesInvoiceGeneratorFWDFrame;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author DELL
 */
public class HeaderTableModel extends AbstractTableModel {

    private String[] columns = {"Invoice N0", " Date", "Customer Name", " Total"};
    private ArrayList<InvoiceHeader> invoices;


    
  
    public HeaderTableModel(ArrayList<InvoiceHeader> invoices) {
        this.invoices = invoices;
    }
    
    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
            
        return columns[column];
    }

    @Override
    public int getRowCount() {
        return invoices.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceHeader header = invoices.get(rowIndex);
        switch(columnIndex) {
            case 0:
                return header.getNum();
            case 1:
                return SalesInvoiceGeneratorFWDFrame.sdf.format(header.getDate());
            case 2:
                return header.getName();
            case 3:
                return header.getTotal();
            default:
                return "";
        }
    }
}