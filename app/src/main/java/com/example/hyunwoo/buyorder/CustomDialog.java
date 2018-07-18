package com.example.hyunwoo.buyorder;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hyunwoo on 2018-02-21.
 */

public class CustomDialog extends Dialog implements AdapterView.OnItemClickListener {
    ArrayAdapter<CharSequence> adspin;

    private static final String TAG_MARKET = "market";
    private static final String TAG_RESULT = "result";
    private HttpConnection httpConn = HttpConnection.getInstance();
    TextView popupproduct2;
    TextView popuporder2;
    TextView popupbuy2;
    TextView popuprequest2;
    TextView popupkg2;
    TextView popupprice2;
    TextView popupornum;
    AutoCompleteTextView popupbuycom2;
    Button popupbutton, popupadd;
    String market, marketname;
    JSONArray marketlist;
    ListView popuplistview;
    Spinner spinner;
    ArrayList<String> marketarraylist = new ArrayList<>();
    String[] items = {};

    CustomDialogListAdapter customDialogListAdapter;

    public CustomDialog(final Context context) {
        super(context);

        customDialogListAdapter = com.example.hyunwoo.buyorder.MainActivity.customDialogListAdapter;

        requestWindowFeature(Window.FEATURE_NO_TITLE);  //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));    //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.screen_popup);  //다이얼로그에서 사용할 레이아웃입니다.

        sendData2();

        popupbuycom2 = (AutoCompleteTextView) findViewById(R.id.popupbuycom2);
        popupproduct2 = (TextView) findViewById(R.id.popupproduct2);
        popuporder2 = (TextView) findViewById(R.id.popuporder2);
        popupbuy2 = (TextView) findViewById(R.id.popupbuy2);
        popuprequest2 = (TextView) findViewById(R.id.popuprequest2);
        popupkg2 = (TextView) findViewById(R.id.popupkg2);
        popupprice2 = (TextView) findViewById(R.id.popupprice2);
        popupornum = (TextView) findViewById(R.id.popupornum);
        popupbutton = (Button) findViewById(R.id.popupbutton);
        popupadd = (Button) findViewById(R.id.popupadd);
        popuplistview = (ListView) findViewById(R.id.popuplistview);
        //popuplistview.setAdapter(customDialogListAdapter);
        //customDialogListAdapter.notifyDataSetChanged();
        popuplistview.setOnItemClickListener(this);

        registerForContextMenu(popuplistview);
        spinner = (Spinner) findViewById(R.id.spinner);

        adspin = ArrayAdapter.createFromResource(getContext(), R.array.selected, android.R.layout.simple_spinner_item);
        adspin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adspin);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // MarketList
    public void sendData2() {
        new Thread() {
            public void run() {
                httpConn.requestWebServer2(callback2);
            }
        }.start();
    }

