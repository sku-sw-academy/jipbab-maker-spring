package sku.splim.jipbapmaker.service;

import sku.splim.jipbapmaker.domain.Preference;
import sku.splim.jipbapmaker.dto.CategoryDTO;
import sku.splim.jipbapmaker.dto.ItemDTO;
import sku.splim.jipbapmaker.dto.PriceDTO;
import sku.splim.jipbapmaker.domain.Category;
import sku.splim.jipbapmaker.domain.Item;
import sku.splim.jipbapmaker.domain.Price;
import sku.splim.jipbapmaker.dto.ShopDTO;
import sku.splim.jipbapmaker.repository.CategoryRepository;
import sku.splim.jipbapmaker.repository.ItemRepository;
import sku.splim.jipbapmaker.repository.PriceRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class PriceService {
    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private Category categoryEntity;

    @Autowired
    private Item itemEntity;

    @Autowired
    private Price priceEntity;

    @Autowired
    private CategoryDTO golbalCategoryDTO;

    @Autowired
    private ItemDTO golbalItemDTO;

    @Autowired
    private ItemService itemService;

    @Autowired
    private PreferenceService preferenceService;

    public List<Price> fetchPrices(String regday, Map<Integer, String> categoryMap) {
        List<Price> prices = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        List<Item> items = new ArrayList<>();

        try {
            LocalDate currentDate = LocalDate.now();
            LocalDate newDate = currentDate.minusDays(10);

            for (Map.Entry<Integer, String> entry : categoryMap.entrySet()) {
                int categoryCode = entry.getKey();
                String categoryName = entry.getValue();

                CategoryDTO categoryDTO = getCategory(categoryCode, categoryName, categories);

                String urlString = createUrlString(regday, categoryCode);
                String response = getResponse(urlString);
                JsonNode dataNode = getDataNode(response);

                if (dataNode != null && dataNode.has("error_code")) {
                    if(dataNode.get("error_code").asText().equals("000")) {
                        JsonNode dataArray = dataNode.get("item");

                        for (JsonNode data : dataArray) {
                            PriceDTO priceDTO = processPriceData(data, regday, categoryDTO, newDate, items);
                            if (priceDTO != null) {
                                prices.add(priceEntity.convertToEntity(priceDTO));
                            }
                        }
                    }
                }
            }

            categoryRepository.saveAll(categories);
            itemRepository.saveAll(items);
            priceRepository.saveAll(prices);

            return prices;
        } catch (IOException e) {
            return null;
        }
    }

    private CategoryDTO getCategory(int categoryCode, String categoryName, List<Category> categories) {
        Category category = categoryRepository.findByCategoryCode(categoryCode);
        if (category == null) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setCategoryCode(categoryCode);
            categoryDTO.setCategoryName(categoryName);
            categories.add(categoryEntity.convertToEntity(categoryDTO));
            return categoryDTO;
        }
        return golbalCategoryDTO.convertToDTO(category);
    }

    private String createUrlString(String regday, int categoryCode) {
        return "https://www.kamis.or.kr/service/price/xml.do?" +
                "action=dailyPriceByCategoryList" +
                "&p_product_cls_code=01" +
                "&p_country_code=" +
                "&p_regday=" + regday +
                "&p_convert_kg_yn=N" +
                "&p_item_category_code=" + categoryCode +
                "&p_cert_key=78773982-b7d0-4a56-b0cf-d0d5bac4059d" +
                "&p_cert_id=4243" +
                "&p_returntype=json";
    }

    private String getResponse(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        connection.disconnect();
        return response.toString();
    }

    private JsonNode getDataNode(String response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response);
        return rootNode.get("data");
    }

    private PriceDTO processPriceData(JsonNode data, String regday, CategoryDTO categoryDTO, LocalDate newDate, List<Item> items) {
        String itemName = data.get("item_name").asText();
        String itemCode = data.get("item_code").asText();
        String kindName = data.get("kind_name").asText();
        String kindCode = data.get("kind_code").asText();
        String rankName = data.get("rank").asText();
        String rankCode = data.get("rank_code").asText();
        String unit = data.get("unit").asText();
        String dpr1 = data.get("dpr1").asText();
        String dpr2 = data.get("dpr2").asText();
        String dpr3 = data.get("dpr3").asText();
        String dpr5 = data.get("dpr5").asText();
        String dpr6 = data.get("dpr6").asText();
        String dpr7 = data.get("dpr7").asText();
        String name = itemCode + kindCode + rankCode + unit;
        String id = name + regday;

        String change = "\\(" + unit + "\\)";
        kindName = kindName.replaceAll(change,"");

        List<Price> tenDays = priceRepository.findByRegday(newDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if (tenDays != null) {
            priceRepository.deleteAllByRegdayBefore(newDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }

        if (dpr1.equals("-") || (priceRepository.findById(id) != null)) {
            return null;
        }

        ItemDTO itemDTO = getItemDTO(itemCode, itemName, categoryDTO, items);

        PriceDTO priceDTO = new PriceDTO();
        priceDTO.setId(id);
        priceDTO.setName(name);
        priceDTO.setItemCode(itemDTO);
        priceDTO.setKindName(kindName);
        priceDTO.setKindCode(Integer.parseInt(kindCode));
        priceDTO.setRankName(rankName);
        priceDTO.setRankCode(Integer.parseInt(rankCode));
        priceDTO.setUnit(unit);
        priceDTO.setDpr1(dpr1);
        priceDTO.setDpr2(dpr2);
        priceDTO.setDpr3(dpr3);
        priceDTO.setDpr5(dpr5);
        priceDTO.setDpr6(dpr6);
        priceDTO.setDpr7(dpr7);
        priceDTO.setRegday(regday);
        priceDTO.setStatus(true);

        Price last = priceRepository.findFirstByNameOrderByIdDesc(name);
        double dpr1_d = Double.parseDouble(dpr1.replace(",", ""));

        if(!dpr2.equals("-")){
            double dpr2_d = Double.parseDouble(dpr2.replace(",", ""));
            if (dpr2_d == 0) {
                priceDTO.setValues(dpr1_d);
            } else {
                double value = (dpr1_d - dpr2_d) / dpr2_d * 100.0;
                String formattedValue = String.format("%.2f", value);
                double roundedValue = Double.parseDouble(formattedValue);
                priceDTO.setValues(roundedValue);
            }
        } else if (last != null) {
            double dpr2_d = Double.parseDouble(last.getDpr1().replace(",", ""));

            if (dpr2_d == 0) {
                priceDTO.setValues(dpr1_d);
            } else {
                double value = (dpr1_d - dpr2_d) / dpr2_d * 100.0;
                String formattedValue = String.format("%.2f", value);
                double roundedValue = Double.parseDouble(formattedValue);
                priceDTO.setValues(roundedValue);
            }

        } else {
            priceDTO.setValues(0D);
        }

        return priceDTO;
    }

    private ItemDTO getItemDTO(String itemCode, String itemName, CategoryDTO categoryDTO, List<Item> items) {
        Item item = itemRepository.findByItemCode(Integer.parseInt(itemCode));
        if (item == null) {
            ItemDTO itemDTO = new ItemDTO();
            itemDTO.setItem_code(Integer.parseInt(itemCode));
            itemDTO.setItem_name(itemName);
            itemDTO.setCategory(categoryDTO);
            itemDTO.setCount(0);
            items.add(itemEntity.convertToEntity(itemDTO));
            return itemDTO;
        }
        return golbalItemDTO.convertToDTO(item);
    }

    public List<String> findDistinctKindNamesByItemCode(int code){
        return priceRepository.findDistinctKindNamesByItemCode(code);
    }

    public List<String> findDistinctRankNamesByKindName(String kindName, int itemCode){
        return priceRepository.findDistinctRankNamesByKindNameAndItemCode(kindName, itemCode);
    }

    public List<Price> findByRegdayOrderByValue(String regday){
        return priceRepository.findByRegdayOrderByValue(regday);
    }

    public List<Price> findFirst3ByRegdayOrderByValue(String regday){
        return priceRepository.findFirst3ByRegdayOrderByValue(regday);
    }

    public List<Price> findByRegdayOrderByValueDESC(String regday){
        return priceRepository.findByRegdayOrderByValueDESC(regday);
    }

    public List<Price> findByItemCodeAndKindNameAndRankNameOrderByRegdayDesc(int itemCode, String kindName, String rankName){
        return priceRepository.findByItemCodeAndKindNameAndRankNameOrderByRegdayDesc(itemCode, kindName, rankName);
    }

    public Price save(Price price) {
        return priceRepository.save(price);
    }

    public List<Price> getPopularItemNames6() {
        List<Price> prices = new ArrayList<>();
        List<Item> items = itemService.getfindTopItemsByCountNotZero();
        List<String> names = new ArrayList<>(Arrays.asList("사과", "참외", "당근", "토마토", "쌀", "감자"));

        if (items != null) {
            for (Item item : items) {
                int code = item.getItemCode();
                Price price = priceRepository.findLatestPriceByItemCode(code);
                prices.add(price);
            }

            // items 리스트의 크기가 6보다 작은 경우 누락된 아이템을 가져옵니다.
            while (items.size() < 6) {
                String missingItemName = getMissingItemName(items, names);
                if (missingItemName != null) {
                    Item missingItem = itemService.findByItemName(missingItemName);
                    if (missingItem != null) {
                        int code = missingItem.getItemCode();
                        Price price = priceRepository.findLatestPriceByItemCode(code);
                        prices.add(price);
                        items.add(missingItem);
                    }
                } else {
                    break; // names 리스트에 추가할 아이템이 없으면 종료합니다.
                }
            }
        } else {
            // items 리스트가 null인 경우, names에 있는 아이템을 가져와서 prices에 추가합니다.
            for (String name : names) {
                Item item = itemService.findByItemName(name);
                if (item != null) {
                    int code = item.getItemCode();
                    Price price = priceRepository.findLatestPriceByItemCode(code);
                    prices.add(price);
                }
            }
        }

        return prices;
    }

    public List<Price> getPopularItemNames9() {
        List<Price> prices = new ArrayList<>();
        List<Item> items = itemService.getfindTopItemsByCountNotZero();
        List<String> names = new ArrayList<>(Arrays.asList("사과", "참외", "당근", "토마토", "쌀", "감자", "고구마", "돼지", "소"));

        if (items != null) {
            for (Item item : items) {
                int code = item.getItemCode();
                Price price = priceRepository.findLatestPriceByItemCode(code);
                prices.add(price);
            }

            // items 리스트의 크기가 6보다 작은 경우 누락된 아이템을 가져옵니다.
            while (items.size() < 9) {
                String missingItemName = getMissingItemName(items, names);
                if (missingItemName != null) {
                    Item missingItem = itemService.findByItemName(missingItemName);
                    if (missingItem != null) {
                        int code = missingItem.getItemCode();
                        Price price = priceRepository.findLatestPriceByItemCode(code);
                        prices.add(price);
                        items.add(missingItem);
                    }
                } else {
                    break; // names 리스트에 추가할 아이템이 없으면 종료합니다.
                }
            }
        } else {
            // items 리스트가 null인 경우, names에 있는 아이템을 가져와서 prices에 추가합니다.
            for (String name : names) {
                Item item = itemService.findByItemName(name);
                if (item != null) {
                    int code = item.getItemCode();
                    Price price = priceRepository.findLatestPriceByItemCode(code);
                    prices.add(price);
                }
            }
        }

        return prices;
    }

    private String getMissingItemName(List<Item> items, List<String> names) {
        for (String name : names) {
            boolean found = false;
            for (Item item : items) {
                if (item.getItemName().equals(name)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return name;
            }
        }
        return null; // 모든 이름이 이미 리스트에 있으면 null을 반환합니다.
    }

    public List<Price> getPreferList(long id){
        List<Price> prices = new ArrayList<>();
        List<Preference> preferences = preferenceService.getPreferList(id, 0);

        for(Preference preference : preferences){
            Item item = preference.getItem();
            List<String> names = priceRepository.findDistinctNamesByItemCode(item.getItemCode());
            for(String name : names){
                Price price = priceRepository.findLatestByProductName(name);
                prices.add(price);
            }
        }

        Collections.sort(prices, new Comparator<Price>() {
            @Override
            public int compare(Price o1, Price o2) {
                int valueComparison = Double.compare(o1.getValues(), o2.getValues());
                if (valueComparison == 0) {
                    return o2.getName().compareTo(o1.getName());
                }
                return valueComparison;
            }
        });

        return prices;
    }

    public List<Price> getList(){
        List<Price> prices = new ArrayList<>();
        List<Item> items = itemService.getfindAll();

        for(Item item : items){
            List<String> names = priceRepository.findDistinctNamesByItemCode(item.getItemCode());
            for(String name : names){
                Price price = priceRepository.findLatestByProductName(name);
                prices.add(price);
            }
        }

        Collections.sort(prices, new Comparator<Price>() {
            @Override
            public int compare(Price o1, Price o2) {
                int valueComparison = Double.compare(o1.getValues(), o2.getValues());
                if (valueComparison == 0) {
                    return o2.getName().compareTo(o1.getName());
                }
                return valueComparison;
            }
        });

        return prices;
    }

}

