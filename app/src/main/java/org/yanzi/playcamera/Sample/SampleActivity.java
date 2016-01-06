package org.yanzi.playcamera.Sample;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;

import org.yanzi.playcamera.CameraActivity;
import org.yanzi.playcamera.R;
import org.yanzi.playcamera.Sample.Sample3_1.Sample3_1Activity;
import org.yanzi.playcamera.Sample.Sample5_10.Sample5_10_Activity;
import org.yanzi.playcamera.Sample.Sample5_5.Sample5_5_Activity;
import org.yanzi.playcamera.Sample.Sample5_9.Sample5_9_Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//ListAcitvity�ο���http://www.cnblogs.com/wservices/archive/2010/06/17/1759793.html
public class SampleActivity extends ListActivity {

    private String []activityName = new String[] {
            "CameraGL",
            "Sample3_1",
            "Sample5_5",
            "Sample5_9",
            "Sample5_10",
    };

    private String []activityDes = new String[] {
            "Main Activity",
            "Sample3_1",
            "Sample5_5",
            "Sample5_9",
            "Sample5_10",
    };

    private Class []activityClass = new Class[] {
            CameraActivity.class,
            Sample3_1Activity.class,
            Sample5_5_Activity.class,
            Sample5_9_Activity.class,
            Sample5_10_Activity.class,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        //ListView�ο� ��
        // http://blog.csdn.net/hellohm/article/details/12356649
        // http://www.cnblogs.com/allin/archive/2010/05/11/1732200.html

        //useArrayAdapter();
        useSimpleAdapter();
        //useSimpleCursorAdapter();
    }

    private void useArrayAdapter() {
        String[] data = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

        // ��XML�е�ListView����Ϊdata������
        ListView listView = getListView();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item, data);

    	/*Android�ٷ��ṩ��ListItem��Layout
	     * ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
    	 * android.R.layout.simple_list_item_1, data);
    	 */

        listView.setAdapter(arrayAdapter);
    }

    private void useSimpleAdapter() {
        // ͼƬ��Դ��ID
        int[] images = new int[] { R.drawable.item_img_a,
                R.drawable.item_img_a, R.drawable.item_img_a,
                R.drawable.item_img_a, R.drawable.item_img_a };

        // ������̬��������Դ
        List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < activityClass.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("ItemImage", images[0]);
            map.put("ItemTitle", activityName[i]);
            map.put("ItemText", activityDes[i]);
            data.add(map);
        }

        // ��XML�е�ListView����ΪListItem������
        ListView listView = getListView();      //��ListActivity��ʹ�øú�����������findViewById()

        // ��̬��������Դ����ListItem��ÿ����ʾ���Ӧ��Key
        String[] from = new String[] { "ItemImage", "ItemTitle", "ItemText" };
        // ListItem��XML�ļ������һ��ImageView ID������TextView ID
        int[] to = new int[] { R.id.ItemImage, R.id.ItemTitle, R.id.ItemText };

        // ����̬��������Դdata�е�������䵽ListItem��XML�ļ�list_item.xml��ȥ
        // �Ӷ�̬��������Դdata�У�ȡ��from������key��Ӧ��valueֵ����䵽to�����ж�ӦID�Ŀؼ���ȥ
        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.list_item_simple_adapter, from, to);
        listView.setAdapter(adapter);
    }

    private void useSimpleCursorAdapter() {
        DBHelper dbHelper = new DBHelper(this);
        // �����ݿ��в�������
        insertDataIntoDB(dbHelper);
        Cursor cursor = dbHelper.query();

        // ��XML�е�ListView����ΪItem������
        ListView listView = getListView();

        // ���ݿ�����ListItem��ÿ����ʾ���Ӧ��column
        String[] from = new String[] { "ItemImage", "ItemTitle", "ItemText" };
        // ListItem��XML�ļ������һ��ImageView ID������TextView ID
        int[] to = new int[] { R.id.ItemImage, R.id.ItemTitle, R.id.ItemText };

        // �����ݿ���������䵽ListItem��XML�ļ�list_item.xml��ȥ
        // �����ݿ���ȡ��from������column��Ӧ��ֵ����䵽to�����ж�ӦID�Ŀؼ���ȥ
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.list_item_simple_adapter, cursor, from, to,
                android.widget.CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(adapter);
    }

    private void insertDataIntoDB(DBHelper dbHelper) {
        dbHelper.clear();
        // ͼƬ��Դ��ID
        int[] images = new int[] { R.drawable.item_img_a,
                R.drawable.item_img_a, R.drawable.item_img_a,
                R.drawable.item_img_a, R.drawable.item_img_a };

        for (int i = 0; i < 5; i++) {
            ContentValues values = new ContentValues();
            values.put("ItemImage", images[i]);
            values.put("ItemTitle", "This is Title " + i);
            values.put("ItemText", "This is text " + i);
            dbHelper.insert(values);
        }
    }

    public class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "testDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createTableSQL = "create table IF NOT EXISTS tbl_test "
                    + "(_id integer primary key autoincrement, ItemImage int, "
                    + "ItemTitle text, ItemText text)";
            db.execSQL(createTableSQL);
        }

        public void insert(ContentValues values) {
            SQLiteDatabase db = getWritableDatabase();
            db.insert("tbl_test", null, values);
        }

        public Cursor query() {
            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.query("tbl_test", null, null, null, null, null, null);
            return cursor;
        }

        public void clear() {
            SQLiteDatabase db = getWritableDatabase();
            db.delete("tbl_test", null, null);
        }

        public void close() {
            SQLiteDatabase db = getWritableDatabase();
            db.close();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Log.e("ListActivity click", "position: " + position + ", id: " + id);
        Intent intent = new Intent();
        intent.setClass(SampleActivity.this, activityClass[position]);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sample, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
