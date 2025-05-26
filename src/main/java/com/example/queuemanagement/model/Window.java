package com.example.queuemanagement.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Window {
    private int id;
    private Set<ServiceType> supportedServices;
    private List<Client> queue = new LinkedList<>();

    public Window(int id, Set<ServiceType> supportedServices) {
        this.id = id;
        this.supportedServices = supportedServices;
    }

    public boolean canServe(ServiceType type) {
        return supportedServices.contains(type);
    }

    public void addClient(Client client) {
        queue.add(client);
    }

    public int getId() {
        return id;
    }

    public List<Client> getQueue() {
        return queue;
    }

    public Set<ServiceType> getSupportedServices() {
        return supportedServices;
    }
    public String getServiceTypesAsString() {
        return supportedServices.stream()
                .map(ServiceType::getUkrainianName)
                .sorted()
                .reduce((a, b) -> a + ", " + b)
                .orElse("—");
    }

}
