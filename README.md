# LLog
一个可以自定义输出方式的日志输出工具

目前实现基础的日志输出（包含控制台，文件，悬浮窗），你可以自己再扩充网络请求的日志输出（虽然不建议，但是作为必要内容的收集还是比较有效方式）。

库里没有对权限作声明,所以在项目中需要自己声明并动态申请.如可能需要读写权限,如果做系统悬浮则需要指引用户开启权限,亦或网络日志需要联网权限.

集成方式:
```java
Step 1. 在工程下 build.gradle 文件添加 jitpack 库
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
Step 2. 在项目中添加依赖
dependencies {
    implementation 'com.github.codeleejj:llog:Tag'
}
```


