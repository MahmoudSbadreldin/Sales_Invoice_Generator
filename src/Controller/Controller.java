/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.HeaderTableModel;
import Model.InvoiceHeader;
import Model.InvoiceLine;
import Model.LineTableModel;
import View.InvHeaderDialog;
import View.InvLineDialog;
import View.SalesInvoiceGeneratorFWDFrame;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.io.FileWriter;

/**
 *
 * @author pc
 */
public class Controller implements ActionListener,ListSelectionListener {
    
private SalesInvoiceGeneratorFWDFrame frame;
private InvHeaderDialog headerDialog;
private InvLineDialog lineDialog;

public Controller (SalesInvoiceGeneratorFWDFrame frame){
    
  this.frame=frame;
}

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int rowIndex=frame.getHeaderTable().getSelectedRow();
                if (rowIndex != -1) {
            InvoiceHeader header = frame.getInvoices().get(rowIndex);
            frame.getInvNoLbl().setText("" + header.getNum());
            frame.getInvDataLbl().setText(SalesInvoiceGeneratorFWDFrame.sdf.format(header.getDate()));
            frame.getCustomerNameLbl().setText(header.getName());
            frame.getInvTotalLbl().setText("" + header.getTotal());
            ArrayList<InvoiceLine> lines = header.getLines();
            frame.setLineTableModel(new LineTableModel(header.getLines()));
        } else {
            frame.getInvNoLbl().setText("");
            frame.getInvDataLbl().setText("");
            frame.getCustomerNameLbl().setText("");
            frame.getInvTotalLbl().setText("");
            frame.setLineTableModel(new LineTableModel());
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
       System.out.println("Action Fired");
       String command=e.getActionCommand();
       switch(command){
            case "Create New Invoice":
                createInvoice();
                break;
            case "Delete Invoice":
                deleteInvoice();
                break;
            case "Create Item":
                createItem();
                break;
            case "Delete Item":
                deleteItem();
                break;
            case "Load":
                load(null,null);
                break;
            case "Save":
                save();
               break;
            case "newInvoiceOK":
                newInvoiceOK();
                break;
            case "newInvoiceCancel":
                newInvoiceCancel();
                break;
            case "newLineOK":
                newLineOK();
                break;
            case "newLineCancel":
                newLineCancel();
                break;
            
    }
    }

    private void createInvoice() {
        headerDialog = new InvHeaderDialog(frame);
        headerDialog.setLocation(300, 300);
        headerDialog.setVisible(true);
    }

    private void deleteInvoice() {
        int selectedRow = frame.getHeaderTable().getSelectedRow();
        if (selectedRow != -1) {
            frame.getInvoices().remove(selectedRow);
            frame.getHeaderTableModel().fireTableDataChanged();
        }
    }

    private void createItem() {
            if (frame.getHeaderTable().getSelectedRow() > -1) {
            lineDialog = new InvLineDialog(frame);
            lineDialog.setLocation(300, 300);
            lineDialog.setVisible(true);
        }
    } 

    private void deleteItem() {
        int selectedInvoice = frame.getHeaderTable().getSelectedRow();
        int selectedItem = frame.getLineTable().getSelectedRow();

        if (selectedInvoice != -1 && selectedItem != -1) {
            frame.getInvoices().get(selectedInvoice).getLines().remove(selectedItem);
            frame.getLineTableModel().fireTableDataChanged();
            frame.getHeaderTableModel().fireTableDataChanged();
            frame.getHeaderTable().setRowSelectionInterval(selectedInvoice, selectedInvoice);
        }
    }

