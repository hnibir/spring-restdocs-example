package spring.micro.services.spring.restdocs.web.controller;

/*
 * Created by Nibir Hossain on 10.08.20
 */

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import spring.micro.services.spring.restdocs.services.BeerService;
import spring.micro.services.spring.restdocs.web.model.BeerDto;

import java.util.UUID;

@RestController
@RequestMapping(value = {"/api/v1/beer"})
@RequiredArgsConstructor
public class BeerController {
    private final BeerService beerService;

    @GetMapping(value = {"/{beerId}"})
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId) {
        return new ResponseEntity<>(beerService.getBeerById(beerId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity saveNewBeer(@RequestBody @Validated BeerDto beerDto) {
        BeerDto savedDto = beerService.createNewBeer(beerDto);
        HttpHeaders headers = new HttpHeaders();
        // TODO add hostname to URL
        headers.add("Location", "/api/v1/beer/" + savedDto.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(value = {"/{beerId}"})
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDto beerDto) {
        beerService.updateBeer(beerId, beerDto);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
