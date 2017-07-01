package cn.ucai.jkbd;
import java.util.List;
import android.app.Application;
import android.util.Log;
import cn.ucai.jkbd.bean.*;
import cn.ucai.jkbd.biz.ExamBiz;
import cn.ucai.jkbd.utils.OkHttpUtils;
import cn.ucai.jkbd.utils.ResultUtils;

/**
 * Created by Zzz on 2017/6/30.
 */

public class ExamApplication extends Application {
    ExamInfo examInfo;
    private  static  ExamApplication instance;
    List<Exam> examList;
    ExamBiz biz=new ExamBiz();

    @Override
    public void onCreate() {
        super.onCreate();
        initDate();
    }

    private void initView() {
    }
    public static ExamApplication getInstance(){
        return instance;
    }

    private void initDate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
           biz.beginExam();

            }
        }).start();

    }


    public ExamInfo getExamInfo() {
        return examInfo;
    }

    public void setExamInfo(ExamInfo examInfo) {
        this.examInfo = examInfo;
    }
  public List<Exam> getExamList() {
        return examList;
    }

    public void setExamList(List<Exam> examList) {
        examList = examList;
    }
}
