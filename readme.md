# Stopwatch

__简单的停表__

异常简单的停表，主要是用于测试方法耗时什么的。

## 使用

```java
Stopwatch watch = Stopwatch.get("testWatch");
System.out.println("停表开始: " + watch.start());
......
System.out.println("停表记录: " + watch.pin());
System.out.println("间隔时间: " + watch.getLastInterval());
......
System.out.println("停表记录: " + watch.pin("开始吃饭"));
......
System.out.println("停表记录: " + watch.pin("吃完"));
System.out.println("吃饭时间: " + watch.getPinInterval("开始吃饭", "吃完"));
// 上面一行代码也等效于下面一行代码
System.out.println("吃饭时间: " + watch.getLastInterval());
System.out.println("停表结束: " + watch.stop());
```

__注意__: 停表只有在`reset`之后才可以再次`start`，或是直接使用`restart`。

## 添加依赖

1. 添加Jitpack仓库源

> maven
> ```xml
> <repositories>
>    <repository>
>        <id>jitpack.io</id>
>        <url>https://jitpack.io</url>
>    </repository>
> </repositories>
> ```

> Gradle
> ```text
> allprojects {
>   repositories {
>       maven { url 'https://jitpack.io' }
>   }
> }
> ```

2. 添加依赖

> maven
> ```xml
>    <dependencies>
>        <dependency>
>            <groupId>com.github.Verlif</groupId>
>            <artifactId>stopwatch</artifactId>
>            <version>1.0</version>
>        </dependency>
>    </dependencies>
> ```

> Gradle
> ```text
> dependencies {
>   implementation 'com.github.Verlif:stopwatch:1.0'
> }
> ```
