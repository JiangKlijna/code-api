## Code-Api

#### 快速开发api,意为一段code或者多段code作为一个api

#### codeapi本身不对外提供服务,由使用者来决定如何使用提供服务以及鉴权等

#### 共有四大组件组成:[Router],[ApiFactory],[FileSystem],[Executor]

#### 以及数据实体:[Api],[Param]

+ Api,Param

```md
  Api的作用是,代表了一段code|多段code,以及执行参数
  Param的作用是,代表了Api执行参数
  这两个类主要为代表数据的实体类
```

+ Router

```md
  Router路由的作用是,通过不同的Request&Response获取到[请求路径]以及[请求参数]
  将[请求路径]交给FileSystem从而获取到Api,最后将[请求参数]和Api一起交给Executor执行 执行结果返回给Response 主要的实现类为ServletFactory,WebsocketFactory...
    ServletFactory: 适合于Servlet容器内使用 
    WebsocketFactory: 适合于由Websocket请求的服务使用(暂时不做)
```

+ ApiFactory

```md
  Api工厂的作用有以下两点:(不同的工厂对应的文件格式不同)
  1.通过文件流得到Api对象 
  2.把Api对象转换为文件流 主要的实现类为XmlApiFactory,JsonApiFactory,YmlApiFactory... 
    XmlApiFactory: xml格式的ApiFactory
    JsonApiFactory: json格式的ApiFactory 
    YmlApiFactory: yml格式的ApiFactory
```

+ FileSystem

```md
  FileSystem文件系统的作用是,通过[请求路径]获取到Api,以及优先获取Xml格式的Api,其次是Json,yml格式的等等
  主要的实现类为ClassPathFileSystem,HttpFileSystem,RootFileSystem 
    ClassPathFileSystem: 通过java Class获取文件资源 
    RootFileSystem: 通过真实的文件系统获取文件 
    HttpFileSystem: 通过一个http服务,获取文件资源
```

+ Executor

```md
  Executor执行者的作用是,通过[请求参数]以及Api执行后得到结果交给Factory 主要的实现类有JDBCExecutor,MongoExecutor,EsExecutor 
    JDBCExecutor: 将Api中的code,此时为sql,通过jdbc执行 
    MongoExecutor: 将Api中的code,此时为mongodb脚本,在mongodb中执行(暂时不做)
    EsExecutor: 将Api中的code,此时为es脚本,在es中执行(暂时不做)
``` 