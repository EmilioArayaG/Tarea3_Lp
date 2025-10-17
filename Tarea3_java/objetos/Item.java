package objetos;

public class Item {
    private final ItemTipo tipo;
    private int cantidad;

    /**
     * crea un item
     * @param tipo tipo de item
     * @param cantidad cantidad inicial
     */
    public Item(ItemTipo tipo, int cantidad){
        this.tipo = tipo;
        this.cantidad = cantidad;
    }

    /** @return tipo del item */
    public ItemTipo tipo(){ return tipo; }

    /** @return cantidad del item */
    public int cantidad(){ return cantidad; }

    /**
     * fija cantidad
     * @param c nueva cantidad
     */
    public void setCantidad(int c){ this.cantidad = c; }
}


