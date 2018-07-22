package com.example.hyunwoo.buyorder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ProductDialog extends Dialog implements AdapterView.OnItemClickListener {
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_CODE = "code";
    private static final String TAG_RESULT = "result";
    private HttpConnection httpConn = HttpConnection.getInstance();

    int loc = 0;
    Button Ok, Cancel;
    ListView productlist;
    TextView Datetext;
    String produ, productname, pdcode;
    JSONArray pdlist;
    private ProductDialog activity;
    public static ProductDialogListAdapter productDialogListAdapter;

    public ProductDialog(final Context context) {
        super(context);
        activity = this;

        productDialogListAdapter = new ProductDialogListAdapter(activity);
        sendData5();

        requestWindowFeature(Window.FEATURE_NO_TITLE);  //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        setContentView(R.layout.product_popup);  //다이얼로그에서 사용할 레이아웃입니다.

        Datetext = (TextView) findViewById(R.id.Datetext);
        productlist = (ListView) findViewById(R.id.productlist);
        Ok = (Button) findViewById(R.id.Ok);
        Cancel = (Button) findViewById(R.id.Cancel);
        productlist.setAdapter(productDialogListAdapter);
        productlist.setOnItemClickListener(this);

        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void sendData5() {
        new Thread() {
            public void run() {
                httpConn.requestWebServer5(callback5);
            }
        }.start();
    }

    private final Callback callback5 = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d(TAG_RESULT, "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            produ = response.body().string();
            Log.d(TAG_RESULT, "서버 응답:" + produ);
            try {
                JSONObject jsonObj = new JSONObject(produ);
                pdlist = jsonObj.getJSONArray(TAG_RESULT);

                for (int j = 0; j < pdlist.length(); j++) {
                    JSONObject object = pdlist.getJSONObject(j);
                    productname = object.getString(TAG_PRODUCT);
                    pdcode = object.getString(TAG_CODE);

                    productDialogListAdapter.addItem(productname, pdcode, "", "");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            productDialogListAdapter.notifyDataSetChanged();
        }
    };

    /**
     * Callback method to be invoked when an item in this AdapterView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param parent   The AdapterView where the click happened.
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     * @param id       The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final CharSequence items2[] = {"국내산", "중국산", "베트남산", "칠레산", "대만산", "뉴질랜드산", "필리핀산", "남아공산", "미국산", "기타"};

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("원산지선택");
        builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.d("테스트", String.valueOf(Log.d("테스트", MainActivity.orderListAdapter.list.get(0).getPrice().toString().substring(0,3))));
            }
        });
        builder.setSingleChoiceItems(items2, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int ccheck = 0;

                if (items2[which].toString().equals("국내산")) {
                    loc = 1;
                }
                if (items2[which].toString().equals("중국산")) {
                    loc = 2;
                }
                if (items2[which].toString().equals("베트남산")) {
                    loc = 3;
                }
                if (items2[which].toString().equals("칠레산")) {
                    loc = 4;
                }
                if (items2[which].toString().equals("대만산")) {
                    loc = 5;
                }
                if (items2[which].toString().equals("뉴질랜드산")) {
                    loc = 6;
                }
                if (items2[which].toString().equals("필리핀산")) {
                    loc = 7;
                }
                if (items2[which].toString().equals("남아공산")) {
                    loc = 8;
                }
                if (items2[which].toString().equals("미국산")) {
                    loc = 9;
                }
                if (items2[which].toString().equals("기타")) {
                    loc = 0;
                }
                for (OrderList list : MainActivity.orderListAdapter.list) {
                    if (list.getProduct().equals(productDialogListAdapter.list.get(position).getProduct()) && list.getOrigin().equals(items2[which].toString())) {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                        builder.setMessage("이미 등록되어있습니다. 구매주문서를 확인해주세요.");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        ccheck = 1;
                        builder.show();
                        break;
                    }
                }
                if (ccheck == 0 && !items2[which].equals("기타")) {
                    MainActivity.orderListAdapter.addItem(productDialogListAdapter.list.get(position).getProduct(), items2[which].toString(), String.valueOf(0),
                            productDialogListAdapter.list.get(position).getCode().toString().substring(4, 9) + String.valueOf(loc) + "00", "", "추가구매");
                    MainActivity.orderListAdapter.notifyDataSetChanged();
                    dismiss();
                } else if (ccheck == 0 && items2[which].equals("기타")) {
                    final android.support.v7.app.AlertDialog.Builder edit2 = new android.support.v7.app.AlertDialog.Builder(getContext());
                    edit2.setTitle("원산지 입력");

                    final EditText edit3 = new EditText(getContext());
                    edit2.setView(edit3);
                    edit2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.orderListAdapter.addItem(productDialogListAdapter.list.get(position).getProduct(), edit3.getText().toString(), String.valueOf(0),
                                    productDialogListAdapter.list.get(position).getCode().toString().substring(4, 9) + String.valueOf(loc) + "00", "", "추가구매");
                            MainActivity.orderListAdapter.notifyDataSetChanged();
                            dialog.cancel();
                        }
                    });
                    edit2.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    edit2.show();
                    dismiss();
                }
                dialog.cancel();
            }
        });
        builder.show();
    }
}
