package com.ruoyi.framework.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.Filter;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.security.CipherUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.framework.shiro.realm.UserRealm;
import com.ruoyi.framework.shiro.session.OnlineSessionDAO;
import com.ruoyi.framework.shiro.session.OnlineSessionFactory;
import com.ruoyi.framework.shiro.web.CustomShiroFilterFactoryBean;
import com.ruoyi.framework.shiro.web.filter.LogoutFilter;
import com.ruoyi.framework.shiro.web.filter.captcha.CaptchaValidateFilter;
import com.ruoyi.framework.shiro.web.filter.kickout.KickoutSessionFilter;
import com.ruoyi.framework.shiro.web.filter.online.OnlineSessionFilter;
import com.ruoyi.framework.shiro.web.filter.sync.SyncOnlineSessionFilter;
import com.ruoyi.framework.shiro.web.session.OnlineWebSessionManager;
import com.ruoyi.framework.shiro.web.session.SpringSessionValidationScheduler;
import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;

/**
 * 权限配置加载
 * 
 * @author ruoyi
 */
@Configuration
public class ShiroConfig
{
    /**
     * Session超时时间，单位为毫秒（默认30分钟）
     */
    @Value("${shiro.session.expireTime}")
    private int expireTime;

    /**
     * 相隔多久检查一次session的有效性，单位毫秒，默认就是10分钟
     */
    @Value("${shiro.session.validationInterval}")
    private int validationInterval;

    /**
     * 同一个用户最大会话数
     */
    @Value("${shiro.session.maxSession}")
    private int maxSession;

    /**
     * 踢出之前登录的/之后登录的用户，默认踢出之前登录的用户
     */
    @Value("${shiro.session.kickoutAfter}")
    private boolean kickoutAfter;

    /**
     * 验证码开关
     */
    @Value("${shiro.user.captchaEnabled}")
    private boolean captchaEnabled;

    /**
     * 验证码类型
     */
    @Value("${shiro.user.captchaType}")
    private String captchaType;

    /**
     * 设置Cookie的域名
     */
    @Value("${shiro.cookie.domain}")
    private String domain;

    /**
     * 设置cookie的有效访问路径
     */
    @Value("${shiro.cookie.path}")
    private String path;

    /**
     * 设置HttpOnly属性
     */
    @Value("${shiro.cookie.httpOnly}")
    private boolean httpOnly;

    /**
     * 设置Cookie的过期时间，秒为单位
     */
    @Value("${shiro.cookie.maxAge}")
    private int maxAge;

    /**
     * 设置cipherKey密钥
     */
    @Value("${shiro.cookie.cipherKey}")
    private String cipherKey;

    /**
     * 登录地址
     */
    @Value("${shiro.user.loginUrl}")
    private String loginUrl;

    /**
     * 权限认证失败地址
     */
    @Value("${shiro.user.unauthorizedUrl}")
    private String unauthorizedUrl;

    /**
     * 是否开启记住我功能
     */
    @Value("${shiro.rememberMe.enabled: false}")
    private boolean rememberMe;

    /**
     * 缓存管理器 使用Ehcache实现
     */
    @Bean
    public EhCacheManager getEhCacheManager()
    {
        net.sf.ehcache.CacheManager cacheManager = net.sf.ehcache.CacheManager.getCacheManager("ruoyi");
        EhCacheManager em = new EhCacheManager();
        if (StringUtils.isNull(cacheManager))
        {
            em.setCacheManager(new net.sf.ehcache.CacheManager(getCacheManagerConfigFileInputStream()));
            return em;
        }
        else
        {
            em.setCacheManager(cacheManager);
            return em;
        }
    }

    /**
     * 返回配置文件流 避免ehcache配置文件一直被占用，无法完全销毁项目重新部署
     */
    protected InputStream getCacheManagerConfigFileInputStream()
    {
        String configFile = "classpath:ehcache/ehcache-shiro.xml";
        InputStream inputStream = null;
        try
        {
            inputStream = ResourceUtils.getInputStreamForPath(configFile);
            byte[] b = IOUtils.toByteArray(inputStream);
            InputStream in = new ByteArrayInputStream(b);
            return in;
        }
        catch (IOException e)
        {
            throw new ConfigurationException(
                    "Unable to obtain input stream for cacheManagerConfigFile [" + configFile + "]", e);
        }
        finally
        {
            IOUtils.closeQuietly(inputStream);
        }
    }

