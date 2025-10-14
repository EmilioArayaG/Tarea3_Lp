package objetos;

public class Item {
    private final ItemTipo tipo;
    private int cantidad;

    public Item(ItemTipo tipo, int cantidad){
        this.tipo = tipo;
        this.cantidad = cantidad;
    }

    public ItemTipo getTipo(){ return tipo; }
    public int getCantidad(){ return cantidad; }
    public void setCantidad(int c){ this.cantidad = c; }
}

