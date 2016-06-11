package com.mycompany.alert;

import org.openspaces.admin.Admin;
import org.openspaces.admin.AdminFactory;

/**
 * Created by moran on 6/11/16.
 */
public class MyMain {

    public static void main(String[] args) {
        Admin admin = new AdminFactory().createAdmin();

        MyAlertConfiguration myAlertConfiguration = new MyAlertConfiguration();
        myAlertConfiguration.setEnabled(true);

        admin.getAlertManager().configure(myAlertConfiguration);

    }
}
