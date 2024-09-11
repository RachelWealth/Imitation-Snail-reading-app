package bjfu.it.duanyingli.dreader.dbHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import bjfu.it.duanyingli.dreader.R;

public class DUserDatabaseHelper extends SQLiteOpenHelper {
    private static  final String DB_NAME = "DReaderUser.db";
    private static  final int DB_VERSION = 1;
    public DUserDatabaseHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE USER(PhoneNum INTEGER PRIMARY KEY AUTOINCREMENT,"
                +"UName TEXT," +
                "Password);");//用户数据表
        db.execSQL("CREATE TABLE BOOK(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "BName TEXT," +
                "BAuthur TEXT," +
                "Description TEXT," +
                "BPrice INTEGER,"+
                "Type TEXT, " +
                "B_Picture_ResourceId INTEGER," +
                "Detail TEXT);");//图书数据表
        db.execSQL("CREATE TABLE FAVOURATE(PhoneNum INTEGER," +
                "_bid INTEGER," +
                "CONSTRAINT PI_Prim PRIMARY KEY (PhoneNum,_bid));");//收藏关系数据表
        db.execSQL("CREATE TABLE TYPE(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TName TEXT," +
                "T_Picture_ResourceId INTEGER);");//图书类别数据库
        db.execSQL("CREATE TABLE CAR(PhoneNum INTEGET," +
                "_bid INTEGER," +
                "BookNum INTEGER," +
                "CONSTRAINT PIC_Prim PRIMARY KEY (PhoneNum,_bid));");
        insertBook(db,"百年孤独","[哥伦比亚]加西亚·马尔克斯","百年孤独是魔幻现实主义文学的代表作", 59, R.drawable.bngd,"小说",
                "《百年孤独》，是哥伦比亚作家加西亚·马尔克斯创作的长篇小说，是其代表作，也是拉丁美洲魔幻现实主义文学的代表作，被誉为“再现拉丁美洲历史社会图景的鸿篇巨著”。\n" +
                        "作品描写了布恩迪亚家族七代人的传奇故事，以及加勒比海沿岸小镇马孔多的百年兴衰，反映了拉丁美洲一个世纪以来风云变幻的历史。作品融入神话传说、民间故事、宗教典故等神秘因素，巧妙地糅合了现实与虚幻，展现出一个瑰丽的想象世界，成为20世纪重要的经典文学巨著之一。");
        insertBook(db,"雾都孤儿","[英国]查尔斯-狄更斯","《雾都孤儿》是英国著名作家查尔斯·狄更斯的长篇小说",36,R.drawable.wdge,"小说",
                "19世纪30年代的英国，在一个寒风料峭的深夜，一个男婴在贫民区里呱呱坠地。在男婴出生后，他的母亲便撒手人寰。谁也不知道产妇的身份，男婴由此成了无名孤儿。后来他被当地教会收养，抚养他的女管事给男婴起名奥利弗（巴尼·克拉克饰）。奥利弗9岁时，由于没人供养他上学读书，于是他进入了济贫院的童工作坊，开始从事繁重的体力劳动。因为奥利弗既不会耍滑偷懒，也不会阿谀奉承，所以经常受到管事的打骂。这些处在发育期的孩子们终日衣不遮体、食不果腹。万般无奈之下，他们决定抽签选定提出加粥的人选，结果被抽中的人正是奥利弗。晚餐时，奥利弗如实提出了要求。大惊失色的管事决定撵走这个造反的隐患。\n" +
                        "奥利弗没能成为打扫烟囱的小工。济贫院以五英镑的奖赏让殡仪馆的老板领回奥利弗。奥利弗做了殡仪馆老板的老板学徒，并且很快得到老板夫妇的器重，但也遭到了年长学徒诺尔的嫉妒。由于诺尔取笑奥利弗死去的母亲，奥利弗在忍无可忍的情况下大打出手。后来，奥利弗不仅被老板误解，还遭到了老板的毒打。一气之下，奥利弗含恨出走，奔向远方的雾都伦敦。\n" +
                        "在伦敦郊区，饥寒交迫的奥利弗遇到了阿特福，阿特福不仅为他提供了栖身之处，还将他引荐给一个叫费金的人（本·金斯利饰）。天真无邪的奥利弗还不知道他住的地方其实是个贼窝，这些孩子都被当作犯罪工具，而费金正是他们的“教父”。一天，奥利弗和阿特福等人一起上街。阿特福行窃时意外败露，而奥利弗则在混乱中被人当作小偷抓进了警局。一位书店老板证明了奥利弗的无辜，而被偷的富翁布朗罗也心生爱怜，于是将奥利弗接到了家中。\n" +
                        "费金和同伙西克（杰米·福尔曼饰）并未善罢甘休，趁奥利弗外出买书之际将其绑架，而布朗罗则误以为小男孩携款潜逃，心中失望不已。又回到贼巢的的奥利弗在费金的哄骗下道出了布朗罗家的境况，并被西克胁迫前去抢劫。虽然抢劫被成功阻止，但奥利弗却被冷枪击中。当西克准备将奥利弗抛进河中之际，同行的托比救下了奥利弗。险恶叵测的西克依然鼓动费金除掉奥利弗以绝后患，而他的女友南茜则试图保护奥利弗，并和布朗罗取得联系，希望帮无辜的奥利弗逃出魔窟。然而，南茜的意图已被费金察觉，不久南茜便被西克残忍的杀害。警方对西克和费金展开了抓捕。西克在逃跑的过程中不慎丧命，而费金也最终被绳之以法。费金临刑前，布朗罗带着奥利弗探望狱中的费金，尽管经历了种种费金造就的不幸，但善良的奥利弗却仍在心底默默为他祈祷");
        insertBook(db,"红楼梦","[清朝]曹雪芹","本书以成乙本位底本，而交以个脂砚斋本及各程本",108, R.drawable.hlm,"国风文化",
                "《红楼梦》，中国古代章回体长篇小说，中国古典四大名著之一，一般认为是清代作家曹雪芹所著。小说以贾、史、王、薛四大家族的兴衰为背景，以富贵公子贾宝玉为视角，以贾宝玉与林黛玉、薛宝钗的爱情婚姻悲剧为主线，描绘了一批举止见识出于须眉之上的闺阁佳人的人生百态，展现了真正的人性美和悲剧美，可以说是一部从各个角度展现女性美以及中国古代社会世态百相的史诗性著作。\n" +
                        "《红楼梦》版本有120回“程本”和80回“脂本”两大系统。程本为程伟元排印的印刷本，脂本为脂砚斋在不同时期抄评的早期手抄本。脂本是程本的底本。\n" +
                        "《红楼梦》是一部具有世界影响力的人情小说，举世公认的中国古典小说巅峰之作，中国封建社会的百科全书，传统文化的集大成者。小说以“大旨谈情，实录其事”自勉，只按自己的事体情理，按迹循踪，摆脱旧套，新鲜别致，取得了非凡的艺术成就。“真事隐去，假语村言”的特殊笔法更是令后世读者脑洞大开，揣测之说久而遂多。二十世纪以来，学术界因《红楼梦》异常出色的艺术成就和丰富深刻的思想底蕴而产生了以《红楼梦》为研究对象的专门学问——红学。");
        insertBook(db,"记忆记忆","[俄语]玛丽亚·斯捷潘诺娃","什么我们有时记不清上周做了什么，却能记得童年往事，好像它们就发生在昨天",75, R.drawable.jyjy,"Turbo专享","" +
                "《记忆记忆》是当代俄语世界著名诗人玛丽亚·斯捷潘诺娃的新类型复合小说：既有历史，也有哲学，更是文学。\n" +
                "\n" +
                "小说主要由两条线串起：一条是作者对于旧物，文献，以及试图“记忆”的人们——所作的文学和哲学的思辨：桑塔格，曼德尔施塔姆，茨维塔耶娃，塞巴尔德，夏洛特·萨洛蒙等 等等等皆进 入了她的视野。在现在与过去中思考中得到新的诠释。\n" +
                "\n" +
                "另一条则是作者通过寻找家族遗迹，回溯俄罗斯近代史中的自我家族史，拼凑出一个犹太家族几代人生命故事的历程：他们有的融入宏大叙事，刚满20岁便牺牲在伟大的卫国战争中；有的与历史擦肩而过：参与了20世纪初期的俄国革命，成为俄国第一批“留法学医女学生”，回国后却就此沉寂；有的参与了热火朝天的苏联大建设，然而在1991年苏联解体之时毅然决然移民德国，有的——诸如在书中隐形却又无处不在的作者本人，则同一个告别过去又满是记忆的国家一起迈入了新世纪，思考俄罗斯的当下，以及或近或远的未来……\n" +
                "\n" +
                "这两条线相依相交，勾勒出巨大20世纪的诡谲风云与微小浪花。精巧复杂，娓娓道来，又包含了俄罗斯式的辽阔和沉思。在追溯与思辨中， “后记忆时代的俄罗斯”得到思考，包括俄罗斯在内的整个欧美文艺界的先贤们被重审，过去与现在、逝者与生者之间的关系和逻辑被再度梳理——“关于他们我所能讲述的越少，他们于我便越亲近。”");
        insertType(db,"Turbo专享",R.drawable.class1);
        insertType(db,"豆瓣8.0+",R.drawable.class2);
        insertType(db,"小说",R.drawable.class3);
        insertType(db,"漫画绘本",R.drawable.class4);
        insertType(db,"青春",R.drawable.class5);
        insertType(db,"推理幻想",R.drawable.class6);
        insertType(db,"历史",R.drawable.class7);
        insertType(db,"国风文化",R.drawable.class8);
        insertType(db,"兴趣培养",R.drawable.class9);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    private static void insertBook(SQLiteDatabase db,String name,String aut, String des, int price, int Pic_R, String type, String details){
        ContentValues BookValue = new ContentValues();
        BookValue.put("BName",name);
        BookValue.put("BAuthur",aut);
        BookValue.put("Description",des);
        BookValue.put("BPrice",price);
        BookValue.put("B_Picture_ResourceId",Pic_R);
        BookValue.put("Type",type);
        BookValue.put("Detail",details);
        long result = db.insert("BOOK",null,BookValue);
        Log.d("sqlite1","insert "+name+",_id:"+result);
    }

    private static void insertType(SQLiteDatabase db,String name, int Pic_R){
        ContentValues TypeValue = new ContentValues();
        TypeValue.put("TName",name);
        TypeValue.put("T_Picture_ResourceId",Pic_R);
        long result = db.insert("TYPE",null,TypeValue);
        Log.d("sqlite1","insert "+name+",_id:"+result);
    }
}
