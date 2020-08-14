package spring.micro.services.spring.restdocs.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import spring.micro.services.spring.restdocs.web.model.BeerDto;
import spring.micro.services.spring.restdocs.web.model.BeerStyleEnum;

import java.util.UUID;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {
    @Override
    public BeerDto getBeerById(UUID beerId) {
        return BeerDto.builder()
                .id(UUID.randomUUID())
                .name("Radler")
                .beerStyle(BeerStyleEnum.PILSNER)
                .build();
    }

    @Override
    public BeerDto createNewBeer(BeerDto beerDto) {
        return BeerDto.builder()
                .id(UUID.randomUUID())
                .build();
    }

    @Override
    public void updateBeer(UUID beerId, BeerDto beerDto) {
        // TODO implementation - would add a real implementation to update beer
    }

    @Override
    public void deleteBeerById(UUID beerId) {
        log.info("Deleting a beer...");
    }
}
