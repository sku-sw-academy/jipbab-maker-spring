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
    public String makeRecipe(@RequestBody GptChatRequest request) {
        List<String> preferences = preferenceService.getPreferListToString(request.getId(), 0);
        List<String> nonPreferences = preferenceService.getPreferListToString(request.getId(), 2);
        String foodList = String.format("preferences : %s, non-preferences : %s, ThriftyItems : %s",
                String.join(", ", preferences),
                String.join(", ", nonPreferences),
                request.getThriftyItems());

        // hard coding for prompt
        String gptSetting = """
                            대답은 한국어로만 해줘. 이제부터는 제가 제공하는 음식 목록에서
                            preferences와 thriftyItems을 반영하여 레시피를 제안하며, nonPreferences은 절대 포함하
                            지 않습니다. 반드시 레시피의 시간과 용량들을 자세히 알려줘. 대답은 이 형식에 맞춰서 똑같이 대답해줘.
                            title: ,content:
                            """;
        String gptAnswer = gptService.getGptAnswer(gptSetting, foodList);

        return gptAnswer;
    }


}
