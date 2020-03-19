# 菠菜项目描述
##目录结构描述
> * LotteryShopCommon 项目公共模块
>     >  主要作用：为封装各个微服务中公共模块。如：4个币种钱包、公共配置、系统配置等。
> * LotteryShopEurekaServer 注册服务；端口：8090
>     >  主要作用：服务中心，注册微服务。
> * LotteryShopAuth 安全验证服务；端口：8083
>     > 主要作用：安全验证、登录用户、权限拦截。
> * LotteryShopBase 基础功能服务；端口：8092
>     > 主要作用：基础微服务，项目基础业务服务。
> * LotteryShopETHWallet eth钱包服务；端口：8095
>     > 主要作用：用户ETH钱包转账、查询余额等
>     > 注意：与改服务通讯需要进行请求加密、响应解密（规则看代码）。
> * LotteryShopWebGateway 页面网关服务；端口：8080
>     > 主要作用：前端页面、登录、退出、管理SESSION等。
>     > 注意：该服务依赖redis管理session会话。
> * LotteryShopAPIGateway API网关服务；端口：8082
>     > 主要作用：集成所有资源服务API、为统一出口便于WEB网关服务调用。后续项目扩展为纯前端项目可直接使用API网关服务。
> * LotteryShopManage 后台管理服务；端口：8070
>     > 主要作用：管理维护前台项目内容等。
> * LoteryShopLotteryA 玩法A 端口 ：8096
>     > 主要作用：区块3号玩法服务购买、开奖、发放奖金、提成、分红等，区块3号所有业务。
> * LoteryShopLotteryB 玩法B端口 ：8097
>    > 主要作用：快3玩法服务购买、开奖、发放奖金、提成、分红等，快3所有业务。


* 前端项目启动顺序
	* LotteryShopEurekaServer
	* LotteryShopAPIGateway
	* LotteryShopAuth
	* LotteryShopBase
	* LotteryShopETHWallet
	* LoteryShopLotteryA
	* LoteryShopLotteryB
	* LotteryShopWebGateway
* 后台管理启动项目	
	* LotteryShopManage

###########环境依赖
ETH公链、
JDK1.8.0_161、
WEB容器（如tomcat）、
kafka消息队列、
redis、
mysql

###########部署步骤
   1、mysql 数据库。
 
   2、redis 缓存数据库。
   
   3、jdk1.8 安装。
   
   4、kafka 服务安装。
   
   5、公链部署eth智能合约。
   
   6、部署运行jar（9个项目）

   7、注意各个微服务application.properties、application.yml 配置文件


