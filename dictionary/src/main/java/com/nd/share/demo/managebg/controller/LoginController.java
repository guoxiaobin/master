package com.nd.share.demo.managebg.controller;

import com.alibaba.fastjson.JSONObject;
import com.nd.share.demo.domain.DictionaryTypeList;
import com.nd.share.demo.domain.LcBaseDatas;
import com.nd.share.demo.managebg.dao.CommonDao;
import com.nd.share.demo.managebg.dao.MBUserInfoDao;
import com.nd.share.demo.managebg.dao.MBWhiteListDao;
import com.nd.share.demo.managebg.domain.MBDictionaryMenus;
import com.nd.share.demo.managebg.domain.MBUserInfo;
import com.nd.share.demo.managebg.entity.DictionaryMenuEntity;
import com.nd.share.demo.managebg.util.ContentUtils;
import com.nd.share.demo.managebg.vm.DictionaryCommon;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户登录
 *
 * @author 郭晓斌(121017)
 * @version created on 20160721.
 */
@RestController
public class LoginController {
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private CommonDao commonDao;
    @Resource
    private MBUserInfoDao mbUserInfoDao;
    @Resource
    private MBWhiteListDao mbWhiteListDao;

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    private JSONObject checkLogin(@RequestBody Map<String, String> map, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        String username = map.get("username");
        String pwd = null;

        // 获取客户端IP
//        String ip = ContentUtils.getIpAddr(request);
//        long count = mbWhiteListDao.countWhiteList(ip);
//        if(Long.compare(0,count) == 0){
//            return getLoginInfo(1, "您没有权限登录，请联系管理员[" + ip + "]", "");
//        }

        try {
            // 密码解密
            pwd = new String(ContentUtils.decode(map.get("pwd")), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            return getLoginInfo(1, "服务器出现错误，请联系管理员<br>失败原因:" + e.getMessage(), "");
        }

        List<MBUserInfo> mbUserInfoList = mbUserInfoDao.searchUserInfo(username, pwd);
        if (mbUserInfoList != null && mbUserInfoList.size() > 0) {
            // 记录权限
            request.getSession().setAttribute("role",mbUserInfoList.get(0).getRole());
            jsonObject = getLoginInfo(0, "", mbUserInfoList.get(0).getUsername());
        } else {
            jsonObject = getLoginInfo(1, "该用户不存在", "");
        }

        // 生成登录状态
        request.getSession().setAttribute("status","0");
        return jsonObject;
    }

    /**
     * 登录初始化
     *
     * @return
     */
    @RequestMapping(value = "/WXERSDP01-ERDFDSFRINDEX", method = RequestMethod.GET)
    private ModelAndView login() {
        List<DictionaryMenuEntity> menuList = new ArrayList<>();
        List<DictionaryTypeList> dictionaryTypeLists = mongoTemplate.findAll(DictionaryTypeList.class);
        if (dictionaryTypeLists != null && dictionaryTypeLists.size() > 0) {
            for (int i = 0; i < dictionaryTypeLists.size(); i++) {
                int typeId = dictionaryTypeLists.get(i).getTypeId();
                String dicName = dictionaryTypeLists.get(i).getDicName();
                Query query = Query.query(Criteria.where("parentTypeId").is(typeId));
                // 获取辞典下的菜单列表
                List<MBDictionaryMenus> dictionaryMenuses = mongoTemplate.find(query, MBDictionaryMenus.class);
                if (dictionaryMenuses != null && dictionaryMenuses.size() > 0) {
                    DictionaryMenuEntity dictionaryMenuEntity = new DictionaryMenuEntity();
                    dictionaryMenuEntity.setDicName(dicName);
                    dictionaryMenuEntity.setTypeId(typeId);
                    dictionaryMenuEntity.setDictionaryMenusList(dictionaryMenuses);
                    menuList.add(dictionaryMenuEntity);
                }
            }
        }
        Map<String, List<DictionaryMenuEntity>> dicMap = new HashMap<>();
        dicMap.put("dicMap", menuList);
        ModelAndView view = new ModelAndView("forward:/WQDFXDDJESP/WONDEREINDEX.jsp", dicMap);
        return view;
    }

    /**
     * 获取辞书Datagrid数据
     *
     * @param page             页码
     * @param rows             每页显示行数
     * @param dictionaryCommon 页面Viewmodel
     * @return
     */
    @RequestMapping(value = "/get/common/{type}/{dicType}", method = RequestMethod.POST)
    private Map<String, Object> getDatagridRecords(int page, int rows, DictionaryCommon dictionaryCommon) {
        Map<String, Object> dataGrids = new HashMap<>();
        String operateType = dictionaryCommon.getOperateType();
        String dicType = commonDao.getDictionaryType(dictionaryCommon.getDicType());
        // 获取对应字典表
        String tblLcBaseDatas = CommonDao.Collection.LCBASEDATAS.getName() + "_" + dicType;

        Query query = Query.query(Criteria.where("type").is(dictionaryCommon.getType()));
        query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "id")));
        // 条件检索
        if (operateType != null && !"".equals(operateType)) {
            String key = dictionaryCommon.getKey();
            String value = dictionaryCommon.getValue();
            if (key != null && !"".equals(key)) {
                query.addCriteria(Criteria.where("key").regex(key));
            }
            if (value != null && !"".equals(value)) {
                query.addCriteria(Criteria.where("value").regex(value));
            }
        }

        // 获取总数
        long total = mongoTemplate.count(query, tblLcBaseDatas);

        // 获取分页数据
        if (Integer.compare(1, page) < 0) {
            query.skip((page - 1) * rows);
        }
        query.limit(rows);

        List<LcBaseDatas> lcBaseDatasList = mongoTemplate.find(query, LcBaseDatas.class, tblLcBaseDatas);
        dataGrids.put("total", total);
        dataGrids.put("rows", lcBaseDatasList);
        return dataGrids;
    }

    /**
     * 编辑白名单
     *
     * @param map
     * @return
     */
    @RequestMapping(value = "/edit/ip", method = RequestMethod.POST)
    private JSONObject addAccessIp(@RequestBody Map<String,String> map){
        JSONObject jsonObject = new JSONObject();
        String ip = map.get("ip");
        try {
            long count = mbWhiteListDao.countWhiteList(ip);
            if("save".equals(map.get("type"))){
                if(count > 0){
                    jsonObject.put("status","warn");
                    jsonObject.put("msg","该IP已存在");
                    return jsonObject;
                }
                mbWhiteListDao.saveWhiteList(ip);
            }
            if("delete".equals(map.get("type"))){
                if(count == 0){
                    jsonObject.put("status","warn");
                    jsonObject.put("msg","该IP不存在");
                    return jsonObject;
                }
                mbWhiteListDao.deleteWhiteList(ip);
            }
            jsonObject.put("status","success");
            jsonObject.put("msg","操作成功");
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("status","fail");
            jsonObject.put("msg",e.getMessage());
        }

        return jsonObject;
    }

    /**
     * 获取用户信息
     *
     * @param page
     * @param rows
     * @param dictionaryCommon
     * @return
     */
    @RequestMapping(value = "/get/userinfo", method = RequestMethod.GET)
    private Map<String, Object> getUserInfoDG(int page, int rows, DictionaryCommon dictionaryCommon){
        Map<String, Object> dataGrids = new HashMap<>();
        String operateType = dictionaryCommon.getOperateType();

        Query query = new Query();
        query.with(new Sort(new Sort.Order(Sort.Direction.ASC, "role")));
        // 条件检索
        if (operateType != null && !"".equals(operateType)) {
            String key = dictionaryCommon.getKey();
            String value = dictionaryCommon.getValue();
            if (key != null && !"".equals(key)) {
                query.addCriteria(Criteria.where("username").is(key));
            }
            if (value != null && !"".equals(value)) {
                query.addCriteria(Criteria.where("rolename").is(value));
            }
        }

        // 获取总数
        long total = mongoTemplate.count(query,MBUserInfo.class);
        // 获取分页数据
        if (Integer.compare(1, page) < 0) {
            query.skip((page - 1) * rows);
        }
        query.limit(rows);

        List<MBUserInfo> mbUserInfoList = mongoTemplate.find(query, MBUserInfo.class);
        dataGrids.put("total", total);
        dataGrids.put("rows", mbUserInfoList);
        return dataGrids;
    }

    /**
     * 返回登录信息
     *
     * @param code
     * @param msg
     * @param username
     * @return
     */
    private JSONObject getLoginInfo(int code, String msg, String username) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("msg", msg);
        jsonObject.put("username", username);

        return jsonObject;
    }
}
