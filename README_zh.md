[English](README.md) | [中文](README_zh.md)

#### 基于Mqtt协议的rpc框架

* 无需注册中心（依赖mqtt broker）
* 基于json传输报文
* 支持广播调用和单个调用
* 消费方可以不依赖提供方jar
* 消费方和提供方网络可隔离
* 提供方不要求必须实现接口
* 可扩展的责任链支持

#### 与dubbo、grpc、motan、等rpc框架的区别

dubbo侧重于内网跨服务之间调用，rpc-mqtt则侧重于不同网络服务之间的调用。

#### 使用场景说明

当一个局域网内的服务需要访问另一个局域网中的客户端程序时，若两者网络无法直接互通，则无法通过Dubbo进行RPC调用，同时客户端程序不具备公网IP地址，导致无法通过HTTP协议发起调用，此时可借助公网部署的MQTT Broker，通过rpc-mqtt实现跨网络的程序调用。

#### 使用指导

首先你需要一个mqtt broker, 代码可参考rpc-mqtt-demo;

#### 其他想法

可以基于mqtt broker的发布-订阅模式，做服务治理的工作；

* 比如消费方订阅管理配置的topic比如'rpc/mqtt/req/config',可以往该topic发送指定某个服务下线的payload,消费方接受到服务下线payload后，将不在调用该服务。
* 在比如还可以在该topic动态发布服务提供方的权重的payload，服务消费方接受到payload,解析得到服务提供方的权重，调用服务的时候根据权重做调用。
* 另外还可以根据服务提供方的clientId扩展做路由策略，比如在调用时配置mvel表达式，当clientId满足mvel表达式指定条件时才会进行调用