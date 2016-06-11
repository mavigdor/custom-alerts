package com.mycompany.alert;

import org.openspaces.admin.Admin;
import org.openspaces.admin.alert.Alert;
import org.openspaces.admin.alert.AlertFactory;
import org.openspaces.admin.alert.AlertSeverity;
import org.openspaces.admin.alert.AlertStatus;
import org.openspaces.admin.internal.alert.InternalAlertManager;
import org.openspaces.admin.internal.alert.bean.AlertBean;
import org.openspaces.admin.machine.Machine;
import org.openspaces.admin.machine.events.MachineLifecycleEventListener;

import java.util.Map;

/**
 * Created by moran on 6/11/16.
 */
public class MyAlertBean implements AlertBean, MachineLifecycleEventListener {

    private Admin admin;
    private Map<String, String> properties;

    public MyAlertBean() {
        System.out.println("My Alert Bean enabled");
    }

    @Override
    public void setAdmin(Admin admin) {

        this.admin = admin;
    }

    @Override
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public Map<String, String> getProperties() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        admin.getMachines().addLifecycleListener(this);
        int size = admin.getMachines().getSize();
        if (size == 0) {
            raiseAlert();
        }
    }

    @Override
    public void destroy() throws Exception {
        admin.getMachines().removeLifeycleListener(this);
    }

    @Override
    public void machineAdded(Machine machine) {
        int size = admin.getMachines().getSize();
        if (size > 0) {
            resolveAlert(machine);
        }
    }

    private void resolveAlert(Machine machine) {
        Alert[] alertsByGroupUid = ((InternalAlertManager)admin.getAlertManager()).getAlertRepository().getAlertsByGroupUid("localhost");
        if (alertsByGroupUid.length != 0 && ! alertsByGroupUid[0].getStatus().isResolved()) {
            AlertFactory f = new AlertFactory();
            f.name("machine size alert");
            f.description("Machine " + machine.getHostName() + " is available");
            f.groupUid("localhost");
            f.severity(AlertSeverity.WARNING);
            f.status(AlertStatus.RESOLVED);
            admin.getAlertManager().triggerAlert(f.toAlert());
        }
    }

    @Override
    public void machineRemoved(Machine machine) {
        int size = admin.getMachines().getSize();
        if (size == 0) {
            raiseAlert();
        }
    }

    private void raiseAlert() {
        AlertFactory f = new AlertFactory();
        f.name("machine size alert");
        f.description("alerts when no machines are available");
        f.groupUid("localhost");
        f.severity(AlertSeverity.WARNING);
        f.status(AlertStatus.RAISED);
        admin.getAlertManager().triggerAlert(f.toAlert());
    }
}
