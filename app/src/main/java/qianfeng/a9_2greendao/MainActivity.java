package qianfeng.a9_2greendao;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.greendao.database.Database;

import java.util.List;

/**
 * 1.在project的gradle文件中引入classpath 'org.greenrobot:greendao-gradle-plugin:3.2.0'
 * 2.配置module级别的gradle文件
 * 3.创建实体类
 * 4.初始化数据库
 */

public class MainActivity extends AppCompatActivity {

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DaoMaster.DevOpenHelper dbHelper = new DaoMaster.DevOpenHelper(this,"android.db");

        Database db = dbHelper.getWritableDb();

        DaoSession daoSession = new DaoMaster(db).newSession();

        // 通过这个daoSession.getUserDao()：获取到所有的实体类，只要是添加了注解的。但是照这种情况来看，只能获取android.db这个数据库里面的所有实体类啊？
        // 这个userDao是我们不断操作数据库中的表的对象！
        userDao = daoSession.getUserDao();
    }

    public void addData(View view) {
        User user = new User();
        user.setPassword("111111");
        user.setUsername("张三");

        userDao.insert(user);
    }

    public void deleteData(View view) {

        // 删除表中所有的数据
//        userDao.deleteAll();
        // 先查询再删除所有符合条件的数据，用list集合返回，再遍历list集合进行数据删除
        // .list代表查询结果是多条，如果确定查询结果只有一条,那就用.unique()
        List<User> list = userDao.queryBuilder().where(UserDao.Properties.Id.ge(5)).list();

        for (int i = 0; i < list.size(); i++) {
//            userDao.delete(list.get(i));
            userDao.deleteByKey(list.get(i).getId());
        }

    }

    public void updateData(View view) {
        User user = userDao.queryBuilder().where(UserDao.Properties.Id.eq(5)).unique();
        if(user == null)
        {
            Toast.makeText(MainActivity.this, "没有该用户", Toast.LENGTH_SHORT).show();
            return;
        }
        user.setUsername("list");
        userDao.update(user);
    }

    public void search(View view) {
        List<User> list = userDao.queryBuilder().where(UserDao.Properties.Id.ge(5)).list();
        for (int i = 0; i < list.size(); i++) {
            Log.d("google-my:", "search: " + list.get(i));
        }
    }
}