    /**
     * 自定义Realm
     */
    @Bean
    public UserRealm userRealm(EhCacheManager cacheManager)
    {
        UserRealm userRealm = new UserRealm();
        userRealm.setAuthorizationCacheName(Constants.SYS_AUTH_CACHE);
        userRealm.setCacheManager(cacheManager);
        return userRealm;
    }

    /**
     * 自定义sessionDAO会话
     */
    @Bean
    public OnlineSessionDAO sessionDAO()
    {
        OnlineSessionDAO sessionDAO = new OnlineSessionDAO();
        return sessionDAO;
    }

    /**
     * 自定义sessionFactory会话
     */
    @Bean
    public OnlineSessionFactory sessionFactory()
    {
        OnlineSessionFactory sessionFactory = new OnlineSessionFactory();
        return sessionFactory;
    }

    /**
     * 会话管理器
     */
    @Bean
    public OnlineWebSessionManager sessionManager()
    {
        OnlineWebSessionManager manager = new OnlineWebSessionManager();
        // 加入缓存管理器
        manager.setCacheManager(getEhCacheManager());
        // 删除过期的session
        manager.setDeleteInvalidSessions(true);
        // 设置全局session超时时间
        manager.setGlobalSessionTimeout(expireTime * 60 * 1000);
        // 去掉 JSESSIONID
        manager.setSessionIdUrlRewritingEnabled(false);
        // 定义要使用的无效的Session定时调度器
        manager.setSessionValidationScheduler(SpringUtils.getBean(SpringSessionValidationScheduler.class));
        // 是否定时检查session
        manager.setSessionValidationSchedulerEnabled(true);
        // 自定义SessionDao
        manager.setSessionDAO(sessionDAO());
        // 自定义sessionFactory
        manager.setSessionFactory(sessionFactory());
        return manager;
    }

