package orm.dataexample.rmc;


import crypto.orm.dataexample.rmc.model.RMC;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.orm.DatabaseManager;
import crypto.orm.dataexample.rmc.model.RMCModel;

import java.util.ArrayList;
import java.util.List;

public class RMCDatabaseManager extends DatabaseManager implements RMCModel {

    public RMCDatabaseManager(String database) throws DatabaseException {
        schemaName = "AuthSchema";
        tableName = schemaName + "." + "rmc";
        table = "(" +
                "associatedUser VARCHAR(50)," +
                "rmcId VARCHAR(68) NOT NULL," +
                "PRIMARY KEY (associatedUser, rmcId)," +
                "FOREIGN KEY (associatedUser) REFERENCES AuthSchema.auth(email) ON DELETE CASCADE" +
                ")";

        init(database);
    }

    @Override
    public boolean addRMC(RMC client) throws DatabaseException {
        return create(client);
    }

    @Override
    public boolean editRMC(RMC client) throws DatabaseException {
        return update(client);
    }

    @Override
    public boolean removeRMC(String id) throws DatabaseException {
        return delete(new RMC(null, id));
    }

    @Override
    public boolean removeRMC(RMC client) throws DatabaseException {
        return delete(client);
    }

    @Override
    public boolean checkRmcUserPair(RMC client) throws DatabaseException {
        return read(client) != null;
    }

    @Override
    public boolean checkRmc(String rmcID) throws DatabaseException {
        return read(new RMC(null, rmcID)) != null;
    }


    public List<RMC> getRMCs() throws DatabaseException {
        return getRMCs("");
    }

    @Override
    public List<RMC> getRMCs(String user) throws DatabaseException {
        List<RMC> rmcs = readAll(RMC.class);
        if (rmcs != null) {
            // filter user rmcs
            List<RMC> userRmcs = new ArrayList<>();
            for (RMC rmc : rmcs) {
                if (rmc.getAssociatedUser().equalsIgnoreCase(user)) {
                    userRmcs.add(rmc);
                }
            }
            return userRmcs;
        } else {
            return null;
        }
    }

    @Override
    public List<RMC> getAllRMCs() throws DatabaseException {
        return readAll(RMC.class);
    }

    @Override
    public List<String> getUnassociatedRmcs(String rmcID) throws DatabaseException {
        List<RMC> rmcs = getRMCs("");
        List<String> user = new ArrayList<>();
        for (RMC rmc : rmcs) {
            user.add(rmc.getAssociatedUser());
        }
        return user;
    }

}