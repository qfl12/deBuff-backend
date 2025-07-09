package com.debuff.debuffbackend.controller;

import com.debuff.debuffbackend.entity.Identityverification;
import com.debuff.debuffbackend.service.IdentityverificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 实名认证信息表(Identityverification)表控制层
 *
 * @author m1822
 * @since 2025-07-09 20:58:45
 */
@RestController
@RequestMapping("identityverification")
public class IdentityverificationController {

    /**
     * 服务对象
     */
    @Autowired
    private IdentityverificationService identityverificationService;

}
