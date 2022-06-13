## guild-plus

### 简介

这是运行在 Nukkit 服务器上的一款公会插件

你可以用这个**插件可以给玩家们创造一个有趣，有竞争性的游戏环境,让玩家们拥有一个目标,共同发展**

### 功能

- 公会商店
- 公会仓库
- 成员管理
- 自定义公会职位
- 自定义公会等级
- 公会战争

### 指令

| 指令名     | 说明         | 权限         |
| ---------- | ------------ | ------------ |
| guild      | 公会主命令   | 普通玩家     |
| guildadmin | 公会管理命令 | 游戏内管理员 |

### 主配置文件

```yaml
# 配置版本
version: "1.0"


# 公会命令
admin-command: guildadmin
guild-command: guild

# 暂时仅支持中文
default-language: zh_CN.yml

# 整数范围(0 - 2,147,483,647)
create-guild-money: 10000

# 开启公会战战争时间 单位 秒
start-guild-war-time: 10

# 发送公会战冷却时间 单位 秒
guild-war-cd-time: 60

# 公会战对战时间 单位 秒
battle-against-time: 60

# 职位划分
post:
  - {
    name: "会长", # 职位名称
    level: 1 # 职位等级 等级越低职位越高
  }
  - {
    name: "副会长",
    level: 2
  }
  - {
    name: "元老",
    level: 3
  }
  - {
    name: "精英",
    level: 4
  }
  - {
    name: "会员",
    level: 5
  }

# 公会等级配置 needContribute 需要的贡献值 allowWarehouses 允许使用的仓库类型 count 公会总人数限制
guildLevel:
  {
    "1": { "needContribute": 100,"allowWarehouses": [ "Hopper" ],"count": 10 },
    "2": { "needContribute": 500,"allowWarehouses": [ "Hopper" ],"count": 15 },
    "3": { "needContribute": 1000,"allowWarehouses": [ "Hopper" ],"count": 16 },
    "4": { "needContribute": 1500,"allowWarehouses": [ "Hopper" ],"count": 17 },
    "5": { "needContribute": 2000,"allowWarehouses": [ "Hopper" ],"count": 18 },
    "6": { "needContribute": 3000,"allowWarehouses": [ "Hopper","Chest","Double Chest" ],"count": 19 }
  }

```

### 效果图

![](https://s3.bmp.ovh/imgs/2022/06/13/71f544be126245c2.png)

![](https://s3.bmp.ovh/imgs/2022/06/13/19e764c2373173d4.png)

![](https://s3.bmp.ovh/imgs/2022/06/13/cee79fa84819d28b.png)

![](https://s3.bmp.ovh/imgs/2022/06/13/a50126b0bf8d49b2.png)
