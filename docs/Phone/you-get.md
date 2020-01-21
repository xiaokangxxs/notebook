# You-Get

 [You-Get](https://github.com/soimort/you-get) 乃一小小哒命令行程序，提供便利的方式来下载网络上的媒体信息。 

 利用`you-get`下载[这个网页](https://v.qq.com/x/cover/mzc00200r7lrqxw/q3055ta10w3.html)的视频: 

```bash
$ you-get https://v.qq.com/x/cover/mzc00200r7lrqxw/q3055ta10w3.html
Site:       QQ.com
Title:      甄子丹“鼠味”拜年送吉祥
Type:       MPEG-4 video (video/mp4)
Size:       2.43 MiB (2546829 Bytes)

Downloading 甄子丹“鼠味”拜年送吉祥.mp4 ...
 100% (  2.4/  2.4MB) ├████████┤[1/1]    3 MB/s

```

`you-get`之功用:

- 下载流行网站之音视频，例如YouTube, Youku, Niconico,以及更多. (文章末尾有支持列表)
- 于您心仪的媒体播放器中观看在线视频，脱离浏览器与广告
- 下载您喜欢的网页上的图片
- 下载任何非HTML内容，例如二进制文件

## 1.Android端通过Termux安装

`其它平台安装请移步GitHub查看`

```bash
#第一步：更新安装器
pkg update && pkg upgrade
#第二步：获取SD卡读写权限
termux-setup-storage
#第三步：安装python和ffmpeg
pkg install python ffmpeg
#第四步：安装you-get
pip install you-get
```

## 2.中文说明

```bash
you-get: version 0.4.1388, a tiny downloader that scrapes the web.
usage: you-get [OPTION]... URL...

A tiny downloader that scrapes the web

optional arguments:
  -V, --version         打印版本并退出
  -h, --help            打印此帮助信息并退出

Dry-run options:
  (no actual downloading)

  -i, --info            打印提取的信息
  -u, --url             打印页面所有可下载URL列表
  --json                打印页面所有可下载URL列表且使用JSON格式

Download options:
  -n, --no-merge        不合并视频部分
  --no-caption          不下载字幕（字幕，歌词，danmaku...）
  -f, --force           强制覆盖现有文件
  --skip-existing-file-size-check
                        跳过已存在的文件，而不检查文件大小
  -F STREAM_ID, --format STREAM_ID
                        设置视频格式的stream_id
  -O FILE, --output-filename FILE
                        设置输出文件名
  -o DIR, --output-dir DIR
                        设置输出目录
  -p PLAYER, --player PLAYER
                        流中提取URL到PLAYER
  -c COOKIES_FILE, --cookies COOKIES_FILE（并非所有视频可供任何人观看。如果需要登录以观看 (例如, 私密视频), 可能必须将浏览器cookie加载进来）
                        加载Netscape的cookie.txt或Mozilla的cookies.sqlite
  -t SECONDS, --timeout SECONDS
                        设置套接字超时
  -d, --debug           显示追踪等调试信息
  -I FILE, --input-file FILE
                        从文件中读取非播放列表网址
  -P PASSWORD, --password PASSWORD
                        视频设置访问密码
  -l, --playlist        喜欢下载播放列表
  -a, --auto-rename     自动重命名同名不同文件
  -k, --insecure        忽略SSL错误

Proxy options:
  -x HOST:PORT, --http-proxy HOST:PORT
                        用于下载的HTTP代理
  -y HOST:PORT, --extractor-proxy HOST:PORT
                        使用HTTP代理只提取
  --no-proxy            不使用代理
  -s HOST:PORT, --socks-proxy HOST:PORT
                        用于下载的SOCKS5代理
```

## 支持网站

| 网站                        | URL                                               | 视频? | 图像? | 音频? |
| --------------------------- | ------------------------------------------------- | ----- | ----- | ----- |
| **YouTube**                 | https://www.youtube.com/                          | ✓     |       |       |
| **Twitter**                 | https://twitter.com/                              | ✓     | ✓     |       |
| VK                          | http://vk.com/                                    | ✓     |       |       |
| Vine                        | https://vine.co/                                  | ✓     |       |       |
| Vimeo                       | https://vimeo.com/                                | ✓     |       |       |
| Vidto                       | http://vidto.me/                                  | ✓     |       |       |
| Veoh                        | http://www.veoh.com/                              | ✓     |       |       |
| **Tumblr**                  | https://www.tumblr.com/                           | ✓     | ✓     | ✓     |
| TED                         | http://www.ted.com/                               | ✓     |       |       |
| SoundCloud                  | https://soundcloud.com/                           |       |       | ✓     |
| Pinterest                   | https://www.pinterest.com/                        |       | ✓     |       |
| MusicPlayOn                 | http://en.musicplayon.com/                        | ✓     |       |       |
| MTV81                       | http://www.mtv81.com/                             | ✓     |       |       |
| Mixcloud                    | https://www.mixcloud.com/                         |       |       | ✓     |
| Metacafe                    | http://www.metacafe.com/                          | ✓     |       |       |
| Magisto                     | http://www.magisto.com/                           | ✓     |       |       |
| Khan Academy                | https://www.khanacademy.org/                      | ✓     |       |       |
| JPopsuki TV                 | http://www.jpopsuki.tv/                           | ✓     |       |       |
| Internet Archive            | https://archive.org/                              | ✓     |       |       |
| **Instagram**               | https://instagram.com/                            | ✓     | ✓     |       |
| Heavy Music Archive         | http://www.heavy-music.ru/                        |       |       | ✓     |
| **Google+**                 | https://plus.google.com/                          | ✓     | ✓     |       |
| Freesound                   | http://www.freesound.org/                         |       |       | ✓     |
| Flickr                      | https://www.flickr.com/                           | ✓     | ✓     |       |
| Facebook                    | https://www.facebook.com/                         | ✓     |       |       |
| eHow                        | http://www.ehow.com/                              | ✓     |       |       |
| Dailymotion                 | http://www.dailymotion.com/                       | ✓     |       |       |
| CBS                         | http://www.cbs.com/                               | ✓     |       |       |
| Bandcamp                    | http://bandcamp.com/                              |       |       | ✓     |
| AliveThai                   | http://alive.in.th/                               | ✓     |       |       |
| interest.me                 | http://ch.interest.me/tvn                         | ✓     |       |       |
| **755 ナナゴーゴー**        | http://7gogo.jp/                                  | ✓     | ✓     |       |
| **niconico ニコニコ動画**   | http://www.nicovideo.jp/                          | ✓     |       |       |
| **163 网易视频 网易云音乐** | http://v.163.com/ http://music.163.com/           | ✓     |       | ✓     |
| 56网                        | http://www.56.com/                                | ✓     |       |       |
| **AcFun**                   | http://www.acfun.tv/                              | ✓     |       |       |
| **Baidu 百度贴吧**          | http://tieba.baidu.com/                           | ✓     | ✓     |       |
| 爆米花网                    | http://www.baomihua.com/                          | ✓     |       |       |
| **bilibili 哔哩哔哩**       | http://www.bilibili.com/                          | ✓     |       |       |
| Dilidili                    | http://www.dilidili.com/                          | ✓     |       |       |
| 豆瓣                        | http://www.douban.com/                            |       |       | ✓     |
| 斗鱼                        | http://www.douyutv.com/                           | ✓     |       |       |
| 凤凰视频                    | http://v.ifeng.com/                               | ✓     |       |       |
| 风行网                      | http://www.fun.tv/                                | ✓     |       |       |
| iQIYI 爱奇艺                | http://www.iqiyi.com/                             | ✓     |       |       |
| 激动网                      | http://www.joy.cn/                                | ✓     |       |       |
| 酷6网                       | http://www.ku6.com/                               | ✓     |       |       |
| 酷狗音乐                    | http://www.kugou.com/                             |       |       | ✓     |
| 酷我音乐                    | http://www.kuwo.cn/                               |       |       | ✓     |
| 乐视网                      | http://www.letv.com/                              | ✓     |       |       |
| 荔枝FM                      | http://www.lizhi.fm/                              |       |       | ✓     |
| 秒拍                        | http://www.miaopai.com/                           | ✓     |       |       |
| MioMio弹幕网                | http://www.miomio.tv/                             | ✓     |       |       |
| 痞客邦                      | https://www.pixnet.net/                           | ✓     |       |       |
| PPTV聚力                    | http://www.pptv.com/                              | ✓     |       |       |
| 齐鲁网                      | http://v.iqilu.com/                               | ✓     |       |       |
| QQ 腾讯视频                 | http://v.qq.com/                                  | ✓     |       |       |
| 阡陌视频                    | http://qianmo.com/                                | ✓     |       |       |
| Sina 新浪视频 微博秒拍视频  | http://video.sina.com.cn/ http://video.weibo.com/ | ✓     |       |       |
| Sohu 搜狐视频               | http://tv.sohu.com/                               | ✓     |       |       |
| 天天动听                    | http://www.dongting.com/                          |       |       | ✓     |
| **Tudou 土豆**              | http://www.tudou.com/                             | ✓     |       |       |
| 虾米                        | http://www.xiami.com/                             |       |       | ✓     |
| 阳光卫视                    | http://www.isuntv.com/                            | ✓     |       |       |
| **音悦Tai**                 | http://www.yinyuetai.com/                         | ✓     |       |       |
| **Youku 优酷**              | http://www.youku.com/                             | ✓     |       |       |
| 战旗TV                      | http://www.zhanqi.tv/lives                        | ✓     |       |       |
| 央视网                      | http://www.cntv.cn/                               | ✓     |       |       |