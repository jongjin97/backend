package com.example.back.controller;

import com.example.back.config.auth.PrincipalDetail;
import com.example.back.dto.RegionDto;
import com.example.back.jpa.service.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/region")
@RequiredArgsConstructor
public class RegionController {

    private final RegionService regionService;

    @GetMapping("/lists")
    public ResponseEntity<List<RegionDto>> getRegionListByUserId(@AuthenticationPrincipal PrincipalDetail principalDetail){

        List<RegionDto> regionDtoList = regionService.selectRegionListByUserId(principalDetail.getId());

        return ResponseEntity.ok(regionDtoList);
    }

    @PostMapping
    public ResponseEntity<RegionDto> saveAndUpdateRegion(@AuthenticationPrincipal PrincipalDetail principalDetail
            , @RequestBody String regionName){
        regionName = regionName.replaceAll("\"", "");
        RegionDto regionDto = regionService.saveAndUpdateRegion(regionName, principalDetail.getUser());
        return  ResponseEntity.ok(regionDto);
    }
    @GetMapping
    public ResponseEntity<RegionDto> getRegionByUserId(@AuthenticationPrincipal PrincipalDetail principalDetail){
        RegionDto regionDto = regionService.selectRegionByUserId(principalDetail.getId());

        return ResponseEntity.ok(regionDto);
    }

}
