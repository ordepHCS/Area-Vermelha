package inovatech24.areavermelha.jsonmanager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class JsonFileUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public static <T> void saveDataToFile(List<T> newData, String filePath) throws IOException {
        objectMapper.writeValue(new File(filePath), newData);
    }

    public static <T> List<T> loadDataFromFile(String filePath, TypeReference<List<T>> typeReference) throws IOException {
        File file = new File(filePath);
        if(!file.exists()) {
            Files.createDirectories(Paths.get(file.getParent()));
            Files.write(Paths.get(filePath), "[]".getBytes());
        }
        if(file.length() == 0) {
            return Collections.emptyList();
        }
        return objectMapper.readValue(file, typeReference);
    }
}