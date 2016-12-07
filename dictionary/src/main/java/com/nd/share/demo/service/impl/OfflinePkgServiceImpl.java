package com.nd.share.demo.service.impl;

import com.nd.share.demo.common.response.ErrorCode;
import com.nd.share.demo.common.response.RestfulException;
import com.nd.share.demo.constants.Messages;
import com.nd.share.demo.domain.OfflineListInfo;
import com.nd.share.demo.domain.OfflinePkg;
import com.nd.share.demo.repository.OfflinePkgRepository;
import com.nd.share.demo.repository.dao.OfflineListInfoDao;
import com.nd.share.demo.repository.dao.OfflinePkgDao;
import com.nd.share.demo.service.OfflinePkgService;
import com.nd.share.demo.service.Entity.offlineListInfoOutput.OfflineListInfoOutput;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 热词
 *
 * @author 郭晓斌(121017)
 * @version created on 20160601.
 */
@Service
public class OfflinePkgServiceImpl implements OfflinePkgService {

    @Resource
    private OfflinePkgRepository offlinePkgRepository;

    @Resource
    private OfflinePkgDao offlinePkgDao;
    
    @Resource 
    private OfflineListInfoDao offlineListInfoDao;

    @Value("${offlinepkg.limit}")
    private int offlinepkgLimit = 0;

    /**
     * 离线包版本保存
     *
     * @param offlinePkg
     */
    @Override
    public void save(OfflinePkg offlinePkg) {
        try {
            // 保存意见
            offlinePkgRepository.save(offlinePkg);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestfulException(ErrorCode.SERVICE_ERROR_SYSTEM, Messages.INTERNAL_SERVER_ERROR, String.format("OfflinePkgServiceImpl, msg:%s", e.getMessage()));
        }
    }

    /**
     * 获取离线包版本信息
     */
    @Override
    public List<OfflinePkg> getOfflinePkg() {
        List<OfflinePkg> offlinePkgs = new ArrayList<>();
        try {
            offlinePkgs = offlinePkgDao.getOfflimePkg("createTime", 2, offlinepkgLimit);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RestfulException(ErrorCode.SERVICE_ERROR_SYSTEM, Messages.INTERNAL_SERVER_ERROR, String.format("OfflinePkgServiceImpl, msg:%s", e.getMessage()));
        }

        return offlinePkgs;
    }

	@Override
	public void saveOfflinePkgs(OfflinePkg pkg) {
	    
		offlinePkgDao.insertOfflinePkg(pkg);
	}
	/**
	 * 获取新增的离线包
	 */
	@Override
	public List<OfflinePkg> getOfflinePkgsAdding(String typeId, Integer version) {
		List<OfflinePkg> offlinePkgs = null;
		try {
			List<OfflinePkg> tmppkgs = new ArrayList<OfflinePkg>();
			tmppkgs = offlinePkgDao.getOfflinePkgsAdding(typeId, version, 2);
			if(tmppkgs == null || tmppkgs.size() == 0){
				offlinePkgs = offlinePkgDao.getOfflinePkgsAdding(typeId, version, null); 
			}else{
				OfflinePkg off = tmppkgs.get(0);
				version = off.getVersion();
				offlinePkgs = offlinePkgDao.getOfflinePkgsAdding(typeId, version-1, null);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return offlinePkgs;
	}
	/**
	 * 获取离线包列表信息并转换为输出格式
	 */
	@Override
	public List<OfflineListInfoOutput> getOfflineListInfo(
			List<Map<String, Object>> requestmap , int isNeedPkgs) {
	    List<OfflineListInfoOutput> outputlist = new ArrayList<OfflineListInfoOutput>();
		List<OfflineListInfo> list = offlineListInfoDao.getOfflineListInfo(null);
		//判断是否传入的json为空,立对应flag
		boolean flag = false;
		if(requestmap == null || requestmap.size() == 0){
			flag = true;
		}
		for(OfflineListInfo i : list){
		    OfflineListInfoOutput infoout = new OfflineListInfoOutput();
			Integer version = -1;
			if(flag){
				version = -1 ;
			}else{
				for(Map<String,Object> j : requestmap){
					if(i.getType().equals(j.get("typeId"))){
						try {
							version = (Integer)j.get("version");
						} catch (Exception e) {
							e.printStackTrace();
				            throw new RestfulException(ErrorCode.SERVICE_ERROR_SYSTEM, Messages.INTERNAL_SERVER_ERROR, String.format("OfflinePkgServiceImpl, msg:%s", e.getMessage()));
				        
						}
						
						break;
					}else{
						continue;
					}
				}
			}
			infoout.setSize(getOfflinePkgsAddingSize(i.getType(), version));
			infoout.setOthers(i.getOthers());
			infoout.setType(i.getType());
			infoout.setTitle(i.getTitle());
			
			if(isNeedPkgs == 1){
			    List<OfflinePkg> pkglist = getOfflinePkgsAdding(i.getType(), version);
	            List<Map<String,Object>> pkgs = new ArrayList<Map<String,Object>>();
	            for(OfflinePkg off : pkglist){
	                Map<String,Object> tmpmap = new HashMap<String, Object>();
	                tmpmap.put("flag", off.getFlag());
	                tmpmap.put("version", off.getVersion());
	                tmpmap.put("url", off.getUrl());
	                pkgs.add(tmpmap);
	            }
	            infoout.setPkgs(pkgs);
			}
			
			outputlist.add(infoout);
			
		}
		
		return outputlist;
	}
	/**
	 * 获取新增的离线包补丁大小
	 */
	@Override
	public long getOfflinePkgsAddingSize(String typeId, int version) {
		List<OfflinePkg> pkglist = getOfflinePkgsAdding(typeId, version);
		long sum = 0L;
		for(OfflinePkg i : pkglist){
			sum += i.getSize();
		}
		return sum;
	}


//    /**
//     * 获取更新列表
//     *
//     * @param offlinePkg 更新条件
//     * @return
//     */
//    @Override
//    public List<OfflinePkg> updateOfflimePkg(OfflinePkg offlinePkg){
//        List<OfflinePkg> offlinePkgs = new ArrayList<>();
//        try {
//            offlinePkgs = offlinePkgDao.updateOfflimePkg(offlinePkg);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RestfulException(ErrorCode.SERVICE_ERROR_SYSTEM, Messages.INTERNAL_SERVER_ERROR, String.format("OfflinePkgServiceImpl, msg:%s", e.getMessage()));
//        }
//
//        return offlinePkgs;
//    }
}
