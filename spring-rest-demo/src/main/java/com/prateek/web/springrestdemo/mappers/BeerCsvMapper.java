package com.prateek.web.springrestdemo.mappers;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.prateek.web.springrestdemo.domain.entities.Beer;
import com.prateek.web.springrestdemo.domain.entities.BeerCSVRecord;
import com.prateek.web.springrestdemo.model.BeerStyle;

@Mapper
public interface BeerCsvMapper {
    @Mapping(target = "quantityOnHand", source = "count")
    @Mapping(target = "upc", source = "row")
    @Mapping(target = "beerStyle", source = "style", qualifiedByName = "beerStyleStringToEnum")
    @Mapping(target = "beerName", source = "beer", qualifiedByName = "beerNameMapper")
    @BeanMapping(ignoreByDefault = true)
    Beer carToCarDto(BeerCSVRecord car);

    @Named("beerNameMapper")
    public static String beerNameMapper(String beerCsvName) {
        return StringUtils.abbreviate(beerCsvName, 50);
    }

    @Named("beerStyleStringToEnum")
    public static BeerStyle beerStyleStringToEnum(String style) {
        return switch (style) {
            case "American Pale Lager" -> BeerStyle.LAGER;
            case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                BeerStyle.ALE;
            case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
            case "American Porter" -> BeerStyle.PORTER;
            case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
            case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
            case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
            case "English Pale Ale" -> BeerStyle.PALE_ALE;
            default -> BeerStyle.PILSNER;
        };
    }
}
