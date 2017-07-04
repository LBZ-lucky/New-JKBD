package cn.ucai.jkbd.activity;

import android.util.Log;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.ucai.jkbd.bean.*;
import java.util.List;
import cn.ucai.jkbd.ExamApplication;
import cn.ucai.jkbd.R;
import cn.ucai.jkbd.bean.ExamInfo;
import cn.ucai.jkbd.bean.Result;
import cn.ucai.jkbd.biz.ExamBiz;
import cn.ucai.jkbd.biz.IExamBiz;
import cn.ucai.jkbd.utils.OkHttpUtils;
import cn.ucai.jkbd.utils.ResultUtils;
import com.squareup.picasso.Picasso;
/**
 * Created by clawpo on 2017/6/29.
 */

public class ExamActivity extends AppCompatActivity {
    LinearLayout layoutLoading,layout_C,layout_D;
    TextView tvView;
    TextView tv_theme,tv_A,tv_B,tv_C,tv_D,tv_load,tv_NO,tv_Theme;
    RadioButton rdA,rdB,rdC,rdD;
    RadioButton[] rds=new RadioButton[4];

    ImageView image;
    ProgressBar loadBar;
    boolean isLoadExamInfo=false;
    boolean isLoadQuestions=false;
    boolean isLoadExamInfoReceiver=false;
    boolean isLoadQuestionsReceiver=false;
    LoadExamBroadcast loadExamBroadcast;
    LoadQuestionBroadcast loadQuestionBroadcast;

    IExamBiz biz;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        initView();
        loadExamBroadcast=new LoadExamBroadcast();
        loadQuestionBroadcast=new LoadQuestionBroadcast();
        setBroadcastListener();
        loadDate();
        biz=new ExamBiz();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadExamBroadcast!=null)
            unregisterReceiver(loadQuestionBroadcast);
        if(loadQuestionBroadcast!=null)
            unregisterReceiver(loadQuestionBroadcast);
    }

    private void setBroadcastListener() {
        registerReceiver(loadExamBroadcast,new IntentFilter(ExamApplication.LOAD_EXAM_INFO));
        registerReceiver(loadQuestionBroadcast,new IntentFilter(ExamApplication.LOAD_EXAM_QUESTION));
    }

    void loadDate()
     {

         layoutLoading.setEnabled(false);
         loadBar.setVisibility(View.VISIBLE);
         tv_load.setText("下载数据……");
         new Thread(new Runnable() {
             @Override
             public void run() {

                 biz.beginExam();
             }
         }).start();

     }
    private void initView() {
        rdA=(RadioButton)findViewById(R.id.rd_A);
        rdB=(RadioButton)findViewById(R.id.rd_B);
        rdC=(RadioButton)findViewById(R.id.rd_C);
        rdD=(RadioButton)findViewById(R.id.rd_D);
        rds[0]=rdA;
        rds[1]=rdB;
        rds[2]=rdC;
        rds[3]=rdD;
        tv_Theme=(TextView)findViewById(R.id.tv_theme);
        tv_NO=(TextView)findViewById(R.id.tv_No);
        loadBar=(ProgressBar)findViewById(R.id.load_bar);
        layoutLoading=(LinearLayout)findViewById(R.id.layout_loading);
        layout_C=(LinearLayout)findViewById(R.id.layout_C);
        layout_D=(LinearLayout)findViewById(R.id.layout_D);
        tvView=(TextView)findViewById(R.id.exam_title);
        tv_A=(TextView)findViewById(R.id.tv_A);
        tv_B=(TextView)findViewById(R.id.tv_B);
        tv_C=(TextView)findViewById(R.id.tv_C);
        tv_D=(TextView)findViewById(R.id.tv_D);
        tv_load=(TextView)findViewById(R.id.tv_load);
        image=(ImageView)findViewById(R.id.imageView);
        layoutLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  loadDate();
            }
        });
        tv_A.setOnClickListener(rdListener);
        tv_B.setOnClickListener(rdListener);
        tv_C.setOnClickListener(rdListener);
        tv_D.setOnClickListener(rdListener);

    }
    View.OnClickListener rdListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private void initDate() {
        if(isLoadExamInfoReceiver&&isLoadQuestionsReceiver)
        {
            if(isLoadExamInfo&&isLoadQuestions)
            {
                layoutLoading.setVisibility(View.GONE);
                ExamInfo examInfo = ExamApplication.getInstance().getExamInfo();
                if (examInfo != null)
                    showData(examInfo);

                    showExam(biz.getExam());
            }
            else{
                layoutLoading.setEnabled(true);
                loadBar.setVisibility(View.GONE);
                tv_load.setText("下载失败，点击重新下载");
            }
        }


    }


    private void showData(ExamInfo examInfo)
    {
            tvView.setText(examInfo.toString());
    }

    private void showExam(Exam exam) {
        if (exam != null)
        {
            tv_NO.setText(biz.getExamIndex());
            tv_Theme.setText(exam.getQuestion());
            tv_A.setText(exam.getItem1());
            tv_B.setText(exam.getItem2());
            tv_C.setText(exam.getItem3());
            tv_D.setText(exam.getItem4());
            if(exam!=null&&!exam.getUrl().equals(""))
            {
                image.setVisibility(View.VISIBLE);
                Picasso.with(this)
                        .load(exam.getUrl())
                        .into(image);
            }else{
                image.setVisibility(View.GONE);
            }
            layout_C.setVisibility(exam.getItem3().equals("")?View.GONE:View.VISIBLE);
            layout_D.setVisibility(exam.getItem4().equals("")?View.GONE:View.VISIBLE);
            rdC.setVisibility(exam.getItem3().equals("")?View.GONE:View.VISIBLE);
            rdD.setVisibility(exam.getItem4().equals("")?View.GONE:View.VISIBLE);
         }else{
            Toast.makeText(this,"没有上一题！",Toast.LENGTH_LONG).show();
        }


        String userAnswer = exam.getUserAnswer();
        if(userAnswer!=null&&!userAnswer.equals(""))
        {
            int userRd=Integer.parseInt(userAnswer)-1;
            rds[userRd].setChecked(true);

        }

    }
    private void saveUserAnswer()
    {

        for (int i = 0; i < rds.length; i++) {
            if(rds[i].isChecked())
            {
                biz.getExam().setUserAnswer(String.valueOf(i+1));
                return ;
            }
        }
    }



    public void preExem(View view)
    {
        saveUserAnswer();
       showExam(biz.preQuestion());
    }

    public void nextExam(View view)
    {
        saveUserAnswer();
        showExam(biz.nextQuestion());
    }

    public class LoadExamBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean isSuccess=intent.getBooleanExtra(ExamApplication.LOAD_DATA_SUCCESS,false);
            if(isSuccess)
            {
              //  log.e("LoadExamBroadcast","LoadExamBroadcast,isSuccess="+isSuccess);
                isLoadExamInfo=true;
            }

            isLoadExamInfoReceiver=true;
            initDate();
        }
    }
    public class LoadQuestionBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isSuccess=intent.getBooleanExtra(ExamApplication.LOAD_DATA_SUCCESS,false);
            if(isSuccess)
            {
            //    log.e("LoadQuestionBroadcast","LoadQuestionBroadcast,isSuccess="+isSuccess);
                isLoadQuestions=true;
            }
            isLoadQuestionsReceiver=true;
            initDate();
        }
    }


}
