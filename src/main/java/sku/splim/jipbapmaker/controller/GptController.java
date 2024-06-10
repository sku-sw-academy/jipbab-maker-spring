package sku.splim.jipbapmaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sku.splim.jipbapmaker.domain.Price;
import sku.splim.jipbapmaker.dto.gpt.GptChatRequest;
import sku.splim.jipbapmaker.dto.gpt.GptChatResponse;
import sku.splim.jipbapmaker.service.GptService;
import sku.splim.jipbapmaker.service.PreferenceService;
import sku.splim.jipbapmaker.service.PriceService;

import java.util.List;

@RestController
@RequestMapping("/api/gpt")
public class GptController {
    private final GptService gptService;
    private final PreferenceService preferenceService;
    private final PriceService priceService;

    @Autowired
    public GptController(GptService gptService, PreferenceService preferenceService, PriceService priceService) {
        this.gptService = gptService;
        this.preferenceService = preferenceService;
        this.priceService = priceService;
    }

    @PostMapping("/recipe")
    public GptChatResponse makeRecipe(@RequestBody GptChatRequest request) {
        List<String> preferences = preferenceService.getPreferListToString(request.getId(), 0);
        List<String> nonPreferences = preferenceService.getPreferListToString(request.getId(), 2);
        String foodList = String.format("preferences : %s, non-preferences : %s, ThriftyItems : %s",
                String.join(", ", preferences),
                String.join(", ", nonPreferences),
                request.getThriftyItems());

        // hard coding for prompt
        String gptSetting = """
                    Always respond in Korean.
                    Use only the ingredients listed under 'preferences' and 'ThriftyItems',
                    and never use any ingredients listed under 'non-preferences'.
                    Based on this, create a recipe.
                    The response format should be title : , content :.
                    title에는 레시피 제목만 적어줘, content에는 조리법을 적어줘.
                    """;
        String gptAnswer = gptService.getGptAnswer(gptSetting, foodList);
        int index = gptAnswer.indexOf("content : ");

        String[] titleAndContent = gptAnswer.split(",");
        String title = gptAnswer.substring(0, index-2);
        String content = gptAnswer.substring(index);

//        String title = titleAndContent[0];
//        String content = titleAndContent[1];

        return new GptChatResponse(title, content);
    }


}
