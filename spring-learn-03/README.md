# spring-learn-03

第一阶段我们做了一个简易的spring ioc了，就像麻雀一样，五脏俱全了，所以后面的步骤，先慢慢优化这只”麻雀“先。

第二阶段我期望优化实例化bean这一块。

第三阶段，通过配置文件来实例化bean。
为了能把 Bean 的定义、注册和初始化交给 Spring.xml 配置化处理，那么就需要实现两大块内容，分别是：资源装载器、资源读取器。很明显，装载跟读取分开来做，先讲资源装载器。

** 资源装载器 **
定义接口Resource，因为读取xml文件是io操作，所以底层方法定义getInputStream，目前只需要从资源包下读取xml文件所以只有ClassPathResource，后面想通过指定文件或者是网络文件都可以进行Resource的扩展。

再做高层的装载器接口ResourceLoader，方法定义getResource。

底层基础基本ok，做个对外的默认资源装载器类DefaultResourceLoader，目前它只有ClassPathResource。

** 资源读取器 **
定义接口BeanDefinitionReader，因为读取器只需要做2件事，读取资源，注册到bean工厂里，所以有2个成员变量即可：bean注册接口跟资源加载器。
AbstractBeanDefinitionReader抽象类是做基础成员的实例化的。

目前只需解析资源包下装载的xml文件信息，只需要搞个XmlBeanDefinitionReader即可，核心的解析方法doLoadBeanDefinitions

```java
protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException, DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);
        Element root = document.getRootElement();

        List<Element> beanList = root.elements("bean");
        for (Element bean : beanList) {

        String id = bean.attributeValue("id");
        String name = bean.attributeValue("name");
        String className = bean.attributeValue("class");
        String beanScope = bean.attributeValue("scope");

        // 获取 Class，方便获取类中的名称
        Class<?> clazz = Class.forName(className);
        // 优先级 id > name
        String beanName = StrUtil.isNotEmpty(id) ? id : name;
        if (StrUtil.isEmpty(beanName)) {
        beanName = StrUtil.lowerFirst(clazz.getSimpleName());
        }

        // 定义Bean
        BeanDefinition beanDefinition = new BeanDefinition(clazz);
        beanDefinition.setScope(beanScope);

        List<Element> propertyList = bean.elements("property");
        // 读取属性并填充
        for (Element property : propertyList) {
        // 解析标签：property
        String attrName = property.attributeValue("name");
        String attrValue = property.attributeValue("value");
        String attrRef = property.attributeValue("ref");
        // 获取属性值：引入对象、值对象
        Object value = StrUtil.isNotEmpty(attrRef) ? new BeanReference(attrRef) : attrValue;
        // 创建属性信息
        PropertyValue propertyValue = new PropertyValue(attrName, value);
        beanDefinition.getPropertyValues().addPropertyValue(propertyValue);
        }
        if (getRegistry().containsBeanDefinition(beanName)) {
        throw new BeansException("Duplicate beanName[" + beanName + "] is not allowed");
        }
        // 注册 BeanDefinition
        getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }
        }


```

这样的话，做完这些我们就可以从资源包下读spring.xml来注册bean啦！

