package inovatech24.areavermelha.services;

import com.fasterxml.jackson.core.type.TypeReference;
import inovatech24.areavermelha.domain.Campaign;
import inovatech24.areavermelha.jsonmanager.JsonFileUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CampaignService {
    private final List<Campaign> campaigns;
    private final AtomicLong counter = new AtomicLong(1);
    private static final String FILE_PATH = "C:\\Users\\pcamp\\OneDrive\\Documentos\\JavaProjects\\Area-Vermelha-main\\data\\campaign.json";


    public CampaignService() throws IOException {
        this.campaigns = new ArrayList<>(JsonFileUtil.loadDataFromFile(FILE_PATH, new TypeReference<>() {}));
        if(!campaigns.isEmpty()) {
            counter.set(campaigns.stream().mapToLong(Campaign::getId).max().orElse(1) + 1);
        }
    }

    public Campaign createCampaign(Campaign campaign) throws IOException {
        campaign.setId(counter.getAndIncrement());
        campaigns.add(campaign);
        JsonFileUtil.saveDataToFile(campaigns, FILE_PATH);
        return campaign;
    }

    public List<Campaign> getAllCampaigns() {
        return new ArrayList<>(campaigns);
    }

    public Optional<Campaign> updateCampaign(Long id, Campaign updatedCampaign) throws IOException {
        Optional<Campaign> existingCampaign = campaigns.stream()
                .filter(campaign -> campaign.getId().equals(id))
                .findFirst();
        if(existingCampaign.isPresent()) {
            campaigns.remove(existingCampaign.get());
            updatedCampaign.setId(id);
            campaigns.add(updatedCampaign);
            JsonFileUtil.saveDataToFile(campaigns, FILE_PATH);
            return Optional.of(updatedCampaign);
        }
        return Optional.empty();
    }

    public boolean deleteCampaign(Long id) throws IOException {
        Optional<Campaign> existingCampaign = campaigns.stream()
                .filter(campaign -> campaign.getId().equals(id))
                .findFirst();
        if(existingCampaign.isPresent()) {
            campaigns.remove(existingCampaign.get());
            JsonFileUtil.saveDataToFile(campaigns, FILE_PATH);
            return true;
        }
        return false;
    }
}