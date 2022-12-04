package nurgling.bots.actions;

import haven.WItem;
import haven.Window;
import nurgling.NAlias;
import nurgling.NGameUI;
import nurgling.NUtils;

import java.util.ArrayList;

public class TransferToContainer implements Action {
    @Override
    public Results run ( NGameUI gui )
            throws InterruptedException {
        Window spwnd = gui.getWindow ( cap );
        NUtils.waitEvent ( ()-> gui.getInventory ( cap )!=null,500 );
        if ( gui.getInventory ( cap ).getFreeSpace () == 0 ) {
            return new Results ( Results.Types.FULL );
        }
        if ( spwnd != null ) {
            /// Находим все необходимые предметы в инвентаре
            ArrayList<WItem> items;
            items = gui.getInventory ().getItems ( names, q );
            if ( items.isEmpty () ) {
                items = gui.getInventory ().getItemsWithInfo ( names , q , true);
            }
            if(aFreeSpace==-1) {
                /// Переносим предметы в контейнер
                for ( WItem item : items ) {
                    if ( gui.getInventory ( cap ).getFreeSpace () == 0 ) {
                        return new Results ( Results.Types.FULL );
                    }
                    if(gui.getInventory ( cap ).getNumberFreeCoord ( item )>0) {
                        NUtils.transferItem ( gui.getInventory (), item ,gui.getInventory ( cap ));
                        if ( gui.getInventory ( cap ).getFreeSpace () == 0 ) {
                            return new Results ( Results.Types.FULL );
                        }
                    }
                }
            }else{
                int freeSpace  = gui.getInventory ( cap ).getFreeSpace () - aFreeSpace;
                if ( gui.getInventory ( cap ).getFreeSpace ()-aFreeSpace <= 0 ) {
                    return new Results ( Results.Types.FULL );
                }
                for(int i = 0; i <freeSpace; i++){
                    WItem item = gui.getInventory ().getItem ( names, q );
                    if (item!=null) {
                        NUtils.transferItem ( gui.getInventory (), item, gui.getInventory ( cap ));
                        if ( gui.getInventory ( cap ).getFreeSpace () - aFreeSpace <= 0 ) {
                            return new Results ( Results.Types.FULL );
                        }
                    }else
                        return new Results ( Results.Types.SUCCESS );
                }
            }
            return new Results ( Results.Types.SUCCESS );
        }
        return new Results ( Results.Types.FAIL );
    }

    public TransferToContainer(
            NAlias names,
            String cap
    ) {
        this.names = names;
        this.cap = cap;
    }

    public TransferToContainer(

            NAlias names,
            String cap,
            int needed
    ) {
        this.aFreeSpace = needed;
        this.names = names;
        this.cap = cap;
    }

    public TransferToContainer(

            NAlias names,
            String cap,
            int needed,
            double q
    ) {
        this(names,cap,needed);
        this.q = q;
    }


    int aFreeSpace = -1;
    NAlias names;
    String cap;
    boolean isInfo;
    double q;
}
