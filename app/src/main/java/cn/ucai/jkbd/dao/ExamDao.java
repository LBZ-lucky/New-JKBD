package cn.ucai.jkbd.dao;

import android.util.Log;

import java.util.List;

import cn.ucai.jkbd.ExamApplication;
import cn.ucai.jkbd.bean.Exam;
import cn.ucai.jkbd.bean.ExamInfo;
import cn.ucai.jkbd.bean.Result;
import cn.ucai.jkbd.utils.OkHttpUtils;
import cn.ucai.jkbd.utils.ResultUtils;

/**
 * Created by Zzz on 2017/7/1.
 */

public class ExamDao implements IExamDao {

    @Override
    public void LoadExamInfo() {
        OkHttpUtils<ExamInfo> utils = new OkHttpUtils<>(ExamApplication.getInstance());
        String uri = "http://101.251.196.90:8080/JztkServer/examInfo";
        utils.url(uri)
                .targetClass(ExamInfo.class)
                .execute(new OkHttpUtils.OnCompleteListener<ExamInfo>() {
                    @Override
                    public void onSuccess(ExamInfo result)
                    {
                        ExamApplication.getInstance().setExamInfo(result);
                        Log.e("main","result="+result);
                    }
                    @Override
                    public void onError(String error)
                    {
                        Log.e("main","error="+error);
                    }
                });

    }

    @Override
    public void LoadQuestionLists() {
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
                            List<Exam> emList=resultFromJson.getResult();
                            if(emList!=null&&emList.size()>0)
                            {
                                ExamApplication.getInstance().setExamList(emList);
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
}
