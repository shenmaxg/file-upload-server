# file-upload-server
【服务端】大文件上传解决方案，切片，断点续传，秒传。

### 介绍
使用 SpringBoot 搭建的文件上传服务端。负责切片文件的接收，文件 Hash 值记录。

配合项目 file-upload-react 使用，项目的完整预览效果如下：

![preview](https://github.com/shenmaxg/file-upload-react/blob/main/public/iamge/preview.gif)

### 启动
运行 FileUploadServerApplication 中的 main 方法。服务会启动在 8080 端口。

### 注意事项
1. application.properties 中配置了文件上传的路径，目前的路径是 /Users/songmengxiang/workspace/temp/，请大家在启动服务之前修改为本机地址。
2. 目前项目没有链接数据库，文件的数据存储在内存中，重启项目后会丢失数据。可以根据需求增加一个 dao 层。

### 相关阅读

[如何做大文件上传](https://zhuanlan.zhihu.com/p/386493135)