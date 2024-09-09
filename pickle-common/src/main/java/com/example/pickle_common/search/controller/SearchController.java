package com.example.pickle_common.search.controller;

import com.example.pickle_common.search.dto.ReadThemeDto;
import com.example.pickle_common.search.service.SearchService;
import com.example.real_common.global.common.CommonResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pickle-common/search")
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/theme")
    public ResponseEntity<CommonResDto<?>> readThemeList() {
        ReadThemeDto.Response result = searchService.readThemeList();
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResDto<>(1, "카테고리 별 테마 조회 성공", result));
    }
}
