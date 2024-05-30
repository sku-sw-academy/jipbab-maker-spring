package sku.splim.jipbapmaker.controller;

import sku.splim.jipbapmaker.dto.PriceDTO;
import sku.splim.jipbapmaker.domain.Item;
import sku.splim.jipbapmaker.domain.Price;
import sku.splim.jipbapmaker.dto.ShopDTO;
import sku.splim.jipbapmaker.service.ItemService;
import sku.splim.jipbapmaker.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("/prices")
public class PriceController {
    @Autowired
    private PriceService priceService;

    @Autowired
    private Item item;

    @Autowired
    private ItemService itemService;

    @GetMapping("/saving/top3/{regday}") //메인페이지 알뜰
    public List<PriceDTO> getTop3Names(@PathVariable("regday") String regday){
        List<Price> prices = priceService.findFirst3ByRegdayOrderByValue(regday);
        List<PriceDTO> priceDTOs = new ArrayList<>();

        for(Price price : prices){
            PriceDTO priceDTO = new PriceDTO();
            priceDTOs.add(priceDTO.convertToDTO(price));
        }

        return priceDTOs;
    }

    @GetMapping("/saving/detail/{regday}") // 알뜰 소비 페이지
    public ResponseEntity<List<PriceDTO>> getDetails(@PathVariable("regday") String regday) {
        List<Price> prices = priceService.findByRegdayOrderByValue(regday);

        if (prices.isEmpty()) {
            // 데이터가 없으면 404 상태 코드를 반환
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 데이터가 있으면 DTO로 변환하여 반환
        List<PriceDTO> priceDTOs = new ArrayList<>();
        for (Price price : prices) {
            PriceDTO priceDTO = new PriceDTO();
            priceDTOs.add(priceDTO.convertToDTO(price));
        }

        return new ResponseEntity<>(priceDTOs, HttpStatus.OK);
    }

    @GetMapping("/shopping/decrease/{regday}")
    public List<ShopDTO> getDShopping(@PathVariable("regday") String regday) {
        List<ShopDTO> shops = new ArrayList<>();
        List<Price> prices = priceService.findByRegdayOrderByValue(regday);
        for(Price price : prices){
            ShopDTO shop = new ShopDTO();
            shop.setName(price.getItemCode().getItemName());
            shop.setKind(price.getKindName());
            shop.setUnit(price.getUnit());
            shop.setRank(price.getRankName());
            shop.setPrice(price.getDpr1());
            shop.setWeek_price(price.getDpr3());

            if(!shop.getWeek_price().equals("-") && !shop.getWeek_price().equals("0")){
                double dpr1_d = Double.parseDouble(shop.getPrice().replace(",", ""));
                double dpr2_d = Double.parseDouble(shop.getWeek_price().replace(",", ""));
                double value = (dpr1_d - dpr2_d) / dpr2_d * 100.0;
                String formattedValue = String.format("%.2f", value);
                double roundedValue = Double.parseDouble(formattedValue);
                shop.setValues(roundedValue);
            }else{
                shop.setValues(0D);
            }

            shops.add(shop);
        }

        Collections.sort(shops, new Comparator<ShopDTO>() {
            @Override
            public int compare(ShopDTO o1, ShopDTO o2) {
                int valueComparison = Double.compare(o1.getValues(), o2.getValues());
                if (valueComparison == 0) {
                    return o1.getName().compareTo(o2.getName());
                }
                return valueComparison;
            }
        });

        return shops;
    }

    @GetMapping("/shopping/increase/{regday}")
    public List<ShopDTO> getIShopping(@PathVariable("regday") String regday) {
        List<ShopDTO> shops = new ArrayList<>();
        List<Price> prices = priceService.findByRegdayOrderByValue(regday);
        for(Price price : prices){
            ShopDTO shop = new ShopDTO();
            shop.setName(price.getItemCode().getItemName());
            shop.setKind(price.getKindName());
            shop.setUnit(price.getUnit());
            shop.setRank(price.getRankName());
            shop.setPrice(price.getDpr1());
            shop.setWeek_price(price.getDpr3());

            if(!shop.getWeek_price().equals("-") && !shop.getWeek_price().equals("0")){
                double dpr1_d = Double.parseDouble(shop.getPrice().replace(",", ""));
                double dpr2_d = Double.parseDouble(shop.getWeek_price().replace(",", ""));
                double value = (dpr1_d - dpr2_d) / dpr2_d * 100.0;
                String formattedValue = String.format("%.2f", value);
                double roundedValue = Double.parseDouble(formattedValue);
                shop.setValues(roundedValue);
            }else{
                shop.setValues(0D);
            }
            shops.add(shop);
        }

        Collections.sort(shops, new Comparator<ShopDTO>() {
            @Override
            public int compare(ShopDTO o1, ShopDTO o2) {
                int valueComparison = Double.compare(o2.getValues(), o1.getValues());
                if (valueComparison == 0) {
                    return o1.getName().compareTo(o2.getName());
                }
                return valueComparison;
            }
        });

        return shops;
    }

    @GetMapping("/kinds/{itemname}")  //검색 결과 페이지
    public List<String> findDistinctKindNamesByItemCode(@PathVariable("itemname") String itemname){
        item = itemService.findByItemName(itemname);
        if (item != null) {
            // 아이템을 찾았다면 해당 아이템 코드를 사용하여 메서드를 호출합니다.
            return priceService.findDistinctKindNamesByItemCode(item.getItemCode());
        } else {
            // 아이템을 찾지 못했을 때의 처리를 추가할 수 있습니다.
            return Collections.emptyList(); // 예: 빈 리스트를 반환
        }
    }

    @GetMapping("/ranks/{item}/{kind}") //검색 결과 페이지
    public List<String> findDistinctRankNamesByKindName(@PathVariable("kind") String kind, @PathVariable("item") String item){
        int code = itemService.findByItemName(item).getItemCode();

        return priceService.findDistinctRankNamesByKindName(kind, code);
    }

    @GetMapping("/search/{item}/{kind}/{rank}")
    public List<PriceDTO> getItemData(@PathVariable("item") String item, @PathVariable("kind")String kind, @PathVariable("rank")String rank){
        int code = itemService.findByItemName(item).getItemCode();
        List<Price> prices = priceService.findByItemCodeAndKindNameAndRankNameOrderByRegdayDesc(code, kind, rank);
        List<PriceDTO> priceDTOs = new ArrayList<>();

        for(Price price : prices){
            PriceDTO priceDTO = new PriceDTO();
            priceDTOs.add(priceDTO.convertToDTO(price));
        }

        return priceDTOs;
    }

    @GetMapping("/popular6")
    public List<PriceDTO> getPopularItemNames6(){
        List<Price> prices = priceService.getPopularItemNames6();
        List<PriceDTO> priceDTOs = new ArrayList<>();

        for(Price price : prices){
            PriceDTO priceDTO = new PriceDTO();
            priceDTOs.add(priceDTO.convertToDTO(price));
        }

        return priceDTOs;
    }

    @GetMapping("/popular9")
    public List<PriceDTO> getPopularItemNames9(){
        List<Price> prices = priceService.getPopularItemNames9();
        List<PriceDTO> priceDTOs = new ArrayList<>();

        for(Price price : prices){
            PriceDTO priceDTO = new PriceDTO();
            priceDTOs.add(priceDTO.convertToDTO(price));
        }

        return priceDTOs;
    }

}
