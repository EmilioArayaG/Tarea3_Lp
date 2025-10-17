package objetos;

public class Item {
    private final ItemTipo tipo;
    private int cantidad;

    /*
     * construye una nueva instancia de un item con su tipo y cantidad.
     * @param tipo: itemtipo - el tipo de item a crear.
     * @param cantidad: int - la cantidad inicial del item.
     * @return
     */
    public Item(ItemTipo tipo, int cantidad){
        this.tipo = tipo;
        this.cantidad = cantidad;
    }

    /*
     * devuelve el tipo enumerado del item.
     * @param ninguno
     * @return itemtipo: el tipo del item.
     */
    public ItemTipo tipo(){ return tipo; }

    /*
     * devuelve la cantidad actual de este item.
     * @param ninguno
     * @return int: la cantidad del item.
     */
    public int cantidad(){ return cantidad; }

    /*
     * establece una nueva cantidad para este item.
     * @param c: int - la nueva cantidad.
     * @return void
     */
    public void setCantidad(int c){ this.cantidad = c; }
}


