# 折翼天使 - 项目总结

## 简介

折翼天使是一个个人网站，基于分布式架构设计，实现了这些功能和特性：前后端分离、单点登录、动态权限管理、博客，以后还会加入网盘、在线音乐、多客户端聊天。

这是一次尝试，把自已所学、所会的知识做成实实在在的东西，整理、完善自己的知识体系。

最开始的时候它是一个SpringBoot的单体博客项目，模仿CSDN、知乎专栏、BiliBili专栏，数据库连接框架是JPA，没有前后端分离，模板引擎是Thymeleaf。后来感觉太简单了...博客也就是文章入库/显示、评论/留言的楼中楼中楼嵌套处理麻烦一些，于是把它改成了分布式的项目，经过尝试、调整，变成了现在这个样子(各种限制下，有些地方是为了用某种技术而用它，并没有很好的考虑到适合与否...不过我将来会改进的)。

## 架构

**父项目**：方便整个项目的依赖管理、打包等操作；

**公共项目**：存放所有子项目都会用到的工具类；

**网关服务**：整个服务器的统一对外接口(服务器只对外开放443[443端口的常规访问流量会转发到1024端口]和22端口，所以需要一个把请求从1024端口转发到其余应用、服务的网关服务)，同时作为单点登录务器，负责身份校验、动态权限验证，用户系统也在这里；

**文件服务**：为网站所有需要在线读取的文档、音乐、图片提供上传、下载、在线链接查询接口；

**博客服务**：基本就是把之前单机博客的后台部分复刻了一遍，新增了单独的留言部分；

**音乐服务**：这个服务现在只是把音乐在页面上的播放问题解决了...其它方面已经有了设计方向和实现方案，但因为时间问题还没开始；

**网盘服务**：解决了Word、Excel、PPT文件在线预览，音乐、视频在线播放的问题，其它方面同音乐服务；

**聊天服务端：**还未开始。

## 收获

### 前后端分离下的跨域请求

#### 前端解决(配置代理)

1. 在package.json中，加入proxy配置：

   > `"proxy": "http://127.0.0.1:1024/"`

   - 会在前端服务器<965>内部开启一个代理服务，指向proxy地址<1024端口>。当访问前端服务器内部没有的资源时，就会自动转发到proxy地址
   - 于是就可以在前端服务器上直接访问后端服务器的资源——因为前端服务器上并没有这些资源，所以该请求会被代理转发到后端服务器，正好避开了cors问题
   - 优点是使用简单，只需要在package.json中加入一行配置限可
   - 但同时，缺点是只能配置一个代理，无法满足项目需要向多个后端服务器发起请求的情况

2. 在src目录下，新建代理配置文件：

   > `src/setupProxy.js`

   具体代理规则配置如下：

   ```javascript
   const proxy = require("http-proxy-middleware")
   
   module.exports = function (app) {
       app.use(
           // “/angel”是虚拟应用：所有向angel应用发起的请求都会被转发到target地址
           proxy("/angel", {
               // 转发目标地址
               target: "http://127.0.0.1:1024/",
               // 是否改变请求头中Host字段的值：false则使用默认值，true则将其改为target中的Host
               changeOrigin: true,
               // 重写请求路径：去除请求路径中的前缀，也即“/angel”虚拟应用
               pathRewrite: {"^/angel": ""}
           }),
           proxy("/github", {
               target: "https://api.github.com/",
               changeOrigin: true,
               pathRewrite: {"^/github": ""}
           })
       )
   }
   ```

   - 以虚拟应用的方式配置多个代理，可以把向后端发起的请求映射为向对应虚拟应用发起请求
   - 优点是足够灵活，足够强大；缺点是配置烦琐，且需要访问后端服务器时，必须向服务器对应的虚拟应用发起请求

#### 后端解决(配置Cors映射)

1. 实现`WebMvcConfigurer`接口，重写`public void addCorsMappings(CorsRegistry)`方法：

   ```java
   @Configuration
   public class WebMvcConfig implements WebMvcConfigurer {
       @Override
       public void addCorsMappings(@NonNull CorsRegistry registry) {
           registry.addMapping("/**")
                   .allowedHeaders("*")
                   .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                   .allowedOrigins("*")
                   .allowCredentials(true)
                   .maxAge(3600);
       }
   }
   ```

2. 通过HttpSecurity启用cors：`http.cors();`
