package com.macro.mall.tiny.modules.ums.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.api.CommonResult;
import com.macro.mall.tiny.modules.ums.model.UmsResource;
import com.macro.mall.tiny.modules.ums.service.UmsResourceService;
import com.macro.mall.tiny.security.component.DynamicSecurityMetadataSource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台资源管理Controller
 * Created by macro on 2020/2/4.
 */
@RestController
@Api(tags = "UmsResourceController")
@Tag(name = "UmsResourceController",description = "后台资源管理")
@RequestMapping("/resource")
public class UmsResourceController {

    @Resource
    private UmsResourceService resourceService;
    @Resource
    private DynamicSecurityMetadataSource dynamicSecurityMetadataSource;

    @ApiOperation("添加后台资源")
    @PostMapping(value = "/create")
    public boolean create(@RequestBody UmsResource umsResource) {
        boolean success = resourceService.create(umsResource);
        dynamicSecurityMetadataSource.clearDataSource();
        return success;
    }

    @ApiOperation("修改后台资源")
    @PostMapping(value = "/update/{id}")
    public boolean update(@PathVariable Long id, @RequestBody UmsResource umsResource) {
        boolean success = resourceService.update(id, umsResource);
        dynamicSecurityMetadataSource.clearDataSource();
        return success;
    }

    @ApiOperation("根据ID获取资源详情")
    @GetMapping(value = "/{id}")
    public CommonResult<UmsResource> getItem(@PathVariable Long id) {
        UmsResource umsResource = resourceService.getById(id);
        return CommonResult.success(umsResource);
    }

    @ApiOperation("根据ID删除后台资源")
    @PostMapping(value = "/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        boolean success = resourceService.delete(id);
        dynamicSecurityMetadataSource.clearDataSource();
        return success;
    }

    @ApiOperation("分页模糊查询后台资源")
    @GetMapping(value = "/list")
    public CommonPage<UmsResource> list(@RequestParam(required = false) Long categoryId,
                                                      @RequestParam(required = false) String nameKeyword,
                                                      @RequestParam(required = false) String urlKeyword,
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<UmsResource> resourceList = resourceService.list(categoryId,nameKeyword, urlKeyword, pageSize, pageNum);
        return CommonPage.restPage(resourceList);
    }

    @ApiOperation("查询所有后台资源")
    @GetMapping(value = "/listAll")
    public List<UmsResource> listAll() {
        return resourceService.list();
    }
}
