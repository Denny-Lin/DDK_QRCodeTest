package com.example.qrcodetest;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
/**
 * 思路：
 * 1、初始化界面和控件
 * 2、添加按钮监听事件
 * 3、用户输入合法性验证
 * 4、调用生成条形码或二维码的方法
 * 5、界面显示生成的图片
 */
public class MainActivity extends Activity {
    //用户输入的需要转换的字符输入框
    private EditText inputET;
    //条形码和二维码按钮
    private Button btnOne,btnTwo;
    //生成的条形码或二维码的图片
    private ImageView qrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState);
        //设置界面无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
        //初始化界面，实例化对象
        initView();
        //给相应的控件添加监听器
        addListener();
    }

    private void addListener() {
        /**
         * 设置条形码按钮监听器
         */
        btnOne.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //该变量用于保存用户输入的字符串
                String url = inputET.getText().toString().trim();
                //判断用户输入的字符串是否包含中文
                for(int i = 0;i<url.length();i++){
                    int c = url.charAt(i);
                    //若包含中文，提示用户条形码不能包含中文，同时结束该操作
                    if(19968<=c && c<40623){
                        //提示用户
                        Toast.makeText(MainActivity.this, "不能包含中文", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //生成的条形码图片
                Bitmap image = null;
                try {
                    //当用户输入的url不为空时
                    if(url != null && !"".equals(url)){
                        //将用户输入的url作为参数，调用创建条形码的方法，生成条形码图片
                        image = createOneQRCode(url);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if(image != null){
                    //将生成的条形码显示到界面的图片中
                    qrCode.setImageBitmap(image);
                }
            }
        });
        /**
         * 设置二维码按钮的监听器
         */
        btnTwo.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //用户输入的需要生成二维码的url字符串
                String url = inputET.getText().toString().trim();
                //声明生成的二维码图片
                Bitmap image = null;

                try {
                    //当用户输入的字符串url不为空时
                    if(url != null && !"".equals(url)){
                        //将用户输入的url作为参数，调用创建二维码的方法
                        image = createTwoQRCode(url);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //若生成的二维码不为空，即生成二维码成功
                if(image != null){
                    //将生成的二维码显示给用户
                    qrCode.setImageBitmap(image);
                }
            }
        });
    }

    /**
     * 初始化界面和控件
     */
    private void initView() {
        inputET = (EditText)findViewById(R.id.input_et);
        btnOne = (Button)findViewById(R.id.btn_one);
        btnTwo = (Button)findViewById(R.id.btn_two);
        qrCode = (ImageView)findViewById(R.id.qr_code);
    }

    /**
     * 创建条形码的方法
     * @return
     * @throws Exception 
     */
    public Bitmap createOneQRCode(String content) throws Exception {
        // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, 500, 200);
        //矩阵的宽度
        int width = matrix.getWidth();
        //矩阵的高度
        int height = matrix.getHeight();
        //矩阵像素数组
        int[] pixels =  new int[width * height];
        //双重循环遍历每一个矩阵点
        for(int y = 0;y<height;y++){
            for(int x = 0;x<width;x++){
                if(matrix.get(x, y)){
                    //设置矩阵像素点的值
                    pixels[y * width +x] = 0xff000000;
                }
            }
        }
        //根据颜色数组来创建位图
        /**
         * 此函数创建位图的过程可以简单概括为为:更加width和height创建空位图，
         * 然后用指定的颜色数组colors来从左到右从上至下一次填充颜色。
         * config是一个枚举，可以用它来指定位图“质量”。
         */
        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bm.setPixels(pixels, 0, width, 0, 0, width, height);
        //将生成的条形码返回给调用者
        return bm;
    }

    /**
     * 创建二维码的方法
     * @return
     * @throws Exception 
     */
    public Bitmap createTwoQRCode(String content) throws Exception {
        // 生成二维码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, 300, 300);
        //矩阵的宽度
        int width = matrix.getWidth();
        //矩阵的高度
        int height = matrix.getHeight();
        //矩阵像素数组
        int[] pixels = new int[width * height];
        //双重循环遍历每一个矩阵点
        for(int y = 0;y<height;y++){
            for(int x = 0;x<width;x++){
                if(matrix.get(x, y)){
                    //设置矩阵像素点的值
                    pixels[y * width +x] = 0xff000000;
                }
            }
        }
        //根据颜色数组来创建位图
        /**
        * 此函数创建位图的过程可以简单概括为为:更加width和height创建空位图，
        * 然后用指定的颜色数组colors来从左到右从上至下一次填充颜色。
        * config是一个枚举，可以用它来指定位图“质量”。
        */
        Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bm.setPixels(pixels, 0, width, 0, 0, width, height);
        //将生成的条形码返回给调用者
        return bm;
    }

    /**
     * 重写创建菜单项方法
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //添加一个退出的菜单项
        menu.add(0, 0, 0, "退出");
        return super.onCreateOptionsMenu(menu);
    }
    /**
     * 重写菜单项点击事件处理方法
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case 0:
            //结束退出
            this.finish();
            break;
        }
        return super.onOptionsItemSelected(item);
    }
}