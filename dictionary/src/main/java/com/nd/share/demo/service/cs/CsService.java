package com.nd.share.demo.service.cs;

import com.nd.sdp.cs.sdk.Dentry;

/**
 * @author jinmj
 * @date : 2016年6月22日 上午11:20:55
 */
public interface CsService {	
	
	public Dentry uploadFile(String uploadfilePath, String saveFileName);  				//文件上传	
	
}
