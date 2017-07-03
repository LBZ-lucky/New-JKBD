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
import android.widget.TextView;
import cn.ucai.jkbd.bean.*;
import java.util.List;
import cn.ucai.jkbd.ExamApplication;
import cn.ucai.jkbd.R;
import cn.ucai.jkbd.bean.ExamInfo;
import cn.ucai.jkbd.bean.Result;
import cn.ucai.jkbd.biz.ExamBiz;
import cn.ucai.jkbd.utils.OkHttpUtils;
import cn.ucai.jkbd.utils.ResultUtils;
import com.squareup.picasso.Picasso;
/**
 * Created by clawpo on 2017/6/29.
 */

public class ExamActivity extends AppCompatActivity {
    LinearLayout layoutLoading;
    TextView tvView;
    TextView tv_theme,tv_A,tv_B,tv_C,tv_D,tv_load;
    ImageView image;
    ProgressBar loadBar;
    boolean isLoadExamInfo=false;
    boolean isLoadQuestions=false;
    boolean isLoadExamInfoReceiver=false;
    boolean isLoadQuestionsReceiver=false;
    LoadExamBroadcast loadExamBroadcast;
    LoadQuestionBroadcast loadQuestionBroadcast;
    ExamBiz biz=new ExamBiz();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        loadDate();
        initView();
        LoadExamBroadcast loadExamBroadcast;
        LoadQuestionBroadcast loadQuestionBroadcast;
        setListener();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(loadExamBroadcast!=null)
            unregisterReceiver(loadQuestionBroadcast);
        if(loadQuestionBroadcast!=null)
            unregisterReceiver(loadQuestionBroadcast);
    }

    private void setListener() {
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
        loadBar=(ProgressBar)findViewById(R.id.load_bar);
        layoutLoading=(LinearLayout)findViewById(R.id.layout_loading);
        tvView=(TextView)findViewById(R.id.exam_title);
        tv_A=(TextView)findViewById(R.id.tv_A);
        tv_B=(TextView)findViewById(R.id.tv_B);
        tv_C=(TextView)findViewById(R.id.tv_C);
        tv_D=(TextView)findViewById(R.id.tv_D);
        tv_load=(TextView)findViewById(R.id.tv_load);
        image=(ImageView)findViewById(R.id.image1);
        layoutLoading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  loadDate();
            }
        });
    }
    private void initDate() {
        if(isLoadExamInfoReceiver&&isLoadQuestionsReceiver)
        {
            if(isLoadExamInfo&&isLoadQuestions)
            {
                layoutLoading.setVisibility(View.GONE);
                ExamInfo examInfo = ExamApplication.getInstance().getExamInfo();
                if (examInfo != null)
                    showData(examInfo);
                List<Exam> examList = ExamApplication.getInstance().getExamList();
                if (examList != null && examList.size() >= 0)
                    showExam(examList);
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
    private void showExam(List<Exam> examList) {
        Exam exam = examList.get(0);
        if (exam != null)
        {
            tv_A.setText(exam.getItem1());
            tv_B.setText(exam.getItem2());
            tv_C.setText(exam.getItem3());
            tv_D.setText(exam.getItem4());
            Picasso.with(this)
                    .load(exam.getUrl())
                    .into(image);
         }
    }

     public class LoadExamBroadcast extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isSuccess=intent.getBooleanExtra(ExamApplication.LOAD_DATA_SUCCESS,false);
            if(isSuccess)
            {
            //    log.e("LoadExamBroadcast","LoadExamBroadcast,isSuccess="+isSuccess);
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
