package com.mycompany.alert;

import org.openspaces.admin.Admin;
import org.openspaces.admin.alert.Alert;
import org.openspaces.admin.alert.AlertFactory;
import org.openspaces.admin.alert.AlertSeverity;
import org.openspaces.admin.alert.AlertStatus;
import org.openspaces.admin.internal.alert.InternalAlertManager;
import org.openspaces.admin.internal.alert.bean.AlertBean;
import org.openspaces.admin.internal.alert.bean.util.AlertBeanUtils;
import org.openspaces.admin.machine.Machine;
import org.openspaces.admin.machine.events.MachineLifecycleEventListener;

import java.util.Map;

/**
 * Created by moran on 6/11/16.
 * @version 2.0
 * @since 12.0.0
 */
public class MyAlertBean implements AlertBean, MachineLifecycleEventListener {

    private Admin admin;
    private Map<String, String> properties;
    private final String UID = AlertBeanUtils.generateBeanUUID(this.getClass());

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
        if (admin.getAlertManager().getAlertStatusByGroupId(UID).isUnresolved()) {
            AlertFactory f = new AlertFactory(UID, AlertSeverity.WARNING, AlertStatus.RESOLVED);
            f.name("machine size alert");
            f.description("Machine " + machine.getHostName() + " is available");

            admin.getAlertManager().triggerAlert(f.create());
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
        AlertFactory f = new AlertFactory(UID, AlertSeverity.WARNING, AlertStatus.RAISED);
        f.name("machine size alert");
        f.description("alerts when no machines are available");

        admin.getAlertManager().triggerAlert(f.create());
    }
}