    /**
     * 安全管理器
     */
    @Bean
    public SecurityManager securityManager(UserRealm userRealm)
    {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 设置realm.
        securityManager.setRealm(userRealm);
        // 记住我
        securityManager.setRememberMeManager(rememberMe ? rememberMeManager() : null);
        // 注入缓存管理器;
        securityManager.setCacheManager(getEhCacheManager());
        // session管理器
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * 退出过滤器
     */
    public LogoutFilter logoutFilter()
    {
        LogoutFilter logoutFilter = new LogoutFilter();
        logoutFilter.setLoginUrl(loginUrl);
        return logoutFilter;
    }

    /**
     * Shiro过滤器配置
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager)
    {
        CustomShiroFilterFactoryBean shiroFilterFactoryBean = new CustomShiroFilterFactoryBean();
        // Shiro的核心安全接口,这个属性是必须的
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        // 身份认证失败，则跳转到登录页面的配置
        shiroFilterFactoryBean.setLoginUrl(loginUrl);
        // 权限认证失败，则跳转到指定页面
        shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);
        // Shiro连接约束配置，即过滤链的定义
        LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 对静态资源设置匿名访问
        filterChainDefinitionMap.put("/template4/**", "anon");
        filterChainDefinitionMap.put("/obj/**", "anon");
        filterChainDefinitionMap.put("/favicon.ico**", "anon");
        filterChainDefinitionMap.put("/ruoyi.png**", "anon");
        filterChainDefinitionMap.put("/html/**", "anon");
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/docs/**", "anon");
        filterChainDefinitionMap.put("/fonts/**", "anon");
        filterChainDefinitionMap.put("/img/**", "anon");
        filterChainDefinitionMap.put("/ajax/**", "anon");
        filterChainDefinitionMap.put("/file/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/ruoyi/**", "anon");
        filterChainDefinitionMap.put("/captcha/captchaImage**", "anon");
        // 退出 logout地址，shiro去清除session
        filterChainDefinitionMap.put("/logout", "logout");
        filterChainDefinitionMap.put("/end", "anon,captchaValidate");
        filterChainDefinitionMap.put("/end1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getUploadList", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getUploadList1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getTally", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getTallyLine", "anon,captchaValidate");
        filterChainDefinitionMap.put("/putaway", "anon,captchaValidate");
        filterChainDefinitionMap.put("/putawayList", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getStorageList", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getStorageList1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/putawayOrder", "anon,captchaValidate");
        filterChainDefinitionMap.put("/putawayEmp", "anon,captchaValidate");
        filterChainDefinitionMap.put("/warehouseList", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getStorage", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getSorting", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getSortingList", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getSortingList1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getSortingLine", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getDelivery", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getDeliveryList", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getDeliveryLine", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getDeliveryLine1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/abc/importStaticData", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/abc/importData", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/abc/importData1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/abc/importData2", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/eiq/importData", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/eiq/importStaticData", "anon,captchaValidate");
        filterChainDefinitionMap.put("/warehouse", "anon,captchaValidate");
        filterChainDefinitionMap.put("//system/production/production", "anon,captchaValidate");
        filterChainDefinitionMap.put("/takeDown", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getLoading", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getLoadingList", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getUpload", "anon,captchaValidate");
        filterChainDefinitionMap.put("/getUploadLine", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/simu/index", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/simu/index1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/simu/index2", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/simu/index3", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/simu/index4", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/simu/index5", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/simu/index6", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/simu/index7", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/simu/index8", "anon,captchaValidate");

        filterChainDefinitionMap.put("/system/simulog/index", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/simulog/index1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/simulog/index2", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/simulog/index3", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/simulog/index4", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/simulog/index5", "anon,captchaValidate");

        // 不需要拦截的访问
        filterChainDefinitionMap.put("/system/public", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/public/list", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/public/export", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/public/add", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/public/edit", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/public/remove", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass1/demo", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass2", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass2/index1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass3/abc", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project11/ass3/abc", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass3/eiq", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass3/pcb", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project11/ass3/eiq", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project11/ass3/pcb", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project11/ass3/pcb", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass4/customer", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass4/goods", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass4/order", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass4/supplier", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass5/eoq", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass5/eoq1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass6/index", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass6/index1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass6/index2", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass6/index3", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass6/index4", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project1/ass7/index", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project2/ass1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project2/ass2/transport", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project2/ass2/send", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project2/ass3/order", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project2/ass3/goods", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project2/ass3/chanel", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project2/ass3/customer", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project2/ass4", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project2/ass5/index", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project2/ass5/index1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project2/ass5/index2", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project2/ass5/index3", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project2/ass5/index4", "anon,captchaValidate");

        filterChainDefinitionMap.put("/system/project3/ass2/order", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project3/ass2/goods", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project3/ass2/customer", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project3/ass2/supplier", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project3/demo", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project3/ass4", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project4/ass1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project4/ass2", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project4/ass3", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/project4/ass4", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/net", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/net1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/net/table", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/net/table1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/net/table2", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/net/table3", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/net/table4", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/netsend/list", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/netsend/export", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/netsend/importData", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/netsend/importTemplate", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/point/point/networkLine", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/point/point/networkplan1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/point/point/network", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/point/point/get3", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/need/list", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/path/list", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/inventory/list", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/costs/list", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/netmap/list", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/netmap1/list", "anon,captchaValidate");
        filterChainDefinitionMap.put("/mainSim", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/score", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/score/list", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/score/export", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/op", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/op/list", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/op/list1", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/op/list2", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/op/list3", "anon,captchaValidate");
        filterChainDefinitionMap.put("/system/op/add", "anon,captchaValidate");
        filterChainDefinitionMap.put("/LogisticsAnalysis//ScoreRank/Index", "anon,captchaValidate");
        filterChainDefinitionMap.put("/StartSimulator/TryStopProcess", "anon,captchaValidate");
        filterChainDefinitionMap.put("/LogisticsAnalysis/Account/LoginToken", "anon,captchaValidate");
        filterChainDefinitionMap.put("/33", "anon,captchaValidate");
        filterChainDefinitionMap.put("/LogisticsAnalysis/ScoreSgGp/GetScore", "anon,captchaValidate");
        filterChainDefinitionMap.put("/account/getContent", "anon,captchaValidate");
        filterChainDefinitionMap.put("/account/blog/open/post", "anon,captchaValidate");
        filterChainDefinitionMap.put("/account/blog/open/list", "anon,captchaValidate");
        filterChainDefinitionMap.put("/account/blog/eidt", "anon,captchaValidate");
        filterChainDefinitionMap.put("/login", "anon,captchaValidate");
        // 注册相关
        filterChainDefinitionMap.put("/register", "anon,captchaValidate");
        // 系统权限列表
        // filterChainDefinitionMap.putAll(SpringUtils.getBean(IMenuService.class).selectPermsAll());

        Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
        filters.put("onlineSession", onlineSessionFilter());
        filters.put("syncOnlineSession", syncOnlineSessionFilter());
        filters.put("captchaValidate", captchaValidateFilter());
        filters.put("kickout", kickoutSessionFilter());
        // 注销成功，则跳转到指定页面
        filters.put("logout", logoutFilter());
        shiroFilterFactoryBean.setFilters(filters);

        // 所有请求需要认证
        filterChainDefinitionMap.put("/**", "user,kickout,onlineSession,syncOnlineSession");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    /**
     * 自定义在线用户处理过滤器
     */
    public OnlineSessionFilter onlineSessionFilter()
    {
        OnlineSessionFilter onlineSessionFilter = new OnlineSessionFilter();
        onlineSessionFilter.setLoginUrl(loginUrl);
        onlineSessionFilter.setOnlineSessionDAO(sessionDAO());
        return onlineSessionFilter;
    }

    /**
     * 自定义在线用户同步过滤器
     */
    public SyncOnlineSessionFilter syncOnlineSessionFilter()
    {
        SyncOnlineSessionFilter syncOnlineSessionFilter = new SyncOnlineSessionFilter();
        syncOnlineSessionFilter.setOnlineSessionDAO(sessionDAO());
        return syncOnlineSessionFilter;
    }

    /**
     * 自定义验证码过滤器
     */
    public CaptchaValidateFilter captchaValidateFilter()
    {
        CaptchaValidateFilter captchaValidateFilter = new CaptchaValidateFilter();
        captchaValidateFilter.setCaptchaEnabled(captchaEnabled);
        captchaValidateFilter.setCaptchaType(captchaType);
        return captchaValidateFilter;
    }

    /**
     * cookie 属性设置
     */
    public SimpleCookie rememberMeCookie()
    {
        SimpleCookie cookie = new SimpleCookie("rememberMe");
        cookie.setDomain(domain);
        cookie.setPath(path);
        cookie.setHttpOnly(httpOnly);
        cookie.setMaxAge(maxAge * 24 * 60 * 60);
        return cookie;
    }

    /**
     * 记住我
     */
    public CookieRememberMeManager rememberMeManager()
    {
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        if (StringUtils.isNotEmpty(cipherKey))
        {
            cookieRememberMeManager.setCipherKey(Base64.decode(cipherKey));
        }
        else
        {
            cookieRememberMeManager.setCipherKey(CipherUtils.generateNewKey(128, "AES").getEncoded());
        }
        return cookieRememberMeManager;
    }

    /**
     * 同一个用户多设备登录限制
     */
    public KickoutSessionFilter kickoutSessionFilter()
    {
        KickoutSessionFilter kickoutSessionFilter = new KickoutSessionFilter();
        kickoutSessionFilter.setCacheManager(getEhCacheManager());
        kickoutSessionFilter.setSessionManager(sessionManager());
        // 同一个用户最大的会话数，默认-1无限制；比如2的意思是同一个用户允许最多同时两个人登录
        kickoutSessionFilter.setMaxSession(maxSession);
        // 是否踢出后来登录的，默认是false；即后者登录的用户踢出前者登录的用户；踢出顺序
        kickoutSessionFilter.setKickoutAfter(kickoutAfter);
        // 被踢出后重定向到的地址；
        kickoutSessionFilter.setKickoutUrl("/login?kickout=1");
        return kickoutSessionFilter;
    }

    /**
     * thymeleaf模板引擎和shiro框架的整合
     */
    @Bean
    public ShiroDialect shiroDialect()
    {
        return new ShiroDialect();
    }

    /**
     * 开启Shiro注解通知器
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
            @Qualifier("securityManager") SecurityManager securityManager)
    {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
}
