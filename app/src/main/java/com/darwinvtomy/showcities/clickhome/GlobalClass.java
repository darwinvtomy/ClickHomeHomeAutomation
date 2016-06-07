package com.darwinvtomy.showcities.clickhome;

import com.darwinvtomy.showcities.clickhome.model.Slave;

/**
 * Created by DARWIN V TOMY on 5/21/2016.
 */
public class GlobalClass {
    private static Slave SELECTED_SLAVE;

    public static void setSelectedSlave(Slave selectedSlave) {

        SELECTED_SLAVE = selectedSlave;
    }

    public static Slave getTheSelectedSlave() {
        return SELECTED_SLAVE;
    }
}
