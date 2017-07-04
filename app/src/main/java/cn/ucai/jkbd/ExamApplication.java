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
