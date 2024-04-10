package com.tienda.facturacion;

import com.tienda.carrito.Carrito;
import com.tienda.carrito.Item;
import com.tienda.clientes.Cliente;

import java.time.format.DateTimeFormatter;

public class FacturaCorporativa extends Factura {

    private boolean taxExempt;//esto habria que ver si no lo ponemos en la cuenta para armar un bool si tiene va y sino no
    private float taxTotalDiscount;
    private enum impuestoProvincial{
        BA(0.57f),
        LP(0.37f),
        CO(0.20f),
        SF(0.4f);
        private final float tasa;
        impuestoProvincial(float tasa){
            this.tasa=tasa;
        }
        public float getTasa(){
            return tasa;
        }
    }

    public FacturaCorporativa(Cliente cliente, Carrito carrito, boolean taxExempt) {//impuesto buenos aires para probar y desgloce
        super(cliente, carrito);
        this.taxExempt = taxExempt;
        this.taxTotalDiscount=descuentoImpuestoProv(impuestoProvincial.BA);
        //falta aplicar la logica del subtotal-impuesto y que a eso se le aplique el descuneto pero habria que ver de donde sacamos
    }
    private float descuentoImpuestoProv(impuestoProvincial tasa){
        float aux1;
        aux1=(float)subtotal*tasa.tasa;
        float aux= (float) (subtotal-aux1);
        return aux;
    }


    public String mostrarFacturaCorporativa() {

        try {
            String fechaFormato = this.fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            StringBuilder items = new StringBuilder();
            for (Item item : this.items) {
                items.append(String.format("%-25s|  $%.2f  | %d |  $%.2f\n",
                        item.getProducto().getNombre(), item.getProducto().getPrecio(), item.getCantidad(), item.getSubtotal() ));
            }


            String formato = """
                
                ------------------------------------------------------------
                \t         TIENDA               \t|\tFecha: %s
                \t     DE COMPUTACIÓN             |\tNro. %d
                ------------------------------------------------------------
                \t%s
                \tCLIENTE CORPORATIVO - DNI %s
                ------------------------------------------------------------
                \t     CONCEPTO            |   PRECIO   | U |   SUBT
                ------------------------------------------------------------
                \t%s
                ------------------------------------------------------------
                \t                                      SUBTOTAL $%.2f
                ------------------------------------------------------------
                \t                       DESCUNETO IMPUESTO PROV $%.2f
                ------------------------------------------------------------
                \t                                         TOTAL $%.2f
                ------------------------------------------------------------
                """;

            return String.format(formato, fechaFormato, this.id, this.cliente.getNombre(), this.cliente.getDni(), items,this.subtotal,this.taxTotalDiscount, this.total);//se agrego la funcion
        } catch (Exception e) {
            return null;
        }
    }
}
