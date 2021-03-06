package cn.enjoy.sys.controller;

import cn.enjoy.core.exception.BusinessException;
import cn.enjoy.core.utils.CommonConstant;
import cn.enjoy.core.utils.response.HttpResponseBody;
import cn.enjoy.core.utils.response.ResponseCodeConstant;
import cn.enjoy.sys.fs.FastDFSClientService;
import cn.enjoy.sys.model.Department;
import cn.enjoy.sys.model.MenuModel;
import cn.enjoy.sys.model.SysResource;
import cn.enjoy.sys.model.SysUser;
import cn.enjoy.sys.service.IDepartmentService;
import cn.enjoy.sys.service.ILoginService;
import cn.enjoy.sys.service.IResourceService;
import com.alibaba.fastjson.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登录管理
 *
 * @author Jack
 * @date 2017/4/19
 */
@RestController
@RequestMapping("/api/system/")
public class LoginController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IResourceService iResourceService;

    @Autowired
    private ILoginService iLoginService;

    @Autowired
    private IDepartmentService iDepartmentService;

    @Resource
    private FastDFSClientService fastDFSClientService;

    /**
     * 获取登录用户的菜单信息
     *
     * @param userId
     * @return
     * @throws Exception
     * @author Jack
     * @date 2020/9/8
     * @version
     */
    @GetMapping("getLoginResource")
    public HttpResponseBody<Map<String, Object>> getLoginResource(String userId) {
        List<SysResource> sysResources = iResourceService.selectbyUserId(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("resource", sysResources);
        return HttpResponseBody.successResponse("查询成功", result);
    }

    /**
     * 获取图片服务器的地址
     *
     * @return
     * @throws Exception
     * @author Jack
     * @date 2020/9/8
     * @version
     */
    @GetMapping("getFileServerUrl")
    public HttpResponseBody<String> getFileServerUrl() {
        String dfsPath = fastDFSClientService.getDfsPath();
        return HttpResponseBody.successResponse("查询成功", dfsPath);
    }

    /**
     * 没有登录
     *
     * @return
     * @throws Exception
     * @author Jack
     * @date 2020/9/8
     * @version
     */
    @RequestMapping(value = "unLogin", method = {RequestMethod.GET, RequestMethod.POST})
    public HttpResponseBody unLogin() {
        return new HttpResponseBody(ResponseCodeConstant.UN_LOGIN_ERROR, "没有登陆");
    }

    /**
     * 没有权限,请重新登陆
     *
     * @return
     * @throws Exception
     * @author Jack
     * @date 2020/9/8
     * @version
     */
    @RequestMapping(value = "accessDenied", method = {RequestMethod.GET, RequestMethod.POST})
    public HttpResponseBody accessDenied() {
        return new HttpResponseBody(ResponseCodeConstant.ACCESS_DENIED, "没有权限,请重新登陆！");
    }

    /**
     * 已经登陆成功
     *
     * @return
     * @throws Exception
     * @author Jack
     * @date 2020/9/8
     * @version
     */
    @GetMapping("logined")
    public HttpResponseBody logined() {
        return new HttpResponseBody(ResponseCodeConstant.SUCCESS, "已经登陆成功！");
    }

    /**
     * 用户登录接口
     *
     * @param session
     * @param userName
     * @param password
     * @param rememberMe
     * @return
     * @throws Exception
     * @author Jack
     * @date 2020/9/8
     * @version
     */
    @PostMapping("login")
    public HttpResponseBody toLogin(HttpSession session, String userName, String password, @RequestParam(defaultValue = "false", required = false) boolean rememberMe) {
        Map<String, Object> data = null;
        String message = null;
        try {
            data = loginAuthenticate(userName, password, rememberMe);
        } catch (Exception e) {
            message = e.getMessage();
            this.log(e);
        }
        if (data == null) {
            return HttpResponseBody.failResponse(message);
        } else {
            return HttpResponseBody.successResponse("登录成功", data);
        }
    }

    /**
     * 用户登出接口
     *
     * @return
     * @throws Exception
     * @author Jack
     * @date 2020/9/8
     * @version
     */
    @PostMapping("logout")
    public HttpResponseBody logout() {
        SecurityUtils.getSubject().logout();
        return HttpResponseBody.successResponse("登出成功");
    }

    private Map<String, Object> loginAuthenticate(String loginName, String password, boolean rememberMe) {
        logger.info("用户{}尝试登录", loginName);
        Map<String, Object> data = new HashMap<>();
        SysUser user;
        //host 用来判断前后台登陆。
        UsernamePasswordToken token = new UsernamePasswordToken(loginName, password);
        if (rememberMe) {
            token.setRememberMe(true);
        }
        try {
            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            user = (SysUser) subject.getSession().getAttribute(CommonConstant.SESSION_USER_KEY);
            if (user != null) {
                user.setPasswordRand(null);
                user.setPassword(null);
            }

            List<MenuModel> menuModels = iLoginService.queryPermissionList(JSONObject.toJSONString(user));
            List<Department> departments = iDepartmentService.selectByUserId(getSessionUserId());
            data.put("userInfo", user);
            data.put("departments", departments);
            data.put("authorityInfo", menuModels);
            logger.info("------------------用户" + loginName + "登录成功-------------------");
        } catch (AuthenticationException e) {
            token.clear();
            throw new BusinessException(ResponseCodeConstant.USER_LOGIN_FAIL_PASSWORD_FAIL, "用户名或密码错误！");
        } catch (Exception e2) {
            logger.error("系统错误", e2);
            token.clear();
            throw new BusinessException("系统错误");
        }
        return data;
    }

    /**
     * 根据父菜单id获取菜单信息
     *
     * @param parentId
     * @return
     * @throws Exception
     * @author Jack
     * @date 2020/9/8
     * @version
     */
    @GetMapping("getResourceByParentId")
    public HttpResponseBody<Map<String, Object>> getResourceByParentId(String parentId) {
        List<SysResource> sysResources = iResourceService.selectResourceByParentId(parentId);
        Map<String, Object> result = new HashMap<>();
        result.put("resource", sysResources);
        return HttpResponseBody.successResponse("查询成功", result);
    }

    private void log(Exception e) {
        if (e instanceof BusinessException) {
            BusinessException be = (BusinessException) e;
            //如果是密码错了就不打堆栈了
            if (ResponseCodeConstant.USER_LOGIN_FAIL_PASSWORD_FAIL.equals(be.getCode())) {
                logger.warn(e.getMessage());
            } else {
                logger.warn(e.getMessage(), e);
            }
        } else {
            logger.warn(e.getMessage(), e);
        }
    }

}
