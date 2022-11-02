package model;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import view.SIGFrame;

public class HeaderTableModel extends AbstractTableModel{
    private String [] columns ={"Num","Customer","Date","Total"};
    private ArrayList<InvoiceHeader>headers;

    public HeaderTableModel(List<InvoiceHeader> headers) {
        this.headers = (ArrayList<InvoiceHeader>) headers;
    }


    public List<InvoiceHeader> getInvoicesArray() {
        return headers;
    }

    @Override
    public boolean isCellEditable(int rowIndex,int columnIndex)
    {
        return rowIndex == 1 && columnIndex == 2;
    }


    @Override
    public int getRowCount() {
        return headers.size();
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
        InvoiceHeader header = headers.get(rowIndex);
        switch(columnIndex)
        {
            case 0:
                return header.getNum();
            case 1:
                return header.getName();
            case 2:
                return SIGFrame.sdf.format(header.getDate());
            case 3:
                return header.getTotal();

        }
        return "";
    }

}
