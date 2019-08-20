package it.richkmeli.jframework.orm.dataexample.device.model;

import it.richkmeli.jframework.orm.DatabaseException;

import java.util.List;

public interface DeviceModel {

    public List<Device> refreshDevice() throws DatabaseException, DatabaseException;

    public List<Device> refreshDevice(String user) throws DatabaseException;

    public boolean addDevice(Device device) throws DatabaseException;

    public boolean editDevice(Device device) throws DatabaseException;

    public boolean removeDevice(String string) throws DatabaseException;

    public Device getDevice(String name) throws DatabaseException;

    public String getEncryptionKey(String name) throws DatabaseException;
}
