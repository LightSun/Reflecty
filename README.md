# Reflecty
这是一个强大的关于反射，序列化与反序列化的一个库. 属于框架的框架。

## 特性
--------------
- 1, 支持任意对象类型。 比如： 基本类型和包装，String. collection, map. 任意自定义Collection(可不继承collection), 任意自定义的map(可以不继承map接口). 还有自定义的任意对象类型。
  - 比如test包的demo. 包装了gson. 但是功能比gson更强大。 自定义map类型无需动态注册都是可以序列化和反序列化。当然也支持动态注册。
  - 同样的，泛型任意嵌套也不用注册adapter.
- 2, 支持给类，字段，方法添加注解。 以便序列化/反序列化时对这些注解进行处理。

- 3, 支持关于版本号的兼容.

- 4, 更多特性。请细看demo.

## 使用此库的库
[LightSun/ReflectyIo](https://github.com/LightSun/ReflectyIo)
