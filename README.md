# BasicAppServer
Basic App Server for BasicVideoCall to get Pano Token

这是一个简单的App Server 示例代码(Go/Java)，可以和[BasicVideoCall](https://github.com/PanoVideo/video-call-samples.git) 搭配使用，完成基本的音视频功能
## 第一步 
将自己的appSecret 填入到该应用的 **APP_SECRET** 变量中，启动该应用。

当需要手动获取token的时候，你可以像下面一样发出HTTP请求，参数中的**appId** 需要填入自己的appId。

```shell script
curl --location --request POST 'http://localhost:8080/app/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "appId": "xxx", 
    "userId": "20190822",
    "channelId": "20190822",
    "duration": 800,
    "privileges": 0,
    "channelDuration": 12
}'
```
## 第二步
将相关参数如appId/channelId等参数填入BasicVideoCall，然后请求该App Server，自动获取token。


## 第三步
通过token完成和Pano Cloud的音视频交互

Tips
>1.请先到https://console.pano.video 注册登录
>2.在应用管理中创建自己的应用
>3.将appId和appSecret 放到该示例代码中

---
完整的文档，请访问拍乐云 [文档中心](https://developer.pano.video/getting-started/intro/)
