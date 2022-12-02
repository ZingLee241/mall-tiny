package com.macro.mall.tiny.modules.ums.controller;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.macro.mall.tiny.common.api.CommonPage;
import com.macro.mall.tiny.common.exception.ApiException;
import com.macro.mall.tiny.common.result.ResultCode;
import com.macro.mall.tiny.modules.ums.dto.UmsAdminLoginParam;
import com.macro.mall.tiny.modules.ums.dto.UmsAdminParam;
import com.macro.mall.tiny.modules.ums.dto.UpdateAdminPasswordParam;
import com.macro.mall.tiny.modules.ums.model.UmsAdmin;
import com.macro.mall.tiny.modules.ums.model.UmsRole;
import com.macro.mall.tiny.modules.ums.service.UmsAdminService;
import com.macro.mall.tiny.modules.ums.service.UmsRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 后台用户管理
 * Created by macro on 2018/4/26.
 */
@RestController
@Api(tags = "UmsAdminController")
@Tag(name = "UmsAdminController",description = "后台用户管理")
@RequestMapping("/admin")
public class UmsAdminController {
    @Value("${jwt.tokenHeader}")
    private String tokenHeader;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Resource
    private UmsAdminService adminService;
    @Resource
    private UmsRoleService roleService;

    @ApiOperation("用户注册")
    @PostMapping( "/register")
    public UmsAdmin register(@Validated @RequestBody UmsAdminParam umsAdminParam) {
        return adminService.register(umsAdminParam);
    }

    @ApiOperation("登录以后返回token")
    @PostMapping( "/login")
    public Map<String, String> login(@Validated @RequestBody UmsAdminLoginParam umsAdminLoginParam) {
        String token = adminService.login(umsAdminLoginParam.getUsername(), umsAdminLoginParam.getPassword());
        if (token == null) {
            throw new ApiException(new ResultCode(ResultCode.VALIDATE_FAILED.getCode(), "用户名或密码错误"));
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", token);
        tokenMap.put("tokenHead", tokenHead);
        return tokenMap;
    }

    @ApiOperation("刷新token")
    @GetMapping( "/refreshToken")
    public Map<String, String> refreshToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String refreshToken = adminService.refreshToken(token);
        if (refreshToken == null) {
            throw new ApiException("token已经过期");
        }
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", refreshToken);
        tokenMap.put("tokenHead", tokenHead);
        return tokenMap;
    }

    @ApiOperation("获取当前登录用户信息")
    @GetMapping("/info")
    public Map<String, Object> getAdminInfo(Principal principal) {
        if(principal==null){
            throw new ApiException(ResultCode.UNAUTHORIZED);
        }
        String username = principal.getName();
        UmsAdmin umsAdmin = adminService.getAdminByUsername(username);
        Map<String, Object> data = new HashMap<>();
        data.put("username", umsAdmin.getUsername());
        data.put("menus", roleService.getMenuList(umsAdmin.getId()));
        data.put("icon", umsAdmin.getIcon());
        List<UmsRole> roleList = adminService.getRoleList(umsAdmin.getId());
        if(CollUtil.isNotEmpty(roleList)){
            List<String> roles = roleList.stream().map(UmsRole::getName).collect(Collectors.toList());
            data.put("roles",roles);
        }
        return data;
    }

    @ApiOperation(value = "登出功能")
    @PostMapping("/logout")
    public boolean logout() {
        return true;
    }

    @ApiOperation("根据用户名或姓名分页获取用户列表")
    @GetMapping( "/list")
    public CommonPage<UmsAdmin> list(@RequestParam(value = "keyword", required = false) String keyword,
                                                   @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                                   @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum) {
        Page<UmsAdmin> adminList = adminService.list(keyword, pageSize, pageNum);
        return CommonPage.restPage(adminList);
    }

    @ApiOperation("获取指定用户信息")
    @GetMapping( "/{id}")
    public UmsAdmin getItem(@PathVariable Long id) {
        return adminService.getById(id);
    }

    @ApiOperation("修改指定用户信息")
    @PostMapping( "/update/{id}")
    public boolean update(@PathVariable Long id, @RequestBody UmsAdmin admin) {
        return adminService.update(id, admin);
    }

    @ApiOperation("修改指定用户密码")
    @PostMapping("/updatePassword")
    public boolean updatePassword(@Validated @RequestBody UpdateAdminPasswordParam updatePasswordParam) {
        return adminService.updatePassword(updatePasswordParam);
    }

    @ApiOperation("删除指定用户信息")
    @PostMapping(value = "/delete/{id}")
    public boolean delete(@PathVariable Long id) {
        return adminService.delete(id);
    }

    @ApiOperation("修改帐号状态")
    @RequestMapping(value = "/updateStatus/{id}", method = RequestMethod.POST)
    public boolean updateStatus(@PathVariable Long id,@RequestParam(value = "status") Integer status) {
        UmsAdmin umsAdmin = new UmsAdmin();
        umsAdmin.setStatus(status);
        return adminService.update(id,umsAdmin);
    }

    @ApiOperation("给用户分配角色")
    @RequestMapping( "/role/update")
    public int updateRole(@RequestParam("adminId") Long adminId,
                                   @RequestParam("roleIds") List<Long> roleIds) {
        int count = adminService.updateRole(adminId, roleIds);
        if (count == 0) {
            throw new ApiException("分配失败！");
        }
        return count;
    }

    @ApiOperation("获取指定用户的角色")
    @GetMapping("/role/{adminId}")
    public List<UmsRole> getRoleList(@PathVariable Long adminId) {
        return adminService.getRoleList(adminId);
    }
}
