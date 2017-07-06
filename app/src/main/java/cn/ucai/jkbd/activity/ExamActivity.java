package cn.ucai.jkbd.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.ucai.jkbd.ExamApplication;
import cn.ucai.jkbd.R;
import cn.ucai.jkbd.bean.Exam;
import cn.ucai.jkbd.bean.ExamInfo;
import cn.ucai.jkbd.biz.ExamBiz;
import cn.ucai.jkbd.biz.IExamBiz;
import cn.ucai.jkbd.view.QuestionAdapter;

/**
 * Created by clawpo on 2017/6/29.
 */

public class ExamActivity extends AppCompatActivity {
    RadioButton[] rds = new RadioButton[4];
    boolean isLoadExamInfo = false;
    boolean isLoadQuestions = false;
    boolean isLoadExamInfoReceiver = false;
    boolean isLoadQuestionsReceiver = false;
    LoadExamBroadcast loadExamBroadcast;
    LoadQuestionBroadcast loadQuestionBroadcast;
    QuestionAdapter adapter;

    IExamBiz biz;
    @BindView(R.id.load_bar) ProgressBar loadBar;
    @BindView(R.id.tv_load) TextView tv_load;
    @BindView(R.id.layout_loading) LinearLayout layoutLoading;
    @BindView(R.id.image1) ImageView image1;
    @BindView(R.id.exam_title) TextView tvView;
    @BindView(R.id.tv_time) TextView tv_Time;
    @BindView(R.id.tv_No) TextView tv_NO;
    @BindView(R.id.tv_theme) TextView tv_Theme;
    @BindView(R.id.imageView) ImageView image;
    @BindView(R.id.tv_A) TextView tv_A;
    @BindView(R.id.tv_B) TextView tv_B;
    @BindView(R.id.tv_C) TextView tv_C;
    @BindView(R.id.tv_D) TextView tv_D;
    @BindView(R.id.layout_C) LinearLayout layout_C;
    @BindView(R.id.layout_D) LinearLayout layout_D;
    @BindView(R.id.rd_A) RadioButton rdA;
    @BindView(R.id.rd_B) RadioButton rdB;
    @BindView(R.id.rd_C) RadioButton rdC;
    @BindView(R.id.rd_D) RadioButton rdD;
    @BindView(R.id.gallery) Gallery gallery;
    @BindView(R.id.btn_next) Button btnNext;
    @BindView(R.id.layout_A) LinearLayout layout_A;
    @BindView(R.id.layout_B) LinearLayout layout_B;
    TextView[] tvs=new TextView[4];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        ButterKnife.bind(this);
        initView();
        loadExamBroadcast = new LoadExamBroadcast();
        loadQuestionBroadcast = new LoadQuestionBroadcast();
        setBroadcastListener();
        loadDate();
        biz = new ExamBiz();


    }

    @Override
    protected void onDestroy() {
        if (loadExamBroadcast != null)
            unregisterReceiver(loadExamBroadcast);
        if (loadQuestionBroadcast != null)
            unregisterReceiver(loadQuestionBroadcast);
        super.onDestroy();

    }

    private void setBroadcastListener() {
        registerReceiver(loadExamBroadcast, new IntentFilter(ExamApplication.LOAD_EXAM_INFO));
        registerReceiver(loadQuestionBroadcast, new IntentFilter(ExamApplication.LOAD_EXAM_QUESTION));
    }

    void loadDate() {

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
//        rdA = (RadioButton) findViewById(R.id.rd_A);
//        rdB = (RadioButton) findViewById(R.id.rd_B);
//        rdC = (RadioButton) findViewById(R.id.rd_C);
//        rdD = (RadioButton) findViewById(R.id.rd_D);
        rds[0] = rdA;
        rds[1] = rdB;
        rds[2] = rdC;
        rds[3] = rdD;
        tvs[0]=tv_A;
        tvs[1]=tv_B;
        tvs[2]=tv_C;
        tvs[3]=tv_D;

//        gallery = (Gallery) findViewById(R.id.gallery);
//        tv_Theme = (TextView) findViewById(R.id.tv_theme);
//        tv_NO = (TextView) findViewById(R.id.tv_No);
//        loadBar = (ProgressBar) findViewById(R.id.load_bar);
//        layoutLoading = (LinearLayout) findViewById(R.id.layout_loading);
//        layout_C = (LinearLayout) findViewById(R.id.layout_C);
//        layout_D = (LinearLayout) findViewById(R.id.layout_D);
//        tvView = (TextView) findViewById(R.id.exam_title);
//        tv_A = (TextView) findViewById(R.id.tv_A);
//        tv_B = (TextView) findViewById(R.id.tv_B);
//        tv_C = (TextView) findViewById(R.id.tv_C);
//        tv_D = (TextView) findViewById(R.id.tv_D);
//        tv_Time = (TextView) findViewById(R.id.tv_time);
//        tv_load = (TextView) findViewById(R.id.tv_load);
//        image = (ImageView) findViewById(R.id.imageView);
//        layoutLoading.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                loadDate();
//            }
//        });


    }

    @OnClick(R.id.layout_loading)
    void onLoadClick() {
        loadDate();
    }

    private void initDate() {
        if (isLoadExamInfoReceiver && isLoadQuestionsReceiver) {
            if (isLoadExamInfo && isLoadQuestions) {
                layoutLoading.setVisibility(View.GONE);
                ExamInfo examInfo = ExamApplication.getInstance().getExamInfo();
                if (examInfo != null) {
                    showData(examInfo);
                }
                initGallery();
                showExam(biz.getExam());
                initTimer(examInfo);
            } else {
                layoutLoading.setEnabled(true);
                loadBar.setVisibility(View.GONE);
                tv_load.setText("下载失败，点击重新下载");
            }
        }


    }

    private void initGallery() {
        adapter = new QuestionAdapter(this);
        gallery.setAdapter(adapter);
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                saveUserAnswer();
                showExam(biz.getExam(position));
            }
        });

    }

    private void initTimer(ExamInfo examInfo) {
        int sumTime = examInfo.getLimitTime() * 60 * 1000;
        final long overTime = sumTime + System.currentTimeMillis();
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long l = overTime - System.currentTimeMillis();
                final long min = (int) (l / 1000 / 60);
                final long sec = (int) (l / 1000 % 60);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_Time.setText("剩余时间：" + min + "分" + sec + "秒");
                    }
                });
            }
        }, 0, 1000);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timer.cancel();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        commit(null);
                    }
                });
            }
        }, sumTime);
    }


    private void showData(ExamInfo examInfo) {
        tvView.setText(examInfo.toString());
    }

    private void showExam(Exam exam) {
        if (exam != null) {
            tv_NO.setText(biz.getExamIndex());
            tv_Theme.setText(exam.getQuestion());
            tv_A.setText(exam.getItem1());
            tv_B.setText(exam.getItem2());
            tv_C.setText(exam.getItem3());
            tv_D.setText(exam.getItem4());

            if (exam.getUrl() != null && !exam.getUrl().equals("")) {
                image.setVisibility(View.VISIBLE);
                Picasso.with(this)
                        .load(exam.getUrl())
                        .into(image);
            } else {
                image.setVisibility(View.GONE);
            }

            for (TextView tv : tvs) {
                tv.setTextColor(getResources().getColor(R.color.black));
            }
            for (RadioButton rd : rds) {
                rd.setChecked(false);
            }
            layout_C.setVisibility(exam.getItem3().equals("") ? View.GONE : View.VISIBLE);
            layout_D.setVisibility(exam.getItem4().equals("") ? View.GONE : View.VISIBLE);
            rdC.setVisibility(exam.getItem3().equals("") ? View.GONE : View.VISIBLE);
            rdD.setVisibility(exam.getItem4().equals("") ? View.GONE : View.VISIBLE);

            String userAnswer = exam.getUserAnswer();
            Log.e("userAnswer", "userAnswer=" + userAnswer + "  " + biz.getExamIndex());
            for (RadioButton rd : rds) {
                rd.setChecked(false);
            }
            if (userAnswer != null && !userAnswer.equals("")) {
                int userRd = Integer.parseInt(userAnswer) - 1;
                Log.e("userRd","userRd="+userRd);
                rds[userRd].setChecked(true);
              for (RadioButton rd : rds) {
                    rd.setEnabled(false);
                  setAnswerTextColor(userAnswer, exam.getAnswer());
               }
            } else {
                setOptions(false);
            }
        }


    }

    private void setAnswerTextColor(String userAnswer, String answer) {
        int i=Integer.parseInt(answer)-1;
        tvs[i].setTextColor(getResources().getColor(R.color.green));
      if(!userAnswer.equals(answer))
      {
          int j=Integer.parseInt(userAnswer)-1;
          tvs[j].setTextColor(getResources().getColor(R.color.red));
          for (int i1 = 0; i1 < tvs.length; i1++) {
              if(i1!=i&&i1!=j)
              {
                  tvs[i1].setTextColor(getResources().getColor(R.color.black));
              }

          }
      }

    }


    private void setOptions(Boolean hasAnswer) {
        for (RadioButton rd : rds) {
            rd.setEnabled(!hasAnswer);
        }
    }

    private void saveUserAnswer() {

        for (int i = 0; i < rds.length; i++) {
            if (rds[i].isChecked()) {

                biz.getExam().setUserAnswer(String.valueOf(i + 1));
                setOptions(true);
                adapter.notifyDataSetChanged();
                return;
            }
        }
        adapter.notifyDataSetChanged();
    }


    public void preExem(View view) {
        saveUserAnswer();
        showExam(biz.preQuestion());
    }

    public void nextExam(View view) {
        saveUserAnswer();
        showExam(biz.nextQuestion());
    }
    public void commit(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("交卷")
                .setTitle("你还有剩余时间,确认交卷吗？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        commit();
                    }
                })
                .setNegativeButton("取消",null);
        builder.setCancelable(false);
        builder.create().show();
    }
    public void commit() {
        saveUserAnswer();
        int score = biz.commitExam();
        View inflate = View.inflate(this, R.layout.layout_result, null);
        TextView tv_Result = (TextView) inflate.findViewById(R.id.tv_result);
        tv_Result.setText("你的分数为" + score + "分！");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.exam_commit32x32)
                .setTitle("交卷")
                // .setMessage("你的分数为"+score+"分！")
                .setView(inflate)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

        builder.create().show();
    }

    public class LoadExamBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            boolean isSuccess = intent.getBooleanExtra(ExamApplication.LOAD_DATA_SUCCESS, false);
            if (isSuccess) {
                //  log.e("LoadExamBroadcast","LoadExamBroadcast,isSuccess="+isSuccess);
                isLoadExamInfo = true;
            }

            isLoadExamInfoReceiver = true;
            initDate();
        }
    }

    public class LoadQuestionBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isSuccess = intent.getBooleanExtra(ExamApplication.LOAD_DATA_SUCCESS, false);
            if (isSuccess) {
                //    log.e("LoadQuestionBroadcast","LoadQuestionBroadcast,isSuccess="+isSuccess);
                isLoadQuestions = true;
            }
            isLoadQuestionsReceiver = true;
            initDate();
        }
    }


}
