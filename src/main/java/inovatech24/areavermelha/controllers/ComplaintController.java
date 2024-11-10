package inovatech24.areavermelha.controllers;

import inovatech24.areavermelha.domain.Complaint;
import inovatech24.areavermelha.domain.User;
import inovatech24.areavermelha.services.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    @Autowired
    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Complaint> createComplaint(@PathVariable Long userId, @RequestBody Complaint complaint) {
        try {
            User user = new User();
            user.setId(userId);
            complaint.setUser(user);
            Complaint createdComplaint = complaintService.createComplaint(user, complaint);
            return new ResponseEntity<>(createdComplaint, HttpStatus.CREATED);
        }catch(IOException exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Complaint>> getAllComplaintsByUser(@PathVariable Long userId) {
        List<Complaint> complaints = complaintService.getAllComplaintsByUser(userId);
        return new ResponseEntity<>(complaints, HttpStatus.OK);
    }

    @GetMapping("/dangerous-areas-ranking")
    public ResponseEntity<List<Map<String, Object>>> getDangerousAreasRanking() {
        List<Map<String, Object>> ranking = complaintService.getDangerousAreasR();
        return new ResponseEntity<>(ranking, HttpStatus.OK);
    }
}