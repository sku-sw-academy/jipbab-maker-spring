package sku.splim.jipbapmaker.service;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CameraService {

    @Value("${camera.api.url}")
    private String openApiURL;

    @Value("${camera.api.key}")
    private String accessKey;

    private static final Logger logger = LoggerFactory.getLogger(CameraService.class);

    private final GptService gptService;

    public CameraService(GptService gptService) {
        this.gptService = gptService;
    }

    public String callApi(MultipartFile imageFile) {
        Gson gson = new Gson();
        String imageContents;

        try {
            byte[] imageBytes = imageFile.getBytes();
            imageContents = Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            logger.error("Failed to read image file", e);
            return "Error: Failed to read image file.";
        }

        Map<String, Object> request = new HashMap<>();
        Map<String, String> argument = new HashMap<>();
        String type = "jpg";  // type을 jpg로 고정

        argument.put("type", type);
        argument.put("file", imageContents);

        request.put("argument", argument);

        try {
            URL url = new URL(openApiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            con.setRequestProperty("Authorization", accessKey);
            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(gson.toJson(request).getBytes(StandardCharsets.UTF_8));
                wr.flush();
            }

            int responseCode = con.getResponseCode();
            logger.info("API Response Code: {}", responseCode);

            try (InputStream is = con.getInputStream()) {
                byte[] buffer = new byte[8192]; // 사용 가능한 바이트 수를 직접 사용하지 않음
                StringBuilder responseBuilder = new StringBuilder();
                int bytesRead;
                while ((bytesRead = is.read(buffer)) != -1) {
                    responseBuilder.append(new String(buffer, 0, bytesRead, StandardCharsets.UTF_8));
                }
                String responseBody = responseBuilder.toString();
                String detectedClasses = processResponse(responseBody);
                return gptService.translateToKorean(detectedClasses);
            }

        } catch (MalformedURLException e) {
            logger.error("Malformed URL: {}", openApiURL, e);
            return "Error: Malformed URL.";
        } catch (IOException e) {
            logger.error("Failed to call API", e);
            return "Error: Failed to call API.";
        }
    }

    private String processResponse(String responseBody) {
        Gson gson = new Gson();
        Map<String, Object> responseMap = gson.fromJson(responseBody, Map.class);
        LinkedTreeMap<String, Object> returnObject = (LinkedTreeMap<String, Object>) responseMap.get("return_object");

        // 객체 검출 결과에서 클래스 정보를 추출하고 중복 없이 저장
        Set<String> classSet = new HashSet<>();
        List<Map<String, Object>> data = (List<Map<String, Object>>) returnObject.get("data");

        for (Map<String, Object> item : data) {
            String className = (String) item.get("class");
            classSet.add(className);
        }

        // Set을 문자열로 변환하여 반환
        return String.join(", ", classSet);
    }
}
