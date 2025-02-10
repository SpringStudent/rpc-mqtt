[English](README.md) | [中文](README_zh.md)

#### RPC Framework Based on the MQTT Protocol

* No need for a registration center
* Uses JSON for message transmission
* Supports both broadcast and single calls
* Consumers do not need to rely on the provider's JAR
* Consumer and provider networks can be isolated
* Providers are not required to implement interfaces

#### Use Case Explanation

This framework is typically used for command transmission (in the IoT field). It allows a service in one local area
network (LAN) to access a client program in another LAN (where the two networks cannot communicate directly and cannot
use Dubbo for RPC). In this case, the client program does not have a public IP and cannot send commands via HTTP
protocol. RPC-MQTT can be used for this scenario.

#### Usage Guide

First, you need an MQTT broker. The code can refer to rpc-mqtt-demo.

#### Other Ideas

It is possible to use the publish-subscribe model of the MQTT broker to perform service governance.

For example, the consumer can subscribe to a management configuration topic, such as rpc/mqtt/req/config. A payload
indicating that a specific service is offline can be sent to this topic. After receiving the service offline payload,
the consumer will no longer call that service. Additionally, the service provider’s weight can be dynamically published
in the same topic. When the consumer receives the payload, it can parse the weight of the service provider and make
calls accordingly based on the weight. Additionally, routing strategies can be extended based on the service provider's clientId, 
such as configuring an MVEL expression during the call. The call will only be made if the clientId meets the conditions specified 
by the MVEL expression.
