package com.nd.share.demo.service;

import com.nd.share.demo.domain.OfflinePkg;
import com.nd.share.demo.service.Entity.offlineListInfoOutput.OfflineListInfoOutput;

import java.util.List;
import java.util.Map;

/**
 * 离线包
 * @author 郭晓斌(121017)
 * @version created on 20160601.
 */
public interface OfflinePkgService {

    /**
     * 离线包版本保存
     *
     * @param offlinePkg
     */
    void save(OfflinePkg offlinePkg);

    /**
     * 获取离线包版本信息
     *
     */
    List<OfflinePkg> getOfflinePkg();

//    /**
//     * 获取更新列表
//     *
//     * @param offlinePkg 更新条件
//     * @return
//     */
//    List<OfflinePkg> updateOfflimePkg(OfflinePkg offlinePkg);
    /**
     * 保存离线包列表
     * @param pkgList
     * @return
     */
    public void saveOfflinePkgs(OfflinePkg pkg);
    /**
     * 获取更新离线包URL列表
     * @param typeId 资源类型ID
     * @param version 客户端当前版本号 (必传,若为空则传-1)
     * @return 需要更新的离线包URL列表
     */
    List<OfflinePkg> getOfflinePkgsAdding(String typeId , Integer version);
    /**
     * 获取资源包列表
     * @param requestmap 传入整合参数
     * @param isNeedPkgs
     * @return 资源包列表
     */
    List<OfflineListInfoOutput> getOfflineListInfo(List<Map<String,Object>> requestmap, int isNeedPkgs);
    /**
     * 获取但前版本应该更新的总包大小
     * @param typeId
     * @param version
     * @return 文件大小位数
     */
    public long getOfflinePkgsAddingSize(String typeId, int version);
}
