package com.macro.mall.tiny.modules.ums.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.modules.ums.model.UmsMenu;
import com.macro.mall.tiny.modules.ums.model.UmsResource;
import com.macro.mall.tiny.modules.ums.model.UmsRole;
import com.macro.mall.tiny.modules.ums.service.UmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 后台用户角色管理
 * Created by macro on 2018/9/30.
 */
@RestController
@Api(tags = "UmsRoleController")
@Tag(name = "UmsRoleController",description = "后台用户角色管理")
@RequestMapping("/role")
public class UmsRoleController {
    @Resource
    private UmsRoleService roleService;

    @ApiOperation("添加角色")
    @PostMapping(value = "/create")
    public boolean create(@RequestBody UmsRole role) {
        return roleService.create(role);
    }

    @ApiOperation("修改角色")
    @PostMapping(value = "/update/{id}")
    public boolean update(@PathVariable Long id, @RequestBody UmsRole role) {
        role.setId(id);
        return roleService.updateById(role);
    }

    @ApiOperation("批量删除角色")
    @PostMapping(value = "/delete")
    public boolean delete(@RequestParam("ids") List<Long> ids) {
        return roleService.delete(ids);
    }


    @ApiOperation("获取所有角色")
    @GetMapping(value = "/listAll")
    public List<UmsRole> listAll() {
        return roleService.list();
    }

    @ApiOperation("根据角色名称分页获取角色列表")
    @GetMapping(value = "/list")
    public CommonPage<UmsRole> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<UmsRole> roleList = roleService.list(keyword, pageSize, pageNum);
        return CommonPage.restPage(roleList);
    }

    @ApiOperation("修改角色状态")
    @PostMapping(value = "/updateStatus/{id}")
    public boolean updateStatus(@PathVariable Long id, Integer status) {
        UmsRole umsRole = new UmsRole();
        umsRole.setId(id);
        umsRole.setStatus(status);
        return roleService.updateById(umsRole);
    }

    @ApiOperation("获取角色相关菜单")
    @GetMapping(value = "/listMenu/{roleId}")
    public List<UmsMenu> listMenu(@PathVariable Long roleId) {
        return roleService.listMenu(roleId);
    }

    @ApiOperation("获取角色相关资源")
    @GetMapping(value = "/listResource/{roleId}")
    public List<UmsResource> listResource(@PathVariable Long roleId) {
        return roleService.listResource(roleId);
    }

    @ApiOperation("给角色分配菜单")
    @PostMapping(value = "/allocMenu")
    public int allocMenu(@RequestParam Long roleId, @RequestParam List<Long> menuIds) {
        return roleService.allocMenu(roleId, menuIds);
    }

    @ApiOperation("给角色分配资源")
    @PostMapping(value = "/allocResource")
    public int allocResource(@RequestParam Long roleId, @RequestParam List<Long> resourceIds) {
        return roleService.allocResource(roleId, resourceIds);
    }

}
