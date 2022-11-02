package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvoiceHeader
{
    private int num;
    private Date date;
    private String name;
    private ArrayList<InvoiceLine> lines;

    public InvoiceHeader(int num, Date date, String name)
    {
        this.num = num;
        this.date = date;
        this.name = name;
    }

    public double getTotal(){
        double total = 0.0;
        for(int i = 0;i<getLines().size();i++){
            InvoiceLine line = getLines().get(i);
            total += line.getTotal();
        }
        return total;
    }

    public ArrayList<InvoiceLine> getLines() {
        if(lines == null)
        {
            lines = new ArrayList<>();
        }
        return lines;
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "InvoiceHeader{" +
                "num=" + num +
                ", date=" + date +
                ", name='" + name + '\'' +
                '}';
    }

    public void addInvLine(InvoiceLine line){
        getLines().add(line);

    }

    public String getDataAsCSV() {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return "" + getNum() + "," + df.format(getDate()) + "," + getName();
    }
}
