package sku.splim.jipbapmaker.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sku.splim.jipbapmaker.domain.Category;
import sku.splim.jipbapmaker.domain.Item;
import sku.splim.jipbapmaker.domain.Price;
import sku.splim.jipbapmaker.repository.CategoryRepository;
import sku.splim.jipbapmaker.repository.ItemRepository;
import sku.splim.jipbapmaker.repository.PriceRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/")
public class HomeController {
    @Autowired
    private PriceRepository priceRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/{regday}")
    public ResponseEntity<List<Price>> fetchDataFromURL(@PathVariable("regday") String regday) {
        List<Price> prices = new ArrayList<>();
        List<Category> categories = new ArrayList<>();
        List<Item> items = new ArrayList<>();

        HashMap<Integer, String> map = new HashMap<>();
        map.put(100, "식량작물");
        map.put(200, "채소류");
        map.put(300, "특용작물");
        map.put(400, "과일류");
        map.put(500, "축산물");
        map.put(600, "수산물");

        try {
            for (int i = 100; i <= 600; i += 100) {
                Category category = categoryRepository.findByCategoryCode(i);

                if(category == null){
                    category = new Category();
                    category.setCategoryCode(i);
                    category.setCategoryName(map.get(i));
                    categories.add(category);
                }

                String urlString = "https://www.kamis.or.kr/service/price/xml.do?" +
                        "action=dailyPriceByCategoryList" +
                        "&p_product_cls_code=01" +
                        "&p_country_code=" +
                        "&p_regday=" + regday +
                        "&p_convert_kg_yn=N" +
                        "&p_item_category_code=" + category.getCategoryCode() +
                        "&p_cert_key=111" +
                        "&p_cert_id=222" +
                        "&p_returntype=json";

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response.toString());
                JsonNode dataNode = rootNode.get("data");
                String errorCode = null;

                if(dataNode != null && dataNode.has("error_code")){
                    errorCode = dataNode.get("error_code").asText();
                }else{
                    connection.disconnect();
                    continue;
                }

                if(errorCode.equals("000")){
                    JsonNode dataArray = rootNode.get("data").get("item");

                    for (JsonNode data : dataArray) {
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
                        LocalDate currentDate = LocalDate.now();

                        // 7일 빼기
                        LocalDate newDate = currentDate.minusDays(7);

                        // 날짜 형식 지정
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                        // 형식에 맞게 날짜 출력
                        String formattedDate = newDate.format(formatter);

                        List<Price> eigthDays = priceRepository.findByRegday(formattedDate);
//
                        if(eigthDays != null)
                            priceRepository.deleteAllByRegday(formattedDate);

                        if(dpr1.equals("-") || (priceRepository.findById(id) != null)){
                            continue;
                        }

                        Item item = itemRepository.findByItemCode(Integer.parseInt(itemCode));

                        if(item == null){
                            item = new Item();
                            item.setItemCode(Integer.parseInt(itemCode));
                            item.setItemName(itemName);
                            item.setCategoryCode(category);
                            items.add(item);
                        }

                        Price price = new Price();
                        price.setId(id);
                        price.setName(name);
                        price.setItemCode(item);
                        price.setKindName(kindName);
                        price.setKindCode(Integer.parseInt(kindCode));
                        price.setRankName(rankName);
                        price.setRankCode(Integer.parseInt(rankCode));
                        price.setUnit(unit);
                        price.setDpr1(dpr1);
                        price.setDpr2(dpr2);
                        price.setDpr3(dpr3);
                        price.setDpr5(dpr5);
                        price.setDpr6(dpr6);
                        price.setDpr7(dpr7);
                        price.setRegday(regday);
                        price.setStatus(true);

                        Price last = priceRepository.findFirstByNameOrderByIdDesc(name);

                        if(last != null){
                            if(last.getDpr1().equals("-")) {
                                price.setValues(0D);
                            }else{
                                double dpr1_d = Double.parseDouble(dpr1.replace(",", ""));
                                double dpr2_d = Double.parseDouble(last.getDpr1().replace(",", ""));

                                if(dpr2_d == 0){
                                    price.setValues(dpr1_d);
                                }

                                else{
                                    double value = (dpr1_d - dpr2_d) / dpr2_d * 100.0;
                                    String formattedValue = String.format("%.2f", value);
                                    double roundedValue = Double.parseDouble(formattedValue);
                                    price.setValues(roundedValue);
                                }
                            }
                        }else{
                            if(dpr2.equals("-")) {
                                price.setValues(0D);
                            }else{
                                double dpr1_d = Double.parseDouble(dpr1.replace(",", ""));
                                double dpr2_d = Double.parseDouble(dpr2.replace(",", ""));

                                if(dpr2_d == 0){
                                    price.setValues(dpr1_d);
                                }

                                else{
                                    double value = (dpr1_d - dpr2_d) / dpr2_d * 100.0;
                                    String formattedValue = String.format("%.2f", value);
                                    double roundedValue = Double.parseDouble(formattedValue);
                                    price.setValues(roundedValue);
                                }
                            }
                        }

                        prices.add(price);
                    }
                }

                else{
                    connection.disconnect();
                    break;
                }

                connection.disconnect();
            }

            categoryRepository.saveAll(categories);
            itemRepository.saveAll(items);
            priceRepository.saveAll(prices);

            return new ResponseEntity<>(prices, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
