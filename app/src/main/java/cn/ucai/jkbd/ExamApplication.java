package cn.ucai.jkbd;
import java.util.List;
import android.app.Application;
import android.util.Log;
import cn.ucai.jkbd.bean.*;
import cn.ucai.jkbd.utils.OkHttpUtils;
import cn.ucai.jkbd.utils.ResultUtils;

/**
 * Created by Zzz on 2017/6/30.
 */

public class ExamApplication extends Application {
    ExamInfo examInfo;
    private  static  ExamApplication instance;
    List<Exam> examList;

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
                OkHttpUtils<ExamInfo> utils = new OkHttpUtils<>(ExamApplication.getInstance());
                String uri = "http://101.251.196.90:8080/JztkServer/examInfo";
                utils.url(uri)
                        .targetClass(ExamInfo.class)
                        .execute(new OkHttpUtils.OnCompleteListener<ExamInfo>() {
                            @Override
                            public void onSuccess(ExamInfo result)
                            {
                                examInfo=result;
                                Log.e("main","result="+result);
                            }
                            @Override
                            public void onError(String error)
                            {
                                Log.e("main","error="+error);
                            }
                        });

                OkHttpUtils<String> util2 = new OkHttpUtils<>(ExamApplication.getInstance());
                String uri2 = "http://101.251.196.90:8080/JztkServer/getQuestions?testType=rand";
                util2.url(uri2)
                        .targetClass(String.class)
                        .execute(new OkHttpUtils.OnCompleteListener<String>() {
                            @Override
                            public void onSuccess(String jsonStr)
                            {
                                Result resultFromJson = ResultUtils.getListResultFromJson(jsonStr);
                                if(resultFromJson!=null&&resultFromJson.getError_code()==0)
                                {
                                    List<Exam>  emList=resultFromJson.getResult();
                                    if(emList!=null&&emList.size()>0)
                                    {
                                        examList=emList;
                                    }
                                }
                            }

                            @Override
                            public void onError(String error)
                            {
                                Log.e("main","error="+error);
                            }
                        });
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
