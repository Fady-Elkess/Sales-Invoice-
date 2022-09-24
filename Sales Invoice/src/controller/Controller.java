package controller;

import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.stream.Collectors;
import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.HeaderTableModel;
import model.InvoiceHeader;
import model.InvoiceLine;
import model.LineTableModel;
import view.SIGFrame;

public class Controller implements ActionListener, ListSelectionListener {

    private SIGFrame frame;

    public Controller(SIGFrame frame)
    {
        this.frame=frame;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        int rowIndex = frame.getHeaderTable().getSelectedRow();
        if(rowIndex!=1)
        {
            InvoiceHeader header = frame.getInvoices().get(rowIndex);
            frame.setLineTableModel(new LineTableModel(header.getLines()));
            frame.getInvCustLbl().setText(header.getName());
            frame.getInvDateLbl().setText(frame.sdf.format(header.getDate()));
            frame.getInvNumLbl().setText(""+header.getNum());
            frame.getInvTotalLbl().setText(""+header.getTotal());

        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Action Fired");
        String command = e.getActionCommand();
        switch(command){
            case "Create Invoice":
                createInvoice();
                break;

            case "Delete Invoice":
                deleteInvoice();
                break;

            case "Create Item":
                createItem();
                break;

            case "Cancel":
                cancel();
                break;

            case "Load":
                load(null,null);
                break;

            case "Save":
                save();
                break;
        }
    }

    private void createInvoice() {
    }

    private void deleteInvoice() {

    }

    private void createItem() {
    }

    private void cancel() {

    }

    public void load(String headerPath, String linePath) {
        File headerFile = null;
        File lineFile = null;
        if(headerPath == null && linePath == null)
        {
            JFileChooser fc = new JFileChooser();
            int result = fc.showOpenDialog(frame);
            if(result == JFileChooser.APPROVE_OPTION)
            {
                headerFile = fc.getSelectedFile();
                result = fc.showOpenDialog(frame);
                if(result == JFileChooser.APPROVE_OPTION)
                {
                    lineFile = fc.getSelectedFile();
                }
            }
        }
        else
        {
            headerFile = new File(headerPath);
            lineFile = new File(linePath);
        }

        if(headerFile!=null&&lineFile!=null)
        {
            try
            {
                java.util.List<String>headerLines = Files.lines(Paths.get(headerFile.getAbsolutePath())).collect(Collectors.toList());

                java.util.List<String>lineLines = Files.lines(Paths.get(lineFile.getAbsolutePath())).collect(Collectors.toList());
                //frame.getInvoice().clear();
                for(String headerLine: headerLines)
                {
                    String[] parts = headerLine.split(",");
                    String numString = parts[0];
                    String dateString = parts[1];
                    String name = parts[2];
                    int num = Integer.parseInt(numString);
                    Date date = frame.sdf.parse(dateString);
                    InvoiceHeader inv = new InvoiceHeader(num,date,name);
                    frame.getInvoices().add(inv);
                }
                System.out.println("Check Point");
                for(String lineLine: lineLines)
                {
                    String[] parts = lineLine.split(",");
                    int num = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    int count = Integer.parseInt(parts[3]);
                    InvoiceHeader inv = frame.getInvoiceByNum(num);
                    InvoiceLine line = new InvoiceLine(name,count,price,inv);
                    inv.getLines().add(line);
                }
                System.out.println("Check Point");
                frame.setHeaderTableModel(new HeaderTableModel(frame.getInvoices()));
                //frame.getInvoiceTable().setModel(new HeaderTableModel(frame.getInvoice()));
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private void save() {
    }



}
