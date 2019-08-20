package it.richkmeli.jframework.orm.dataexample.rmc.model;


import it.richkmeli.jframework.orm.DatabaseException;

import java.util.List;

public interface RMCModel {

    public boolean addRMC(RMC client) throws DatabaseException;

    public boolean editRMC(RMC client) throws DatabaseException;

    public boolean removeRMC(String user) throws DatabaseException;

    public boolean removeRMC(RMC client) throws DatabaseException;

    public boolean checkRmcUserPair(RMC client) throws DatabaseException;

    public boolean checkRmc(String rmcID) throws DatabaseException;

    public List<RMC> getRMCs(String user) throws DatabaseException;

    public List<String> getUnassociatedRmcs(String rmcID) throws DatabaseException;

}
