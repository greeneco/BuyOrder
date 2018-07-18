package com.example.hyunwoo.buyorder;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    // Dialog
    CustomDialog cd;

    // OKHTTP
    private static final String TAG_RESULT = "result";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_COMPANY = "company";
    private static final String TAG_KG = "kg";
    private static final String TAG_PRICE = "price";
    private static final String TAG_LOC = "loc";
    private static final String TAG_ETC = "etc";
    private static final String TAG_ORDER = "order";
    private static final String TAG_BUY = "buy";

    private HttpConnection httpConn = HttpConnection.getInstance();
    String buyorder, product, buyorderlist, company, loc, etc, order, buy;
    JSONArray buylist, popuplist;
    Double kg;
    int check = 0, price;

    // Date
    long Now = System.currentTimeMillis();
    Date day = new Date(Now);
    SimpleDateFormat dat = new SimpleDateFormat("yyyy-MM-dd");
    String formatDate = dat.format(day);
    Calendar cal = Calendar.getInstance();
    TextView date;

    // 구매 리스트
    public static OrderListAdapter orderListAdapter;
    private ListView orderView;

    public static CustomDialogListAdapter customDialogListAdapter;
    private MainActivity activity;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customDialogListAdapter = new CustomDialogListAdapter(activity);
        activity = this;

        // Custom Dialog
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비
        int height = dm.heightPixels; //디바이스 화면 높이
        cd = new CustomDialog(this);
        WindowManager.LayoutParams wm = cd.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기 위해
        wm.copyFrom(cd.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = width;  //화면 너비의 절반
        wm.height = (int) (height / 1.3);  //화면 높이의 절반

        orderListAdapter = new OrderListAdapter();
        orderView = findViewById(R.id.orderView);
        orderView.setOnItemClickListener(listener);
        orderView.setAdapter(orderListAdapter);

        cal.add(Calendar.DATE, 1);
        sendData(dat.format(cal.getTime()));
        cal.add(Calendar.DATE, -1);
        cd.popuplistview.setAdapter(customDialogListAdapter);
        registerForContextMenu(cd.popuplistview);

        cal.setTime(day);
        date = findViewById(R.id.date);
        date.setText(formatDate + "   구매 주문서");
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderListAdapter.getCount() == 0 && check == 0) {
                    cal.add(Calendar.DATE, 1);
                    sendData(dat.format(cal.getTime()));
                    Log.d("날짜", dat.format(cal.getTime()));
                    cal.add(Calendar.DATE, -1);
                    check = 1;
                }
            }
        });
        cd.popupadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int che2 = 0;
                for (BuyViewItem item : customDialogListAdapter.buylist1) {
                    if (item.getBuycom1().equals(cd.popupbuycom2.getText().toString()) && item.getOrigin1().equals(cd.spinner.getSelectedItem().toString()) && !cd.popupbuycom2.getText().toString().equals("")
                            && !cd.popupkg2.getText().toString().equals("") && !cd.popupprice2.getText().toString().equals("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("이미 등록되어있습니다. 구매내역에서 변경해주세요.");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        builder.show();
                        cd.popupbuycom2.setText("");
                        cd.popupkg2.setText("");
                        cd.popupprice2.setText("");
                        che2 = 1;
                        break;
                    }
                }
                if ((cd.popupbuycom2.getText().toString().equals("") || cd.popupkg2.getText().toString().equals("") || cd.popupprice2.getText().toString().equals("")) && che2 == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("입력을 확인해주세요.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                    che2 = 1;
                }
                if (che2 == 0) {
                    customDialogListAdapter.addItem(cd.popupbuycom2.getText().toString(), cd.spinner.getSelectedItem().toString(),
                            Double.valueOf(cd.popupkg2.getText().toString()), Integer.parseInt(cd.popupprice2.getText().toString()), cd.popupornum.getText().toString());
                    cd.popuplistview.setAdapter(customDialogListAdapter);
                    cd.popupbuy2.setText("" + (Double.parseDouble(cd.popupbuy2.getText().toString()) + Double.parseDouble(cd.popupkg2.getText().toString())));
                    cd.sendData3(cd.popupbuycom2.getText().toString(), Double.valueOf(cd.popupkg2.getText().toString()), Integer.parseInt(cd.popupprice2.getText().toString()),
                            cd.spinner.getSelectedItem().toString(), cd.popupornum.getText().toString(), dat.format(cal.getTime()), 2);
                    cd.popupbuycom2.setText("");
                    cd.popupkg2.setText("");
                    cd.popupprice2.setText("");
                }
            }
        });
        cd.popupbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cd.dismiss();
                orderListAdapter.list.clear();
                cal.add(Calendar.DATE, 1);
                sendData(dat.format(cal.getTime()));
                cal.add(Calendar.DATE, -1);
            }
        });
    }

    // 리스트뷰 아이템 선택
    AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
            cd.popupbuycom2.setFocusableInTouchMode(true);
            cd.popupbuycom2.requestFocus();
            cd.popupproduct2.setText(orderListAdapter.list.get(position).getProduct());
            cd.popuporder2.setText(orderListAdapter.list.get(position).getOrderkg() + "kg");
            cd.popuprequest2.setText(orderListAdapter.list.get(position).getRequest());
            cd.popupornum.setText(orderListAdapter.list.get(position).getPrice());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, cd.items);
            cd.popupbuycom2.setAdapter(adapter);
            cd.popupbuy2.setText((double) 0 + "");
            cd.popupbuycom2.setText("");
            cd.popupkg2.setText("");
            cd.popupprice2.setText("");
            customDialogListAdapter.buylist1.clear();
            cd.popuplistview.setAdapter(customDialogListAdapter);
            sendData4(orderListAdapter.list.get(position).getPrice().toString());
            cd.spinner.setAdapter(cd.adspin);
            cd.show();
        }
    };

    // 구매주문서 리스트
    private void sendData(final String day) {
        new Thread() {
            public void run() {
                httpConn.requestWebServer(day, callback);
            }
        }.start();
    }

    private final Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d(TAG_RESULT, "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            buyorder = response.body().string();
            Log.d(TAG_RESULT, "서버 응답:" + buyorder);
            try {
                JSONObject jsonObj = new JSONObject(buyorder);
                buylist = jsonObj.getJSONArray(TAG_RESULT);

                for (int j = 0; j < buylist.length(); j++) {
                    JSONObject object = buylist.getJSONObject(j);
                    order = object.getString(TAG_ORDER);
                    product = object.getString(TAG_PRODUCT);
                    kg = Double.valueOf(object.getString(TAG_KG));
                    loc = object.getString(TAG_LOC);
                    etc = object.getString(TAG_ETC);
                    buy = object.getString(TAG_BUY);

                    if (buy.equals("null")) {
                        orderListAdapter.addItem(product, loc, String.valueOf(kg), "", order, etc);
                    } else {
                        orderListAdapter.addItem(product, loc, String.valueOf(kg), buy, order, etc);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    orderListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    public void sendData4(final String popupproduct) {
        new Thread() {
            public void run() {
                httpConn.requestWebServer4(popupproduct, callback4);
            }
        }.start();
    }

    private final Callback callback4 = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d(TAG_RESULT, "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            buyorderlist = response.body().string();
            Log.d(TAG_RESULT, "서버 응답:" + buyorderlist);
            try {
                JSONObject jsonObj = new JSONObject(buyorderlist);
                popuplist = jsonObj.getJSONArray(TAG_RESULT);

                for (int j = 0; j < popuplist.length(); j++) {
                    JSONObject object = popuplist.getJSONObject(j);
                    company = object.getString(TAG_COMPANY);
                    kg = Double.valueOf(object.getString(TAG_KG));
                    price = Integer.parseInt(object.getString(TAG_PRICE));
                    loc = object.getString(TAG_LOC);
                    order = object.getString(TAG_ORDER);

                    customDialogListAdapter.addItem(company, loc, kg, price, order);

                    cd.popupbuy2.setText("" + (Double.parseDouble(cd.popupbuy2.getText().toString()) +
                            Double.parseDouble(customDialogListAdapter.buylist1.get(j).getKG1().toString())));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    customDialogListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    //날짜 증가
    public void mOnClickPLUS(View v) {
        cal.add(Calendar.DATE, 1);
        date.setText(dat.format(cal.getTime()) + "   구매 주문서");
        orderListAdapter.list.clear();
        orderListAdapter.notifyDataSetChanged();
        check = 0;
    }

    //날짜 감소
    public void mOnClickMINUS(View v) {
        cal.add(Calendar.DATE, -1);
        date.setText(dat.format(cal.getTime()) + "   구매 주문서");
        orderListAdapter.list.clear();
        orderListAdapter.notifyDataSetChanged();
        check = 0;
    }
}
