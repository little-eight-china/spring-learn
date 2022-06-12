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
protected void doLoadBeanDefinitions(InputStream inputStream) throws ClassNotFoundException {
        Document doc = XmlUtil.readXML(inputStream);
        Element root = doc.getDocumentElement();
        NodeList childNodes = root.getChildNodes();

        for (int i = 0; i < childNodes.getLength(); i++) {
            // 判断元素
            if (!(childNodes.item(i) instanceof Element)) continue;
            // 判断对象
            if (!"bean".equals(childNodes.item(i).getNodeName())) continue;

            // 解析标签
            Element bean = (Element) childNodes.item(i);
            String id = bean.getAttribute("id");
            String name = bean.getAttribute("name");
            String className = bean.getAttribute("class");
            // 获取 Class，方便获取类中的名称
            Class<?> clazz = Class.forName(className);
            // 优先级 id > name
            String beanName = StrUtil.isNotEmpty(id) ? id : name;
            if (StrUtil.isEmpty(beanName)) {
                beanName = StrUtil.lowerFirst(clazz.getSimpleName());
            }

            // 定义Bean
            BeanDefinition beanDefinition = new BeanDefinition(clazz);
            // 读取属性并填充
            for (int j = 0; j < bean.getChildNodes().getLength(); j++) {
                if (!(bean.getChildNodes().item(j) instanceof Element)) continue;
                if (!"property".equals(bean.getChildNodes().item(j).getNodeName())) continue;
                // 解析标签：property
                Element property = (Element) bean.getChildNodes().item(j);
                String attrName = property.getAttribute("name");
                String attrValue = property.getAttribute("value");
                String attrRef = property.getAttribute("ref");
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

