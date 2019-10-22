# 彩票店项目描述

## LotteryShopCommon 项目公共模块
## LotteryShopEurekaServer 注册服务；端口：8081
## LotteryShopAuth 安全验证服务；端口：8083
## LotteryShopBase 基础功能服务；端口：8092
## LotteryShopETHWallet eth钱包服务；端口：8095
## LotteryShopWebGateway 页面网关服务；端口：8080
## LotteryShopAPIGateway API网关服务；端口：8082
## LotteryShopManage 后台管理服务；端口：8085


* 项目启动顺序
	* LotteryShopEurekaServer
	* LotteryShopAuth
	* LotteryShopBase
	* LotteryShopETHWallet
	* LotteryShopManage
	* LotteryShopAPIGateway
	* LotteryShopWebGateway

### 集成SpringBoot、 Spring Security、 MyBatis、 OAuth2、 Redis实现资源访问授权认证。
### 用户认证的token使用RedisTokenStore保存在redis中，提高系统并发能力。

