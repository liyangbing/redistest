package com.example;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
// 导入对应产品模块的 client
import com.tencentcloudapi.cvm.v20170312.CvmClient;
// 导入要请求接口对应的 request response 类
import com.tencentcloudapi.cvm.v20170312.models.DescribeZonesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeZonesResponse;
//导入可选配置类
import com.tencentcloudapi.common.profile.HttpProfile;

public class DescribeZones{
    public static void main(String [] args) {
        try{
            // 实例化一个认证对象，入参需要传入腾讯云账户 secretId、secretKey
            Credential cred = new Credential("secretId", "secretKey");

            // 实例化要请求产品（以 cvm 为例）的 client 对象
            CvmClient client = new CvmClient(cred, "ap-guangzhou");

            // 实例化一个请求对象
            DescribeZonesRequest req = new DescribeZonesRequest();

            // 通过 client 对象调用想要访问的接口，需要传入请求对象
            DescribeZonesResponse resp = client.DescribeZones(req);


            // 输出 json 格式的字符串回包
            System.out.println(DescribeZonesRequest.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }
}