package com.busticket.amedora.busticketverify;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.busticket.amedora.busticketverify.model.*;
import com.busticket.amedora.busticketverify.model.Transaction;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.busticket.amedora.busticketverify.utils.*;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Amedora on 7/17/2015.
 */
public class ScannerActivity extends Activity {
     static String QR_RESULT;
    static String qrContent ="";
    LinearLayout myLayout;
    TextView tvResultText ;
    Button btnScanResultButton;
    String[] dias, accdata;//String[] accdata
    private static boolean myflag = false;
    String[] QrCont;
    String bankName;
    boolean success =false;
    String ERRORMSG;
    ProgressDialog pDialog  =null;
    ProgressDialog pDialog2  =null;
    public static String TAG_BANK_NAME,TAG_SHORT_NAME;
    DatabaseHelper db = new DatabaseHelper(this);
    ArrayList<HashMap<String, String>> accountList;
    List<NameValuePair> nameValuePairs;
    RequestQueue mQueue;

    HttpClient httpclient;
    HttpPost httppost;
    HttpResponse response;

    protected void onCreate(Bundle savedInstanceBundle){
        super.onCreate(savedInstanceBundle);
        setContentView(R.layout.activity_qrscanner);
        HandleClick hc = new HandleClick();
        findViewById(R.id.btnScan).setOnClickListener(hc);

        mQueue = Volley.newRequestQueue(getApplicationContext());
    }

    private class HandleClick implements View.OnClickListener {
        public void onClick(View arg0) {
            IntentIntegrator integrator = new IntentIntegrator(ScannerActivity.this);
            integrator.addExtra("SCAN_WIDTH", 640);
            integrator.addExtra("SCAN_HEIGHT", 480);
            integrator.addExtra("SCAN_MODE", "QR_CODE_MODE,PRODUCT_MODE");
            //customize the prompt message before scanning
            integrator.addExtra("PROMPT_MESSAGE", "Scanner Start!");
            integrator.initiateScan(IntentIntegrator.QR_CODE_TYPES);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //TextView tvResult=(TextView)findViewById(R.id.tvResult);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            String contents = result.getContents();
            if (contents != null) {
                //showDialog(R.string.result_succeeded, result.toString());
                //tvResult.setText(result.toString());
                QR_RESULT = intent.getStringExtra("SCAN_RESULT");
                qrContent = QR_RESULT;
                QrCont = qrContent.split(";");

                Log.e("QR STRING", QrCont.toString());

                //tvResult.setText(qrContent);
               showAlert();
            } else {

            }
        }
    }

