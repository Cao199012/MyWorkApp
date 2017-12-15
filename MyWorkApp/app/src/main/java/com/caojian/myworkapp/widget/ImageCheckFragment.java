package com.caojian.myworkapp.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.caojian.myworkapp.MyApplication;
import com.caojian.myworkapp.R;
import com.caojian.myworkapp.ui.base.BaseTitleActivity;
import com.caojian.myworkapp.until.Until;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CJ on 2017/9/8.
 */

public class ImageCheckFragment extends AppCompatDialogFragment  {

    public static ImageCheckFragment newInstance()
    {
        ImageCheckFragment fragment = new ImageCheckFragment();
        return fragment;
    }

    @BindView(R.id.edit_imgecheck)
    AppCompatEditText editImageCheck;
    @BindView(R.id.img_imagecheck)
    ImageView imgShow;
    @BindView(R.id.btn_imagecheck)
    Button btnGetImage;

    private FragmentImageCheckListener mListrner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.imgecheck_fragment,container,false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getCodeImg();
    }

    private void init() {
        btnGetImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                getCodeImg();
            }

        });
        imgShow.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCodeImg();
            }});

    }

    @OnClick(R.id.btn_cancel)
    public void checkCancel()
    {
        mListrner.cancelCheck();
    }
    @OnClick(R.id.btn_sure)
    public void checkSumbit()
    {
          if(editImageCheck.getText() != null && !editImageCheck.getText().toString().trim().equals("")){
                mListrner.submitCheck(editImageCheck.getText().toString().trim());
            }else
            {
                ((BaseTitleActivity)getActivity()).showToast("请输入图形中的内容", Toast.LENGTH_SHORT);
            }
    }
    //获取图形验证码
    public void getCodeImg()
    {
        ((BaseTitleActivity)getActivity()).showProgress(getActivity());
        final Handler handler = new Handler(){
            public void handleMessage(Message msg) {
                ((BaseTitleActivity)getActivity()).hideProgress();
                if(msg.what == 1){
                    btnGetImage.setVisibility(View.GONE);
                    imgShow.setVisibility(View.VISIBLE);
                    imgShow.setImageBitmap(nBitmap);
                }else{
                    ((BaseTitleActivity)getActivity()).showToast("获取图形验证码失败",Toast.LENGTH_SHORT);
                    btnGetImage.setVisibility(View.VISIBLE);
                    btnGetImage.setText("请重试");
                    imgShow.setVisibility(View.GONE);
                }
//                isLoad = false;
//                dismissProgressDialog();
            };
        };
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                byte[] byteArray;
                HttpURLConnection connection = null;
                Message m = new Message();
                // TODO Auto-generated method stub
                try {

                    URL url = new URL(Until.HTTP_BASE_URL+"getCaptchaImage.do");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(1500);
                    connection.setRequestProperty("deviceNo",((MyApplication)getActivity().getApplicationContext()).getDeviceId());
                    connection.connect();
                    InputStream in = connection.getInputStream();
                    m.obj = in;
                    try {
                        byteArray = readInputStream(in);
                        nBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                        m.what = 1;
                    } catch (Exception e) {
                        m.what = -1;
                    }
                } catch (IOException e2) {
                    m.what = -1;
                    // throw new Exception("获取照片信息失败！");
                }
                handler.sendMessage(m);
                if(connection != null)
                {
                    connection.disconnect();
                }
            }

        });
        thread.start();
    }


    private Bitmap nBitmap;

    private static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024]; //创建一个Buffer字符串
        //每次读取的字符串长度，如果为-1，代表全部读取完毕
        int len = 0;
        //使用一个输入流从buffer里把数据读取出来
        while( (len=inStream.read(buffer)) != -1 ){
            //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
            outStream.write(buffer, 0, len);
        }
        inStream.close();   //关闭输入流
        return outStream.toByteArray();  //把outStream里的数据写入内存
    }



    @Override
    public void onAttach(Context context) {
        if(context instanceof FragmentImageCheckListener)
        {
            mListrner = (FragmentImageCheckListener) context;
        }else {
            throw new RuntimeException("activity必须implements接口");
        }
        super.onAttach(context);
    }

    public interface FragmentImageCheckListener{
        void cancelCheck();
        void submitCheck(String code);
    }
}
