package com.example.queuemanagement.controller;

import com.example.queuemanagement.model.*;
import com.example.queuemanagement.repository.ClientRepository;
import com.example.queuemanagement.repository.ArchivedClientRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class QueueController {

    private final ClientRepository clientRepository;
    private final ArchivedClientRepository archivedClientRepository;
    private final List<Window> windows;
    private int queueCounter = 1;

    public QueueController(ClientRepository clientRepository, ArchivedClientRepository archivedClientRepository) {
        this.clientRepository = clientRepository;
        this.archivedClientRepository = archivedClientRepository;
        this.windows = List.of(
                new Window(1, Set.of(ServiceType.DEPOSIT, ServiceType.CREDIT, ServiceType.CARD)),
                new Window(2, Set.of(ServiceType.MORTGAGE, ServiceType.SAFE, ServiceType.PAYMENT)),
                new Window(3, Set.of(ServiceType.CASH, ServiceType.TRANSFER, ServiceType.CURRENCY_EXCHANGE))
        );
    }

    @GetMapping
    public String showHome() {
        return "home";
    }

    @GetMapping("/queue")
    public String showQueue(Model model) {
        List<Client> allClients = clientRepository.findAll();
        windows.forEach(w -> w.getQueue().clear());

        for (Client client : allClients) {
            windows.stream()
                    .filter(w -> w.getSupportedServices().containsAll(client.getServiceTypes()))
                    .findFirst()
                    .ifPresent(w -> w.addClient(client));
        }

        Map<ServiceType, String> serviceMap = ServiceType.getUkrainianMap();
        Map<Integer, String> windowServiceText = new HashMap<>();

        for (Window w : windows) {
            List<String> names = w.getSupportedServices()
                    .stream()
                    .map(serviceMap::get)
                    .toList();
            windowServiceText.put(w.getId(), String.join(", ", names));
        }

        model.addAttribute("windows", windows);
        model.addAttribute("serviceMap", serviceMap);
        model.addAttribute("windowServices", windowServiceText);
        return "queue";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("client", new Client());

        Map<Integer, Set<ServiceType>> windowServices = new HashMap<>();
        for (Window w : windows) {
            windowServices.put(w.getId(), w.getSupportedServices());
        }

        model.addAttribute("windowServices", windowServices);
        model.addAttribute("serviceMap", ServiceType.getUkrainianMap());
        return "add-client";
    }

    private Map<Integer, Set<ServiceType>> getWindowServices() {
        Map<Integer, Set<ServiceType>> map = new LinkedHashMap<>();
        for (Window w : windows) {
            map.put(w.getId(), w.getSupportedServices());
        }
        return map;
    }

    @PostMapping("/add")
    public String addClient(@ModelAttribute Client client, Model model) {
        List<ServiceType> selected = client.getServiceTypes().stream().toList();

        List<Window> matchingWindows = windows.stream()
                .filter(w -> w.getSupportedServices().containsAll(selected))
                .toList();

        if (matchingWindows.size() != 1) {
            model.addAttribute("client", client);
            model.addAttribute("windowServices", getWindowServices());
            model.addAttribute("serviceMap", ServiceType.getUkrainianMap());
            model.addAttribute("error", "❌ Усі обрані послуги мають належати до одного вікна.");
            return "add-client";
        }

        // 🔍 Перевірка на унікальність — той самий телефон і ті самі послуги
        boolean alreadyExists = clientRepository.findAll().stream()
                .anyMatch(existing ->
                        Objects.equals(existing.getPhone(), client.getPhone()) &&
                                Objects.equals(existing.getFirstName(), client.getFirstName()) &&
                                Objects.equals(existing.getLastName(), client.getLastName()) &&
                                Objects.equals(new HashSet<>(existing.getServiceTypes()), new HashSet<>(client.getServiceTypes()))
                );

        if (alreadyExists) {
            model.addAttribute("client", client);
            model.addAttribute("windowServices", getWindowServices());
            model.addAttribute("serviceMap", ServiceType.getUkrainianMap());
            model.addAttribute("error", "⚠️ Цей клієнт вже є в черзі.");
            return "add-client";
        }

        client.setQueueNumber(queueCounter++);
        clientRepository.save(client);
        return "redirect:/queue";
    }



    @PostMapping("/serve/{windowId}")
    public String serveClient(@PathVariable int windowId) {
        windows.stream()
                .filter(w -> w.getId() == windowId)
                .findFirst()
                .ifPresent(w -> {
                    if (!w.getQueue().isEmpty()) {
                        Client toRemove = w.getQueue().get(0);
                        ArchivedClient archived = new ArchivedClient(toRemove, LocalDateTime.now());
                        archivedClientRepository.save(archived);
                        clientRepository.deleteById(toRemove.getId());
                    }
                });
        return "redirect:/queue";
    }

    @GetMapping("/archive")
    public String showArchive(Model model) {
        List<ArchivedClient> archived = archivedClientRepository.findAll();
        Map<Long, List<String>> archivedServiceNames = new HashMap<>();
        Map<ServiceType, String> serviceMap = ServiceType.getUkrainianMap();

        for (ArchivedClient client : archived) {
            List<String> names = client.getServiceTypes().stream()
                    .map(serviceMap::get)
                    .toList();
            archivedServiceNames.put(client.getId(), names);
        }

        model.addAttribute("archivedClients", archived);
        model.addAttribute("archivedServiceNames", archivedServiceNames);

        return "archive";
    }

    @PostMapping("/archive/clear")
    public String clearArchive() {
        archivedClientRepository.deleteAll();
        return "redirect:/archive";
    }

    @PostMapping("/remove/{clientId}")
    public String removeClient(@PathVariable Long clientId) {
        clientRepository.deleteById(clientId);
        return "redirect:/queue";
    }

}
