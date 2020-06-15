package cn.enjoy.sys.service;

import cn.enjoy.sys.model.Oauth2Client;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * @author Ray
 * @date 2018/3/23.
 */
@RequestMapping("/sys/service/IAuthorizeService")
public interface IAuthorizeService {

     @RequestMapping(value = "/checkClientId", method = RequestMethod.POST)
     boolean checkClientId(@RequestParam("clientId") String clientId) ;

     @RequestMapping(value = "/checkClientSecret", method = RequestMethod.POST)
     boolean checkClientSecret(@RequestParam("clientSecret") String clientSecret) ;

     @RequestMapping(value = "/getExpireIn")
     long getExpireIn() ;

     @RequestMapping(value = "/findClientByClientId", method = RequestMethod.POST)
     Oauth2Client findClientByClientId(@RequestParam("clientId") String clientId);

     /**
      * 查找所有的子系统
      * @return
      */
     @RequestMapping(value = "/getClientMap")
     Map<String, Oauth2Client> getClientMap();
}
