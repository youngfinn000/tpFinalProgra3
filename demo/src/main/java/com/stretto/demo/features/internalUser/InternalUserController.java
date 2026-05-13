package com.stretto.demo.features.internalUser;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/internalUser")
public class InternalUserController {
    private final InternalUserService internalUserService;

}
