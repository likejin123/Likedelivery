# 一.后台管理功能

### 1.访问页面

http://localhost:8080/backend/page/login/login.html

![image-20230604154940690](C:\Users\Likejin\AppData\Roaming\Typora\typora-user-images\image-20230604154940690.png)

+ 登录账号

默认 ：账户：admin 密码：123456

员工 ： 账号：（管理员添加账号） 密码123456

## 2.管理后台

![image-20230604155007651](C:\Users\Likejin\AppData\Roaming\Typora\typora-user-images\image-20230604155007651.png)



+ 功能1：员工管理

管理员（admin账户）可以对员工管理（增加员工，删除员工，禁用员工状态）

员工 无法操作。只能看

+ 功能2：分类管理

分类管理：菜品或者套餐都有所属分类

功能：增，删，该

+ 功能3：菜品管理

菜品管理：每个具体的菜

功能：增，删，改（修改菜品信息，下架菜品），查

+ 功能4：套餐管理

套餐管理：每个具体的套餐

功能：增，删，改（修改套餐信息，下架套餐），查

+ 功能5：订单明细

订单明细：移动端提交订单之后产生订单明细

功能：修改订单状态（配送，完成） 

配送：移动端用户从已下单->正在配送

完成：移动端用户从正在配送->已完成  （发送给移动端用户短信）

注意：短信验证功能实现。防止短信发不过去。全都注释掉。在TODO下

# 二. 移动端界面

## 1.访问页面

http://localhost:8080/front/page/login.html

开启网页适配模式

![image-20230604160954995](C:\Users\Likejin\AppData\Roaming\Typora\typora-user-images\image-20230604160954995.png)

## 2.功能

+ 功能1：登录

没有账号自动注册账号。

有账号登录账号。

注意：短信验证功能实现。防止短信发不过去。全都注释掉。在TODO下

+ 功能2：下单流程

1.添加地址并设置默认地址

![image-20230604162650648](C:\Users\Likejin\AppData\Roaming\Typora\typora-user-images\image-20230604162650648.png)

2.添加购物车（选择商品规格）

![image-20230604162458549](C:\Users\Likejin\AppData\Roaming\Typora\typora-user-images\image-20230604162458549.png)

3.去结算（跳转确定页面）

![image-20230604162527436](C:\Users\Likejin\AppData\Roaming\Typora\typora-user-images\image-20230604162527436.png)

4.去支付（支付 = 下单 + 结算）

![image-20230604162611365](C:\Users\Likejin\AppData\Roaming\Typora\typora-user-images\image-20230604162611365.png)

5.查看订单状态

已下单（刚下单） 正在配送（正在配送） 已完成（配送完成）

![image-20230604162903772](C:\Users\Likejin\AppData\Roaming\Typora\typora-user-images\image-20230604162903772.png)