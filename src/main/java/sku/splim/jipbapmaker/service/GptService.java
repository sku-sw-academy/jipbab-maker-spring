package sku.splim.jipbapmaker.service;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.theokanning.openai.OpenAiHttpException;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.service.OpenAiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sku.splim.jipbapmaker.config.GptProperties;
import sku.splim.jipbapmaker.dto.gpt.GptResponse;
import sku.splim.jipbapmaker.repository.RecipeRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class GptService {
    private static final Logger logger = LoggerFactory.getLogger(GptService.class);

    private final OpenAiService service;
    private final RecipeRepository recipeRepository;
    private final Translate translate;

    private final static String GPT_MODEL = "gpt-4";
    private final static double TEMPERATURE = 0.5;
    private static final Duration DURATION = Duration.ofSeconds(120);
    private static final String IMAGE_SAVE_DIRECTORY = "/Users/hiho1010/JipbabMaker/images/";

    @Autowired
    public GptService(GptProperties gptProperties, RecipeRepository recipeRepository, @Value("${google.api.key}") String apiKey) {
        this.service = new OpenAiService(gptProperties.getApiKey(), DURATION);
        this.recipeRepository = recipeRepository;

        // API 키를 시스템 속성으로 설정
        System.setProperty("GOOGLE_API_KEY", apiKey);
        this.translate = TranslateOptions.getDefaultInstance().getService();  // Google Translate API 클라이언트 초기화
    }

    // Create a message to send to GPT
    private List<ChatMessage> getMessage(String message) {
        String system = """
                        preferences: [사용자가 선호하는 재료 목록]
                        non-preferences: [사용자가 선호하지 않는 재료 목록]
                        thriftyItems: [사용자가 저렴하게 구할 수 있는 재료 목록]
                        사용자가 제공한 재료를 사용하여 새로운 요리 레시피를 생성해주세요.
                        레시피 제목은 이전 레시피 제목과 겹치지 않게 해주세요.
                        모든 재료의 용량과 각 과정의 조리 시간을 포함하여 자세한 레시피를 작성해주세요.
                        레시피는 한국어로 작성하고, 한 사람분량으로 만드세요. 절대 레시피 외에는 아무 말도 붙이지 말아주세요.
                        답변은 무조건 이 형식을 따라해주세요.
                        title: [recipe title] content: [recipe content]
                        """;

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("system", system));
        messages.add(new ChatMessage("user", message));
        return messages;
    }

    public String getGptAnswer(String message) {
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(getMessage(message))
                .model(GPT_MODEL)
                .temperature(TEMPERATURE)
                .build();

        try {
            return service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();
        } catch (OpenAiHttpException e) {
            if (e.getMessage().contains("You exceeded your current quota")) {
                logger.error("API 사용 한도를 초과했습니다. 요금제 및 사용량을 확인해주세요.", e);
            } else {
                logger.error("OpenAiHttpException 발생: ", e);
            }
            return "오류가 발생했습니다. 관리자에게 문의하세요.";
        } catch (Exception e) {
            logger.error("알 수 없는 오류가 발생했습니다: ", e);
            return "오류가 발생했습니다. 관리자에게 문의하세요.";
        }
    }

    public String[] parseGptAnswer(String answer) {
        String[] splitAnswer = answer.split("title: ");
        String title = "";
        String content = "";

        if (splitAnswer.length > 1) {
            String[] splitContent = splitAnswer[1].split("content: ");
            title = splitContent[0].trim();
            if (splitContent.length > 1) {
                content = splitContent[1].trim();
            }
        }
        return new String[]{title, content};
    }

    public String generatePicture(String title) {
        CreateImageRequest createImageRequest = CreateImageRequest.builder()
                .prompt("A high-resolution photo of " + title + " on a white plate, professional food photography, studio lighting")
                .size("512x512")
                .n(1)
                .build();
        return service.createImage(createImageRequest).getData().get(0).getUrl();
    }

    public void saveImage(String imageUrl) {
        try {
            BufferedImage image = ImageIO.read(new URL(imageUrl));
            String fileName = extractFileName(imageUrl);
            String filePath = IMAGE_SAVE_DIRECTORY + fileName;
            File outputfile = new File(filePath);
            ImageIO.write(image, "png", outputfile);
            logger.info("Image saved successfully: " + outputfile.getAbsolutePath());
        } catch (IOException e) {
            logger.error("Failed to save image: ", e);
        }
    }

    private String extractFileName(String url) {
        return url.substring(url.lastIndexOf('/') + 1);
    }

    public String translateToEnglish(String text) {
        Translation translation = translate.translate(
                text,
                Translate.TranslateOption.sourceLanguage("ko"),
                Translate.TranslateOption.targetLanguage("en")
        );
        return translation.getTranslatedText();
    }

    public GptResponse generateRecipeWithImage(String foodList) {
        String gptAnswer = getGptAnswer(foodList);
        String[] parsedAnswer = parseGptAnswer(gptAnswer);
        String title = parsedAnswer[0];
        String content = parsedAnswer[1];

        // 제목을 영어로 변환
        String translatedTitle = translateToEnglish(title);

        // 이미지 생성 및 저장
        String imageUrl = generatePicture(translatedTitle);
        saveImage(imageUrl);

        // GptResponse에 이미지 URL 추가
        return GptResponse.builder()
                .title(title)
                .content(content)
                .imageUrl(imageUrl)
                .build();
    }
}
