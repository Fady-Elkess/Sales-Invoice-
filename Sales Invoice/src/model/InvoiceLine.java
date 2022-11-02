package model;

public class InvoiceLine
{
    private String name;
    private int count;
    private double price;
    InvoiceHeader inv;

    public InvoiceLine(String name, int count, double price, InvoiceHeader inv)
    {
        this.name = name;
        this.count = count;
        this.price = price;
        this.inv = inv;
    }

    public double getTotal(){
        return count*price;
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

    public InvoiceHeader getInv() {
        return inv;
    }

    public void setInv(InvoiceHeader inv) {
        this.inv = inv;
    }

    @Override
    public String toString() {
        return "InvoiceLine{" +
                "name='" + name + '\'' +
                ", count=" + count +
                ", price=" + price +
                '}';
    }

    public String getDataAsCSV() {
        return "" + getInv().getNum() + "," + getName() + "," + getPrice() + "," + getTotal();
    }
}

