#### 基于Mqtt协议的rpc框架

* 无需注册中心
* 基于json传输报文
* 支持广播调用和单个调用
* 消费方不依赖提供方jar
* 消费方和提供方网络可隔离
* 提供方不强制实现接口

#### 使用场景说明

一般用于指令透传，一个局域网内的服务想访问另外一个局域网内的客户端程序，此时客户端程序无公网IP，无法通过http协议发送指令,rpc-mqtt便可用于此场景。

#### 使用指导

首先你需要一个mqtt broker, 代码可参考rpc-mqtt-demo