    private void save() {
        JFileChooser fc = new JFileChooser();
        File headerFile = null;
        File lineFile = null;
        int result = fc.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            headerFile = fc.getSelectedFile();
            result = fc.showSaveDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                lineFile = fc.getSelectedFile();
            }
        }
        
        if (headerFile != null && lineFile != null) {
            String headerData = "";
            String lineData = "";
            for (InvoiceHeader inv : frame.getInvoices()) {
                headerData += inv.getCSV();
                headerData += "\n";
                for (InvoiceLine line : inv.getLines()) {
                    lineData += line.getCSV();
                    lineData += "\n";
                }
            }
            try {
                FileWriter headerFW = new FileWriter(headerFile);
                FileWriter lineFW = new FileWriter(lineFile);
                headerFW.write(headerData);
                lineFW.write(lineData);
                headerFW.flush();
                lineFW.flush();
                headerFW.close();
                lineFW.close();
                
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(frame, JOptionPane.ERROR_MESSAGE);
            }
            
        }
    }

    public void load(String headerPath, String linePath) {
        File headerFile = null;
        File lineFile = null;
        if (headerPath == null && linePath == null) {
            JFileChooser fc = new JFileChooser();
            int result = fc.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                headerFile = fc.getSelectedFile();
                result = fc.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    lineFile = fc.getSelectedFile();
                }
            }
        } else {
            headerFile = new File(headerPath);
            lineFile = new File(linePath);
        }

        if (headerFile != null && lineFile != null) {
            try {
             
               List <String> headerLines = Files.lines(Paths.get(headerFile.getAbsolutePath())).collect(Collectors.toList());
               List<String> lineLines = Files.lines(Paths.get(lineFile.getAbsolutePath())).collect(Collectors.toList());
                frame.getInvoices().clear();
                for (String headerLine : headerLines) {
                    String[] parts = headerLine.split(","); 
                    String numString = parts[0];
                    String dateString = parts[1];
                    String name = parts[2];
                    int num = Integer.parseInt(numString);
                    Date date = SalesInvoiceGeneratorFWDFrame.sdf.parse(dateString);
                    InvoiceHeader inv = new InvoiceHeader(num, date,name );
                    frame.getInvoices().add(inv);
                }
               
                for (String lineLine : lineLines) {
                    String[] parts = lineLine.split(",");
                    int num = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    int count = Integer.parseInt(parts[3]);
                    InvoiceHeader INV = frame.getInvoiceByNum(num);
                    InvoiceLine line = new InvoiceLine(name, count ,price , INV);
                    INV.getLines().add(line);
                }
                frame.setHeaderTableModel(new HeaderTableModel(frame.getInvoices()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void newInvoiceOK() {
        String name = headerDialog.getCustNameField().getText();
        String dateStr = headerDialog.getInvDateField().getText();
        newInvoiceCancel();
        try {
            Date date = frame.sdf.parse(dateStr);
            InvoiceHeader inv = new InvoiceHeader(frame.getNxInvNum(),date , name);
            frame.getInvoices().add(inv);
            frame.getHeaderTableModel().fireTableDataChanged();
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(frame, "Error in Data Format","ERROR",   JOptionPane.ERROR_MESSAGE);
        }
    }

    private void newInvoiceCancel() {
        headerDialog.setVisible(false);
        headerDialog.dispose();
        headerDialog = null;
    }

    private void newLineOK() {
        String name = lineDialog.getItemNameField().getText();
        String countStr = lineDialog.getItemCountField().getText();
        String priceStr = lineDialog.getItemPriceField().getText();
        newLineCancel();
        try {
            int count = Integer.parseInt(countStr);
            double price = Double.parseDouble(priceStr);
            int currentInv = frame.getHeaderTable().getSelectedRow();
            InvoiceHeader inv = frame.getInvoices().get(currentInv);
            InvoiceLine line = new InvoiceLine(name,count ,price , inv);
            inv.getLines().add(line);
            frame.getHeaderTableModel().fireTableDataChanged();
            frame.getHeaderTable().setRowSelectionInterval(currentInv, currentInv);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, JOptionPane.ERROR_MESSAGE);
        }
    }

    private void newLineCancel() {
        lineDialog.setVisible(false);
        lineDialog.dispose();
        lineDialog = null;
    }


   
       
       
    }
    
    
    
    
    
    
    
    
    

    