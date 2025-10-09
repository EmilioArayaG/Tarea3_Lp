package objetos;

public class Item {
    private final ItemTipo tipo;
    private final int cantidad;

    public Item(ItemTipo tipo, int cantidad){
        if (cantidad <= 0) throw new IllegalArgumentException("cantidad <= 0");
        this.tipo = tipo;
        this.cantidad = cantidad;
    }
    public ItemTipo tipo(){return tipo; }
    public int cantidad(){return cantidad; }

    @Override public String toString(){return  tipo + " x" + cantidad; }
}