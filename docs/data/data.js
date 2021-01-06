let data={
  msg:'请求成功!',
  code : 200,
  datas:[
    {
      vid:0,
      navId:0,
      title:'公开课-使用HDFS构建云盘',
      coverUrl:'http://file.xiaokang.cool/logo_bg.jpg',
      url:'http://mpvideo.qpic.cn/0bf2liaayaaamqaiqgy5kfpvawwdbrnaadaa.f10002.mp4?dis_k=479fde01f252855a5e778ca97761aa7f&dis_t=1609249717&spec_id=undefined1609249718&vid=wxv_1445821011194347522&format_id=10002'
    },
    {
      vid:1,
      navId:0,
      title:'玩儿转Linux（上）',
      coverUrl:'http://file.xiaokang.cool/logo_bg.jpg',
      url:'http://mpvideo.qpic.cn/0bf234aakaaakiadavy4lvpvbx6daxpqabia.f10002.mp4?dis_k=946c5d2f4dc7aea7825e7f06cc315604&dis_t=1609249810&spec_id=undefined1609249812&vid=wxv_1444383521006747648&format_id=10002'
    },
    {
      vid:2,
      navId:0,
      title:'玩儿转Linux（下）',
      coverUrl:'http://file.xiaokang.cool/logo_bg.jpg',
      url:'http://mpvideo.qpic.cn/0bf2yqaaiaaa2uadhyi4lvpvbrgdatcaabaa.f10002.mp4?dis_k=a32227bacb38ae3de85b107269aefc1d&dis_t=1609249868&spec_id=undefined1609249870&vid=wxv_1444390022295388163&format_id=10002'
    },
    {
      vid:3,
      navId:0,
      title:'公开课-高可用Hadoop集群',
      coverUrl:'http://file.xiaokang.cool/logo_bg.jpg',
      url:'http://mpvideo.qpic.cn/0bf2qubdmaacaaanjarvhfpvfbodg2cqenqa.f10002.mp4?dis_k=0bd83efe444e4c9a57f9885ee28551fe&dis_t=1609334369&spec_id=undefined1609334369&vid=wxv_1474813231188754441&format_id=10002'
    },{
      vid:4,
      navId:0,
      title:'公开课-Hadoop前置准备',
      coverUrl:'http://file.xiaokang.cool/logo_bg.jpg',
      url:'http://mpvideo.qpic.cn/0bf26uaeaaaacealdyysbfpvb5odid2qaqaa.f10002.mp4?dis_k=5c295c04304ed545777b54f4e5d18e3f&dis_t=1609334912&spec_id=undefined1609334912&vid=wxv_1434197309038067712&format_id=10002'
    },{
      vid:5,
      navId:0,
      title:'公开课-大数据平台初体验',
      coverUrl:'http://file.xiaokang.cool/logo_bg.jpg',
      url:'http://mpvideo.qpic.cn/0b78oiaamaaafmaev4auifpva4wdazzaabqa.f10002.mp4?dis_k=317128265c3ab7cbf90c2db00a80226e&dis_t=1609335039&spec_id=undefined1609335039&vid=wxv_1435685328341499905&format_id=10002'
    },{
      vid:6,
      navId:0,
      title:'手把手教你做一份儿在线简历(上)',
      coverUrl:'http://file.xiaokang.cool/logo_bg.jpg',
      url:'http://mpvideo.qpic.cn/0bf2pqazsaabwianufir2fpvc7gdtf6adgia.f10002.mp4?dis_k=1675721ef0d9c7651af850fd7278c93d&dis_t=1609335111&spec_id=undefined1609335112&vid=wxv_1431250595863920641&format_id=10002'
    },{
      vid:7,
      navId:0,
      title:'手把手教你做一份儿在线简历(下)',
      coverUrl:'http://file.xiaokang.cool/logo_bg.jpg',
      url:'http://mpvideo.qpic.cn/0bf2juabkaaa6yafgiyqlbpvatodcvgqafia.f10002.mp4?dis_k=bd97274975898d584f7a379a51aebf36&dis_t=1609335186&spec_id=undefined1609335186&vid=wxv_1431263168743145472&format_id=10002'
    },{
      vid:8,
      navId:0,
      title:' Web Scraper爬虫',
      coverUrl:'http://file.xiaokang.cool/logo_bg.jpg',
      url:'http://mpvideo.qpic.cn/0bf2jqaaiaaafeaory7rf5pfatgdargaabaa.f10002.mp4?dis_k=568833085e07350594c5308d074a77e0&dis_t=1609335278&spec_id=undefined1609335278&vid=wxv_1397889829308006403&format_id=10002'
    },
    {
      vid:0,
      navId:1,
      title:'Hadoop前置准备',
      coverUrl:'http://file.xiaokang.cool/logo_bg.jpg',
      url:'http://mpvideo.qpic.cn/0bf26uaeaaaacealdyysbfpvb5odid2qaqaa.f10002.mp4?dis_k=2295f9bde96171ab4e236d2b76d683a9&dis_t=1609249986&spec_id=undefined1609249988&vid=wxv_1434197309038067712&format_id=10002'
    },
    {
      vid:1,
      navId:1,
      title:'HA-Hadoop集群动态添加、删除节点(上)',
      coverUrl:'http://file.xiaokang.cool/logo_bg.jpg',
      url:'http://mpvideo.qpic.cn/0bf2kqaacaaajmaihwxuhrpfavgdafkaaaia.f10002.mp4?dis_k=82d38b13b0d3c9304a7b78d3d6f02a06&dis_t=1609250084&spec_id=undefined1609250086&vid=wxv_1400731926809608192&format_id=10002'
    },
    {
      vid:2,
      navId:1,
      title:'HA-Hadoop集群动态添加、删除节点(中)',
      coverUrl:'http://file.xiaokang.cool/logo_bg.jpg',
      url:'http://mpvideo.qpic.cn/0bf2j4aacaaatqaigdhuh5pfat6dafhqaaia.f10002.mp4?dis_k=f27e3b2522c0008e60ea9173c4832d85&dis_t=1609250171&spec_id=undefined1609250173&vid=wxv_1400734944057688065&format_id=10002'
    },
    {
      vid:3,
      navId:1,
      title:'HA-Hadoop集群动态添加、删除节点(下)',
      coverUrl:'http://file.xiaokang.cool/logo_bg.jpg',
      url:'http://mpvideo.qpic.cn/0b78lyaagaaazaaiazhufbpfaxwdanpaaaya.f10002.mp4?dis_k=b9437512e6d17b8b0d0ed267a8d62986&dis_t=1609250230&spec_id=undefined1609250232&vid=wxv_1400738204726394880&format_id=10002'
    }
  ]
}
let navItem=['推荐','大数据','Python','Linux','Vim','Docker','数据库']
let cpsTabs=[
  {
    icon: '/static/images/all.png',
    text: '全部',
    tabId: 0,
  },
  {
    icon: '/static/images/ele.png',
    text: '饿了么',
    tabId: 1,
  },
  {
    icon: '/static/images/meituan.png',
    text: '美团',
    tabId: 2,
  },
  {
    icon: '/static/images/huaxiaozhu.png',
    text: '花小猪打车',
    tabId: 3,
  },
  {
    icon: '/static/images/didi.jpg',
    text: '滴滴出行',
    tabId: 4,
  },
  {
    icon: '/static/images/jd.png',
    text: '京东',
    tabId: 5,
  }
]
let welfareList=[
  {
    name: '饿了么红包',
    icon: '/static/images/welfare/ele.png',
    bannerPic: '/static/images/welfare/ele_coupons.png',
    url: 'https://s.click.ele.me/9NZfYsu',
    type: 1,
    tabId: 1,
    minapp: {
      appid: 'wxece3a9a4c82f58c9',
      path: 'ele-recommend-price/pages/index/index?spm=a2ogi.19758203.0.0&isGuest=true'
    }
  },
  {
    name: '饿了么果蔬',
    icon: '/static/images/welfare/ele.png',
    bannerPic: '/static/images/welfare/ele_subsidy.png',
    url:'https://s.click.ele.me/zxTMdsu',
    type: 1,
    tabId: 1,
    minapp: {
      appid: 'wxece3a9a4c82f58c9',
      path: 'pages/sharePid/web/index?scene=https://s.click.ele.me/zxTMdsu'
    }
  },
  {
    name: '美团团购',
    icon: '/static/images/welfare/meituan.png',
    bannerPic: '/static/images/welfare/meituan_banner.png',
    url:'http://dpurl.cn/IyMijrM',
    type: 1,
    tabId: 2,
    minapp: {
      appid: 'wxde8ac0a21135c07d',
      path: '/index/pages/h5/h5?weburl=https%3a%2f%2factivityunion-marketing.meituan.com%2fmtzcoupon%2findex.html%3fchannel%3dunion%26utm_source%3d60413%26cpsMedia%3d1345367594131103777'
    }
  },
  {
    name: '美团外卖红包',
    icon: '/static/images/welfare/meituan.png',
    bannerPic: '/static/images/welfare/meituan_banner.png',
    url:'http://dpurl.cn/ctbjDrg',
    type: 1,
    tabId: 2,
    minapp: {
      appid: 'wx2c348cf579062e56',
      path: 'outer_packages/r2xinvite/coupon/coupon?inviteCode=NnOIp-QOs8SiYF1dcSlL5r8phPrCf6qkH7evMyjIoup2NXxNCLYcBbd3bqpv2X2IIB5OqvBsQ7ngXC2mFBje-pDjltwmsAKtObd4z9e4DIZ9l_1DGZ1_w-pqjqUaA-Fd3AOztFzLFLYB_t3rFnO78soX3DkX69tnYphtvhS2z_4'
    }
  },
  {
    name: '花小猪红包',
    icon: '/static/images/huaxiaozhu.png',
    bannerPic: '/static/images/welfare/huaxiaozhu_coupons.png',
    url:'https://hxz.didichuxing.com/',
    type: 2,
    tabId: 3,
    minapp: {
      appid: 'wxd98a20e429ce834b',
      path: 'kfpub/pages/gift?jump_home=1&f_dpsid=10c4eb96f6e84e0185f2e038b385a86a&latitude=40.04726969401042&g_channel=2f9e032794de100a0b8eb5a203bd2a37&entrance_channel=&channel=2001001001&xpsid=a03294edd6064bbfaf49a33466983ccd&share_code=bvolqe56qag556k5nkhg&dchn=zxOgqk&channel_id=2001001001&longitude=116.42628499348959&city_id=1'
    }
  },
  {
    name: '滴滴红包',
    icon: '/static/images/didi.jpg',
    bannerPic: '/static/images/welfare/huaxiaozhu_coupons.png',
    url:'https://www.didichuxing.com/',
    type: 2,
    tabId: 4,
    minapp: {
      appid: 'wxaf35009675aa0b2a',
      path: 'passenger-recommend/views/coupon/index?template=1&sharecode=bvq7b7bu4bav8pj79nug&root_dchn=w0WWXX&channel_id=1100000090&scene=14'
    }
  }

]
//将数据暴露出去，出口命名为respData
module.exports={
  respData:data,
  navItem,
  cpsTabs,
  welfareList
}