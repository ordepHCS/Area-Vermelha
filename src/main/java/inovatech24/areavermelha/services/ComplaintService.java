package inovatech24.areavermelha.services;

import com.fasterxml.jackson.core.type.TypeReference;
import inovatech24.areavermelha.domain.Complaint;
import inovatech24.areavermelha.domain.User;
import inovatech24.areavermelha.jsonmanager.JsonFileUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ComplaintService {
    private final List<Complaint> complaints;
    private final AtomicLong counter = new AtomicLong(1);
    private static final String FILE_PATH = "C:\\Users\\pcamp\\OneDrive\\Documentos\\JavaProjects\\areavermelha\\data\\complaint.json";


    public ComplaintService() throws IOException {
        this.complaints = new ArrayList<>(JsonFileUtil.loadDataFromFile(FILE_PATH, new TypeReference<>() {}));
        if(!complaints.isEmpty()) {
            counter.set(complaints.stream().mapToLong(Complaint::getId).max().orElse(1) + 1);
        }
    }

    public Complaint createComplaint(User user, Complaint complaint) throws IOException {
        complaint.setId(counter.getAndIncrement());
        complaint.setUser(user);
        complaints.add(complaint);
        JsonFileUtil.saveDataToFile(complaints, FILE_PATH);
        return complaint;
    }

    public List<Complaint> getAllComplaintsByUser(Long userId) {
        return complaints.stream()
                .filter(complaint -> complaint.getUser().getId().equals(userId))
                .toList();
    }

    public List<Map<String, Object>> getDangerousAreasR() {
        return complaints.stream()
                .collect(Collectors.groupingBy(Complaint::getNeighborhood, Collectors.counting()))
                .entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .map(entry -> {
                    Map<String, Object> areaInfo = new HashMap<>();
                    areaInfo.put("area", entry.getKey());
                    areaInfo.put("count", entry.getValue());
                    return areaInfo;
                })
                .collect(Collectors.toList());
    }
}
