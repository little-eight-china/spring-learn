# <img src="assets/spring-framework.png" width="80" height="80"> spring-learn
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)](https://github.com/little-eight-china/spring-learn)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![Stars](https://img.shields.io/github/stars/little-eight-china/spring-learn)](https://img.shields.io/github/stars/little-eight-china/spring-learn)
[![Forks](https://img.shields.io/github/forks/little-eight-china/spring-learn)](https://img.shields.io/github/forks/little-eight-china/spring-learn)

## 关于

**spring-learn**是一个从简到繁一步步搭建的简化版spring的项目，它能帮助你快速熟悉spring源码和掌握spring的核心原理。抽取了spring的核心逻辑，保留spring的核心功能:，包括ioc的bean生命周期和作用域、bean容器扩展点、资源加载器、注解注入，以及应用上下文，事件监听器，aop等核心功能。

## 学习说明
### [spring-learn-01](https://github.com/little-eight-china/spring-learn/tree/main/spring-learn-01)
第一阶段，主要做个能生产bean对象的工厂出来，就像是做个简易的spring ioc出来。
### [spring-learn-02](https://github.com/little-eight-china/spring-learn/tree/main/spring-learn-02)
第二阶段我期望优化实例化bean这一块。
目前只是支持默认的无参构造函数进行实例化，那下面就做个在实例化时填充成员变量的bean。
定义beanDefinition的作用域：单例Singleton与原型Prototype。
### [spring-learn-03](https://github.com/little-eight-china/spring-learn/tree/main/spring-learn-03)
第三阶段，通过配置文件来实例化bean。
为了能把 Bean 的定义、注册和初始化交给 Spring.xml 配置化处理，那么就需要实现两大块内容，分别是：资源装载器、资源读取器。很明显，装载跟读取分开来做，先讲资源装载器。
### [spring-learn-04](https://github.com/little-eight-china/spring-learn/tree/main/spring-learn-04)
做完了三个阶段，我们可以通过这个做好的框架使用上比较完整的spring ioc功能（注册、获取bean）了，但缺乏了对bean对象的额外扩展功能，所以第四阶段将去做bean对象的扩展。
### [spring-learn-05](https://github.com/little-eight-china/spring-learn/tree/main/spring-learn-05)
第五阶段，做应用上下文，更优雅地管理bean的生命周期。
### [spring-learn-06](https://github.com/little-eight-china/spring-learn/tree/main/spring-learn-06)
第六个阶段，我想用的爽一点，所以先实现注解方式的注入，这样的话，我就不用大费周章地写spring.xml的信息，并且维护bean也更加方便。

## 参考
- [small-spring](https://github.com/fuzhengwei/small-spring)
- [Spring源码](https://github.com/spring-projects/spring-framework)
- 《Spring技术内幕-第2版》
