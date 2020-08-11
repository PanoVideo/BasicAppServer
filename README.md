# BasicAppServer
Basic App Server for BasicVideoCall to get pano token

这是一个简单的App Server 示例代码(Go/Java)，可以和[BasicVideoCall](https://github.com/PanoVideo/video-call-samples.git) 搭配使用,完成基本上音视频功能
## 第一步 
将自己的appSecret 填入到该应用的 **APP_SECRET** 变量中，启动该应用。
## 第二步
将相关参数如appId/channelId等参数填入BasicVideoCall，然后请求该App Server, 获取token。

启动该示例代码之后，你可以像这样发出类似请求
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

## 第三步
通过token完成和Pano Cloud的音视频交互

Tips
>1.请先到https://console.pano.video 注册登录
>2.在应用管理中创建自己的应用
>3.将appId和appSecret 放到该示例代码中

---
完整的文档，请访问拍乐云 [文档中心](https://developer.pano.video/getting-started/intro/)
