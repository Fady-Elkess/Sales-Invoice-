package model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import view.SIGFrame;

public class LineTableModel extends AbstractTableModel {
    private String[] columns={"Item","Unit Price","Count","Total"};
    private ArrayList<InvoiceLine>lines;

    public LineTableModel(ArrayList<InvoiceLine>lines) {
        this.lines = lines;
    }

    public ArrayList<InvoiceLine> getInvoiceLines() {
        return lines;
    }
    @Override
    public int getRowCount() {
        return lines.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }
    @Override
    public String getColumnName(int column){
        if(column ==0)
        {
            return columns[0];
        }
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InvoiceLine line = lines.get(rowIndex);
        switch(columnIndex)
        {
            case 0:
                return line.getName();
            case 1:
                return line.getPrice();
            case 2:
                return line.getCount();
            case 3:
                return line.getTotal();

        }
        return "";
    }

}
