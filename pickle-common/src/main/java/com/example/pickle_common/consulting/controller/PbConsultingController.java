package com.example.pickle_common.consulting.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pickle-common/consulting/pb")
@Validated
@RequiredArgsConstructor
public class PbConsultingController {
}
