package com.imitationmafengwo.message;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created with IntelliJ IDEA.
 * User: xiejm
 * Date: 3/4/13
 * Time: 11:46 AM
 */
public class Message {
    public final static int PRIORITY_NORMAL = 1;
    public final static int PRIORITY_HIGH = 2;
    public final static int PRIORITY_EXTREMELY_HIGH = 3;

    private static ConcurrentLinkedQueue<Message> mCachedMessagePool = new ConcurrentLinkedQueue<Message>();
    private final static int MAX_CACHED_MESSAGE_OBJ = 15;

    public enum Type {
        NONE,
        // this message is used to destroy the message pump,
        // we use the "Poison Pill Shutdown" approach, see: http://stackoverflow.com/a/812362/668963
        DESTROY_MESSAGE_PUMP,

        LOCATION_CHANGEED, // 位置变更

        //        SWITCH_PAGE,                                                // push a page on top of the previously most front Page
//        POP_PAGE,                                                   // pop the most front Page
        EXIT,
        NETTRACE,
        NETWORK_STATE_CHANGED,
        PROMPT_TO_ASK_DELETE_DOWNLOAD_RECORD,
        MAIN_ACTIVITY_FINISHED,
        ACCOUNT_INFO_CHANGE,
        SHOW_ANIMATIONS_TOAST,
        TOGGLE_SLIDING_MENU,
        //for
        USER_PROFILE_CHANGE,                                //个人资料变更
        MULU_DATA_CHANGE,                                    //目录数据下载完毕更新
        INVALATE_READER_FRESH,                                //阅读页刷新
        LOGIN_TYPE_CHANGE,                                    //logiin type change
        MULU_DATA_EXIT_WANT_UPDATE,                            //目录数据已存在，需要更新
        BOOKDTAIL_UPDATE_MULU_FINISHED,                        //书籍详情更新目录完毕
        USER_INFO_CHANGE,                                //USER INFO CHANGE
        ORDER_CHAPTER_ALL,                                //订阅全部
        ORDER_CHAPTER_CUR,                                //订阅当前章节
        //        BAZIERPAGE_SIGLE_TOUCH_EVENT,								//阅读页点击事件
//        BAZIERPAGE_FLING_TOUCH_EVENT,								//阅读页拽事件
//        HORIZONTAL_SIGLE_TOUCH_EVENT,								//阅读页拽事件
//        HORIZONTAL_FLING_TOUCH_EVENT,								//阅读页拽事件
//        VERTICAL_SIGLE_TOUCH_EVENT,								//阅读页拽事件
//        VERTICAL_FLING_TOUCH_EVENT,								//阅读页拽事件
//        FLING_SIGLE_TOUCH_EVENT,								//阅读页拽事件
//        FLING_FLING_TOUCH_EVENT,								//阅读页拽事件
        FLING_GET_NEXT_CHART,                                //滑动页获取下一章
        FLING_GET_PRE_CHART,                                //滑动页获取上一章
        FLING_GET_CUR_CHART,                                //滑动页获取当前章
        FLING_FRESH_TITLE,                                //滑动页刷新标题和页码
        FLING_FRESH_PAGE,                                //滑动页刷新标题和页码
        FLING_SWITCH_TO_IMAGEVIEW,                        //切换到图片预览界面
        FLING_SHOWMENU_TOUCH_EVENT,                        //显示菜单页
        CUR_FRESH_VIEW,                                    //刷新仿真界面
        FRESH_POCKET_DATA,                                    //火袋数据更新
        WXPAY_SUCC,                                    //微信支付成功
        WXPAY_FAIL,                                    //微信支付失败或取消
        LOAD_DATA_NOW_FOR_LAZY,                        //触发加载数据，用于延迟加载
        TURN_TO_LOGIN_ACTIVITY,                        //跳转到用户登录界面
        KARTPAY_SUCC,                                //卡支付成功
        FRESH_USERMONEY_SUCC,                                //刷新火券成功
        FRESH_USERTICKET_SUCC,                                //刷新月票成功
        CLICK_SELETED_SHEET_ITEM,                    //书架编辑下的点击事件
        TURN_TO_MAINTAB_FRAGMENT,                    //跳转到书库
        POCKET_DATA_CHANGE,                        //刷新书架数据
        FAC_COUNT,                       //点赞数变更(长评LcmtDetailReplyFragment-》精彩书评fragment)
        NOTIFY_DOWNLOAD_DATA_VIEW,            //刷新下载的UI
        NOTIFY_DOWNLOAD_DATA_BYKEEYID,            //刷新下载的进度
        NOTIFY_DOWNLOAD_DATA_FINISHED,            //刷新下载完成ui
        ONEKEY_START_NOVELS_TASK,            //一键缓存 开始下载小说任务
        ONEKEY_START_NOVELS_SHOW,                                    //一键缓存  显示一键缓存按钮
        ONEKEY_START_NOVELS_ONE_NOVEL,                                //一键缓存  一键缓存某本书
        ONEKEY_START_NOVELS_ONE_ALBUM,                                //一键缓存  一键缓存某个音频
        NOTIFY_RELOAD_DOWNLOAD_DATA,                                //重新load下载数据
        TO_DETAIL_AND_PLAY_A_SONG,                                    //打开音频详情,并播放一首特定的音频
        ADD_CONTAINER_CMTS_COMMENT,                                 //添加列表简评论
        ADD_CONTAINER_LCMTS_COMMENT,                                //添加列表书评评论(从书评列表添加的BookComment)
        ADD_REPLY_CMTS_COMMENT,                                     //添加简评回复
        ADD_REPLY_LCMTS_COMMENT,                                    //添加长评回复
        ADD_ABLUMS_CMTS_COMMENT,                                    //添加专辑列表简评论
        ADD_ABLUMS_REPLY_CMTS_COMMENT,                              //添加专辑评论回复
        SF_READER_DOWNLOAD_ITEM_ONCLICK,                            //阅读器下载列表点击事件
        SF_READER_MULU_ITEM_ONCLICK,                                //阅读器目录列表点击事件
        SF_READER_REDRAW_CONTENT,                                   //重画阅读页
        SF_READER_FRESH_CONTENT,                                    //章节阅读刷新内容事件
        SF_READER_SWITCH_NEXT_PAGE,                                 //获取数据后翻下一页事件
        SF_READER_SWITCH_PRE_PAGE,                                  //获取数据后翻上一页事件
        SF_READER_SEEK_TO_CHARPTER,                                 //拖拽到某章节请求章节内容返回
        SF_READER_SET_BACKGROUD,                                    //设置背景颜色
        SF_READER_OPEN_VIP_CONTENT,                                 //打开VIP提示
        SF_READER_CATE_EDIT_CHAGE,                                  //书签编辑更改
        SF_READER_ON_CLICK_CATE,                                    //书签编辑更改
        SF_READER_ON_CLICK_ITEMCATE,                                //书签编辑更改
        SHEET_SHOW_BOOTOM_LAYOUT,                                   //显示底部按钮
        SHEET_HIDE_BOOTOM_LAYOUT,                                   //隐藏底部按钮
        BOOKCONTENT_MULU_DATA_CHANGE,                                //目录数据下载完毕阅读页更新
        MULUPAGE_MULU_DATA_CHANGE,                                    //目录数据下载完毕目录页更新
        DOWNLOAD_MULU_DATA_CHANGE,                                    //目录数据下载完毕下载页更新
        SF_READER_SEEK_TO_CHARPTER_HISTROY,                         //拖拽到某章节请求章节内容并按历史纪录返回
        SF_MYNOVEL_GET_NOVELTYPE,                                   //获取选择的小说类型
        SF_MYNOVEL_SELETED_NOVELTYPE,                               //点击小说分类事件
        SF_MYNOVEL_CREATE_SUCC,                                     //创建小说成功
        SF_MYCHAPTER_ADDORFIX_SUCC,                                 //创建章节成功
        SF_MYCHAPTER_COMMITAPPLY_SUCC,                              //提交审核成功
        SF_MYNOVLE_FIX_VOLUME,                                      //修改卷信息
        SF_MYNOVLE_REPLY_COMMENT,                                   //评论管理回复评论
        SF_RECOMMENDROOM_AD,                                        //轮播图更新
        SF_COMIC_COMMENT_UPDATE,                                    //漫画评论列表更新
        SF_COMIC_COMMENT_REPLY_ONCLICK,                             //点击漫画评论列表回复
        SF_CMT_INPUT_ONCLICK,                                 //点击小说短评列表回复
        SF_NOVEL_CMT_COMMENT_UPDATE,                                //短评列表更新
        SF_NOVEL_LCMT_REPLY_ONCLICK,                                //点击小说长评列表回复
        SF_LCMT_LIST_UPDATA,                                        //书评列表刷新
        SF_LCMT_COMMENT_COUNT_UPDATA,                               //评论数变更(精彩书评fragment响应事件)
        ADD_LCMT_LIST_ACTION_POINT,                                 //添加书评列表点赞
        SF_EDIT_MYMESSAGE,                                          //编辑消息
        SF_MYMESSAGE_LIST_UPDATE,                                   //更新我的消息列表
        SF_COMIC_DETAIL_LIST_PAIXU,                                 //点击漫画详情排序按钮
        SF_COMIC_DETAIL_LIST_SHOWMORE,                              //点击漫画详情显示更多按钮
        SF_COMIC_DETAIL_LIST_SHOWPOINT,                              //点击评分按钮
        QQPOCKET_PAY_SUCC,                                             //qq钱包支付成功
        QQPOCKET_PAY_FAIL,                                             //qq钱包支付失败或取消
        SF_MYNOVEL_SELETED_NOVELTAGS,                                //点击小说标签事件（我的小说）
        SF_MYNOVEL_SELETED_NOVELTAGS_POSITION,                        //记录小说tags位置（我的小说）
        SF_COMIC_DETAIL_STORY_SUCCEED,                               //收藏漫画成功
        FIND_PSW_SUCC,                                                 //找回密码成功
        SF_LOAD_SHOWWAITDIALOG,                                      //显示加载对话框
        BIND_USER_INFO_SUCC,                                         //绑定功能
        GUIDE_START,                                                 //引导页开始
        SVIP_ZOOM_INIT,                                                 //SVIP详情页放大图片初始化
        SHOW_OR_HIDE_MAIN_MENUS,                                     //隐藏或显示主菜单按钮
        SHOW_SHARE_MENU,                                             //弹出分享菜单
        HIDE_SAVE_DRAFT,                                            //隐藏草稿布局
        SAVE_DRAFT_SUCC,                                            //保存草稿成功
        REMOVE_DRAFT_SUCC,                                          //删除草稿成功
        HOT_TAGS_UPDATES,                                           //热门标签更新
        ALBUM_MANAGER_SELETED_A_SONG,                                //选中删除一个音频下载
        ALBUM_MANAGER_PLAY_A_SONG,                                  //选中播放一个音频下载
        ALBUM_DETAIL_SEEKTO_POS,                                    //音频详情推拽播放
        ALBUM_DETAIL_SHOW_MULU,                                     //音频详情显示目录
        ALBUM_DETAIL_GO_BACK,                                       //音频详情点击退出
        ALBUM_DETAIL_SHOWORSTOP_LOADING,                            //控制显示laoding
        ALBUM_DETAIL_PLAY,                                          //详情播放
        ALBUM_DETAIL_PAUSE,                                          //详情暂停播放
        ALBUM_DETAIL_PRE_PLAY,                                       //详情上一首
        ALBUM_DETAIL_NEXT_PLAY,                                      //详情下一首
        ALBUM_DETAIL_SHOW_SHARE,                                     //详情分享
        ALBUM_DETAIL_SELETED_A_SONG,                                //选中一个音频
        ALBUM_DETAIL_PLAY_A_SONG,                                   //播放一个音频
        ALBUM_DETAIL_RELOAD_CACHE,                                   //刷新界面数据
        SF_MYNOVEL_SELETED_TAG_CONTEST,                               //点击征文事件
        SF_MYNOVEL_SELETED_CONTEST,                                   //选择征文类型
        SF_PERSONAL_HOMEPAGE_LCMT_REPLY,                               //个人主页书评添加了一条回复
        SF_PERSONAL_HOMEPAGE_CMT_FAV,                                   //个人主页简评点赞
        SF_FEEDBACK_DELETE_HASREPLY,                                   //删除有回复提示
        SF_SETTING_DELETE_HASREPLY,                                   //删除设置有回复提示
        SF_UPDATE_USER_BG,                                                  //更新个人主页背景图片
        SF_ALBUM_ACTION_USER,                                               //点击用户显示“@用户”
        SF_ALBUM_FAV_FRESHE_UI,                                         //音频评论点赞刷新界面
        SF_NOVEL_FAV_FRESHE_UI,                                         //小说评论点赞刷新界面
        SF_COMIC_FAV_FRESHE_UI,                                         //漫画评论点赞刷新界面
        SF_PERSONAL_HOMEPAGE_LCMT_FAVCOUNT,                              //个人主页书评点赞
        SF_PERSONAL_HOMEPAGE_CMT_REPLY,                                   //个人主页短评添加了一条回复
        SF_ALBUM_REPLY_FRESHE_UI,                                         //音频评论详情添加一条回复
        SF_READ_SYS_MESSAGE_SUCESS,                                       //读取系统消息成功
    }