    private final Callback callback2 = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d(TAG_RESULT, "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            market = response.body().string();
            Log.d(TAG_RESULT, "서버 응답:" + market);
            try {
                JSONObject jsonObj = new JSONObject(market);
                marketlist = jsonObj.getJSONArray(TAG_RESULT);

                for (int j = 0; j < marketlist.length(); j++) {
                    JSONObject object = marketlist.getJSONObject(j);
                    marketname = object.getString(TAG_MARKET);

                    marketarraylist.add(marketname);
                }
                items = marketarraylist.toArray(new String[marketarraylist.size()]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void sendData3(final String company, final Double kg, final int price, final String origin, final String order, final String date, final int chc) {
        new Thread() {
            public void run() {
                httpConn.requestWebServer3(company, kg, price, origin, order, date, chc, callback3);
            }
        }.start();
    }

    private final Callback callback3 = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d(TAG_RESULT, "콜백오류:" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        final CharSequence items2[] = {"거래처 변경", "원산지 변경", "무게 변경", "단가 변경", "삭제"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("구매처 : " + customDialogListAdapter.buylist1.get(position).getBuycom1() + "    " + "원산지 : " + customDialogListAdapter.buylist1.get(position).getOrigin1()
                + "    " + "무게 : " + customDialogListAdapter.buylist1.get(position).getKG1() + "kg" + "    " + "단가 : " + customDialogListAdapter.buylist1.get(position).getPrice1() + "원");
        builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setSingleChoiceItems(items2, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (items2[which].toString().equals("거래처 변경")) {
                    final android.support.v7.app.AlertDialog.Builder edit2 = new android.support.v7.app.AlertDialog.Builder(getContext());
                    edit2.setTitle("거래처 변경");
                    edit2.setMessage("거래처 : " + customDialogListAdapter.buylist1.get(position).getBuycom1());
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, items);
                    final AutoCompleteTextView edit3 = new AutoCompleteTextView(getContext());

                    edit3.setAdapter(adapter);
                    edit2.setView(edit3);

                    edit2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int check = 0;
                            for (BuyViewItem item : customDialogListAdapter.buylist1) {
                                if (edit3.getText().toString().equals(item.getBuycom1()) && customDialogListAdapter.buylist1.get(position).getOrigin1().equals(item.getOrigin1())) {
                                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                                    builder.setMessage("이미 등록되어있습니다. 구매내역에서 변경해주세요.");
                                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                                    check = 1;
                                    builder.show();
                                    break;
                                }
                            }
                            if (check == 0) {
                                sendData3(customDialogListAdapter.buylist1.get(position).getBuycom1(), customDialogListAdapter.buylist1.get(position).getKG1(),
                                        customDialogListAdapter.buylist1.get(position).getPrice1(), customDialogListAdapter.buylist1.get(position).getOrigin1(), popupornum.getText().toString(), edit3.getText().toString(), 3);
                                customDialogListAdapter.buylist1.get(position).setBuycom1(edit3.getText().toString());
                                customDialogListAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    edit2.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    edit2.show();
                    dialog.cancel();
                }
                if (items2[which].toString().equals("원산지 변경")) {
                    final String[] Example = new String[]{"국내산", "중국산", "베트남산", "칠레산", "대만산", "뉴질랜드산", "필리핀산", "남아공산", "미국산", "기타"};
                    new AlertDialog.Builder(getContext()).setTitle("원산지 변경").setItems(Example, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int check = 0;
                            if (Example[which].toString().equals("기타")) {
                                final android.support.v7.app.AlertDialog.Builder edit2 = new android.support.v7.app.AlertDialog.Builder(getContext());
                                edit2.setTitle("원산지 변경");
                                edit2.setMessage("현재 원산지 = " + customDialogListAdapter.buylist1.get(position).getOrigin1());

                                final EditText edit3 = new EditText(getContext());
                                edit2.setView(edit3);
                                edit2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        sendData3(customDialogListAdapter.buylist1.get(position).getBuycom1(), customDialogListAdapter.buylist1.get(position).getKG1(),
                                                customDialogListAdapter.buylist1.get(position).getPrice1(), customDialogListAdapter.buylist1.get(position).getOrigin1(),
                                                popupornum.getText().toString(), edit3.getText().toString(), 4);
                                        customDialogListAdapter.buylist1.get(position).setOrigin1(edit3.getText().toString());
                                        customDialogListAdapter.notifyDataSetChanged();
                                    }
                                });
                                edit2.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                                edit2.show();
                                dialog.cancel();
                                Log.d("테스트", customDialogListAdapter.buylist1.get(position).getOrigin1());
                                check = 1;
                            } else {
                                for (BuyViewItem item : customDialogListAdapter.buylist1) {
                                    if (Example[which].toString().equals(item.getOrigin1()) && customDialogListAdapter.buylist1.get(position).getBuycom1().equals(item.getBuycom1())) {
                                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                                        builder.setMessage("이미 등록되어있습니다. 구매내역에서 변경해주세요.");
                                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                                        builder.show();
                                        check = 1;
                                    }
                                }
                            }
                            if (check == 0) {
                                sendData3(customDialogListAdapter.buylist1.get(position).getBuycom1(), customDialogListAdapter.buylist1.get(position).getKG1(),
                                        customDialogListAdapter.buylist1.get(position).getPrice1(), customDialogListAdapter.buylist1.get(position).getOrigin1(),
                                        popupornum.getText().toString(), Example[which].toString(), 4);
                                customDialogListAdapter.buylist1.get(position).setOrigin1(Example[which].toString());
                                customDialogListAdapter.notifyDataSetChanged();
                            }
                        }

                    }).setNegativeButton("", null).show();
                    dialog.cancel();
                }
                if (items2[which].toString().equals("무게 변경")) {
                    final android.support.v7.app.AlertDialog.Builder edit2 = new android.support.v7.app.AlertDialog.Builder(getContext());
                    edit2.setTitle("무게 변경");
                    edit2.setMessage("현재 무게 = " + customDialogListAdapter.buylist1.get(position).getKG1());

                    final EditText edit3 = new EditText(getContext());
                    edit2.setView(edit3);
                    edit3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                    edit2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendData3(customDialogListAdapter.buylist1.get(position).getBuycom1(), customDialogListAdapter.buylist1.get(position).getKG1(),
                                    customDialogListAdapter.buylist1.get(position).getPrice1(), customDialogListAdapter.buylist1.get(position).getOrigin1(),
                                    popupornum.getText().toString(), edit3.getText().toString(), 5);
                            if (customDialogListAdapter.buylist1.get(position).getKG1() > Double.valueOf(edit3.getText().toString())) {
                                double dm = customDialogListAdapter.buylist1.get(position).getKG1() - Double.valueOf(edit3.getText().toString());
                                popupbuy2.setText("" + (Double.parseDouble(popupbuy2.getText().toString()) - dm));
                            } else {
                                double dm = Double.valueOf(edit3.getText().toString()) - customDialogListAdapter.buylist1.get(position).getKG1();
                                popupbuy2.setText("" + (Double.parseDouble(popupbuy2.getText().toString()) + dm));
                            }
                            customDialogListAdapter.buylist1.get(position).setKG1(Double.valueOf(edit3.getText().toString()));
                            customDialogListAdapter.notifyDataSetChanged();
                        }
                    });
                    edit2.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    edit2.show();
                    dialog.cancel();
                }
                if (items2[which].toString().equals("단가 변경")) {
                    final android.support.v7.app.AlertDialog.Builder edit2 = new android.support.v7.app.AlertDialog.Builder(getContext());
                    edit2.setTitle("단가 변경");
                    edit2.setMessage("현재 단가 = " + customDialogListAdapter.buylist1.get(position).getPrice1());

                    final EditText edit3 = new EditText(getContext());
                    edit2.setView(edit3);
                    edit3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

                    edit2.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendData3(customDialogListAdapter.buylist1.get(position).getBuycom1(), customDialogListAdapter.buylist1.get(position).getKG1(),
                                    customDialogListAdapter.buylist1.get(position).getPrice1(), customDialogListAdapter.buylist1.get(position).getOrigin1(),
                                    popupornum.getText().toString(), edit3.getText().toString(), 6);
                            customDialogListAdapter.buylist1.get(position).setPrice1(Integer.parseInt(edit3.getText().toString()));
                            customDialogListAdapter.notifyDataSetChanged();
                        }
                    });
                    edit2.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    edit2.show();
                    dialog.cancel();
                }
                if (items2[which].toString().equals("삭제")) {
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());
                    builder.setMessage("삭제하시겠습니까?");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            sendData3(customDialogListAdapter.buylist1.get(position).getBuycom1(), customDialogListAdapter.buylist1.get(position).getKG1(),
                                    customDialogListAdapter.buylist1.get(position).getPrice1(), customDialogListAdapter.buylist1.get(position).origin1, popupornum.getText().toString(), "", 9);
                            popupbuy2.setText("" + (Double.parseDouble(popupbuy2.getText().toString()) - customDialogListAdapter.buylist1.get(position).getKG1()));
                            customDialogListAdapter.buylist1.remove(position);
                            customDialogListAdapter.notifyDataSetChanged();
                            dialog.cancel();
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                    dialog.cancel();
                }
            }
        });
        builder.show();
    }
}