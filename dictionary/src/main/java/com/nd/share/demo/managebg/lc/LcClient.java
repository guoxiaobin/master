package com.nd.share.demo.managebg.lc;

import com.nd.gaea.client.http.WafSecurityHttpClient;
import com.nd.share.demo.managebg.constants.Contants;
import com.nd.share.demo.managebg.cs.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by Kylin on 2016/6/23 0023.
 */
@Component
public class LcClient {
    private final static Logger logger = LoggerFactory.getLogger(LcClient.class);

    @Value("${lc.uri}")
    private transient String lcHost;
    @Value("${lc.version}")
    private transient String lcVer;

    public Session getSessionFromLc(String type) {
        String wdType = "assets";
        if (type.equals(Contants.wordType)) {
            wdType = "coursewareobjects";
        }
        try {
            WafSecurityHttpClient wafSecurityHttpClient = new WafSecurityHttpClient();
            // wafSecurityHttpClient.getForEntity()
            String url = this.lcHost + "/" + this.lcVer + "/" + wdType + "/none/uploadurl?uid=686872&renew=false&coverage=Org/nd/";
            Session session = wafSecurityHttpClient.getForObject(url, Session.class);
            return session;

        } catch (Exception e) {
            logger.error("Get session from lc exception: \n" + e);
            return null;
        }

    }

}