    public Message(Type type, Object data, long value, int priority, Object sender) {
        this.type = type;
        this.data = data;
        this.value = value;
        this.priority = priority;
        this.sender = sender;
    }

    public Message(Type type, Object data, long value) {
        this(type, data, value, PRIORITY_NORMAL, null);
    }

    public Message(Type type, Object data) {
        this(type, data, 0, PRIORITY_NORMAL, null);
    }

    public Message(Type type, long value) {
        this(type, null, value);
    }

    public void reset() {
        type = Type.NONE;
        data = null;
        value = 0;
        priority = PRIORITY_NORMAL;
        sender = null;
    }

    public void recycle() {
        if (mCachedMessagePool.size() < MAX_CACHED_MESSAGE_OBJ) {
            reset();
            mCachedMessagePool.add(this);
        }
    }

    public static Message obtainMessage(Message.Type msgType, Object data, long value, int priority, Object sender) {
        Message message = mCachedMessagePool.poll();

        if (message != null) {
            message.type = msgType;
            message.data = data;
            message.value = value;
            message.priority = priority;
            message.sender = sender;

        } else {
            message = new Message(msgType, data, value, priority, sender);
        }

        return message;
    }

    public Type type;
    public Object data;
    public int priority;
    public long value;
    public Object sender;

    public int referenceCount;
}
