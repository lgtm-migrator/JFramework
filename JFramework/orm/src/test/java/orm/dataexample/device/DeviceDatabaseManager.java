package orm.dataexample.device;

import crypto.orm.dataexample.device.model.DeviceModel;
import it.richkmeli.jframework.orm.DatabaseException;
import it.richkmeli.jframework.orm.DatabaseManager;
import crypto.orm.dataexample.device.model.Device;

import java.util.ArrayList;
import java.util.List;

public class DeviceDatabaseManager extends DatabaseManager implements DeviceModel {

    public DeviceDatabaseManager(String database) throws DatabaseException {
        schemaName = "AuthSchema";
        tableName = schemaName + "." + "device";
        table = "(" +
                "name VARCHAR(50) NOT NULL," +
                "ip VARCHAR(25) NOT NULL," +
                "serverPort VARCHAR(10)," +
                "lastConnection VARCHAR(25)," +
                "encryptionKey VARCHAR(32)," +
                "associatedUser VARCHAR(50)," +
                "commands " + ("mysql".equalsIgnoreCase(dbtype) ? "TEXT" : "VARCHAR(1000)") + "," +
                "commandsOutput " + ("mysql".equalsIgnoreCase(dbtype) ? "TEXT" : "VARCHAR(1000)") + "," +
                "PRIMARY KEY (name)," +
                "FOREIGN KEY (associatedUser) REFERENCES AuthSchema.auth(email) ON DELETE SET NULL" +
                ")";

        init(database);

    }


    public List<Device> getAllDevices() throws DatabaseException {
        return getUserDevices(null);
    }


    // TODO ORM aggiungi foreign keys
    public List<Device> getUserDevices(String user) throws DatabaseException {
        List<Device> devices = readAll(Device.class);
        if (devices != null) {
            if (user != null) {
                // filter user devices
                List<Device> userDevices = new ArrayList<>();
                for (Device device : devices) {
                    if (device.getAssociatedUser().equalsIgnoreCase(user)) {
                        userDevices.add(device);
                    }
                }
                return userDevices;
            } else {
                // return all devices
                return devices;
            }
        } else {
            return null;
        }
    }

    /*public boolean addDevice(Device device) throws DatabaseException {
        return add(device);
    }*/

    public boolean addDevice(Device device) throws DatabaseException {
        return create(device);
    }

    public boolean editDevice(Device device) throws DatabaseException {
        return update(device);
    }

    public Device getDevice(String name) throws DatabaseException {
        return read(new Device(name, null, null, null, null, null, null, null));
    }


    public boolean removeDevice(String name) throws DatabaseException {
        return delete(new Device(name, null, null, null, null, null, null, null));
    }

    public String getEncryptionKey(String name) throws DatabaseException {
        Device device = getDevice(name);
        if (device != null) {
            return device.getEncryptionKey();
        } else {
            return null;
        }
    }

    public boolean editCommands(String deviceName, String commands) throws DatabaseException {
        return update(new Device(deviceName, null, null, null, null, null, commands, null));
    }

    public String getCommands(String deviceName) throws DatabaseException {
        Device device = getDevice(deviceName);
        if (device != null) {
            return device.getCommands();
        } else {
            return null;
        }
    }

    public boolean setCommandsOutput(String deviceName, String commandsOutput) throws DatabaseException {
        return update(new Device(deviceName, null, null, null, null, null, null, commandsOutput));
    }

    public String getCommandsOutput(String deviceName) throws DatabaseException {
        Device device = getDevice(deviceName);
        if (device != null) {
            return device.getCommandsOutput();
        } else {
            return null;
        }
    }

}