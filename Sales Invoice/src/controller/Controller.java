package controller;

import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.HeaderTableModel;
import model.InvoiceHeader;
import model.InvoiceLine;
import model.LineTableModel;
import view.InvoiceHeaderDialog;
import view.InvoiceLineDialog;
import view.SIGFrame;

public class Controller implements ActionListener, ListSelectionListener {

    private SIGFrame frame;

    private DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    public Controller(SIGFrame frame)
    {
        this.frame=frame;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        System.out.println("Invoice Selected!");
        invoicesTableRowSelected();
    }

    private void invoicesTableRowSelected() {
        int selectedRowIndex = frame.getHeaderTable().getSelectedRow();
        if (selectedRowIndex >= 0){
            InvoiceHeader row = frame.getHeaderTableModel().getInvoicesArray().get(selectedRowIndex);
            frame.getInvCustLbl().setText(row.getName());
            frame.getInvDateLbl().setText(df.format(row.getDate()));
            frame.getInvNumLbl().setText("" + row.getNum());
            frame.getInvTotalLbl().setText("" + row.getTotal());
            ArrayList<InvoiceLine> lines = row.getLines();
            frame.setLineTableModel(new LineTableModel(lines));
            frame.getLineTable().setModel(frame.getLineTableModel());
            frame.getLineTableModel().fireTableDataChanged();

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action Fired");
        String command = e.getActionCommand();
        switch(command){
            case "Create Invoice":
                displayNewInvoiceDialog();
                break;

            case "Create Item":
                displayNewLineDialog();
                break;

            case "Delete Invoice":
                deleteInvoice();
                break;

            case "createLineCancel":
                createLineCancel();
                break;

            case "createLineOK":
                createLineOK();
                break;

            case "Load":
                load();
                break;

            case "Save":
                save();
                break;

            case "createInvCancel":
                createInvCancel();
                break;

            case "createInvOK":
                createInvOK();
                break;

            case "Delete Item":
                deleteLineBtn();
                break;
        }
    }

    private void displayInvoices(){
        for (InvoiceHeader header :frame.getInvoicesArray()) {
            System.out.println(header);
        }
    }
    private void displayNewInvoiceDialog() {
        frame.setHeaderDialog(new InvoiceHeaderDialog(frame));
        frame.getHeaderDialog().setVisible(true);

    }
    private void displayNewLineDialog() {
        frame.setLineDialog(new InvoiceLineDialog(frame));
        frame.getLineDialog().setVisible(true);

    }

    private InvoiceHeader findInvoiceByNum(int invNum){
        InvoiceHeader header = null;
        for(InvoiceHeader inv : frame.getInvoicesArray()) {
            if (invNum == inv.getNum()){
                header = inv;
                break;
            }
        }
        return header ;
    }

    private int getNextInvoiceNum() {
        int max = 0;
        for(InvoiceHeader header : frame.getInvoicesArray()) {
            if (header.getNum()> max) {
                max = header.getNum();

            }
        }
        return max + 1;
    }
    private void createInvCancel() {
        frame.getHeaderDialog().setVisible(false);
        frame.getHeaderDialog().dispose();
        frame.setHeaderDialog(null);
    }

    private void createInvOK() {
        String custName = frame.getHeaderDialog().getCustNameField().getText();
        String invDateStr = frame.getHeaderDialog().getInvDateField().getText();
        frame.getHeaderDialog().setVisible(false);
        frame.getHeaderDialog().dispose();
        frame.setHeaderDialog(null);
        try {
            Date invDate = df.parse(invDateStr);
            int invNum = getNextInvoiceNum();
            InvoiceHeader InvoiceHeader = new InvoiceHeader(invNum, invDate, custName);
            frame.getInvoicesArray().add(InvoiceHeader);
            frame.getHeaderTableModel().fireTableDataChanged();}
        catch (ParseException ex) {
            JOptionPane.showMessageDialog(frame , "Wrong date Format, please adjust it " , "Error" , JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            displayInvoices();
        }
    }

    private void deleteInvoice() {
        int invIndex = frame.getHeaderTable().getSelectedRow();
        InvoiceHeader header = frame.getHeaderTableModel().getInvoicesArray().get(invIndex);
        frame.getHeaderTableModel().getInvoicesArray().remove(invIndex);
        frame.getHeaderTableModel().fireTableDataChanged();
        frame.setLineTableModel(new LineTableModel(new ArrayList<InvoiceLine>()));
        frame.getLineTable().setModel(frame.getLineTableModel());
        frame.getLineTableModel().fireTableDataChanged();
        frame.getInvCustLbl().setText("");
        frame.getInvDateLbl().setText("");
        frame.getInvNumLbl().setText("");
        frame.getInvTotalLbl().setText("");
    }

    private void deleteLineBtn() {

        int lineIndex = frame.getLineTable().getSelectedRow();
        InvoiceLine line = frame.getLineTableModel().getInvoiceLines().get(lineIndex);
        frame.getLineTableModel().getInvoiceLines().remove(lineIndex);
        frame.getHeaderTableModel().fireTableDataChanged();
        frame.getLineTableModel().fireTableDataChanged();
        frame.getInvTotalLbl().setText("" + line.getInv().getTotal());
        JOptionPane.showMessageDialog(null, "Line Deleted Successfully ! ");
        displayInvoices();
    }

    private void createLineCancel() {
        frame.getLineDialog().setVisible(false);
        frame.getLineDialog().dispose();
        frame.setLineDialog(null);
    }

    private void createLineOK() {
        String itemName = frame.getLineDialog().getItemNameField().getText();
        String itemCountStr = frame.getLineDialog().getItemCountField().getText();
        String itemPriceStr = frame.getLineDialog().getItemPriceField().getText();
        frame.getLineDialog().setVisible(false);
        frame.getLineDialog().dispose();
        frame.setLineDialog(null);
        int itemCount = Integer.parseInt(itemCountStr);
        Double itemPrice = Double.parseDouble(itemPriceStr);
        int headerIndex = frame.getHeaderTable().getSelectedRow();
        InvoiceHeader invoice = frame.getHeaderTableModel().getInvoicesArray().get(headerIndex);
        InvoiceLine invoiceLine = new InvoiceLine(itemName, itemCount, itemPrice, invoice);
        invoice.addInvLine(invoiceLine);
        frame.getLineTableModel().fireTableDataChanged();
        frame.getHeaderTableModel().fireTableDataChanged();
        frame.getInvTotalLbl().setText("" + invoice.getTotal());
        displayInvoices();
    }

    public void load() {
        JOptionPane.showMessageDialog(frame, "Please, select header file!", "Attention", JOptionPane.WARNING_MESSAGE);
        JFileChooser openFile = new JFileChooser();
        int result = openFile.showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File headerFile = openFile.getSelectedFile();
            try{
                FileReader headerFr = new FileReader(headerFile);
                BufferedReader headerBr = new BufferedReader (headerFr);
                String headerLine = null;

                while (( headerLine = headerBr.readLine()) != null) {
                    String[] headerParts = headerLine.split(",");
                    String invNumStr = headerParts[0];
                    String invDateStr = headerParts[1];
                    String custName = headerParts[2];

                    int invNum = Integer.parseInt(invNumStr);
                    Date invDate = df.parse(invDateStr);

                    InvoiceHeader inv = new InvoiceHeader(invNum, invDate, custName);
                    frame.getInvoicesArray().add(inv);

                }

                JOptionPane.showMessageDialog(frame, "Please, select lines file!", "Attention", JOptionPane.WARNING_MESSAGE);
                result = openFile.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File linesFile = openFile.getSelectedFile();
                    BufferedReader linesBr= new BufferedReader(new FileReader(linesFile));
                    String linesLine = null;
                    while ((linesLine = linesBr.readLine()) !=null) {
                        String[] lineParts = linesLine.split(",");
                        String invNumStr = lineParts[0];
                        String itemName = lineParts[1];
                        String itemPriceStr = lineParts[3];
                        String itemCountStr = lineParts[2];
                        int invNum = Integer.parseInt(invNumStr);
                        double itemPrice = Double.parseDouble(itemPriceStr);
                        int itemCount = Integer.parseInt(itemCountStr);
                        InvoiceHeader header = findInvoiceByNum(invNum);
                        InvoiceLine invLine = new InvoiceLine(itemName, itemCount, itemPrice, header);
                        header.getLines().add(invLine);
                    }
                    frame.setHeaderTableModel(new HeaderTableModel(frame.getInvoicesArray()));
                    frame.getHeaderTable().setModel(frame.getHeaderTableModel());
                    frame.getHeaderTable().validate();

                }
                System.out.println("Check");
            } catch (ParseException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Date Format Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Number Format Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "File Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Read Error\n" + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        displayInvoices();
    }

    private void save() {
        String headers = "";
        String lines = "";
        for (InvoiceHeader header : frame.getInvoicesArray()) {
            headers += header.getDataAsCSV();
            headers += "\n";
            for (InvoiceLine line : header.getLines()) {
                lines += line.getDataAsCSV();
                lines += "\n";
            }
        }
        JOptionPane.showMessageDialog(frame, "Please, select file to save header data!", "Attention", JOptionPane.WARNING_MESSAGE);
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File headerFile = fileChooser.getSelectedFile();
            try {
                FileWriter hFW = new FileWriter(headerFile);
                hFW.write(headers);
                hFW.flush();
                hFW.close();

                JOptionPane.showMessageDialog(frame, "Please, select file to save lines data!", "Attention", JOptionPane.WARNING_MESSAGE);
                result = fileChooser.showSaveDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File linesFile = fileChooser.getSelectedFile();
                    FileWriter lFW = new FileWriter(linesFile);
                    lFW.write(lines);
                    lFW.flush();
                    lFW.close();
                }
                JOptionPane.showMessageDialog(null, "File Saved Successfully ! ");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



}
