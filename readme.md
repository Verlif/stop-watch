# Stopwatch

__简单的停表__

异常简单的停表，主要是用于测试方法耗时什么的。

## 使用

```java
Stopwatch watch = Stopwatch.get("testWatch");
System.out.println("停表开始: " + watch.start());
Thread.sleep(123);
System.out.println("停表记录: " + watch.pin());
System.out.println("间隔时间: " + watch.getLastInterval(TimeUnit.MILLISECONDS));
Thread.sleep((long) (Math.random() * 1000));
System.out.println("停表记录: " + watch.pin("开始吃饭"));
Thread.sleep((long) (Math.random() * 1000));
System.out.println("停表记录: " + watch.pin("吃完"));
System.out.println("吃饭时间: " + watch.getPinInterval("开始吃饭", "吃完", TimeUnit.MILLISECONDS));
System.out.println("停表结束: " + watch.stop());
System.out.println("打点时间：" + Arrays.toString(watch.getIntervalLine(TimeUnit.MILLISECONDS).toArray()));
watch.restart();
```

输出如下：

```text
停表开始: 1494107535848000
停表记录: 1494107661364700
间隔时间: 125.5167
停表记录: 1494108176334400
停表记录: 1494108683787000
吃饭时间: 507.4526
停表结束: 1494108683924800
打点时间：[125.5167, 514.9697, 507.4526, 0.1378]
```

__注意__: 停表只有在`reset`之后才可以再次`start`，或是直接使用`restart`。

## 常用方法

### Stopwatch.start(name)

通过Stopwatch来启动停表，用于多个停表的处理。可通过`Stopwatch.get(name)`来获取对应停表。

### pin

主要的计时打点方法，使用`watch.pin`或`watch.pin(name)`来记录时间点。

### getPinInterval

获取打点`pin`的间隔时间，例如：

- `watch.getPinInterval(2, TimeUnit.MILLISECONDS))` - 获取第二个pin点与上一个pin点之间的间隔时间
- `watch.getPinInterval("吃饭", TimeUnit.MILLISECONDS))` - 获取名为`吃饭`的pin点与上一个pin点之间的间隔时间
- `watch.getPinInterval("开始吃饭", "吃完", TimeUnit.MILLISECONDS))` - 获取名为`开始吃饭`的pin点与名为`吃完`的pin点之间的间隔时间

### getIntervalLine

获取所有pin点的间隔时间列表

## 添加依赖

[![Release](https://jitpack.io/v/Verlif/stopwatch.svg)](https://jitpack.io/#Verlif/stopwatch)

1. 添加Jitpack仓库源

    maven
    
    ```xml
    <repositories>
       <repository>
           <id>jitpack.io</id>
           <url>https://jitpack.io</url>
       </repository>
    </repositories>
    ```
    
    Gradle
    
    ```text
    allprojects {
      repositories {
          maven { url 'https://jitpack.io' }
      }
    }
    ```

2. 添加依赖

    maven
    
    ```xml
       <dependencies>
           <dependency>
               <groupId>com.github.Verlif</groupId>
               <artifactId>stopwatch</artifactId>
               <version>版本号</version>
           </dependency>
       </dependencies>
    ```
    
    Gradle
    
    ```text
    dependencies {
      implementation 'com.github.Verlif:stopwatch:版本号'
    }
    ```
