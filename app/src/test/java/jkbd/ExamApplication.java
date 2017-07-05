package jkbd;

import android.app.Application;

import java.util.List;

import cn.ucai.jkbd.bean.*;

/**
 * Created by Zzz on 2017/6/30.
 */

public class ExamApplication extends Application {
    public static String LOAD_EXAM_INFO="load_exam_info";
    public static String LOAD_EXAM_QUESTION="load_exam_question";
    public static String LOAD_DATA_SUCCESS="load_data_success";
    ExamInfo examInfo;
    private  static  ExamApplication instance;
    List<Exam> examList;


    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
    }

    private void initView() {
    }
    public static ExamApplication getInstance(){
        return instance;
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
        this.examList = examList;
    }
}
