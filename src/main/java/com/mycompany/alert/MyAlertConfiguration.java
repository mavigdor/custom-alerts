package com.mycompany.alert;

import org.openspaces.admin.alert.config.AlertConfiguration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by moran on 6/11/16.
 */
public class MyAlertConfiguration implements AlertConfiguration {
    private boolean enabled;
    private Map<String, String> properties = new HashMap<String, String>();

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String getBeanClassName() {
        return MyAlertBean.class.getName();
    }

    @Override
    public void setProperties(Map<String, String> properties) {
        this.properties = properties;
    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }
}
