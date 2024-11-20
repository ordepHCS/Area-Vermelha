package inovatech24.areavermelha.services;

import com.fasterxml.jackson.core.type.TypeReference;
import inovatech24.areavermelha.domain.Complaint;
import inovatech24.areavermelha.domain.User;
import inovatech24.areavermelha.jsonmanager.JsonFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ComplaintService {
    private final List<Complaint> complaints;
    private final AtomicLong counter = new AtomicLong(1);
    private static final String FILE_PATH = "C:\\Users\\pcamp\\OneDrive\\Documentos\\JavaProjects\\Area-Vermelha-main\\data\\complaint.json";

    public List<Map<String, Object>> getDangerousAreasRByHashtags() {
        List<String> predefinedHashtags = Arrays.asList(
                "#areadefloresta", "#ruasemiluminação", "#ruacomiluminação",
                "#ruasemasfalto", "#ruaasfaltada", "#poucamovimentação",
                "#pontodeonibus", "#ruacomburacos", "#areaescolar"
        );

        Map<String, Long> hashtagCount = predefinedHashtags.stream()
                .collect(Collectors.toMap(
                        hashtag -> hashtag,
                        hashtag -> complaints.stream()
                                .filter(complaint -> complaint.getHashtags() != null && complaint.getHashtags().contains(hashtag))
                                .count()
                ));

        List<Map<String, Object>> hashtagRanking = hashtagCount.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .map(entry -> {
                    Map<String, Object> rankingInfo = new HashMap<>();
                    rankingInfo.put("hashtag", entry.getKey());
                    rankingInfo.put("count", entry.getValue());
                    return rankingInfo;
                })
                .collect(Collectors.toList());
        return hashtagRanking;
    }

    @Autowired
    private GoogleMapsService googleMapsService;

    public ComplaintService() throws IOException {
        this.complaints = new ArrayList<>(JsonFileUtil.loadDataFromFile(FILE_PATH, new TypeReference<>() {}));
        if(!complaints.isEmpty()) {
            counter.set(complaints.stream().mapToLong(Complaint::getId).max().orElse(1) + 1);
        }
    }

    public Complaint createComplaint(User user, Complaint complaint) throws IOException {
        complaint.setId(counter.getAndIncrement());
        complaint.setUser(user);
        complaint.setOccurrenceCode(UUID.randomUUID().toString());

        String fullAddress = complaint.getStreet() + ", " + complaint.getNeighborhood() + ", " +
                complaint.getCity() + ", " + complaint.getState() + ", " + complaint.getCep();

        double[] coordinates = googleMapsService.getCoordinates(fullAddress);
        if(coordinates != null) {
            complaint.setLatitude(coordinates[0]);
            complaint.setLongitude(coordinates[1]);
        }

        complaints.add(complaint);
        JsonFileUtil.saveDataToFile(complaints, FILE_PATH);
        return complaint;
    }

    public List<Complaint> getAllComplaints() {
        return new ArrayList<>(complaints);
    }

    public List<Complaint> getAllComplaintsByUser(Long userId) {
        return complaints.stream()
                .filter(complaint -> complaint.getUser().getId().equals(userId))
                .toList();
    }

    public List<Complaint> getComplaintsByCategory(String category) {
        return complaints.stream()
                .filter(complaint -> complaint.getHashtags() != null && complaint.getHashtags().contains(category))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getDangerousAreasR() {
        Map<String, Long> neighborhoodCount = complaints.stream()
                .filter(complaint -> complaint.getNeighborhood() != null && !complaint.getNeighborhood().isEmpty())
                .collect(Collectors.groupingBy(Complaint::getNeighborhood, Collectors.counting()));
        List<Map<String, Object>> areaRanking = neighborhoodCount.entrySet().stream()
                .sorted((entry1, entry2) -> Long.compare(entry2.getValue(), entry1.getValue()))
                .map(entry -> {
                    Map<String, Object> areaInfo = new HashMap<>();
                    areaInfo.put("neighborhood", entry.getKey());
                    areaInfo.put("count", entry.getValue());
                    return areaInfo;
                })
                .collect(Collectors.toList());
        return areaRanking;
    }
}