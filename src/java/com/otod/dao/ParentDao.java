/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.otod.dao;

import com.otod.db.Connector;

public class ParentDao {

    private Connector connector;

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }
}
