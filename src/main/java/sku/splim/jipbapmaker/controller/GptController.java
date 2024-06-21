package sku.splim.jipbapmaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sku.splim.jipbapmaker.domain.Price;
import sku.splim.jipbapmaker.dto.gpt.GptRequest;
import sku.splim.jipbapmaker.dto.gpt.GptResponse;
import sku.splim.jipbapmaker.service.GptService;
import sku.splim.jipbapmaker.service.PreferenceService;
import sku.splim.jipbapmaker.service.PriceService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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
    public GptResponse makeRecipe(@RequestBody GptRequest request) {
        List<String> preferences = preferenceService.getPreferListToString(request.getId(), 0);
        List<String> nonPreferences = preferenceService.getPreferListToString(request.getId(), 2);
        String foodList = String.format("preferences : %s, non-preferences : %s, ThriftyItems : %s",
                String.join(", ", preferences),
                String.join(", ", nonPreferences),
                request.getThriftyItems());

        return gptService.generateRecipeWithImage(foodList);
    }

    @PostMapping("/recipe/imagePath")
    public GptResponse makeImageRecipe(@RequestBody GptRequest request) {
        List<String> preferences = preferenceService.getPreferListToString(request.getId(), 0);
        List<String> nonPreferences = preferenceService.getPreferListToString(request.getId(), 2);
        preferences.add(request.getThriftyItems());

        LocalDateTime now = LocalDateTime.now();
        LocalDate currentDate;
// 현재 시간이 16시 이전이면 전날을 사용하고, 16시 이후면 당일을 사용
        if (now.getHour() < 16) {
            currentDate = now.minusDays(1).toLocalDate();
        } else {
            currentDate = now.toLocalDate();
        }

        String regday = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        List<Price> prices = priceService.findFirst3ByRegdayOrderByValue(regday);
        String thriftyItems = convertPricesToString(prices);

        String foodList = String.format("preferences : %s, non-preferences : %s, ThriftyItems : %s",
                String.join(", ", preferences),
                String.join(", ", nonPreferences),
                thriftyItems);

        return gptService.generateRecipeWithImage(foodList);
    }

    private String convertPricesToString(List<Price> prices) {
        // 리스트의 각 Price 객체의 itemCode에서 itemName 필드를 추출하고 이를 콤마로 구분된 문자열로 결합
        return prices.stream()
                .map(price -> price.getItemCode().getItemName())
                .collect(Collectors.joining(", "));
    }
}
