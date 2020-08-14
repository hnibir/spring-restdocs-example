package spring.micro.services.spring.restdocs.web.mappers;

/*
 * Created by Nibir Hossain on 14.08.20
 */

import org.mapstruct.Mapper;
import spring.micro.services.spring.restdocs.domain.Beer;
import spring.micro.services.spring.restdocs.web.model.BeerDto;

@Mapper(uses = {DateMapper.class})
public interface BeerMapper {
    BeerDto beerToBeerDto(Beer beer);
    Beer beerDtoToBeer(BeerDto beerDto);
}
