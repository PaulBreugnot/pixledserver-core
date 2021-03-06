package com.pixled.pixledserver.core.group;

import com.pixled.pixledserver.core.ToggleState;
import com.pixled.pixledserver.core.device.base.Device;
import com.pixled.pixledserver.core.device.base.DeviceDto;
import com.pixled.pixledserver.core.state.deviceGroup.DeviceGroupState;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
public class DeviceGroup {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "device_group_composition",
            joinColumns = { @JoinColumn(name = "group_id") },
            inverseJoinColumns = { @JoinColumn(name = "device_id") }
    )
    private List<Device> devices;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private DeviceGroupState deviceGroupState;

    public DeviceGroup() {
        devices = new ArrayList<>();
        deviceGroupState = new DeviceGroupState();
    }

    public DeviceGroup(String name) {
        this();
        this.name = name;
    }

    public DeviceGroup(DeviceGroupDto deviceGroupDto) {
        devices = new ArrayList<>();
        id = deviceGroupDto.getId();
        name = deviceGroupDto.getName();
        deviceGroupState = new DeviceGroupState(deviceGroupDto.getState());
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    public DeviceGroupState getDeviceGroupState() {
        return deviceGroupState;
    }

    public void setDeviceGroupState(DeviceGroupState deviceGroupState) {
        this.deviceGroupState = deviceGroupState;
    }

    public void updateStatus() {
        ToggleState toggleState = ToggleState.OFF;
        int i = 0;
        while (toggleState == ToggleState.OFF && i < devices.size()) {
            if (devices.get(i).getDeviceState().getToggleState() == ToggleState.ON) {
                toggleState = ToggleState.ON;
            }
            i++;
        }
        deviceGroupState.setToggleState(toggleState);
    }

    public void switchGroup() {
        deviceGroupState.setToggleState(deviceGroupState.getToggleState() == ToggleState.ON ? ToggleState.OFF : ToggleState.ON);
        for (Device device : devices) {
            device.getDeviceState().setToggleState(deviceGroupState.getToggleState());
        }
        for (Device device : devices) {
            for (DeviceGroup deviceGroup : device.getDeviceGroups()) {
                deviceGroup.updateStatus();
            }
        }
    }
}
