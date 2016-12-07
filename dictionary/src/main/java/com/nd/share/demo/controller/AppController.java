package com.nd.share.demo.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nd.share.demo.constants.Constants;
import com.nd.share.demo.domain.AppUpdInfo;
import com.nd.share.demo.service.AppService;

/**
 * 热修复检测
 *
 * @author jinmj
 * @date : 2016年6月14日 下午3:29:55
 */
@RestController
@RequestMapping(value = Constants.VERSION_CURRENT + "/update")
public class AppController {

	@Resource
	private AppService appService;

	/**
     * 热修复检测
     *
     * @param Integer version
     * @param Integer type
     * @return
     */
    @RequestMapping(value = "/app/{version}", method = RequestMethod.GET)
    private AppUpdInfo getAppVersionInfo(@PathVariable("version") Integer version, @RequestParam(value = "type", required = true) Integer type) {
         return appService.getAppVersionInfo(version, type);
    }
    /*@RequestMapping(value = "/app/{version}/update", method = RequestMethod.GET)
    private AppUpdInfo updateAppVersionInfo(@PathVariable("version") Integer version, @RequestParam(value = "type", required = true) Integer type) {
        return appService.saveAppVersionInfo(version, type);
    }*/

}