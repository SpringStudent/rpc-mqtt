[English](README.md) | [中文](README_zh.md)

#### RPC Framework Based on the MQTT Protocol

* No need for a registration center(depending on mqtt broker)
* Uses JSON for message transmission
* Supports both broadcast and single calls
* Consumers do not need to rely on the provider's JAR
* Consumer and provider networks can be isolated
* Providers are not required to implement interfaces

#### Use Case Explanation

When a service within one local area network (LAN) needs to access a client program in another LAN, if the two networks
cannot directly communicate, RPC calls via Dubbo become unfeasible. Additionally, if the client program lacks a public
IP address, sending commands over the HTTP protocol is also impossible. In such scenarios, a publicly deployed MQTT
Broker can be utilized to enable cross-network program invocation through rpc-mqtt.

#### Usage Guide

First, you need an MQTT broker. The code can refer to rpc-mqtt-demo.

#### Other Ideas

It is possible to use the publish-subscribe model of the MQTT broker to perform service governance.

For example, the consumer can subscribe to a management configuration topic, such as rpc/mqtt/req/config. A payload
indicating that a specific service is offline can be sent to this topic. After receiving the service offline payload,
the consumer will no longer call that service. Additionally, the service provider’s weight can be dynamically published
in the same topic. When the consumer receives the payload, it can parse the weight of the service provider and make
calls accordingly based on the weight. Additionally, routing strategies can be extended based on the service provider's
clientId, such as configuring an MVEL expression during the call. The call will only be made if the clientId meets the 
conditions specified by the MVEL expression.