    private void OpenDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Select Account");
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.dialog_initiate_pay_select_bank, null);
        dialog.setView(v);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                pDialog = ProgressDialog.show(ScannerActivity.this, "",
                        "Processing Request...", true);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Looper.prepare();

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                if(AppStatus.getInstance(ScannerActivity.this).isOnline()){
                                    sendSMS();
                                }else{
                                    processTransaction();
                                }
                                handler.removeCallbacks(this);

                                Looper.myLooper().quit();
                            }
                        }, 2000);

                        Looper.loop();
                    }
                }).start();
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();
        Spinner sp = (Spinner)v.findViewById(R.id.spSelectBank);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.YELLOW);
                ((TextView) parent.getChildAt(0)).setTextSize(25);
                bankName = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        accountList = new ArrayList<HashMap<String, String>>();
        List<Account> bank = db.getAllAccount();
        System.out.println(bank.size());
        // loop through each website
        dias = new String[bank.size()];
        for (int i = 0; i < bank.size(); i++) {
            Account s = bank.get(i);
            // creating new HashMap
            HashMap<String, String> map = new HashMap<String, String>();
            // adding each child node to HashMap key => value

            map.put(TAG_BANK_NAME, String.valueOf(s.getAccount_no()));
            // adding HashList to ArrayList
            accountList.add(map);
            // add sqlite id to array
            // used when deleting a website from sqlite
            dias[i] = String.valueOf(s.getAccount_no() + "," + s.getBank());
        }

        ArrayAdapter adp = new ArrayAdapter(this,android.R.layout.simple_spinner_item,dias);
        adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adp);
    }

    public void processTransaction(){
       // pDialog.dismiss();
        try{
            accdata = bankName.split(",");

            httpclient=new DefaultHttpClient();
            httppost= new HttpPost("http://mylcpschoolbook.net/VirtualPOS/vposapi/");

           String cusData = Installation.appId(this)+";"+accdata[1]+";"+accdata[0]+";"+db.getBankSortCode(accdata[1]);
           /*  String tr = "{'transaction':{'cus_app_id':'"+ Installation.appId(this)+"','cus_bank_name':'"+accdata[1]
                    +"','cus_bank_no':'"+accdata[0]+"','cus_bank_code':'"+db.getBankSortCode(accdata[1])+"'}}";*/
            nameValuePairs = new ArrayList<>(2);
            nameValuePairs.add(new BasicNameValuePair("controller","transaction"));
            nameValuePairs.add(new BasicNameValuePair("action",  "create"));
            nameValuePairs.add(new BasicNameValuePair("merchData",QR_RESULT));
            nameValuePairs.add(new BasicNameValuePair("cusData",cusData));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response = httpclient.execute(httppost);
            Integer status = response.getStatusLine().getStatusCode();
            if(status == 200){
               //tvResultText.setText("API Server reached");
                Toast.makeText(ScannerActivity.this, "server reached", Toast.LENGTH_SHORT).show();
                String result = EntityUtils.toString(response.getEntity());
                JSONObject jsonResponse;
                jsonResponse = new JSONObject(result);
                // CONVERT RESPONSE STRING TO JSON ARRAY
                JSONArray ja = jsonResponse.getJSONArray("data");
                // ITERATE THROUGH AND RETRIEVE CLUB FIELDS
                int n = ja.length();
                for (int i = 0; i < n; i++) {
                    // GET INDIVIDUAL JSON OBJECT FROM JSON ARRAY
                    JSONObject jo = ja.getJSONObject(i);
                    success = jo.optBoolean("success");
                    //tvMessage.setText(jo.optString("errmsg"));
                    //userId = jo.getInt("id");
                    ERRORMSG =jo.optString("errmsg");
                    System.out.println("Status " +jo.optString("errmsg"));
                    if(i>0){
                        break;
                    }
                }

                if(success){
                    Transaction trans = new Transaction();
                    trans.setAccount_no(accdata[0]);
                    //trans.setApp_id();
                    trans.setTrans_amount(Double.parseDouble(QrCont[0].replace("?", "")));
                    trans.setNarration("Transaction Initiated");
                    trans.setTrans_type("Sales");
                    trans.setTransId(QrCont[1]);

                    if(db.createTransaction(trans)>0){
                       // Intent regIntent = new Intent(ScannerActivity.this, TransactionListingActivity.class);
                        pDialog.dismiss();
                        //startActivity(regIntent);
                    }else{
                        helperAlert("Transaction was not successful");
                        pDialog.dismiss();
                    }

                }else{
                    helperAlert("Transaction was not successful");
                    pDialog.dismiss();

                }
            }
        }catch (Exception e){
            pDialog.dismiss();
            helperAlert(e.getMessage());
        }

    }

    public void showAlert(){

                AlertDialog.Builder builder = new AlertDialog.Builder(ScannerActivity.this);
                builder.setTitle("Ticket Verification");
                builder.setMessage("A transaction of "+QrCont[0].replace("?","₦")+" ")

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //this.finalize();
                                OpenDialog();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ScannerActivity.myflag =false;
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }

    public void helperAlert(String msg){

        AlertDialog.Builder build = new AlertDialog.Builder(ScannerActivity.this);
        build.setTitle("Transaction");
        build.setMessage(msg)

                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //this.finalize();

                    }
                });

        AlertDialog alert = build.create();
        alert.show();

    }

    private void sendSMS(){
        Log.i("Send SMS", "");
        /*Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse(phone));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address"  , new String ("01234"));
        smsIntent.putExtra("sms_body"  ,message);*/
        String cusData = Installation.appId(this)+";"+accdata[1]+";"+accdata[0]+";"+db.getBankSortCode(accdata[1]);
        String message = QR_RESULT +"__"+cusData ;
        try {
           /* startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");*/
            String phone = "08063577714";
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ScannerActivity.this,
                    "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }

    }


    private void postTicket(){
        List<Ticket> usedTickets = db.getUsedTickets();
        int k =0;
        if(usedTickets.size()>=1){
            for(Ticket ticket : usedTickets){
                String url ="http://41.77.173.124:81/busticketAPI/tickets/update/"+ticket.getTicket_id()+"/1";
                JsonObjectRequest jUpdateTicket = new JsonObjectRequest(Request.Method.GET,url,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.getString("msg") == "success"){
                                Toast.makeText(ScannerActivity.this,"Record Updated ton server Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }catch(Exception e){
                            Toast.makeText(ScannerActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ScannerActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
                mQueue.add(jUpdateTicket);
            }
        }

    }

}
