package cn.ucai.jkbd.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import cn.ucai.jkbd.bean.*;
import java.util.List;
import cn.ucai.jkbd.ExamApplication;
import cn.ucai.jkbd.R;
import cn.ucai.jkbd.bean.ExamInfo;
import cn.ucai.jkbd.bean.Result;
import cn.ucai.jkbd.utils.OkHttpUtils;
import cn.ucai.jkbd.utils.ResultUtils;

/**
 * Created by clawpo on 2017/6/29.
 */

public class ExamActivity extends AppCompatActivity {

    TextView tvView;

    TextView tv_theme,tv_A,tv_B,tv_C,tv_D;
    ImageView image;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);


        initDate();
        initView();

    }
    private void initView() {
        tvView=(TextView)findViewById(R.id.exam_title);
        tv_A=(TextView)findViewById(R.id.tv_A);
        tv_B=(TextView)findViewById(R.id.tv_B);
        tv_C=(TextView)findViewById(R.id.tv_C);
        tv_D=(TextView)findViewById(R.id.tv_D);
        image=(ImageView)findViewById(R.id.image1);
    }
    private void initDate() {
        ExamInfo examInfo=ExamApplication.getInstance().getExamInfo();
        if(examInfo!=null)
            showData(examInfo);
        List<Exam> examList = ExamApplication.getInstance().getExamList();
          if(examList!=null&&examList.size()>=0)
              showExam(examList);

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
            Picasso.with(ExamActivity.this)
                    .load(exam.getUrl())
                    .into(image);
         }
    }



}
